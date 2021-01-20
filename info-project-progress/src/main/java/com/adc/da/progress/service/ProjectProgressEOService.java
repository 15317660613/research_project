package com.adc.da.progress.service;

import com.adc.da.budget.entity.Project;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.progress.dao.ProjectMilepostEODao;
import com.adc.da.progress.dao.ProjectNameEODao;
import com.adc.da.progress.dao.ProjectRateEODao;
import com.adc.da.progress.dao.StageOrderEODao;
import com.adc.da.progress.entity.*;
import com.adc.da.progress.page.ProjectMilepostEOPage;
import com.adc.da.progress.page.ProjectNameEOPage;
import com.adc.da.progress.page.ProjectRateEOPage;
import com.adc.da.progress.page.StageOrderEOPage;
import com.adc.da.progress.util.StatusUtils;
import com.adc.da.progress.vo.TreeVO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static com.adc.da.progress.util.StatusUtils.COMPLETED;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-07-18
 */
@Service("progressDetailEOService")
@Transactional(value = "transactionManager",
    readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
@Slf4j
public class ProjectProgressEOService {

    @Autowired
    private ProjectRateEODao projectRateEODao;

    @Autowired
    private ProjectRateEOService projectRateEOService;

    @Autowired
    private StageOrderEODao stageOrderEODao;

    @Autowired
    private ProjectNameEODao projectNameEODao;

    @Autowired
    private ProjectMilepostEODao projectMilepostEODao;

    @Autowired
    private ProjectProgressEOService projectProgressEOService;
    @Autowired
    private ProjectRepository projectRepository;

    /**
     * 项目进度 名称
     */
//    private List<ProjectNameEO> projectNameEOList;

    /**
     * 项目进度 流程
     */
    //private List<ProjectRateEO> projectRateEOList;

    /**
     * 项目进度 顺序
     */
    //private List<StageOrderEO> stageOrderEOList;

    /**
     * 全局projectId
     */
    //private  String projectId;

    /**
     * 查询所有的流程记录
     */
    private List<ProjectRateEO> initProjectRateList(String projectId) {

        ProjectRateEOPage projectRateEOPage = new ProjectRateEOPage();
        projectRateEOPage.setProjectId(projectId);
        projectRateEOPage.setOrderBy(" CREATE_TIME_ ASC");
        List<ProjectRateEO> projectRateEOList = new ArrayList<>();
        projectRateEOList = projectRateEODao.queryByList(projectRateEOPage);
        if(CollectionUtils.isEmpty(projectRateEOList)){
            Project project = projectRepository.findById(projectId);
            String projectType = String.valueOf(project.getProjectType());
            try {
                ProjectRateEO projectRateEO = projectRateEOService.initProjectProgressData(projectId,projectType);
                projectRateEOList.add(projectRateEO);
            } catch (Exception e) {
                log.error("创建进度数据失败" + e.toString());
            }
        }
        return projectRateEOList;

    }

    /**
     * 获取 进度顺序
     */
    private List<StageOrderEO> initStageOrderList(List<ProjectRateEO> projectRateEOList, String projectId) throws Exception{
        String projectType;
        if (CollectionUtils.isNotEmpty(projectRateEOList)) {
            projectType = projectRateEOList.get(0).getProcessNameId();
            /*
             * 对于第一条数据进行删除操作，因为第一条记录不属于流程信息，属于进度顺序
             */
            projectRateEOList.remove(0);
        } else {
            throw new AdcDaBaseException("ProjectId error : " + projectId);
        }
        StageOrderEOPage stageOrderEOPage = new StageOrderEOPage();
        stageOrderEOPage.setProjectType(projectType);
        return stageOrderEODao.queryByList(stageOrderEOPage);
    }

    /**
     * 获取 项目进度 名称数据
     *
     * @param stageOrderIds
     */
     private List<ProjectNameEO> initProjectName(List<String> stageOrderIds) {

        ProjectNameEOPage projectNameEOPage = new ProjectNameEOPage();
        projectNameEOPage.setStageOrderIds(stageOrderIds);
        projectNameEOPage.setOrderBy("EXT_INFO_1_ ASC");
        return projectNameEODao.queryByList(projectNameEOPage);
    }

    /**
     * 查询流程进度数据
     *
     * @param projectId
     * @return
     */
    public List<ProjectProgressEO> getProjectProgressPage(String projectId) throws Exception{

        //this.projectId = projectId;
        /*
         * 查询所有的流程记录
         */
        List<ProjectRateEO> projectRateEOList = initProjectRateList(projectId);

        /* 获取 进度顺序 */
        List<StageOrderEO> stageOrderEOList = initStageOrderList(projectRateEOList, projectId);


        /* 结果集初始化，容量位阶段长度 */
        List<ProjectProgressEO> resultList = new ArrayList<>(stageOrderEOList.size());
        List<String> stageOrderIds = new ArrayList<>();
        for (StageOrderEO entity : stageOrderEOList) {
            stageOrderIds.add(entity.getId());

            ProjectProgressEO result = new ProjectProgressEO();
            result.setStageLevel(entity.getLevel());
            result.setProjectId(projectId);
            result.setStageName(entity.getStageName());
            result.setStageNameId(entity.getId());
            resultList.add(result);

        }

        /*
         * 获取 项目进度 名称数据
         */
        List<ProjectNameEO> projectNameEOList = initProjectName(stageOrderIds);

        /*
         * 初始化项目进度顺序与名称的对应关系
         */
        Map<String, TreeSet<Integer>> stageNameMap = initStageNameMap(projectNameEOList);

        /*
         *  项目进度名称与项目进度流程
         */
        Map<String, TreeSet<Integer>> nameRateMap = initNameRateMap(projectRateEOList);

        /*
         *    获取流程实例相关信息
         *
         */
        Set<String> instanceIdSet = getInstanceIdSet(projectRateEOList);
        Map<String, InstanceDetailEO> instanceInfoMap = initProcessInstanceInfo(instanceIdSet);

        /*
         * 对每个阶段详情进行组装数据 初始化
         */
        List<ProgressDetailEO> subResultList;
        List<InstanceDetailEO> instanceList;
        TreeSet<Integer> valueStageNameMap;
        TreeSet<Integer> valueNameRateMap;
        ProjectNameEO projectNameEO;

        /*
         *   组装结果集
         */
        for (ProjectProgressEO result : resultList) {
            subResultList = new ArrayList<>();
            String keyStageNameMap = result.getStageNameId();
            valueStageNameMap = stageNameMap.get(keyStageNameMap);
            for (Integer valueStageNameIndex : valueStageNameMap) {
                /*
                 * 组装 result subResultList
                 */
                instanceList = new ArrayList<>();
                ProgressDetailEO subResult = new ProgressDetailEO();
                projectNameEO = projectNameEOList.get(valueStageNameIndex);
                if (null != projectNameEO) {
                    subResult.setProcessName(projectNameEO.getProcName());
                    subResult.setProcessDefinitionKey(projectNameEO.getProcDefKey());
                    String keyNameRateMap = projectNameEO.getId();
                    valueNameRateMap = nameRateMap.get(keyNameRateMap);

                    if (CollectionUtils.isNotEmpty(valueNameRateMap)) {
                        addSubResult(valueNameRateMap, instanceList, instanceInfoMap,projectRateEOList);
                    }
                    /* 设置项目进度-顺序，状态 */
                    if (CollectionUtils.isNotEmpty(instanceList)) {
                        result.setStageStatus(COMPLETED);
                    }
                    subResult.setProcessInstance(instanceList);
                    /*
                     * 回显 流程执行次数限制
                     */
                    subResult.setProcessLimit(projectNameEO.getExtInfo2());
                    subResultList.add(subResult);
                } else {
                    log.warn("valueStageNameIndex error  {}", valueStageNameIndex);
                }
            }
            result.setProgressDetailEOList(subResultList);
        }

        return resultList;
    }

    /**
     * 组装 subResult  InstanceDetailEO List
     *
     * @param valueNameRateMap
     * @param instanceList
     * @param instanceInfoMap
     */
    private void addSubResult(TreeSet<Integer> valueNameRateMap, List<InstanceDetailEO> instanceList,
        Map<String, InstanceDetailEO> instanceInfoMap,List<ProjectRateEO> projectRateEOList) {
        for (Integer valueNameRateIndex : valueNameRateMap) {
            /*
             * 组装 subResult  InstanceDetailEO List
             */
            ProjectRateEO projectRateEO = projectRateEOList.get(valueNameRateIndex);
            if (null != projectRateEO) {
                String instanceId = projectRateEO.getProcInstId();
                /* 存在发起人终止的情况，发起人终止的流程此处会为null，故需要再次校验 */
                if (null != instanceInfoMap.get(instanceId)) {
                    instanceList.add(instanceInfoMap.get(instanceId));
                }
            } else {
                log.warn("valueNameRateIndex error  {}", valueNameRateIndex);
            }
        }
    }

    /**
     * 项目进度顺序 与 项目进度名称 进行关联
     *
     * @return
     */
    private Map<String, TreeSet<Integer>> initStageNameMap(List<ProjectNameEO> projectNameEOList) {
        Map<String, TreeSet<Integer>> map = new HashMap<>();
        int index = 0;
        TreeSet<Integer> value;
        for (ProjectNameEO projectNameEO : projectNameEOList) {
            String key = projectNameEO.getStageOrderId();
            if (map.containsKey(key)) {
                value = map.get(key);
            } else {
                value = new TreeSet<>();
            }
            value.add(index++);
            map.put(key, value);
        }

        return map;
    }

    /**
     * 流程Id集合
     */
    //private Set<String> instanceIds;

    /**
     * 项目进度名称 与 项目进度流程 进行关联
     *
     * @return
     */
    private Map<String, TreeSet<Integer>> initNameRateMap(List<ProjectRateEO> projectRateEOList) {
        Map<String, TreeSet<Integer>> map = new HashMap<>();
        int index = 0;
        TreeSet<Integer> value;
        //Set<String> instanceIds = new TreeSet<>();
        for (ProjectRateEO eo : projectRateEOList) {
            /*
             *  添加流程实例id信息
             */
            //instanceIds.add(eo.getProcInstId());

            String key = eo.getProcessNameId();
            if (map.containsKey(key)) {
                value = map.get(key);
            } else {
                value = new TreeSet<>();
            }
            value.add(index++);
            map.put(key, value);
        }
        return map;
    }


    /**
     * 项目进度名称 与 项目进度流程 进行关联
     *
     * @return
     */
    private  Set<String> getInstanceIdSet(List<ProjectRateEO> projectRateEOList) {
        Set<String> instanceIds = new TreeSet<>();
        for (ProjectRateEO eo : projectRateEOList) {
            /*
             *  添加流程实例id信息
             */
            instanceIds.add(eo.getProcInstId());
        }
        return instanceIds;
    }

    /**
     * 工作流相关，ACT_HI相关服务
     */
    @Autowired
    HistoryService historyService;

    /**
     * 工作流相关， ACT_RU相关服务
     */
    @Autowired
    TaskService taskService;

    /**
     * 初始化流程实例
     *
     * @return
     * @see #initProcessInstanceInfo(Set<String> )
     */
    private Map<String, InstanceDetailEO> initProcessInstanceInfo(Set<String> instanceIds) {
        Map<String, InstanceDetailEO> resultMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(instanceIds)) {
            /*
             * 查询Rate表中流程数据信息
             */
            List<HistoricProcessInstance> instanceResult = historyService.createHistoricProcessInstanceQuery()
                                                                         .processInstanceIds(instanceIds).list();

            for (HistoricProcessInstance processInstance : instanceResult) {
                InstanceDetailEO result = new InstanceDetailEO();
                String instanceId = processInstance.getId();
                result.setProcessInstanceId(instanceId);
                result.setProcessStartTime(processInstance.getStartTime());
                /*
                 *  判断流程是否完成
                 */
                if (null != processInstance.getEndTime()) {

                    /*
                     *  针对结束原因进行处理
                     */
                    String value = (String) historyService.createHistoricVariableInstanceQuery()
                                                          .processInstanceId(instanceId)
                                                          .variableName("approve").list().get(0).getValue();

                    if ("shutdownProcess".equals(value)) {
                        /*
                         * 针对发起终止的流程单独处理,
                         * 根据需求说明，显示为未发起,
                         * 即过滤该流程在进度中的显示
                         */
                        continue;
                    } else {
                        result.setProcessStatus(StatusUtils.COMPLETED);
                    }
                    /*
                     * 设置流程时间，结束时间
                     */
                    Date processTime = processInstance.getEndTime();
                    result.setProcessEndTime(processTime);
                    result.setProcessTime(processTime);
                } else {
                    result.setProcessStatus(StatusUtils.NOT_COMPLETED);
                    Date processTime = taskService.createTaskQuery().processInstanceId(instanceId).list().get(0)
                                                  .getCreateTime();
                    result.setProcessTime(processTime);
                }
                resultMap.put(instanceId, result);
            }
        }
        return resultMap;
    }


    public List<TreeVO> getTree(String projectId) throws Exception{

        List<TreeVO> treeVOList = new ArrayList<>();
        //查询项目下所有的里程碑备用
        ProjectMilepostEOPage projectMilepostEOPage = new ProjectMilepostEOPage();
        projectMilepostEOPage.setProjectId(projectId);
        List<ProjectMilepostEO> projectMilepostEOList = projectMilepostEODao.queryByList(projectMilepostEOPage);

        if (StringUtils.isNotEmpty(projectId)) {
            //查找项目下所有的阶段
            List<ProjectProgressEO> projectProgressEOList = this.getProjectProgressPage(projectId);
            if (CollectionUtils.isEmpty(projectProgressEOList)){
                throw new AdcDaBaseException("当前项目下没有创建阶段阶段！");
            }

            //循环构造一级项目阶段节点
            for (ProjectProgressEO projectProgressEO : projectProgressEOList){
                treeVOList.add(
                        new TreeVO(projectId+projectProgressEO.getStageNameId(), projectProgressEO.getStageName(),"" ,null,1)
                );
                //循环插入当前阶段的里程碑节点
                if(CollectionUtils.isEmpty(projectMilepostEOList)){
                    continue ;
                }
                for (ProjectMilepostEO projectMilepostEO : projectMilepostEOList){
                    if (StringUtils.contains(projectMilepostEO.getStageId(),projectProgressEO.getStageNameId())){
                        treeVOList.add(
                                new TreeVO(projectMilepostEO.getId(), projectMilepostEO.getMilepostName() ,"",projectMilepostEO.getStageId(),2)
                        );
//                        //循环插入成果物节点
//                        List<ProjectMilepostResultEO> projectMilepostResultEOList = projectMilepostResultEOService.selectByMilepostId(projectMilepostEO.getId());
//                        for (ProjectMilepostResultEO projectMilepostResultEO : projectMilepostResultEOList){
//                            treeVOList.add(
//                                    new TreeVO(projectMilepostResultEO.getId(), projectMilepostResultEO.getFileName() , projectMilepostResultEO.getFileId(),projectMilepostEO.getId(),3)
//                            );
//                        }
                    }
                }
            }

            return treeVOList;

        } else {
            throw new AdcDaBaseException("projectId is null");
        }

    }


}
