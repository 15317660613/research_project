package com.adc.da.research.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.research.dao.HiMemberEODao;
import com.adc.da.research.entity.HiMemberEO;
import com.adc.da.research.entity.MemberEO;
import com.adc.da.research.page.HiMemberEOPage;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.adc.da.research.utils.CompareObject.READ_ONLY;
import static com.adc.da.research.utils.CompareObject.WRITE_DIFFERENT;

/**
 * <br>
 * <b>功能：</b>RS_HI_PROJECT_MEMBER HiProjectMemberEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-25 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("hiMemberEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class HiMemberEOService extends BaseService<HiMemberEO, String> {

    @Autowired
    private HiMemberEODao dao;

    @Autowired
    private MemberEOService memberEOService;

    @Autowired
    private InfoEOService infoEOService;

    @Autowired
    private HiMemberEOService hiProjectMemberEOService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ChildTaskRepository childTaskRepository;

    public HiMemberEODao getDao() {
        return dao;
    }

    /**
     * 新增
     *
     * @param eo
     * @return
     * @throws Exception
     */
    @Override
    public int insertSelective(HiMemberEO eo) throws Exception {
        HiMemberEOPage queryPage = new HiMemberEOPage();
        queryPage.setMemberNameId(eo.getMemberNameId());
        queryPage.setResearchProjectId(eo.getResearchProjectId());
        queryPage.setProcBusinessKey(eo.getProcBusinessKey());
        if (dao.queryByCount(queryPage) == 0) {
            eo.setId(UUID.randomUUID10());
            /*
             * 设置mask
             */
            eo.initMask(new HiMemberEO(), READ_ONLY);
            return this.getDao().insertSelective(eo);
        } else {
            return -1;
        }
    }

    /**
     * @param id
     * @param key
     * @throws Exception
     */
    public void deleteByPrimaryKey(String id, String key) throws Exception {
        HiMemberEO hiMemberEO = hiProjectMemberEOService.selectByPrimaryKey(id);
        if (null != hiMemberEO && StringUtils.isNotEmpty(hiMemberEO.getResearchProjectId())) {
            Project project = projectRepository.findById(hiMemberEO.getResearchProjectId());
            if (null != project) {
                Set<String> taskIdSet = new HashSet<>();
                List<Task> taskList = taskRepository.findByProjectIdAndTaskStatusAndDelFlagNot(
                        project.getId(),
                        ProjectStatusEnums.EXECUTE.getStatus(),
                        true);
                for (Task task : taskList) {
                    if (CommonUtil.arrayContains(task.getMemberIds(), hiMemberEO.getMemberNameId()) > -1
                            || StringUtils.equals(task.getApproveUserId(), hiMemberEO.getMemberNameId())) {
                        throw new AdcDaBaseException("您不能删除" + hiMemberEO.getMemberName() + ",该成员在项目下有任务！");
                    }
                    taskIdSet.add(task.getId());
                }
                if (CollectionUtils.isNotEmpty(taskIdSet)) {
                    List<ChildrenTask> childrenTaskList = childTaskRepository
                            .findByTaskIdInAndStatusAndDelFlagNot(taskIdSet, ProjectStatusEnums.EXECUTE.getStatus(), true);
                    for (ChildrenTask childrenTask : childrenTaskList) {
                        if (CommonUtil.arrayContains(childrenTask.getMemberIds(), hiMemberEO.getMemberNameId()) > -1
                                || StringUtils.equals(childrenTask.getApproveUserId(), hiMemberEO.getMemberNameId())) {
                            throw new AdcDaBaseException("您不能删除" + hiMemberEO.getMemberName() + ",该成员在项目下有子任务！");
                        }
                    }
                }
            }
        }
        HiMemberEO delHiMemberEO = new HiMemberEO();
        delHiMemberEO.setId(id);
        delHiMemberEO.setProcBusinessKey(key);
        this.getDao().deleteByPrimaryKey(delHiMemberEO);
    }

    /**
     * 进行字段对比，对比完成后进行重名校验
     *
     * @param vo
     * @return
     * @throws Exception
     */
    public int updateAndSetMask(HiMemberEO vo) throws Exception {

        /*
         * 优先进行重名校验
         */
        if (updateCheck(vo) == -1) {
            return -1;
        }

        infoEOService.checkAndUpdateStatus(vo);
        String id = vo.getId();

        /*
         *  获取历史数据，进行转换
         */
        MemberEO sourceEO = memberEOService.selectByPrimaryKey(id);
        HiMemberEO target = new HiMemberEO();
        if (sourceEO != null) {
            /*
             * 证明为非新增数据
             */
            BeanUtils.copyProperties(sourceEO, target);
        }
        /*
         * 更新差异标识，同时选择性更改target
         * 初始化BusinessKey
         */
        target.initMask(vo, WRITE_DIFFERENT);
        target.setId(id);
        target.setProcBusinessKey(vo.getProcBusinessKey());
        target.setResearchProjectId(vo.getResearchProjectId());
        return this.getDao().updateByPrimaryKeySelective(target);
    }

    /**
     * 进行重名校验
     *
     * @param t
     * @return
     * @throws Exception
     */
    public int updateCheck(HiMemberEO t) {
        HiMemberEOPage queryPage = new HiMemberEOPage();
        queryPage.setMemberNameId(t.getMemberNameId());
        queryPage.setResearchProjectId(t.getResearchProjectId());
        queryPage.setProcBusinessKey(t.getProcBusinessKey());
        List<HiMemberEO> queryList = dao.queryByList(queryPage);
        if (CollectionUtils.isNotEmpty(queryList)) {
            if (1 == queryList.size() && t.getId().equals(queryList.get(0).getId())) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return 0;
        }
    }

}
