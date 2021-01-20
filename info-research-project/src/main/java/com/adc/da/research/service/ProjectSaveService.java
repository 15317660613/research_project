package com.adc.da.research.service;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.repository.UserWithProjectsRepository;
import com.adc.da.budget.service.ProjectService;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.capital.dao.CapitalBudgetEODao;
import com.adc.da.capital.dao.CapitalExpenditureEODao;
import com.adc.da.capital.page.CapitalBudgetEOPage;
import com.adc.da.capital.page.CapitalExpenditureEOPage;
import com.adc.da.login.util.UserUtils;
import com.adc.da.oa.service.ProjectPoolService;
import com.adc.da.progress.service.ProjectRateEOService;
import com.adc.da.research.dao.*;
import com.adc.da.research.entity.*;
import com.adc.da.research.page.*;
import com.adc.da.research.vo.CheckBaseVO;
import com.adc.da.research.vo.CheckMidtermVO;
import com.adc.da.research.vo.CheckPointVO;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.adc.da.budget.constant.ProjectType.NO_BUSINESS_PROJECT;
import static com.adc.da.budget.constant.ProjectType.RESEARCH_PROJECT;

/**
 * <br>
 * <b>功能：</b>RS_PROJECT_INFO InfoEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectSaveService")
@Transactional(value = "transactionManager", readOnly = false,
        propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectSaveService {

    @Autowired
    private InfoEODao infoEODao;

    @Autowired
    private MemberEODao memberEODao;

    @Autowired
    private KpiEODao kpiEODao;
    @Autowired
    private UserEODao userEODao;
    @Autowired
    private OrgEODao orgEODao;

    @Autowired
    private CapitalBudgetEODao capitalBudgetEODao;

    @Autowired
    private CapitalExpenditureEODao capitalExpenditureEODao;

    @Autowired
    private ProgressEODao progressEODao;

    @Autowired
    private ProjectDocEODao projectDocEODao;

    @Autowired
    private SummaryEODao summaryEODao;

    @Autowired
    ProjectRateEOService projectRateEOService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private KpiDeliverableEODao kpiDeliverableEODao;

    @Autowired
    private OrgListDao orgListDao;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;

    @Autowired
    private OrgEOService orgEOService;

    @Autowired
    private UserEOService userEOService;
    /**
     * 初始化查询条件
     * 0 "基本信息",
     */
    private static final int INT_INFO = 0;

    /**
     * 1 "成员信息",
     */
    private static final int INT_MEMBER = 1;

    /**
     * 2 "项目简介",
     */
    private static final int INT_SUMMARY = 2;

    /**
     * 3 "考核指标",
     */
    private static final int INT_KPI = 3;

    /**
     * 4 "交付物的技术指标"
     */
    private static final int INT_KPI_DELIVERABLE = 4;

    /**
     * 5 "工作进度安排",
     */
    private static final int INT_PROGRESS = 5;

    /**
     * 6 "资金来源",
     */
    private static final int INT_BUDGET = 6;

    /**
     * 7 "资金支出",
     */
    private static final int INT_EXPENDITURE = 7;

    /**
     * 8 type=1 "实施方案",
     * type=2 "提交项目申请书"
     */
    private static final int INT_DOC = 8;

    /**
     * 9 "提交项目申请书"
     */
    private static final int INT_DOC_2 = 9;

    /**
     * 全表单查询条件初始化
     *
     * @return
     */
    private RSBasePage[] initRSPage(String projectId) {
        return initRSPage(projectId, false);
    }

    private RSBasePage[] initRSPage(String projectId, Boolean flag) {
        RSBasePage[] rsBasePages;
        if (flag) {
            rsBasePages = new RSBasePage[10];
            rsBasePages[INT_DOC_2] = new ProjectDocEOPage();
        } else {
            rsBasePages = new RSBasePage[9];
        }
        rsBasePages[INT_INFO] = new InfoEOPage();
        rsBasePages[INT_MEMBER] = new MemberEOPage();
        rsBasePages[INT_SUMMARY] = new SummaryEOPage();
        rsBasePages[INT_KPI] = new KpiEOPage();
        rsBasePages[INT_PROGRESS] = new ProgressEOPage();
        rsBasePages[INT_BUDGET] = new CapitalBudgetEOPage();
        rsBasePages[INT_EXPENDITURE] = new CapitalExpenditureEOPage();
        rsBasePages[INT_DOC] = new ProjectDocEOPage();
        rsBasePages[INT_KPI_DELIVERABLE] = new KpiDeliverableEOPage();
        /*
         * 设置查询条件，批量编辑产生
         */
        for (RSBasePage page : rsBasePages) {
            page.setResearchProjectId(projectId);
        }
        return rsBasePages;
    }

    /**
     * 返回节点审核数据
     */
    public CheckPointVO checkpointData(String projectId, boolean showAll) {
        /*
         * 初始化查询条件
         * 0 "基本信息",
         * 1 "成员信息",
         * 2 "项目简介",
         * 3 "考核指标",  4"交付物的技术指标"
         * 5 "工作进度安排",
         * 6 "资金来源",
         * 7 "资金支出",
         * 8 "实施方案","提交项目申请书"
         */

        RSBasePage[] rsBasePages = initRSPage(projectId);
        /*
         * 设置负责人标识
         */
        ((MemberEOPage) rsBasePages[INT_MEMBER]).setLeaderFlag(1);
        /*
         * 筛选未发起的 工作内容
         */
        if (!showAll) {
            ((ProgressEOPage) rsBasePages[INT_PROGRESS]).setExtInfo1(0);
            ((ProgressEOPage) rsBasePages[INT_PROGRESS]).setExtInfo2("-1");
        }

        /*
         * 0,1,3
         */
        List<InfoEO> infoEOList = infoEODao.queryByList(rsBasePages[INT_INFO]);
        List<MemberEO> memberEOList = memberEODao.queryByList(rsBasePages[INT_MEMBER]);
        List<ProgressEO> progressEOList = progressEODao.queryByList(rsBasePages[INT_PROGRESS]);
        if (CollectionUtils.isNotEmpty(infoEOList)
                && CollectionUtils.isNotEmpty(memberEOList)
                && CollectionUtils.isNotEmpty(progressEOList)) {
            CheckPointVO result = new CheckPointVO();

            initCheckBaseVO(result, memberEOList.get(0), infoEOList.get(0));

            int len = progressEOList.size();
            String[][] detailMap = new String[len][2];

            for (int i = 0; i < len; i++) {
                ProgressEO progressEO = progressEOList.get(i);
                detailMap[i][0] = progressEO.getId();
                detailMap[i][1] = progressEO.getCheckDetail();
            }

            result.setCheckDetailArray(detailMap);

            return result;
        } else if (CollectionUtils.isEmpty(progressEOList)) {
            throw new AdcDaBaseException("该项目 无未完成的 检查内容");
        } else {
            throw new AdcDaBaseException("checkpointData 项目信息不全");

        }

    }

    /**
     * 进行科研表单校验
     */
    public CheckFormEO researchProjectCheck(String projectId) {

        /*
         * 初始化查询条件
         */
        RSBasePage[] rsBasePages = initRSPage(projectId, true);

        BaseDao[] baseDaoArray = new BaseDao[10];
        baseDaoArray[INT_INFO] = infoEODao;
        baseDaoArray[INT_MEMBER] = memberEODao;
        baseDaoArray[INT_SUMMARY] = summaryEODao;
        baseDaoArray[INT_KPI] = kpiEODao;
        baseDaoArray[INT_KPI_DELIVERABLE] = kpiDeliverableEODao;

        baseDaoArray[INT_PROGRESS] = progressEODao;
        baseDaoArray[INT_BUDGET] = capitalBudgetEODao;
        baseDaoArray[INT_EXPENDITURE] = capitalExpenditureEODao;
        baseDaoArray[INT_DOC] = projectDocEODao;
        /*   "实施方案"*/
        ((ProjectDocEOPage) rsBasePages[INT_DOC]).setDocType("1");
        /*"提交项目申请书"*/
        baseDaoArray[INT_DOC_2] = projectDocEODao;
        ((ProjectDocEOPage) rsBasePages[INT_DOC_2]).setDocType("2");

        int[] count = new int[10];
        for (int i = 0; i < 10; i++) {
            count[i] = baseDaoArray[i].queryByCount(rsBasePages[i]);
        }

        String[] str = new String[]{"基本信息",
                "成员信息",
                "项目简介",
                "考核指标", "考核指标-交付物的技术指标",
                "工作进度安排",
                "资金来源",
                "资金支出",
                "实施方案",
                "提交项目申请书"
        };

        CheckFormEO checkFormEO = new CheckFormEO();
        checkFormEO.setCount(count);
        checkFormEO.setStr(str);
        return checkFormEO;

    }

    /**
     * 应用于存储Oracle科研数据到ES
     *
     * @param projectId
     * @param createUserId
     * @return
     * @throws Exception
     */
    public Project save(String projectId, String createUserId) throws Exception {
        return save(projectId, createUserId, true);
    }

    /**
     * 存储科研项目信息到ES,
     * 同时 初始化项目进度信息
     *
     * @param projectId
     * @param createUserId
     * @return
     * @throws NoSuchMethodException
     */
    public Project save(String projectId, String createUserId, boolean isNotUpdate) throws Exception {
        InfoEO infoEO = infoEODao.selectByPrimaryKey(projectId);
        /*
         * 初始化eo
         */
        Project resultEO = initResultEO(projectId, createUserId, infoEO);

        /*
         * 存入ES
         */
        this.insert(resultEO, isNotUpdate);
        /*
         * 表示该项目已启动
         */
        infoEO.setExtInfo4("1");
        infoEODao.updateByPrimaryKeySelective(infoEO);
        /*
         * 初始化项目进度信息
         */
        if (isNotUpdate) {
            projectRateEOService.initProjectProgressData(projectId, RESEARCH_PROJECT.toString());
        }

        return (resultEO);
    }

    /**
     * 初始化脚本信息
     *
     * @param projectId
     * @param createUserId
     * @param infoEO
     * @return
     */
    private Project initResultEO(String projectId, String createUserId, InfoEO infoEO) {
        MemberEOPage queryMember = new MemberEOPage();
        queryMember.setOrderBy("LEADER_FLAG_ DESC, EXT_INFO_1_");
        queryMember.setResearchProjectId(projectId);

        List<MemberEO> memberList = memberEODao.queryByList(queryMember);

        String contractNO = infoEO.getExtInfo2();

        Project resultEO = new Project();
        resultEO.setId(projectId);

        resultEO.setProjectBeginTime(infoEO.getResearchProjectBeginTime());
        resultEO.setProjectEndTime(infoEO.getResearchProjectEndTime());

        resultEO.setName(infoEO.getResearchProjectName());
        resultEO.setContractNo(contractNO);

        //业务类型
        resultEO.setBusinessId(infoEO.getResearchProjectTypeId());
        resultEO.setBusiness(infoEO.getResearchProjectTypeName());
        //业务id
        resultEO.setBudgetId(infoEO.getBusinessId());
        resultEO.setBudget(infoEO.getBusinessName());

        //负责人
        MemberEO leader = memberList.get(0);

        resultEO.setProjectLeaderId(leader.getMemberNameId());

        resultEO.setCreateUserId(createUserId);

        resultEO.setProjectLeader(leader.getMemberName());
        /* 所属部门 */
        resultEO.setDeptId(infoEO.getOwnDepartmentId());
        //项目组成员（包含负责人）
        int index = 0;
        int length = memberList.size();
        String[] memberIds = new String[length];
        StringBuilder memberName = new StringBuilder();
        String[] memberNames = new String[length];
        MemberEO memberEO;
        List<Map<String, String>> mapList = new ArrayList<>();
        Map<String, String> map;

        for (; index < length; index++) {
            memberEO = memberList.get(index);
            String id = memberEO.getMemberNameId();
            String name = memberEO.getMemberName();

            memberName.append(name).append(",");
            memberIds[index] = id;
            memberNames[index] = name;
            map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            mapList.add(map);
        }

        resultEO.setProjectMemberNames(memberName.substring(0, memberName.length() - 1));
        resultEO.setProjectMemberIds(memberIds);
        resultEO.setMemberNames(memberNames);
        resultEO.setMapList(mapList);
        List<Map<String, String>> userIdDeptNameKv = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(memberIds)) {
            List<String> projectMemberIdList = new ArrayList<>(Arrays.asList(memberIds));
            List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(projectMemberIdList);
            userIdDeptNameKv = CommonUtil.userIdDeptNamesMapKv(userEOList,orgEODao);
        }
        resultEO.setUserIdDeptNameMapList(userIdDeptNameKv);

        resultEO.setProjectType(RESEARCH_PROJECT);
        return resultEO;
    }

    /**
     * 中期检查
     *
     * @param projectId
     */
    public CheckMidtermVO checkMidtermData(String projectId) {
        CheckMidtermVO result = new CheckMidtermVO();
        RSBasePage[] rsBasePages = initRSPage(projectId);
        /*
         * 设置负责人标识
         */
        ((MemberEOPage) rsBasePages[INT_MEMBER]).setLeaderFlag(1);

        KpiDeliverableEOPage kpiDeliverableEOPage = new KpiDeliverableEOPage();
        kpiDeliverableEOPage.setResearchProjectId(projectId);

        List<InfoEO> infoEOList = infoEODao.queryByList(rsBasePages[INT_INFO]);
        List<MemberEO> memberEOList = memberEODao.queryByList(rsBasePages[INT_MEMBER]);
        List<KpiEO> kpiEOList = kpiEODao.queryByList(rsBasePages[INT_KPI]);
        List<SummaryEO> summaryEOList = summaryEODao.queryByList(rsBasePages[INT_SUMMARY]);
        List<KpiDeliverableEO> kpiDeliverableEOList = kpiDeliverableEODao.queryByList(kpiDeliverableEOPage);

        if (CollectionUtils.isNotEmpty(infoEOList)
                && CollectionUtils.isNotEmpty(memberEOList)
                && CollectionUtils.isNotEmpty(kpiEOList)
                && CollectionUtils.isNotEmpty(summaryEOList)
                && CollectionUtils.isNotEmpty(kpiDeliverableEOList)) {
            initCheckBaseVO(result, memberEOList.get(0), infoEOList.get(0));

            /*
             * 设置目标，研究内容
             */
            SummaryEO summaryEO = summaryEOList.get(0);
            result.setTarget(summaryEO.getTotalTarget());
            result.setContent(summaryEO.getResearchProjectContent());

            /*
             * 拼接考核指标
             * 发明专利数：X；实用新型数：X；外观专利数：X；软件著作权：X；目标刊物中国内外核心论文：X；其他论文：X；
             * 交付物：
             * 1、某某交付物（交付物的技术指标：XXXX）
             * 2、某某交付物（交付物的技术指标：XXXX）
             */
            KpiEO kpiEO = kpiEOList.get(0);

            StringBuilder builder = new StringBuilder();
            builder.append("发明专利数：").append(kpiEO.getNumInventionPatents()).append('；')
                    .append("实用新型数：").append(kpiEO.getNumUtilityModels()).append('；')
                    .append("外观专利数：").append(kpiEO.getNumAppearancePatents()).append('；')
                    .append("软件著作权：").append(kpiEO.getNumCopyright()).append('；')
                    .append("目标刊物中国内外核心论文：").append(kpiEO.getNumCorePapers()).append('；')
                    .append("其他论文：").append(kpiEO.getNumOtherPapers()).append('；')
                    .append(System.lineSeparator());

            int len = kpiDeliverableEOList.size();
            if (len > 0) {
                builder.append("交付物：")
                        .append(System.lineSeparator());
                /*
                 * 1、某某交付物（交付物的技术指标：XXXX）
                 */
                for (int i = 0; i < len; i++) {
                    builder.append(i + 1).append('、')
                            .append(kpiDeliverableEOList.get(i).getName())
                            .append("（交付物的技术指标：").append(kpiDeliverableEOList.get(i).getKpi()).append('）')
                            .append(System.lineSeparator());
                }
            }

            result.setAssessmentIndex(builder.toString());
            return result;

        } else {
            throw new AdcDaBaseException("checkpointData 项目信息不全");

        }

    }

    /**
     * * 初始化
     * * 项目基本信息
     * * 所属部门基本信息
     * * 负责人信息
     *
     * @param result
     * @param memberEO
     * @param infoEO
     * @return
     * @author Lee Kwanho 李坤澔
     * date 2019-09-03
     **/
    private void initCheckBaseVO(CheckBaseVO result, MemberEO memberEO, InfoEO infoEO) {

        result.setResearchProjectId(infoEO.getResearchProjectId());
        result.setResearchProjectName(infoEO.getResearchProjectName());
        result.setOwnDepartmentName(infoEO.getOwnDepartmentName());
        result.setOwnDepartmentId(infoEO.getOwnDepartmentId());
        result.setLeaderId(memberEO.getMemberNameId());
        result.setLeaderName(memberEO.getMemberName());

    }

    @Autowired
    private BudgetEODao budgetEODao;

    @Autowired
    private ProjectPoolService projectPoolService;

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * 应用于 科研类与经营类项目的新建操作
     *
     * @param projectEO 从表单获取数据
     * @return projectEO
     * @author Lee Kwanho 李坤澔
     * date 2018-12-04
     **/
    public Project insert(Project projectEO, boolean isNotUpdate) {
        if (projectEO == null) {
            throw new AdcDaBaseException("projectEO is null");
        }
        //查询改业务下是否有重名的项目
        checkSameProjectName(projectEO);
        /*
         * 设置日期
         */
        if (StringUtils.isEmpty(projectEO.getId())) {
            throw new AdcDaBaseException("项目id不能为空");
        }
        if (null == projectEO.getCreateTime()) {
            projectEO.setCreateTime(new Date());
        }
        projectEO.setModifyTime(new Date());
        /*
         * 设置 setProjectStartTime
         */
        projectEO.setProjectStartTime(projectEO.getStartTime());
        /*
         * 若开始时间为空，将startTime设置为BeginTime
         */
        if (StringUtils.isEmpty(projectEO.getProjectBeginTime())) {
            projectEO.setProjectBeginTime(projectEO.getStartTime());
        }
        projectEO.setFinishedStatus(ProjectStatusEnums.EXECUTE.getStatus());
        BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(projectEO.getBudgetId());

        Set<String> memberIdSet = new HashSet<>();
        HashMap<String,List<String>> DeptUserIdMap = new HashMap<>();
        List<Map<String,String>> deptInfoListMap = projectEO.getDeptInfoListMap();// 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
        List<String> userIdList = new ArrayList<>();
        for(Map<String,String> map :deptInfoListMap){
            int type =  Integer.parseInt(map.get("type").toString());
            String deptId =  map.get("deptId").toString();
            Set<String> tempSet =new HashSet<>();
            if(type == 1){
                List<String> deptlist = new ArrayList<>();
                deptlist.add(deptId);
                List<UserEO> list = userEOService.getAllUserEOsByOrgIds(deptlist);//根据部门id查询成员
                for(UserEO userEO:list){
                    memberIdSet.add(userEO.getUsid());
                    tempSet.add(userEO.getUsid());
                }
                DeptUserIdMap.put(deptId,new ArrayList<>(tempSet));
            }else if(type == 2){
                List<OrgEO> childDeptList = orgEOService.getOrgEOByPid(deptId);//根据部门ID查询子部门
                for(OrgEO childDept:childDeptList){//遍历子部门的ID
                    String id = childDept.getId();
                    userIdList.add(id);
                }
                List<UserEO> list = userEOService.getAllUserEOsByOrgIds(userIdList);//根据部门id查询成员
                for(UserEO userEO:list){
                    memberIdSet.add(userEO.getUsid());
                    tempSet.add(userEO.getUsid());
                }
                DeptUserIdMap.put(deptId,new ArrayList<>(tempSet));
            }
        }
        projectEO.setDeptIdUserIdList(DeptUserIdMap);
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();
        String[] projectMemberIds = projectEO.getProjectMemberIds();
        if (CollectionUtils.isNotEmpty(memberIdSet)||CollectionUtils.isNotEmpty(projectMemberIds)) {
            Set<String> projectMemberIdSet = new HashSet<>(Arrays.asList(projectMemberIds));

            projectMemberIdSet.addAll(memberIdSet);

            if (StringUtils.isNotEmpty(projectEO.getProjectAdminId())) { //将项目管理员加入项目成员里面去
                projectMemberIdSet.add(projectEO.getProjectAdminId());
            }
            if (StringUtils.isNotEmpty(projectEO.getProjectLeaderId()) && !projectMemberIdSet
                    .contains(projectEO.getProjectLeaderId())) {
                projectMemberIdSet.add(projectEO.getProjectLeaderId());
            }

            projectEO.setProjectMemberIds(projectMemberIdSet.toArray(new String[projectMemberIdSet.size()]));
            List<String> projectMemberIdList = new ArrayList<>(projectMemberIdSet);
            List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(projectMemberIdList);
            String[] projectMemberNameArr = new String[userEOList.size()];
            for (int i = 0; i < userEOList.size(); i++) {
                UserEO userEO = userEOList.get(i);
                projectMemberNameArr[i] = userEO.getUsname();
            }
            projectEO.setMemberNames(projectMemberNameArr);
            projectEO.setProjectMemberNames(StringUtils.join(projectMemberNameArr, ','));
            projectEO.setMapList(CommonUtil.userIdUsnameMapKv(userEOList));
            projectEO.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList, orgEOList));
        }
        if (null != budgetEO) {
            projectEO.setPm(budgetEO.getPm());
            projectEO.setProjectTeam(budgetEO.getProjectTeam());
            projectEO.setBusinessCreateUserId(budgetEO.getCreateUserId());
            projectEO.setBusinessAdminId(budgetEO.getBusinessAdminId());
            projectEO.setBusinessAdminName(budgetEO.getBusinessAdminName());
            // 上面已加入memIdSet 会统一添加
//            if (StringUtils.isNotEmpty(projectEO.getBusinessAdminId())) {
//                UserWithProjects userWithProjects = userWithProjectsRepository.findOne(projectEO.getBusinessAdminId());
//                if (null == userWithProjects) {
//                    userWithProjects = new UserWithProjects();
//                    userWithProjects.setUserId(projectEO.getBusinessAdminId());
//                }
//                userWithProjects.getProjectIds().add(projectEO.getId());
//                userWithProjectsRepository.save(userWithProjects);
//            }
        }


        /* false == isNotUpdate */
        if (!isNotUpdate) {
            //对已有记录的删除
            deleteOldMemberInfo(projectEO);
            Project oldProject = projectRepository.findById(projectEO.getId());
            projectService.updateProjectAdmin(projectEO, oldProject, budgetEO);

        }
        //新增项目时，添加项目和人的关联关系
        projectPoolService.saveUserWithProject(projectEO.getProjectMemberIds(), projectEO);

        projectRepository.save(projectEO);
        return projectEO;
    }


    /***
     * @author zyh 新增判断业务下面的项目名是否重复
     * @param projectEO
     */
    private void checkSameProjectName(Project projectEO) {
        String budgetId = projectEO.getBudgetId();
        if (StringUtils.isNotEmpty(budgetId)) {
            List<Project> projects = projectRepository.findByBudgetIdAndDelFlagNot(budgetId, true);
            if (StringUtils.isNotEmpty(projects)) {
                for (Project project : projects) {
                    if (StringUtils.equals(projectEO.getName(), project.getName())&&!StringUtils.equals(project.getId(),projectEO.getId())) {
                        throw new AdcDaBaseException("该项目已存在！");
                    }
                    continue;
                }
            }
        }
    }

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ChildTaskRepository childTaskRepository;

    /**
     * 对旧数据进行删除
     *
     * @param projectEO
     */
    private void deleteOldMemberInfo(Project projectEO) {

        String[] projectLeader = new String[2];
        projectLeader[INT_NAME] = projectEO.getProjectLeader();
        projectLeader[INT_ID] = projectEO.getProjectLeaderId();

        String[] nowMemberIds = projectEO.getProjectMemberIds();
        String projectId = projectEO.getId();

        Project formEO = projectRepository.findOne(projectId);
        String[] formMemberIds = formEO.getProjectMemberIds();

        Set<String> removeUserIdSet = new TreeSet<>(Arrays.asList(formMemberIds));

       // List<Task> taskList = taskRepository.findByProjectIdAndDelFlagNot(projectEO.getId(),true);
//        Set<String> taskIdSet = new HashSet<>();
//        for (Task task : taskList){
//            taskIdSet.add(task.getId());
//        }
//        List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdInAndDelFlagNot(taskIdSet,true);
//        Set<String> childTaskIdSet = new HashSet<>();
//        for (ChildrenTask childrenTask : childrenTaskList){
//            taskIdSet.add(childrenTask.getId());
//        }
//        projectPoolService.saveUserWithProjectInTask();

        /*
         * 除去已有成员
         */
        List<String> nowMemberIdList = Arrays.asList(nowMemberIds);
        if (StringUtils.isNotEmpty(projectEO.getProjectAdminId())) {
            nowMemberIdList.add(projectEO.getProjectAdminId());
        }
        projectEO.setProjectMemberIds(nowMemberIdList.toArray(new String[nowMemberIdList.size()]));
        removeUserIdSet.removeAll(new HashSet<>(nowMemberIdList));

        Set<String> taskIdSet = new HashSet<>();
        List<Task> taskList = taskRepository.findByProjectIdAndDelFlagNot(projectId, Boolean.TRUE);
        if (CollectionUtils.isNotEmpty(removeUserIdSet)) {
            doTask(removeUserIdSet, taskIdSet, projectLeader, taskList);

            /*
             * 子任务
             */
            Set<String> childTaskIdSet = new HashSet<>();
            List<ChildrenTask> childrenTaskList = doChildTask(
                    childTaskIdSet,
                    taskList,
                    removeUserIdSet,
                    taskIdSet,
                    projectLeader);
            Set<String> projectIdSet = new TreeSet<>();

            projectIdSet.add(projectId);
            projectPoolService
                    .deleteUserWithProject(removeUserIdSet, projectIdSet, taskIdSet, childTaskIdSet, projectLeader[INT_ID]);
            saveESData(taskList, childrenTaskList);
        }
    }

    /**
     * 对子任务做处理
     *
     * @param childTaskIdSet
     * @param taskList
     * @param removeUserIdList
     * @param taskIdSet
     * @param projectLeader
     * @return
     */
    public List<ChildrenTask> doChildTask(Set<String> childTaskIdSet, List<Task> taskList,
                                          Set<String> removeUserIdList, Set<String> taskIdSet, String[] projectLeader) {
        List<ChildrenTask> childrenTaskList;
        if (CollectionUtils.isNotEmpty(taskList) && CollectionUtils.isNotEmpty(removeUserIdList)) {
            childrenTaskList = childTaskRepository
                    .findByTaskIdInAndDelFlagNot(
                            taskIdSet,
                            Boolean.TRUE);
            /*
             * 创建人替换创建人，成员替换成员，下级任务存在 A 创建 给B的情况
             */
            childrenTaskList.forEach(childrenTask -> {
                childTaskIdSet.add(childrenTask.getId());
                String childIdCreateId = childrenTask.getCreateUserId();
                String childPersonId = childrenTask.getPersonId();
                Set<String> memberIdSet = new HashSet<>(Arrays.asList(childrenTask.getMemberIds()));
                int removeIndex = CommonUtil.arrayContainsSetOne(childrenTask.getMemberIds(),memberIdSet);
                if (removeUserIdList.contains(childPersonId)||removeIndex>-1) {
                    childrenTask.setPersonId(projectLeader[INT_ID]);
                    childrenTask.setPersonName(projectLeader[INT_NAME]);
                    if (removeIndex>-1){
                        String [] memberIdArr = CommonUtil.removeOneByArrayIndex(childrenTask.getMemberIds(),removeIndex);
                        childrenTask.setMemberIds(memberIdArr);
                        if (CollectionUtils.isNotEmpty(memberIdArr)) {
                            List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(Arrays.asList(memberIdArr));
                            childrenTask.setMemberNames(CommonUtil.getUserNameArr(userEOList));
                            childrenTask.setMemberNameString(StringUtils.join(childrenTask.getMemberNames(),','));
                        }
                    }
                }
                if (removeUserIdList.contains(childIdCreateId)) {
                    childrenTask.setCreateUserId(projectLeader[INT_ID]);
                }

            });
        } else {
            childrenTaskList = new ArrayList<>();
        }
        return childrenTaskList;
    }


    /**
     * 对子任务做处理
     *
     * @param deleteUserIdSet
     * @param childrenTaskList
     * @param projectLeader
     * @return
     */
    public void doChildTaskNew(Set<String> deleteUserIdSet, List<ChildrenTask> childrenTaskList, String[] projectLeader) {
        Map<String,Set<String>> needRemoveUserIdAndChildRenTaskIdSetMap = new HashMap<>();
        for (ChildrenTask childrenTask : childrenTaskList) {
            List<Map<String, String>> oldTaskMapList = childrenTask.getMapsList();
            List<Map<String, String>> taskMapList = new ArrayList<>();

            Set<String> taskMemberIdSet = new HashSet<>();
           List<String> taskMemberNameList = new ArrayList<>();
            for (Map<String, String> map : oldTaskMapList) {
                //如果项目中被删除的成员存在任务成员中，那么要忽略掉
                String userId = map.get("id");
                if (!deleteUserIdSet.contains(userId)) {
                    taskMapList.add(map);
                    taskMemberIdSet.add(userId);
                    taskMemberNameList.add(map.get("name"));
                }else { //如果是要踢掉的，就得先看看是不是负责人，不是负责人，就直接放在待干掉的map里
                    if(!StringUtils.equals(childrenTask.getApproveUserId(),userId)){
                        Set<String> needRemoveTaskIdSet = needRemoveUserIdAndChildRenTaskIdSetMap.get(userId);
                        if (CollectionUtils.isEmpty(needRemoveTaskIdSet)) {
                            needRemoveTaskIdSet = new HashSet<>();
                            needRemoveTaskIdSet.add(childrenTask.getId());
                            needRemoveUserIdAndChildRenTaskIdSetMap.put(userId,needRemoveTaskIdSet);
                        }else {
                            needRemoveTaskIdSet.add(childrenTask.getId());
                            needRemoveUserIdAndChildRenTaskIdSetMap.put(userId,needRemoveTaskIdSet); //这句应该可以不要，为了保险起见 加上
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(taskMemberIdSet)) {
                List<String> projectMemberIdList = new ArrayList<>(taskMemberIdSet);
                List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(projectMemberIdList);
                childrenTask.setUserIdDeptNameMapList( CommonUtil.userIdDeptNamesMapKv(userEOList,orgEODao) );
                childrenTask.setMemberNames( CommonUtil.getUserNamesArrayList(userEOList).toArray(new String[userEOList.size()]) );
                childrenTask.setMemberNameString(StringUtils.join(childrenTask.getMemberNames(),','));
            }
            if(StringUtils.isEmpty(childrenTask.getApproveUserId())){
                childrenTask.setApproveUserId(projectLeader[INT_ID]);
                childrenTask.setApproveUserName(projectLeader[INT_NAME]);
            }
            childrenTask.setMapsList(taskMapList);
        }
        //根据needRemoveUserIdAndTaskIdSetMap 清除关系
        if (CollectionUtils.isNotEmpty(needRemoveUserIdAndChildRenTaskIdSetMap)) {
            Set<String> userIdSet = needRemoveUserIdAndChildRenTaskIdSetMap.keySet();
            List<UserWithProjects> userWithProjectsList = userWithProjectsRepository.findByUserIdIn(userIdSet);
            for (UserWithProjects userWithProjects : userWithProjectsList) {
                userWithProjects.getChildTaskIds().removeAll(needRemoveUserIdAndChildRenTaskIdSetMap.get(userWithProjects.getUserId()));
            }
            userWithProjectsRepository.save(userWithProjectsList);
        }
    }

    /**
     * 对子任务做处理
     *
     * @param deleteUserIdSet
     * @return
     */
    public void doBudgetDelNew(Set<String> deleteUserIdSet,Project project) {
        // 如果不在项目组里，那么也不一定能看到业务了？ 所以要查下，这个用户是否在这个业务的其他项目里？
        List<Project> projectInSameBudget = projectRepository.findByBudgetId(project.getBudgetId());
        Map<String, String> needRemoveUserIdBudgetIdMap = new HashMap<>();
        for (String userId : deleteUserIdSet) {
            boolean isNeedDelBudgetId = true; //当前业务下所有的项目成员都没有原任务成员，那就要从userWithProject里删掉
            for (Project eo : projectInSameBudget) {
                if (CommonUtil.arrayContainsSetOne(eo.getProjectMemberIds(), deleteUserIdSet) > -1) {
                    isNeedDelBudgetId = false;
                    break;
                }
            }
            if (isNeedDelBudgetId) {
                needRemoveUserIdBudgetIdMap.put(userId, project.getBudgetId());
            }
        }
        //needRemoveUserIdBudgetIdMap 清除关系
        if (CollectionUtils.isNotEmpty(needRemoveUserIdBudgetIdMap)) {
            Set<String> userIdSet = needRemoveUserIdBudgetIdMap.keySet();
            List<UserWithProjects> userWithProjectsList = userWithProjectsRepository.findByUserIdIn(userIdSet);
            for (UserWithProjects userWithProjects : userWithProjectsList) {
                userWithProjects.getChildTaskIds().remove(needRemoveUserIdBudgetIdMap.get(userWithProjects.getUserId()));
            }
            userWithProjectsRepository.save(userWithProjectsList);
        }
    }

    /**
     * 更新ES 相关数据，项目，任务，子任务，左侧项目树
     */
    private void saveESData(List<Task> taskList, List<ChildrenTask> childrenTaskList) {

        if (CollectionUtils.isNotEmpty(taskList)) {
            //存储 任务
            taskRepository.save(taskList);
        }
        if (CollectionUtils.isNotEmpty(childrenTaskList)) {
            //存储 任务
            childTaskRepository.save(childrenTaskList);
        }

    }

    private static final int INT_ID = 0;

    private static final int INT_NAME = 1;

    /**
     * 处理任务
     *
     * @param deleteUserId
     * @param taskIdSet
     * @param projectLeader
     */
    public void doTask(Set<String> deleteUserId, Set<String> taskIdSet,
                       String[] projectLeader, List<Task> taskList) {
        for (Task task : taskList) {
            taskIdSet.add(task.getId());
            String[] memberIds;

            List<Map<String, String>> taskMapListSource = task.getMapsList();
            List<Map<String, String>> taskMapList = new ArrayList<>();

            Set<String> idList = new TreeSet<>();
            Set<String> nameList = new TreeSet<>();
            boolean leaderFlag = false;
            for (Map<String, String> map : taskMapListSource) {
                String userId = map.get("id");
                /*
                 * 先判断是否是负责人
                 */
                if (projectLeader[INT_ID].equals(userId)) {
                    leaderFlag = true;
                }
                if (!deleteUserId.contains(userId)) {
                    taskMapList.add(map);
                    idList.add(userId);
                    nameList.add(map.get("name"));
                }
            }
            /*
             *  添加负责人相关信息
             */
            if (!leaderFlag) {
                Map<String, String> leaderInfo = new HashMap<>();
                leaderInfo.put("id", projectLeader[INT_ID]);
                leaderInfo.put("name", projectLeader[INT_NAME]);
                taskMapList.add(leaderInfo);
                idList.add(projectLeader[INT_ID]);
                nameList.add(projectLeader[INT_NAME]);
            }


            /*
             * 生成 相关属性
             */
            memberIds = idList.toArray(new String[0]);
          List<String> memberNamesList = new ArrayList<>();
            List<Map<String, String>> userIdDeptNameKv = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(memberIds)) {
                List<String> projectMemberIdList = new ArrayList<>(Arrays.asList(memberIds));
                List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(projectMemberIdList);
                memberNamesList = CommonUtil.getUserNameList(userEOList);
                userIdDeptNameKv = CommonUtil.userIdDeptNamesMapKv(userEOList,orgEODao);
            }
            if(StringUtils.isEmpty(task.getApproveUserId())){
                task.setApproveUserId(projectLeader[INT_ID]);
                task.setApproveUserName(projectLeader[INT_NAME]);
            }
            task.setCreateUserId(projectLeader[INT_ID]);
            task.setMemberIds(memberIds);
            task.setMemberNames(memberNamesList.toArray(new String[memberNamesList.size()]));
            task.setMemberNameString(StringUtils.join(memberNamesList,','));
            task.setMapsList(taskMapList);
            task.setUserIdDeptNameMapList(userIdDeptNameKv);
        }
    }
    /**
     * 处理任务
     *
     * @param deleteUserIdSet
     * @param projectLeader
     */
    public void doTaskNew(Set<String> deleteUserIdSet, String[] projectLeader, List<Task> taskList) {
        Map<String,Set<String>> needRemoveUserIdAndTaskIdSetMap = new HashMap<>();
        for (Task task : taskList) {
            List<Map<String, String>> oldTaskMapList = task.getMapsList();
            List<Map<String, String>> taskMapList = new ArrayList<>();

            Set<String> taskMemberIdSet = new HashSet<>();
            Set<String> taskMemberNameSet = new HashSet<>();
            for (Map<String, String> map : oldTaskMapList) {
                //如果项目中被删除的成员存在任务成员中，那么要忽略掉
                String userId = map.get("id");
                if (!deleteUserIdSet.contains(userId)) {
                    taskMapList.add(map);
                    taskMemberIdSet.add(userId);
                    taskMemberNameSet.add(map.get("name"));
                }else { //如果是要踢掉的，就得先看看是不是负责人，不是负责人，就直接放在待干掉的map里
                    if(!StringUtils.equals(task.getApproveUserId(),userId)){
                        Set<String> needRemoveTaskIdSet = needRemoveUserIdAndTaskIdSetMap.get(userId);
                        if (CollectionUtils.isEmpty(needRemoveTaskIdSet)) {
                            needRemoveTaskIdSet = new HashSet<>();
                            needRemoveTaskIdSet.add(task.getId());
                            needRemoveUserIdAndTaskIdSetMap.put(userId,needRemoveTaskIdSet);
                        }else {
                            needRemoveTaskIdSet.add(task.getId());
                            needRemoveUserIdAndTaskIdSetMap.put(userId,needRemoveTaskIdSet); //这句应该可以不要，为了保险起见 加上
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(taskMemberIdSet)) {
                List<String> projectMemberIdList = new ArrayList<>(taskMemberIdSet);
                List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(projectMemberIdList);
                task.setUserIdDeptNameMapList( CommonUtil.userIdDeptNamesMapKv(userEOList,orgEODao) );
                task.setMemberNames( CommonUtil.getUserNamesArrayList(userEOList).toArray(new String[userEOList.size()]) );
                task.setMemberNameString(StringUtils.join(task.getMemberNames(),','));
            }
            if(StringUtils.isEmpty(task.getApproveUserId())){
                task.setApproveUserId(projectLeader[INT_ID]);
                task.setApproveUserName(projectLeader[INT_NAME]);
            }
            task.setMapsList(taskMapList);
        }
        //根据needRemoveUserIdAndTaskIdSetMap 清除关系
        if (CollectionUtils.isNotEmpty(needRemoveUserIdAndTaskIdSetMap)) {
            Set<String> userIdSet = needRemoveUserIdAndTaskIdSetMap.keySet();
            List<UserWithProjects> userWithProjectsList = userWithProjectsRepository.findByUserIdIn(userIdSet);
            for (UserWithProjects userWithProjects : userWithProjectsList) {
              userWithProjects.getTaskIds().removeAll(needRemoveUserIdAndTaskIdSetMap.get(userWithProjects.getUserId()));
            }
            userWithProjectsRepository.save(userWithProjectsList);
        }
    }

    /**
     * 新建日常事务类
     **/
    public Project saveDailyProject(Project project) {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登录可能过期，请重新登录！");
        }
        //查询改业务下是否有重名的项目
        checkSameProjectName(project);
//        UserEO userEO = userEODao.getUserWithRoles(userId);
        //id 随机生成
        project.setId(UUID.randomUUID10());
        //创建人
        project.setCreateUserId(userEO.getUsid());
        project.setCreateUserName(userEO.getUsname());
        //删除标记
        project.setDelFlag(Boolean.FALSE);

        //部门
        List<OrgWithLevelEO> orgEOList = orgListDao.getUserOrgWhitLeafAndLev(userEO.getUsid());
        if (CollectionUtils.isNotEmpty(orgEOList)) {
            project.setDeptId(orgEOList.get(0).getId());
        }
        //项目类型，日常事务类
        project.setProjectType(NO_BUSINESS_PROJECT);
        Set<String> memberIdSet = new HashSet<>();
        HashMap<String,List<String>> DeptUserIdMap = new HashMap<>();
        List<Map<String,String>> deptInfoListMap = project.getDeptInfoListMap();// 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
        Map<String,List<String>> deptIdUserIdList = project.getDeptIdUserIdList();//所选部门id 及部门下用户id List
        List<String> userIdList = new ArrayList<>();
        for(Map<String,String> map :deptInfoListMap){
            int type =  Integer.parseInt(map.get("type").toString());
            String deptId =  map.get("deptId").toString();
            Set<String> tempSet =new HashSet<>();
            if(type == 1){
                List<String> deptlist = new ArrayList<>();
                deptlist.add(deptId);
                List<UserEO> list = userEOService.getAllUserEOsByOrgIds(deptlist);//根据部门id查询成员
                for(UserEO userEO1:list){
                    memberIdSet.add(userEO1.getUsid());
                    tempSet.add(userEO1.getUsid());
                }
                DeptUserIdMap.put(deptId,new ArrayList<>(tempSet));
            }else if(type == 2){
                List<OrgEO> childDeptList = orgEOService.getOrgEOByPid(deptId);//根据部门ID查询子部门
                for(OrgEO childDept:childDeptList){//遍历子部门的ID
                    String id = childDept.getId();
                    userIdList.add(id);
                }
                List<UserEO> list = userEOService.getAllUserEOsByOrgIds(userIdList);//根据部门id查询成员
                for(UserEO userEO1:list){
                    memberIdSet.add(userEO1.getUsid());
                    tempSet.add(userEO1.getUsid());
                }
                DeptUserIdMap.put(deptId,new ArrayList<>(tempSet));
            }
        }
        project.setDeptIdUserIdList(DeptUserIdMap);
        List<OrgEO> allOrgEOList = orgEODao.queryOrgAll();
        String[] projectMemberIds = project.getProjectMemberIds();
        Set<String> projectMemberIdSet = new HashSet<>(Arrays.asList(projectMemberIds));
        if (StringUtils.isNotEmpty(project.getProjectLeaderId())) {
            projectMemberIdSet.add(project.getProjectLeaderId());
        }
        if (StringUtils.isNotEmpty(project.getProjectAdminId())) {
            projectMemberIdSet.add(project.getProjectAdminId());
        }
        if (CollectionUtils.isNotEmpty(memberIdSet)||CollectionUtils.isNotEmpty(projectMemberIds)) {
            projectMemberIdSet.addAll(memberIdSet);
            if (StringUtils.isNotEmpty(project.getProjectAdminId())) { //将项目管理员加入项目成员里面去
                projectMemberIdSet.add(project.getProjectAdminId());
            }
            if (StringUtils.isNotEmpty(project.getProjectLeaderId()) && !projectMemberIdSet
                    .contains(project.getProjectLeaderId())) {
                projectMemberIdSet.add(project.getProjectLeaderId());
            }

            project.setProjectMemberIds(projectMemberIdSet.toArray(new String[projectMemberIdSet.size()]));
            List<String> projectMemberIdList = new ArrayList<>(projectMemberIdSet);
            List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(projectMemberIdList);
            String[] projectMemberNameArr = new String[userEOList.size()];
            for (int i = 0; i < userEOList.size(); i++) {
                UserEO userEO1 = userEOList.get(i);
                projectMemberNameArr[i] = userEO1.getUsname();
            }
            project.setMemberNames(projectMemberNameArr);
            project.setProjectMemberNames(StringUtils.join(projectMemberNameArr, ','));
            project.setMapList(CommonUtil.userIdUsnameMapKv(userEOList));
            project.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList, allOrgEOList));
        }
        project.setFinishedStatus(ProjectStatusEnums.EXECUTE.getStatus());
        project.setModifyTime(new Date());
        project.setCreateTime(new Date());
        BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(project.getBudgetId());
        if (null != budgetEO) {
            project.setPm(budgetEO.getPm());
            project.setProjectTeam(budgetEO.getProjectTeam());
            project.setBusinessCreateUserId(budgetEO.getCreateUserId());
            project.setBusinessAdminId(budgetEO.getBusinessAdminId());
            project.setBusinessAdminName(budgetEO.getBusinessAdminName());
            // admin 已经在memberIdSet 中 不需要外插入
//            if (StringUtils.isNotEmpty(project.getBusinessAdminId())) {
//                UserWithProjects userWithProjects = userWithProjectsRepository.findOne(project.getBusinessAdminId());
//                if (null == userWithProjects) {
//                    userWithProjects = new UserWithProjects();
//                    userWithProjects.setUserId(project.getBusinessAdminId());
//                }
//                userWithProjects.getProjectIds().add(project.getId());
//                userWithProjectsRepository.save(userWithProjects);
//            }
        }

        //新增项目时，添加项目和人的关联关系
        projectPoolService.saveUserWithProject(project.getProjectMemberIds(), project);

        projectRepository.save(project);
        return project;
    }
}
