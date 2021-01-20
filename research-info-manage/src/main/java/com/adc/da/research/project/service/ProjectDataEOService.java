package com.adc.da.research.project.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.config.dao.ProcessStageEODao;
import com.adc.da.research.config.entity.ProcessStageEO;
import com.adc.da.research.config.page.ProcessStageEOPage;
import com.adc.da.research.project.dao.BudgetDetailHistoryEODao;
import com.adc.da.research.project.dao.BudgetFundHistoryEODao;
import com.adc.da.research.project.dao.CheckTargetHistoryEODao;
import com.adc.da.research.project.dao.ImplementationProcEODao;
import com.adc.da.research.project.dao.MemberInfoHistoryEODao;
import com.adc.da.research.project.dao.ProjectDataEODao;
import com.adc.da.research.project.dao.RContractInfoEODao;
import com.adc.da.research.project.dao.WorkProgressHistoryEODao;
import com.adc.da.research.project.dto.ProjectDataEODto;
import com.adc.da.research.project.entity.*;
import com.adc.da.research.project.enumcase.RoleEnum;
import com.adc.da.research.project.page.*;
import com.adc.da.research.project.poi.ScheduleTradeTablePolicy;
import com.adc.da.research.project.poi.domain.CatarcData;
import com.adc.da.research.project.poi.domain.ResearchProjectsData;
import com.adc.da.research.project.poi.table.ResearchContentData;
import com.adc.da.research.project.vo.ProjectContractInfoVO;
import com.adc.da.research.project.vo.ProjectDataVO;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.page.DicTypeEOPage;
import com.adc.da.sys.service.DicTypeEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.UUID;
import com.adc.da.util.utils.*;
import com.adc.da.word.XWPFTemplate;
import com.adc.da.word.config.Configure;
import com.adc.da.word.datas.RowRenderData;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <br>
 * <b>功能：</b>RS_PROJECT_DATA ProjectDataEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectDataEOService")
@Slf4j
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectDataEOService extends BaseService<ProjectDataEO, String> {

    public static final String RESEARCH_ADMIN = "科研管理员";

    private static final Logger logger = LoggerFactory.getLogger(ProjectDataEOService.class);

    @Autowired
    private ProjectDataEODao dao;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private JudgeInfoEOService judgeInfoEOService;

    @Autowired
    private DicTypeEOService dicTypeEOService;
    @Autowired
    private OrgEODao orgEODao;

    @Autowired
    private ScoreInfoEOService scoreInfoEOService;

    @Autowired
    private ProjectDataEOService projectDataEOService;

    @Autowired
    private UserEOService userService;

    @Autowired
    private ImplementationProcEOService implementationProcEOService;

    @Autowired
    private ImplementationProcFileEOService implementationProcFileEOService;

    @Autowired
    private AnnexFileEOService annexFileEOService;

    @Autowired
    private ProcessStageEODao processStageEODao;

    @Autowired
    private RContractInfoEODao rContractInfoEODao;

    @Autowired
    private ImplementationProcEODao implementationProcEODao;

    @Autowired
    private MemberInfoEOService memberInfoEOService;
    @Autowired
    private WorkProgressEOService workProgressEOService;


    @Autowired
    private CheckTargetEOService checkTargetEOService;

    @Autowired
    private ResearchBudgetDetailEOService researchBudgetDetailEOService;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private BudgetDetailEOService budgetDetailEOService;

    @Autowired
    private BudgetFundEOService budgetFundEOService;

    @Autowired
    private BudgetFundHistoryEODao budgetFundHistoryEODao;

    @Autowired
    private BudgetDetailHistoryEODao budgetDetailHistoryEODao;

    @Autowired
    private WorkProgressHistoryEODao workProgressHistoryEODao;

    @Autowired
    private CheckTargetHistoryEODao checkTargetHistoryEODao;

    @Autowired
    private MemberInfoHistoryEODao memberInfoHistoryEODao;


    @Value("${RAS_PRI_KEY:}")
    private String RSAprivateKey;

    public ProjectDataEODao getDao() {
        return dao;
    }

    /**
     * 导出项目一览信息数据
     *
     * @param exportParams
     * @return
     * @throws Exception
     */
    public Workbook exportProjectData(ExportParams exportParams, ProjectDataEOPage eoPage) throws Exception {

        List<ProjectDataEO> list = this.queryByPage(eoPage);

        List<ProjectDataEODto> resutList = new ArrayList<>();
        if (list.size() > 0) {
            resutList = beanMapper.mapList(list, ProjectDataEODto.class);
        }
        return ExcelExportUtil.exportExcel(exportParams, ProjectDataEODto.class, resutList);
    }


    public List<ProjectDataEO> queryDeclareByPage(ProjectDataEOPage page) throws Exception {
        Integer rowCount = this.getDao().queryDeclareByCount(page);
        page.getPager().setRowCount(rowCount);
        return this.getDao().queryDeclareByPage(page);
    }
    /**
     * 项目申报列表加当前项目的专家人员
     *
     * @param page
     * @return
     * @throws Exception
     */
    public List<ProjectDataEO> queryBydeclare(ProjectDataEOPage page) throws Exception {

        List<ProjectDataEO> rows = new ArrayList<>();
        List<ProjectDataEO> declareRows = new ArrayList<>();


        //查询项目中对应的专家人员
       // page.setStageName("项目申报");
        List<ProjectDataEO> expertRows = this.queryDeclareByPage(page);
        List<ProjectDataEO> expertDeclareRows = new ArrayList<>();
        List<JudgeInfoEO> judgeInfoEOList = new ArrayList<>();

        for (ProjectDataEO p : expertRows) {
            JudgeInfoEOPage judgeInfoEOPage = new JudgeInfoEOPage();
            judgeInfoEOPage.setProjectId(p.getId());
            judgeInfoEOPage.setExpertUserId(UserUtils.getUserId());
            judgeInfoEOList = judgeInfoEOService.queryByList(judgeInfoEOPage);
            if (judgeInfoEOList.size() > 0) {
                expertDeclareRows.add(p);

            }
        }

        //分权限
        String roleIds = UserUtils.getRoleIds();

        if (roleIds.contains(RoleEnum.ADMIN.getId()) || roleIds.contains(RoleEnum.RSADMIN.getId()) || roleIds
                .contains(RoleEnum.SUPERADMIN.getId())) {//管理员角色
           // page.setStageName("申报申请");
          // rows = this.getDao().queryByPage(page);
          //  page.setStageName("项目申报");
            declareRows = this.queryDeclareByPage(page);

        } else {
            if (expertDeclareRows.size() > 0) {//专家人员
                declareRows.addAll(expertDeclareRows);

            }
            page.setTechnicalDirector(UserUtils.getUserId());
            List<ProjectDataEO> teList = this.queryDeclareByPage(page);
            if(declareRows.size()>0){
                List<String> declareRowIds = declareRows.stream().map(ProjectDataEO::getId).collect(Collectors.toList());

                List<ProjectDataEO> ProjectDataTemp = teList.stream().filter(e -> !declareRowIds.contains(e.getId())).collect(Collectors.toList());

                rows.addAll(ProjectDataTemp);

            }else {
                declareRows=teList;
            }



        }


        rows.addAll(declareRows);


        //加专家人员列表
        for (ProjectDataEO p : rows) {
            JudgeInfoEOPage judgeInfoEOPage = new JudgeInfoEOPage();
            judgeInfoEOPage.setProjectId(p.getId());
            List<JudgeInfoEO> judgeList = judgeInfoEOService.queryByList(judgeInfoEOPage);
            if (judgeList.size() > 0) {
                for (JudgeInfoEO j : judgeList) {
                    ScoreInfoEOPage ScorePage = new ScoreInfoEOPage();
                    ScorePage.setExpertUserId(j.getExpertUserId());
                    ScorePage.setProjectId(j.getProjectId());
                    List<ScoreInfoEO> scoreInfoEOList = scoreInfoEOService.queryByList(ScorePage);
                    if (scoreInfoEOList.size() > 0) {
                        j.setState(scoreInfoEOList.get(0).getState());
                    }

                }

                p.setJudgeInfoEOs(judgeList);
                DicTypeEO dicType = dicTypeEOService.getDicTypeById(judgeList.get(0).getJudgeMethodId());
                if (dicType != null) {
                    p.setJudgeMethod(dicType.getDicTypeName());
                }

            }
        }

        return rows;

    }

    /**
     * 项目立项签约列表
     *
     * @param page
     * @return
     * @throws Exception
     */
    public List<ProjectDataEO> queryBySet(ProjectDataEOPage page) throws Exception {
        List<ProjectDataEO> list;
//       获取角色编号
        String roleIds = UserUtils.getRoleIds();
        if (roleIds.contains(RoleEnum.ADMIN.getId()) || roleIds.contains(RoleEnum.RSADMIN.getId()) || roleIds
                .contains(RoleEnum.SUPERADMIN.getId())) {//管理员角色
            page.setStageName("立项签约");
            list = projectDataEOService.queryByPage(page);
        } else {//技术负责人
            page.setTechnicalDirector(UserUtils.getUserId());
            page.setStageName("立项签约");
            list = projectDataEOService.queryByPage(page);
        }
//      分角色返回状态为立项签约阶段的数据
        return list;

    }

    /**
     * 项目执行列表
     *
     * @param page
     * @return
     * @throws Exception
     */
    public List<ProjectDataEO> queryByPerform(ProjectDataEOPage page) throws Exception {


        List<ProjectDataEO>  list ;
        //       获取角色编号
        String roleIds = UserUtils.getRoleIds();
        if (roleIds.contains(RoleEnum.ADMIN.getId()) || roleIds.contains(RoleEnum.RSADMIN.getId()) || roleIds
                .contains(RoleEnum.SUPERADMIN.getId())) {//管理员角色
            page.setStageName("项目执行");
            list = projectDataEOService.queryByPage(page);
        } else {//技术负责人
            page.setTechnicalDirector(UserUtils.getUserId());
            page.setStageName("项目执行");
            list = projectDataEOService.queryByPage(page);
        }
        for(ProjectDataEO p:list){
            WorkProgressEOPage workProgressEOPage=new WorkProgressEOPage();
            workProgressEOPage.setProjectId(p.getId());
            List<WorkProgressEO> workProgressEOList = workProgressEOService.queryByList(workProgressEOPage);
           if(workProgressEOList.size()>0) {
               for (WorkProgressEO w : workProgressEOList) {

                   if (!StringUtils.equals(w.getExamineType(),"结项验收") && !StringUtils.equals(w.getExamineType(),"项目验收") && StringUtils.isEmpty(w.getCheckMethod())||StringUtils.equals(w.getExt3(),"1")) {
                       //如果有工作安排到检查时间前一周 修改对应项目的标识
                       Calendar c = Calendar.getInstance();
                       //过去七天
                       c.setTime(w.getExamineTime());
                       c.add(Calendar.DATE, -7);
                       Date d = c.getTime();
                       Date date = new Date();
                       if (d.before(date)) {
                           p.setToCheck("待检查");
                          // p.setProjectStatus("待检查");
                           //dao.updateByPrimaryKeySelective(p);
                       }
                   }else  if (!w.getExamineType().equals("结项验收") && !w.getExamineType().equals("项目验收")){
                       p.setInCheck("检查中");
                   }


               }
           }
        }




        return list;

    }

    /**
     * 项目变更列表
     *
     * @param page
     * @return
     * @throws Exception
     */
    public List<ProjectDataEO> queryByChangePage(ProjectDataEOPage page) throws Exception {
        List<ProjectDataEO> list;
        //       获取角色编号
        String roleIds = UserUtils.getRoleIds();
        if (roleIds.contains(RoleEnum.ADMIN.getId()) || roleIds.contains(RoleEnum.RSADMIN.getId()) || roleIds
                .contains(RoleEnum.SUPERADMIN.getId())) {//管理员角色

            list = dao.queryByChangePage(page);
        } else {//技术负责人
            page.setTechnicalDirector(UserUtils.getUserId());

            list = dao.queryByChangePage(page);
        }

        return list;

    }

    /**
     * 项目验收
     *
     * @param page
     * @return
     * @throws Exception
     */
    public List<ProjectDataEO> queryByacceptancePage(ProjectDataEOPage page) throws Exception {

            WorkProgressEOPage workProgressEOPage=new WorkProgressEOPage();
            List<WorkProgressEO> workProgressEOList = workProgressEOService.queryByList(workProgressEOPage);
            if(workProgressEOList.size()>0) {
                for (WorkProgressEO w : workProgressEOList) {

                    if (w.getExamineType().equals("结项验收") ) {
                        //如果有工作安排到结项时间前一月 修改对应项目的状态
                        Calendar c = Calendar.getInstance();
                        //过去一月
                        c.setTime(w.getExamineTime());
                        c.add(Calendar.DATE, -30);
                        Date d = c.getTime();
                        Date date = new Date();
                        if (d.before(date)&&StringUtils.isEmpty(w.getCheckMethod())) {
                            updateProjectStage(w.getProjectId(),"待验收");//修改阶段


                        }
                    }
                }
            }


        List<ProjectDataEO> list;
        String roleIds = UserUtils.getRoleIds();
        if (roleIds.contains(RoleEnum.ADMIN.getId()) || roleIds.contains(RoleEnum.RSADMIN.getId()) || roleIds
                .contains(RoleEnum.SUPERADMIN.getId())) {//管理员角色
            page.setStageName("验收结项");
            list = projectDataEOService.queryByPage(page);
        } else {//技术负责人
            page.setTechnicalDirector(UserUtils.getUserId());
            page.setStageName("验收结项");
            list = projectDataEOService.queryByPage(page);
        }
//      分角色返回状态项目验收结项阶段的数据
        return list;
    }

    /**
     * 项目一览列表加当前项目的专家人员
     *
     * @param page
     * @return
     * @throws Exception
     */
    public List<ProjectDataEO> queryByExpert(ProjectDataEOPage page) throws Exception {
        List<ProjectDataEO> viewRows;


        RestTemplate restTemplate=new RestTemplate();
        List<Long> fileid=null;
        //获取token
        Map<String, String> params = restTemplate.getForObject("http://192.2.12.169:8070/login?username=admin&password=1qaz9ol.", Map.class);
        Map<String, Object> stringMap=new HashMap<>();
        stringMap.put("htbh","ht001");
        stringMap.put("sqr","00198");//当前人
        stringMap.put("sqks","group");//部门编号  org_code
        SimpleDateFormat newDate=new SimpleDateFormat("yyyy-MM-dd");
        String format = newDate.format(new Date());
        stringMap.put("sqrq",format);
        stringMap.put("ktmc","课题01");
        stringMap.put("ktfzr","00198");
        stringMap.put("htmc","合同01");
        stringMap.put("htje","1.0");
        stringMap.put("khmc","1");
        stringMap.put("qyyf","1");
        stringMap.put("htqdrq",format);
        stringMap.put("ktksrq",format);
        stringMap.put("ktjsrq",format);
        //stringMap.put("ktdzb","");
        stringMap.put("lcspyj","1");
        stringMap.put("adcode","XMSB20201216002");//项目id
        stringMap.put("tjr","00198");//人员编号  //当前人
        stringMap.put("status","add");
        stringMap.put("flowId","");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity< Map<String, Object>> requestEntity = new HttpEntity<>(stringMap, headers);

        String fileUrl="http://192.2.12.169:8070/DC_ProConAdd?Authorization=" + params.get("token");
        String post = restTemplate.postForObject(fileUrl, requestEntity, String.class);
        System.out.println(post);
        logger.info(post);
        
        //分权限
        String roleIds = UserUtils.getRoleIds();
        if (roleIds.contains(RoleEnum.ADMIN.getId()) || roleIds.contains(RoleEnum.RSADMIN.getId()) || roleIds
                .contains(RoleEnum.SUPERADMIN.getId())) {//管理员角色

            viewRows = this.queryByPage(page);

        } else {
            page.setTechnicalDirector(UserUtils.getUserId());
            viewRows = this.queryByPage(page);
        }

        if (viewRows.size() > 0) {
            for (ProjectDataEO p : viewRows) {
                JudgeInfoEOPage judgeInfoEOPage = new JudgeInfoEOPage();
                judgeInfoEOPage.setProjectId(p.getId());
                List<JudgeInfoEO> judgeInfoEOList = judgeInfoEOService.queryByList(judgeInfoEOPage);
                if (judgeInfoEOList.size() > 0) {
                    p.setJudgeInfoEOs(judgeInfoEOList);
                }
            }
        }
        return viewRows;

    }

    public int declarePage4TipsCount(ProjectDataEOPage page) throws Exception {
        return this.getDao().declarePage4TipsCount(page);
    }

    public ProjectDataEO selectByProjectId(String id) throws Exception {
        return this.getDao().selectByProjectId(id);
    }

    public List<ProjectDataEO> declarePage4Tips(ProjectDataEOPage page) throws Exception {
        String loginUserId = UserUtils.getUserId();
        if (StringUtils.isEmpty(loginUserId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Subject subject = SecurityUtils.getSubject();
        if (!subject.hasRole(RESEARCH_ADMIN)) {
            page.setLoginUserId(loginUserId);
        }
        Integer rowCount = this.declarePage4TipsCount(page);
        page.getPager().setRowCount(rowCount);
        List<ProjectDataEO> rows = this.getDao().declarePage4Tips(page);
        if(rows.size()>0) {
            //加专家人员列表
            for (ProjectDataEO p : rows) {
                JudgeInfoEOPage judgeInfoEOPage = new JudgeInfoEOPage();
                judgeInfoEOPage.setProjectId(p.getId());
                List<JudgeInfoEO> judgeList = judgeInfoEOService.queryByList(judgeInfoEOPage);
                if (CollectionUtils.isNotEmpty(judgeList)) {
                    String judgeMethodId = "";
                    for (JudgeInfoEO j : judgeList) {
                        ScoreInfoEOPage ScorePage = new ScoreInfoEOPage();
                        ScorePage.setExpertUserId(j.getExpertUserId());
                        ScorePage.setProjectId(j.getProjectId());
                        List<ScoreInfoEO> scoreInfoEOList = scoreInfoEOService.queryByList(ScorePage);
                        if (CollectionUtils.isNotEmpty(scoreInfoEOList)) {
                            j.setState(scoreInfoEOList.get(0).getState());
                        }
                        if(StringUtils.equals(j.getExpertUserId(),loginUserId)){
                            p.setCurrentJudgeStatus(String.valueOf(j.getState()));
                        }
                        if(StringUtils.isNotEmpty(j.getJudgeMethodId())){
                            judgeMethodId = j.getJudgeMethodId();
                        }
                    }
                    p.setJudgeInfoEOs(judgeList);
                    DicTypeEO dicType = dicTypeEOService.getDicTypeById(judgeMethodId);
                    if (dicType != null) {
                        p.setJudgeMethod(dicType.getDicTypeName());
                    }
                }
            }
        }
        return rows;
    }

    public void updateProjectStatusByProjectId(String projectId, String projectStatus) {
        dao.updateProjectStatusByProjectId(projectId, projectStatus);

    }

    public void addOrUpdate(ProjectDataVO projectDataVO) throws Exception {


        ProjectDataEO projectDataEO = new ProjectDataEO();
        BeanUtils.copyProperties(projectDataVO, projectDataEO);
        //验证token信息
        String token = projectDataVO.getToken();
        /*
        String newToken = decrypt(token);
        List<UserEO> tokenUsers = userService.getDao().selectListByUserCode(newToken);
        if (CollectionUtils.isEmpty(tokenUsers)) {
            throw new AdcDaBaseException("用户token信息不存在");
        }*/

        projectDataEO.setProjectStatus("待填报");
        //说明-->以技术负责人举例:OA系统传过来的是技术负责人的工号,页面传过来的是直接封装好的
        //项目id
        String id = projectDataEO.getId();
        //技术负责人(以此是否为空判断是OA传入还是页面传入,OA传入是technicalDirector为空,页面传入时technicalDirector不为空)
        String technicalDirector = projectDataEO.getTechnicalDirector();
        //课题负责人工号
        String subjectDirectorCode = projectDataEO.getSubjectDirectorCode();
        //技术负责人工号
        String technicalDirectorCode = projectDataEO.getTechnicalDirectorCode();
        //经办人工号
        String projectApplicantCode = projectDataEO.getProjectApplicantCode();
        //项目类别名称
        String projectTypeName = projectDataEO.getProjectTypeName();
//        String projectTypeId = projectDataEO.getProjectTypeId();
        //承担方式名称
        String undertakingName = projectDataEO.getUndertakingName();
//        String undertakingId = projectDataEO.getUndertakingId();
        //申报单位名称
        String reportingUnitName = projectDataEO.getReportingUnitName();
        HashSet<String> memberUserIds = new HashSet<>();
        RContractInfoEO rContractInfoEO = new RContractInfoEO();
        rContractInfoEO.setProjectId(id);
        String subjectDirectorId = "";
        //OA传过来的不管是新增还是修改都会带着项目id
        if (ObjectUtil.isNull(technicalDirector)) {
            //OA
            //要根据工号去TS_USER表查出用户ID,姓名 进行拼接保存
            String subjectDirectorStr = null;
            String technicalDirectorStr = null;
            UserEO userEOTemp = new UserEO();
            String projectTypeIdTemp = null;
            String undertakingIdTemp = null;
            if (ObjectUtil.isNotNull(technicalDirectorCode)) {
                List<UserEO> userEOS = userService.selectListByUserCode(technicalDirectorCode);
                UserEO userEO = userEOS.stream().findFirst().get();
                Map<String, String> map = new HashMap<>();
                List<Map<String, String>> listmap = new ArrayList<>();
                map.put("TECHNICAL_DIRECTOR_ID", userEO.getUsid());
                map.put("TECHNICAL_DIRECTOR_NAME", userEO.getUsname());
                rContractInfoEO.setPartybUser(userEO.getUsname());
                rContractInfoEO.setPartybTel(userEO.getOfficePhone());
                rContractInfoEO.setPartybMobile(userEO.getCellPhoneNumber());
                rContractInfoEO.setPartybEmail(userEO.getEmail());
                memberUserIds.add(userEO.getUsid());
                listmap.add(map);
                technicalDirectorStr = JSONArray.fromObject(listmap).toString();
            }
            if (ObjectUtil.isNotNull(projectApplicantCode)) {
                List<UserEO> userEOS = userService.selectListByUserCode(projectApplicantCode);
                userEOTemp = userEOS.stream().findFirst().get();
                memberUserIds.add(userEOTemp.getUsid());
            }
            if (ObjectUtil.isNotNull(subjectDirectorCode)) {
                List<UserEO> userEOS = userService.selectListByUserCode(subjectDirectorCode);
                UserEO userEO = userEOS.stream().findFirst().get();
                Map<String, String> map = new HashMap<>();
                List<Map<String, String>> listmap = new ArrayList<>();
                map.put("SUBJECT_DIRECTOR_ID", userEO.getUsid());
                map.put("SUBJECT_DIRECTOR_NAME", userEO.getUsname());
                memberUserIds.add(userEO.getUsid());
                subjectDirectorId = userEO.getUsid();
                listmap.add(map);
                subjectDirectorStr = JSONArray.fromObject(listmap).toString();
            }
            //根据编码(所属部门)，要从TS_ORG表根据 org_desc 字段查出部门id,进行保存
            if (ObjectUtil.isNotNull(projectTypeName)) {
                DicTypeEOPage dicTypeEOPage = new DicTypeEOPage();
                dicTypeEOPage.setDicTypeName(projectTypeName);
                dicTypeEOPage.setDicId("ED99J43FHM");
                try {
                    List<DicTypeEO> dicTypeEOS = dicTypeEOService.queryByPage(dicTypeEOPage);
                    DicTypeEO dicTypeEO = dicTypeEOS.stream().findFirst().get();
                    //项目类型id
                    projectTypeIdTemp = dicTypeEO.getId();
                } catch (Exception e) {
                    log.error("项目类型查询失败，失败原因：" + e.getMessage());
                }
            }
            //查询部门信息
            String deptName = projectDataVO.getDeptName();
            rContractInfoEO.setPartybDept(deptName);
            if (ObjectUtil.isNotNull(deptName)) {
                try {
                    List<OrgEO> orgEOS = orgEODao.listOrgByOrgDesc(deptName);
                    OrgEO orgEO = orgEOS.stream().findFirst().get();
                    projectDataEO.setDeptId(orgEO.getId());
                } catch (Exception e) {
                    log.error("项目类型查询失败，失败原因：" + e.getMessage());
                }
            }

            if (ObjectUtil.isNotNull(undertakingName)) {
                DicTypeEOPage dicTypeEOPage = new DicTypeEOPage();
                dicTypeEOPage.setDicTypeName(undertakingName);
                dicTypeEOPage.setDicId("4D4Q4ALMVW");
                try {
                    List<DicTypeEO> dicTypeEOS = dicTypeEOService.queryByPage(dicTypeEOPage);
                    DicTypeEO dicTypeEO = dicTypeEOS.stream().findFirst().get();
                    //承担方式id
                    undertakingIdTemp = dicTypeEO.getId();
                } catch (Exception e) {
                    log.error("承担方式查询失败，失败原因：" + e.getMessage());
                }
            }
            //根据申报单位从RS_COMPANY查询id
            String reportingUnitId = dao.getIdByName(reportingUnitName);
            projectDataEO.setTechnicalDirector(technicalDirectorStr);
            projectDataEO.setSubjectDirector(subjectDirectorStr);
            projectDataEO.setProjectApplicantName(userEOTemp.getUsname());
            projectDataEO.setProjectApplicantId(userEOTemp.getUsid());
            projectDataEO.setProjectTypeId(projectTypeIdTemp);
            projectDataEO.setUndertakingId(undertakingIdTemp);
            projectDataEO.setReportingUnitId(reportingUnitId);
            //先根据目录id去数据库查询,看是否存在,如果不存在怎为新增,如果存在则为修改
            int projectDataCount = dao.selectProjectDataCount(id);
            if (projectDataCount == 0) {
                //新增RS_PROJECT_DATA
                dao.insert(projectDataEO);
                updateOrAddImplementationProcEOAndAnnexFileForProjectDataEO(projectDataEO);

                //新增合同信息
                rContractInfoEO.setId(UUID.randomUUID10());
                rContractInfoEO.setDelFlag(0);
                rContractInfoEO.setCreateTime(new Date());
                rContractInfoEO.setModifyTime(new Date());
                try {
                    rContractInfoEODao.insertSelective(rContractInfoEO);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            } else {
                //修改RS_PROJECT_DATA
                dao.updateByPrimaryKeySelective(projectDataEO);
                annexFileEOService.deleteByProjectId(projectDataEO.getId());
                updateOrAddImplementationProcEOAndAnnexFileForProjectDataEO(projectDataEO);
                updateRContractInfoEOByProjectId(rContractInfoEO);
            }
        } else {//页面
            UserEO loginUserEO = UserUtils.getUser();
            if (StringUtils.isEmpty(loginUserEO)) {
                throw new AdcDaBaseException("登陆可能过期，请登录！");
            }
            JSONArray jsonArray = JSONArray.fromObject(technicalDirector);
            String techinicalUserId = jsonArray.getJSONObject(0).getString("TECHNICAL_DIRECTOR_ID");
            JSONArray subjectDirectorJsonArray = JSONArray.fromObject(projectDataVO.getSubjectDirector());
            subjectDirectorId = subjectDirectorJsonArray.getJSONObject(0).getString("SUBJECT_DIRECTOR_ID");
            UserEO userEO = userService.getUserWithRoles(techinicalUserId);
            rContractInfoEO.setPartybUser(userEO.getUsname());
            rContractInfoEO.setPartybTel(userEO.getOfficePhone());
            rContractInfoEO.setPartybMobile(userEO.getCellPhoneNumber());
            rContractInfoEO.setPartybEmail(userEO.getEmail());
            OrgEO orgEO = orgEODao.getOrgEOById(projectDataVO.getDeptId());
            rContractInfoEO.setPartybDept(orgEO.getName());
            memberUserIds.add(techinicalUserId);
            memberUserIds.add(projectDataVO.getProjectApplicantId());
            memberUserIds.add(subjectDirectorId);
            if (ObjectUtil.isNull(id)) {
                //新增RS_PROJECT_DATA
                projectDataEO.setId(UUID.randomUUID10());
                id = projectDataEO.getId();
                dao.insert(projectDataEO);
                updateOrAddImplementationProcEOAndAnnexFileForProjectDataEO(projectDataEO);
                //新增合同信息
                rContractInfoEO.setProjectId(id);
                rContractInfoEO.setId(UUID.randomUUID10());
                rContractInfoEO.setDelFlag(0);
                rContractInfoEO.setCreateTime(new Date());
                rContractInfoEO.setModifyTime(new Date());
                try {
                    rContractInfoEODao.insertSelective(rContractInfoEO);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            } else {
                //修改RS_PROJECT_DATA
                dao.updateByPrimaryKeySelective(projectDataEO);
                annexFileEOService.deleteByProjectId(projectDataEO.getId());
                updateOrAddImplementationProcEOAndAnnexFileForProjectDataEO(projectDataEO);
                //修改合同信息
                updateRContractInfoEOByProjectId(rContractInfoEO);
            }
        }
        List<MemberInfoEO> memberInfoEOList = new ArrayList<>();
        int i=0;
        for(String memberUserId:memberUserIds){
            MemberInfoEO memberInfoEO = new MemberInfoEO();
            memberInfoEO.setMemberUserId(memberUserId);
            UserEO memberUser = userService.getUserWithRoles(memberUserId);
            if (CollectionUtils.isNotEmpty(memberUser.getOrgEOList())) {
                memberInfoEO.setDeptId(memberUser.getOrgEOList().get(0).getId());
            }
            memberInfoEO.setUndertakingTypeId("1");//标识该成员是项目成员
            memberInfoEO.setSort(i++);
            if(StringUtils.equals(subjectDirectorId,memberUserId)){
                memberInfoEO.setUndertakingTypeId("0");//标识该成员是科研项目负责人
                memberInfoEO.setSort(0);
            }
            memberInfoEO.setCreateTime(new Date());
            memberInfoEO.setDelFlag(0);
            memberInfoEO.setId(UUID.randomUUID10());
            memberInfoEO.setProjectId(id);
            memberInfoEOList.add(memberInfoEO);
            memberInfoEOService.deleteByProjectIdAndMemberUserId(id,memberUserId);
        }
        try {
            memberInfoEOService.batchSaveMember(memberInfoEOList);
        } catch (Exception e) {
            logger.error("新增项目负责人错误 " + new Gson().toJson(memberInfoEOList), e);
            throw new AdcDaBaseException("新增项目负责人错误!");
        }
    }

    public void updateRContractInfoEOByProjectId(RContractInfoEO rContractInfoEO) {
        rContractInfoEO.setModifyTime(new Date());
        try {
            rContractInfoEODao.updateRContractInfoEOByProjectId(rContractInfoEO);
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new AdcDaBaseException("修改合同信息失败，请联系管理员!");
        }
    }

    /**
     * token解密
     *
     * @param token
     * @return
     */
    private String decrypt(String token) {
        try {
            String newToken = RSAUtils.privateDecrypt(token, RSAUtils.getPrivateKey(this.RSAprivateKey));
            return newToken;
        } catch (NoSuchAlgorithmException e1) {
            logger.error("rsa解析失败");
            throw new AdcDaBaseException("rsa解析失败");
        } catch (InvalidKeySpecException e2) {
            logger.error("rsa解析失败,私钥非法");
            throw new AdcDaBaseException("rsa解析失败,私钥非法");
        }

    }

    private void updateOrAddImplementationProcEOAndAnnexFileForProjectDataEO(ProjectDataEO projectDataEO) {
        String firstProcessStageId = "";
        List<ImplementationProcEO> implementationProcEOList = new ArrayList<>();
        ProcessStageEOPage processStageEOPage = new ProcessStageEOPage();
        processStageEOPage.setOrderBy("sort");
        processStageEOPage.setOrder("ASC");
        List<ProcessStageEO> processStageEOList = processStageEODao.queryByList(processStageEOPage);

        ImplementationProcEOPage implementationProcEOPage = new ImplementationProcEOPage();
        implementationProcEOPage.setProjectId(projectDataEO.getId());
        List<ImplementationProcEO> implementationProcEOS = null;
        try {
            implementationProcEOS = implementationProcEOService.queryByList(implementationProcEOPage);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        if (CollectionUtils.isNotEmpty(processStageEOList)&&CollectionUtils.isEmpty(implementationProcEOS)) {
            for (ProcessStageEO processStageEO : processStageEOList) {
                ImplementationProcEO implementationProcEO = new ImplementationProcEO();
                implementationProcEO.setId(UUID.randomUUID10());
                implementationProcEO.setProjectId(projectDataEO.getId());
                implementationProcEO.setProceeStageId(processStageEO.getId());
                implementationProcEO.setDelFlag(0);
                //2代表状态为未进行
                implementationProcEO.setStatus(2);
                implementationProcEOList.add(implementationProcEO);
            }
            //1表示正在进行
            implementationProcEOList.get(0).setStatus(1);
            implementationProcEOList.get(0).setStartTime(new Date());
            implementationProcEOService.batchInsertSelective(implementationProcEOList);
            firstProcessStageId = implementationProcEOList.get(0).getId();
        }
        List<ImplementationProcFileEO> implementationProcFileEOList = new ArrayList<>();
        //新增RS_ANNEX_FILE
        if (CollectionUtils.isNotEmpty(projectDataEO.getAnnexFileEOList())) {
            for (AnnexFileEO annexFileEO : projectDataEO.getAnnexFileEOList()) {
                annexFileEO.setId(UUID.randomUUID10());
                annexFileEO.setProjectId(projectDataEO.getId());
                annexFileEO.setAnnexFileType("2");//项目申报书附件-OA
                annexFileEO.setProceeStageId(firstProcessStageId);
                annexFileEO.setCreateUserId(UserUtils.getUserId());
                annexFileEO.setCreateTime(new Date());
                annexFileEO.setCreateUserName(UserUtils.getUser().getUsname());
                annexFileEO.setDelFlag(0);
                ImplementationProcFileEO implementationProcFileEO = new ImplementationProcFileEO();
                implementationProcFileEO.setId(UUID.randomUUID10());
                implementationProcFileEO.setProjectId(projectDataEO.getId());
                implementationProcFileEO.setProcessId(implementationProcEOList.get(0).getId());
                implementationProcFileEO.setFileId(annexFileEO.getFileId());
                implementationProcFileEO.setDelFlag(0);
                implementationProcFileEOList.add(implementationProcFileEO);
            }
            try {
                annexFileEOService.batchInsertAnnexFile(projectDataEO.getAnnexFileEOList());
                implementationProcFileEOService.batchInsertImplementationProcFileEO(implementationProcFileEOList);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

    public List<ProjectDataEO> queryByPageForFunds(ProjectDataEOPage page) {
        return dao.queryByPageForFunds(page);
    }

    public Integer queryByCountForFunds(ProjectDataEOPage page) {
        return dao.queryByCountForFunds(page);
    }

    public List<ProjectDataEO> queryByListForFunds(ProjectDataEOPage page) {
        return dao.queryByListForFunds(page);
    }

    /**
     * 科研项目合同信息查询
     *
     * @param page
     */
    public List<ProjectDataEOPage> getProjectContractInfo(ProjectContractInfoVO page) {
        try {
            List<ProjectDataEOPage> projectContractInfo = dao.getProjectContractInfo(page);
            return projectContractInfo;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AdcDaBaseException("科研项目合同信息查询失败");
        }
    }

    /**
     * 申报提交
     *
     * @param projectDataEO
     */
    public void projectDeclareSubmit(ProjectDataEO projectDataEO) {
        try {

            //查询当前项目所有阶段
            ImplementationProcEOPage implementationProcEOPage = new ImplementationProcEOPage();
            implementationProcEOPage.setProjectId(projectDataEO.getId());
            implementationProcEOPage.setOrderBy("status");
            implementationProcEOPage.setOrder("ASC");
            List<ImplementationProcEO> implementationProcEOS = implementationProcEOService.queryByList(implementationProcEOPage);
            int i=0;
            for (ImplementationProcEO imp:implementationProcEOS) {
               if(i==1){
                   ImplementationProcEO impEO =new ImplementationProcEO();
                   impEO.setId(imp.getId());
                   impEO.setStartTime(new Date());
                   impEO.setStatus(1);
                   implementationProcEOService.updateByPrimaryKeySelective(impEO);
                   i=0;
                }
               if(imp.getProceeStageId().equals(projectDataEO.getStageId())){
                   //当包含当前阶段时修改值
                   ImplementationProcEO impEO =new ImplementationProcEO();
                   impEO.setId(imp.getId());
                   impEO.setEndTime(new Date());
                   impEO.setStatus(0);
                   implementationProcEOService.updateByPrimaryKeySelective(impEO);
                   i=1;//标识为1 修改下一阶段
               }

            }
            this.updateProjectStatusByProjectId(projectDataEO.getId(),"待评审");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AdcDaBaseException("提交失败");
        }
    }
    /**
     * 项目提交
     *
     * @param projectDataEO
     */
    public void projectSubmit(ProjectDataEO projectDataEO) {
        try {
            //查询当前项目所有阶段
            ImplementationProcEOPage implementationProcEOPage = new ImplementationProcEOPage();
            implementationProcEOPage.setProjectId(projectDataEO.getId());
            implementationProcEOPage.setOrderBy("status");
            implementationProcEOPage.setOrder("ASC");
            List<ImplementationProcEO> implementationProcEOS = implementationProcEOService.queryByList(implementationProcEOPage);
            if(StringUtils.equals(projectDataEO.getStageId(),"STAGE01")) {//项目申报提交
                for (int j = 0; j < implementationProcEOS.size(); j++) {
                    String proceeStageId = implementationProcEOS.get(j).getProceeStageId();
                    String procesId = implementationProcEOS.get(j).getId();
                    if (StringUtils.equals(proceeStageId,projectDataEO.getStageId())) {
                        //当包含当前阶段时修改值
                        ImplementationProcEO impEO = new ImplementationProcEO();
                        impEO.setId(procesId);
                        impEO.setEndTime(new Date());
                        impEO.setStatus(0);
                        implementationProcEOService.updateByPrimaryKeySelective(impEO);
                    }
                    if (StringUtils.equals(proceeStageId,"STAGE02")) {
                        ImplementationProcEO impEONext = new ImplementationProcEO();
                        impEONext.setId(procesId);
                        impEONext.setStartTime(new Date());
                        impEONext.setStatus(1);
                        implementationProcEOService.updateByPrimaryKeySelective(impEONext);
                    }
                }
                projectDataEO.setProjectTime(new Date());
                projectDataEO.setProjectStatus("审核中");
                this.updateByPrimaryKeySelective(projectDataEO);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AdcDaBaseException("提交失败");
        }
    }

    /**
     * 修改项目对应阶段
     * @param projectId
     * @throws Exception
     */
    public void updateProjectStage(String projectId,String status) throws Exception {
        try {
        //查询当前项目所有阶段
        ImplementationProcEOPage implementationProcEOPage = new ImplementationProcEOPage();
        implementationProcEOPage.setProjectId(projectId);
        implementationProcEOPage.setOrderBy("status");
        implementationProcEOPage.setOrder("ASC");
        List<ImplementationProcEO> implementationProcEOS = implementationProcEOService.queryByList(implementationProcEOPage);
        ProjectDataEOPage projectDataEOPage=new ProjectDataEOPage();
        projectDataEOPage.setId(projectId);
        projectDataEOPage.setStageName("项目执行");
        List<ProjectDataEO> projectDataEOS = this.queryByPage(projectDataEOPage);
        if(projectDataEOS.size()>0) {
            for (int j = 0; j < implementationProcEOS.size(); j++) {

                if (implementationProcEOS.get(j).getProceeStageId().equals(projectDataEOS.get(0).getStageId())) {
                    //当包含当前阶段时修改值
                    ImplementationProcEO impEO = new ImplementationProcEO();
                    impEO.setId(implementationProcEOS.get(j).getId());
                    impEO.setEndTime(new Date());
                    impEO.setStatus(0);
                    implementationProcEOService.updateByPrimaryKeySelective(impEO);
                    //下一条修改值
                    if (j + 1 < implementationProcEOS.size()) {
                        ImplementationProcEO impEONext = new ImplementationProcEO();
                        impEONext.setId(implementationProcEOS.get(j + 1).getId());
                        impEONext.setStartTime(new Date());
                        impEONext.setStatus(1);
                        implementationProcEOService.updateByPrimaryKeySelective(impEONext);
                    }
                }

            }
            this.updateProjectStatusByProjectId(projectId,status);
        }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AdcDaBaseException("提交失败");
        }

    }

    public void downLoadMonthReport(HttpServletResponse response, String projectId, String fileName) throws Exception {
        final ProjectDataEO projectDataEO = this.selectByProjectId(projectId);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        //
        RContractInfoEOPage page = new RContractInfoEOPage();
        page.setProjectId(projectId);
        final List<RContractInfoEO> rContractInfoEOS = rContractInfoEODao.queryByList(page);
        if (CollectionUtils.isEmpty(rContractInfoEOS)) {
            throw new AdcDaBaseException("科研项目合同书资料不全");
        }
        final RContractInfoEO rContractInfoEO = rContractInfoEOS.get(0);
        //------------------科研项目合同书首页----------------
        ResearchProjectsData tableData = new ResearchProjectsData();
        //项目编号
        tableData.setProjectCode(projectDataEO.getProjectCode());
        //项目名称
        tableData.setProjectName(projectDataEO.getProjectName());
        //甲方联系人
        tableData.setPartyAContact(rContractInfoEO.getPartyaName());
        //甲方电话
        tableData.setPartyATelephone(rContractInfoEO.getPartyaTel());
        //甲方电子邮件
        tableData.setPartyAEmail(rContractInfoEO.getPartyaEmail());
        //乙方负责人
        tableData.setPartyBprincipal(rContractInfoEO.getPartybName());
        //乙方姓名
        tableData.setPartyBName(rContractInfoEO.getPartybUser());
        //乙方工作部门
        tableData.setPartyBDepartment(rContractInfoEO.getPartybDept());
        //已方电话
        tableData.setPartyBTelephone(rContractInfoEO.getPartybTel());
        //已方传真
        tableData.setPartyBFox(rContractInfoEO.getPartybFax());
        //已方电子邮件
        tableData.setPartyBEmail(rContractInfoEO.getPartybEmail());
        //项目承担部门
        tableData.setPartyCDepartment(rContractInfoEO.getPartycName());

        //部门负责人
        tableData.setHeadDepartment(rContractInfoEO.getPartycUser());
        //科研联络人
        tableData.setResearchContact(rContractInfoEO.getResearchContact());
        //丙方电话
        tableData.setPartyCTelephone(rContractInfoEO.getPartycTel());
        //丙方传真
        tableData.setPartyCFox(rContractInfoEO.getPartycFax());
        //丙方电子邮件
        tableData.setPartyCEmail(rContractInfoEO.getPartycEmail());

        //主要研究内容
        List<RowRenderData> researchContentList = new ArrayList<>();
        if (StringUtils.isNotBlank(projectDataEO.getResearchContent())){
            final String[] rowRenderDataSplit = projectDataEO.getResearchContent().split("\\r\\n", -1);
            if (Objects.nonNull(researchContentList)) {
                for (String item : rowRenderDataSplit) {
                    RowRenderData rowRenderData = RowRenderData.build(item);
                    researchContentList.add(rowRenderData);
                }
            }
            ResearchContentData researchContentData = new ResearchContentData();
            if (CollectionUtils.isNotEmpty(researchContentList)){
                researchContentData.setScheduleTradeRowRenderDataList(researchContentList);
                tableData.setResearchContent(researchContentData);
            }
        }
        if (StringUtils.isNotBlank(projectDataEO.getResearchTarget())){
            //研究目标
            List<RowRenderData> researchTargetList = new ArrayList<>();
            final String[] rowResearchTargetDataSplit = projectDataEO.getResearchTarget().split("\\r\\n", -1);
            if (Objects.nonNull(researchTargetList)) {
                for (String item : rowResearchTargetDataSplit) {
                    RowRenderData rowRenderData = RowRenderData.build(item);
                    researchTargetList.add(rowRenderData);
                }
            }
            ResearchContentData researchTargetDataSplitData = new ResearchContentData();

            if (CollectionUtils.isNotEmpty(researchTargetList)){
                researchTargetDataSplitData.setScheduleTradeRowRenderDataList(researchTargetList);
                tableData.setResearchTarget(researchTargetDataSplitData);
            }
        }


        //------------------------节点信息-------------------------------------
        WorkProgressEOPage workProgressEOPage=new WorkProgressEOPage();
        workProgressEOPage.setProjectId(projectId);
        final List<WorkProgressEO> workProgressEOS = workProgressEOService.queryByList(workProgressEOPage);
        final List<WorkProgressEO> progressAList
                = workProgressEOS.stream()
                .filter(item -> !item.getExamineType().equals("2")).collect(Collectors.toList());
        List<RowRenderData> progressAContentList = new ArrayList<>();

        progressAList.forEach(item->{
            RowRenderData data=RowRenderData.build(
                    format.format(item.getExamineTime()),
                    item.getExamineContent(),
                    item.getNodeGoals()
            );
            progressAContentList.add(data);
        });
        ResearchContentData progressAContentData = new ResearchContentData();
        if (CollectionUtils.isNotEmpty(progressAContentList)){
            progressAContentData.setScheduleTradeRowRenderDataList(progressAContentList);
            tableData.setProgressA(progressAContentData);
        }

        List<RowRenderData> progressBContentList = new ArrayList<>();
        final List<WorkProgressEO> progressBList
                = workProgressEOS.stream()
                .filter(item -> item.getExamineType().equals("2")).collect(Collectors.toList());

        progressBList.forEach(item->{
            RowRenderData data=RowRenderData.build(
                    item.getNodeGoals()
            );
            tableData.setProgressDate(format.format(item.getExamineTime()));
            progressBContentList.add(data);
        });


        ResearchContentData progressBContentData = new ResearchContentData();
        if (CollectionUtils.isNotEmpty(progressBContentList)){
            progressBContentData.setScheduleTradeRowRenderDataList(progressBContentList);
            tableData.setProgressB(progressBContentData);
        }

        //------------------------课题参与人员----------------------------------
        List<RowRenderData> memberContentList = new ArrayList<>();
        MemberInfoEOPage memberInfoEOPage=new MemberInfoEOPage();
        memberInfoEOPage.setProjectId(projectId);
        final List<MemberInfoEO> memberInfoEOS = memberInfoEOService.queryByList(memberInfoEOPage);
        AtomicInteger number=new AtomicInteger(0);
        memberInfoEOS.forEach(item->{
            number.set(number.get()+1);
            RowRenderData data=RowRenderData.build(
                    number.toString()
                    ,item.getUsname()
                    ,item.getJobLevel()
                    ,item.getUndertakingTypeId()=="0"?"项目负责人":"项目成员"
                    ,item.getWorkHours()==null?"":item.getWorkHours().toString()

            );
            memberContentList.add(data);
        });
        ResearchContentData memberContentData = new ResearchContentData();
        if (CollectionUtils.isNotEmpty(memberContentList)){
            memberContentData.setScheduleTradeRowRenderDataList(memberContentList);
            tableData.setUserTable(memberContentData);
        }

        //------------------------项目经费预算和拨款方式表格整理----------------------
        BudgetFundEOPage budgetFundEOPage = new BudgetFundEOPage();
        budgetFundEOPage.setProjectId(projectId);
        //年份非空并排序
        List<BudgetFundEO> budgetFundEOS = budgetFundEOService.queryByList(budgetFundEOPage);
        budgetFundEOS = budgetFundEOS.stream().filter(item -> Objects.nonNull(item.getBudgetYear()))
                .sorted(Comparator.comparing(BudgetFundEO::getBudgetYear)).collect(Collectors.toList());
        //拿到年份信息
        final Set<String> years = budgetFundEOS.stream().map(BudgetFundEO::getBudgetYear).collect(Collectors.toSet());
        List<Integer> yearsList = new ArrayList<>();
        years.forEach(item -> {
            yearsList.add(Integer.parseInt(item));
        });
        Collections.sort(yearsList);
        //根据科目分组
        final Map<String, List<BudgetFundEO>> budgetFundEOGroup = budgetFundEOS.stream().collect
                (Collectors.groupingBy(BudgetFundEO::getBudgetType));
        //项目经费总计
        tableData.setTotalFunding(String.valueOf(projectDataEO.getTotalFunding()));
        //t2_1 到t2_3
        if (yearsList.size() == 3) {
            tableData.setSubjectYear1(String.valueOf(yearsList.get(0)).substring(2, 4));
            tableData.setSubjectYear2(String.valueOf(yearsList.get(1)).substring(2, 4));
            tableData.setSubjectYear3(String.valueOf(yearsList.get(2)).substring(2, 4));
        }
        if (yearsList.size() < 3 && yearsList.size() > 2) {
            tableData.setSubjectYear1(String.valueOf(yearsList.get(0)).substring(2, 4));
            tableData.setSubjectYear2(String.valueOf(yearsList.get(1)).substring(2, 4));
        }
        if (yearsList.size() == 1) {
            tableData.setSubjectYear1(String.valueOf(yearsList.get(0)).substring(2, 4));
        }
        //申请拨款
        if (budgetFundEOGroup.containsKey("申请拨款")) {
            final List<BudgetFundEO> budgetFundsqbk = budgetFundEOGroup.get("申请拨款");
            budgetFundsqbk.forEach(item -> {
                if (Objects.nonNull(tableData.getSubjectYear1())
                        && item.getBudgetYear().equals("20" + tableData.getSubjectYear1())) {
                    tableData.setApplyFundsYear1(String.valueOf(item.getBudgetAmount()));
                }
                if (Objects.nonNull(tableData.getSubjectYear2())
                        && item.getBudgetYear().equals("20" + tableData.getSubjectYear2())) {
                    tableData.setApplyFundsYear2(String.valueOf(item.getBudgetAmount()));
                }
                if (Objects.nonNull(tableData.getSubjectYear3())
                        && item.getBudgetYear().equals("20" + tableData.getSubjectYear3())) {
                    tableData.setApplyFundsYear3(String.valueOf(item.getBudgetAmount()));
                }
                tableData.setApplyFundsYear4(item.getExt1());
            });
        }
        //部门自筹
        if (budgetFundEOGroup.containsKey("部门自筹")) {
            final List<BudgetFundEO> budgetFundBMZC = budgetFundEOGroup.get("部门自筹");
            budgetFundBMZC.forEach(item -> {
                if (Objects.nonNull(tableData.getSubjectYear1())
                        && item.getBudgetYear().equals("20" + tableData.getSubjectYear1())) {
                    tableData.setSelfFundYear1(String.valueOf(item.getBudgetAmount()));
                }
                if (Objects.nonNull(tableData.getSubjectYear2())
                        && item.getBudgetYear().equals("20" + tableData.getSubjectYear2())) {
                    tableData.setSelfFundYear2(String.valueOf(item.getBudgetAmount()));
                }
                if (Objects.nonNull(tableData.getSubjectYear3())
                        && item.getBudgetYear().equals("20" + tableData.getSubjectYear3())) {
                    tableData.setSelfFundYear3(String.valueOf(item.getBudgetAmount()));
                }
                tableData.setSelfFundYear4(item.getExt1());
            });
        }
        //计算合计
        tableData.setSumSelfYear1(String.valueOf(
                Double.parseDouble(tableData.getApplyFundsYear1() == null ? "0" : tableData.getApplyFundsYear1())
                        + Double.parseDouble(tableData.getSelfFundYear1() == null ? "0" : tableData.getSelfFundYear1())));
        tableData.setSumSelfYear2(
                String.valueOf(Double.parseDouble(tableData.getApplyFundsYear2() == null ? "0" : tableData.getApplyFundsYear2())
                        + Double.parseDouble(tableData.getSelfFundYear2() == null ? "0" : tableData.getSelfFundYear2())));
        tableData.setSumSelfYear3(
                String.valueOf(Double.parseDouble(tableData.getApplyFundsYear3() == null ? "0" : tableData.getApplyFundsYear3())
                        + Double.parseDouble(tableData.getSelfFundYear3() == null ? "0" : tableData.getSelfFundYear3())));


        BudgetDetailEOPage budgetDetailEOPage = new BudgetDetailEOPage();
        budgetDetailEOPage.setProjectId(projectId);
        List<BudgetDetailEO> budgetDetailEOS = budgetDetailEOService.queryByList(budgetDetailEOPage);

        //表格第二部分
        //根据年度非空进行数据筛选
        budgetDetailEOS =
                budgetDetailEOS.stream()
                        .filter(item -> Objects.nonNull(item.getBudgetYear())).collect(Collectors.toList());

        //拿到年份信息
        final Set<String> yearDetail = budgetDetailEOS.stream().
                map(BudgetDetailEO::getBudgetYear).collect(Collectors.toSet());
        List<Integer> yearsListDetail = new ArrayList<>();
        yearDetail.forEach(item -> {
            yearsListDetail.add(Integer.parseInt(item));
        });
        Collections.sort(yearsListDetail);

        tableData.setTotalFunding(String.valueOf(projectDataEO.getTotalFunding()));
        //t2_1 到t2_3
        if (yearsListDetail.size() == 3) {
            tableData.setAppropriationYear1(String.valueOf(yearsListDetail.get(0)));
            tableData.setAppropriationYear2(String.valueOf(yearsListDetail.get(1)));
            tableData.setAppropriationYear3(String.valueOf(yearsListDetail.get(2)));
        }
        if (yearsListDetail.size() < 3 && yearsListDetail.size() > 2) {
            tableData.setAppropriationYear1(String.valueOf(yearsListDetail.get(0)));
            tableData.setAppropriationYear2(String.valueOf(yearsListDetail.get(1)));
            tableData.setAppropriationYear3("20__");

        }
        if (yearsListDetail.size() == 1) {
            tableData.setAppropriationYear1(String.valueOf(yearsListDetail.get(0)));
            tableData.setAppropriationYear2("20__");
            tableData.setAppropriationYear3("20__");

        }
        budgetDetailEOS.forEach(item -> {
            if (Objects.nonNull(tableData.getAppropriationYear1()) && !tableData.getAppropriationYear1().contains("_")
                    && item.getBudgetYear().equals(tableData.getAppropriationYear1())) {
                if (item.getBudgetDetailTypeName().equals("设备费")) {
                    tableData.setEquipmentYear1(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("购置设备费")) {
                    tableData.setPurchaseYear1(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("试制设备费")) {
                    tableData.setTrialYear1(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("设备租赁费")) {
                    tableData.setLeaseYear1(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("材料费")) {
                    tableData.setMaterialYear1(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().equals("测试化验加工费")) {
                    tableData.setAssayYear1(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("燃料动力费")) {
                    tableData.setFuelYear1(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("交流费")) {
                    tableData.setExchangeYear1(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("会议费")) {
                    tableData.setMeetingYear1(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("差旅费")) {
                    tableData.setTravelYear1(String.valueOf(item.getBudgetAmount()));
                }

                if (item.getBudgetDetailTypeName().contains("事务费")) {
                    tableData.setAffairsYear1(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().equals("软件购置费")) {
                    tableData.setSoftYear1(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("其他费用")) {
                    tableData.setOtherYear1(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("外协费")) {
                    tableData.setOutsourcYear1(String.valueOf(item.getBudgetAmount()));
                }
            }

            if (Objects.nonNull(tableData.getAppropriationYear2()) && !tableData.getAppropriationYear2().contains("_")
                    && item.getBudgetYear().equals(tableData.getAppropriationYear2())) {
                if (item.getBudgetDetailTypeName().equals("设备费")) {
                    tableData.setEquipmentYear2(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("购置设备费")) {
                    tableData.setPurchaseYear2(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("试制设备费")) {
                    tableData.setTrialYear2(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("设备租赁费")) {
                    tableData.setLeaseYear2(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("材料费")) {
                    tableData.setMaterialYear2(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().equals("测试化验加工费")) {
                    tableData.setAssayYear2(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("燃料动力费")) {
                    tableData.setFuelYear2(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("交流费")) {
                    tableData.setExchangeYear2(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("会议费")) {
                    tableData.setMeetingYear2(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("差旅费")) {
                    tableData.setTravelYear2(String.valueOf(item.getBudgetAmount()));
                }

                if (item.getBudgetDetailTypeName().contains("事务费")) {
                    tableData.setAffairsYear2(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().equals("软件购置费")) {
                    tableData.setSoftYear2(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("其他费用")) {
                    tableData.setOtherYear2(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("外协费")) {
                    tableData.setOutsourcYear2(String.valueOf(item.getBudgetAmount()));
                }
            }

            if (Objects.nonNull(tableData.getAppropriationYear3()) && !tableData.getAppropriationYear3().contains("_")
                    && item.getBudgetYear().equals(tableData.getAppropriationYear3())) {
                if (item.getBudgetDetailTypeName().equals("设备费")) {
                    tableData.setEquipmentYear3(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("购置设备费")) {
                    tableData.setPurchaseYear3(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("试制设备费")) {
                    tableData.setTrialYear3(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("设备租赁费")) {
                    tableData.setLeaseYear3(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("材料费")) {
                    tableData.setMaterialYear3(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().equals("测试化验加工费")) {
                    tableData.setAssayYear3(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("燃料动力费")) {
                    tableData.setFuelYear3(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("交流费")) {
                    tableData.setExchangeYear3(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("会议费")) {
                    tableData.setMeetingYear3(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("差旅费")) {
                    tableData.setTravelYear3(String.valueOf(item.getBudgetAmount()));
                }

                if (item.getBudgetDetailTypeName().contains("事务费")) {
                    tableData.setAffairsYear3(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().equals("软件购置费")) {
                    tableData.setSoftYear3(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("其他费用")) {
                    tableData.setOtherYear3(String.valueOf(item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("外协费")) {
                    tableData.setOutsourcYear3(String.valueOf(item.getBudgetAmount()));
                }
            }
        });

        //合计
        tableData.setSumYear2(String.valueOf(Double.parseDouble(
                tableData.getEquipmentYear1() == null ? "0" : tableData.getEquipmentYear1())
                + Double.parseDouble(tableData.getPurchaseYear2() == null ? "0" : tableData.getPurchaseYear2())
                + Double.parseDouble(tableData.getTrialYear2() == null ? "0" : tableData.getTrialYear2())
                + Double.parseDouble(tableData.getLeaseYear2() == null ? "0" : tableData.getLeaseYear2())
                + Double.parseDouble(tableData.getMaterialYear2() == null ? "0" : tableData.getMaterialYear2())
                + Double.parseDouble(tableData.getAssayYear2() == null ? "0" : tableData.getAssayYear2())
                + Double.parseDouble(tableData.getFuelYear2() == null ? "0" : tableData.getFuelYear2())
                + Double.parseDouble(tableData.getTravelYear2() == null ? "0" : tableData.getTravelYear2())
                + Double.parseDouble(tableData.getMeetingYear2() == null ? "0" : tableData.getMeetingYear2())
                + Double.parseDouble(tableData.getExchangeYear2() == null ? "0" : tableData.getExchangeYear2())
                + Double.parseDouble(tableData.getAffairsYear2() == null ? "0" : tableData.getAffairsYear2())
                + Double.parseDouble(tableData.getSoftYear2() == null ? "0" : tableData.getSoftYear2())
                + Double.parseDouble(tableData.getOtherYear2() == null ? "0" : tableData.getOtherYear2())
                + Double.parseDouble(tableData.getOutsourcYear2() == null ? "0" : tableData.getOutsourcYear2())));

        tableData.setSumYear1(String.valueOf(Double.parseDouble(
                tableData.getEquipmentYear2() == null ? "0" : tableData.getEquipmentYear2())
                + Double.parseDouble(tableData.getPurchaseYear3() == null ? "0" : tableData.getPurchaseYear3())
                + Double.parseDouble(tableData.getTrialYear3() == null ? "0" : tableData.getTrialYear3())
                + Double.parseDouble(tableData.getLeaseYear3() == null ? "0" : tableData.getLeaseYear3())
                + Double.parseDouble(tableData.getMaterialYear3() == null ? "0" : tableData.getMaterialYear3())
                + Double.parseDouble(tableData.getAssayYear3() == null ? "0" : tableData.getAssayYear3())
                + Double.parseDouble(tableData.getFuelYear3() == null ? "0" : tableData.getFuelYear3())
                + Double.parseDouble(tableData.getTravelYear3() == null ? "0" : tableData.getTravelYear3())
                + Double.parseDouble(tableData.getMeetingYear3() == null ? "0" : tableData.getMeetingYear3())
                + Double.parseDouble(tableData.getExchangeYear3() == null ? "0" : tableData.getExchangeYear3())
                + Double.parseDouble(tableData.getAffairsYear3() == null ? "0" : tableData.getAffairsYear3())
                + Double.parseDouble(tableData.getSoftYear3() == null ? "0" : tableData.getSoftYear3())
                + Double.parseDouble(tableData.getOtherYear3() == null ? "0" : tableData.getOtherYear3())
                + Double.parseDouble(tableData.getOutsourcYear3() == null ? "0" : tableData.getOutsourcYear3())));

        tableData.setSumYear1(String.valueOf(Double.parseDouble(
                tableData.getEquipmentYear3() == null ? "0" : tableData.getEquipmentYear3())
                + Double.parseDouble(tableData.getPurchaseYear1() == null ? "0" : tableData.getPurchaseYear1())
                + Double.parseDouble(tableData.getTrialYear1() == null ? "0" : tableData.getTrialYear1())
                + Double.parseDouble(tableData.getLeaseYear1() == null ? "0" : tableData.getLeaseYear1())
                + Double.parseDouble(tableData.getMaterialYear1() == null ? "0" : tableData.getMaterialYear1())
                + Double.parseDouble(tableData.getAssayYear1() == null ? "0" : tableData.getAssayYear1())
                + Double.parseDouble(tableData.getFuelYear1() == null ? "0" : tableData.getFuelYear1())
                + Double.parseDouble(tableData.getTravelYear1() == null ? "0" : tableData.getTravelYear1())
                + Double.parseDouble(tableData.getMeetingYear1() == null ? "0" : tableData.getMeetingYear1())
                + Double.parseDouble(tableData.getExchangeYear1() == null ? "0" : tableData.getExchangeYear1())
                + Double.parseDouble(tableData.getAffairsYear1() == null ? "0" : tableData.getAffairsYear1())
                + Double.parseDouble(tableData.getSoftYear1() == null ? "0" : tableData.getSoftYear1())
                + Double.parseDouble(tableData.getOtherYear1() == null ? "0" : tableData.getOtherYear1())
                + Double.parseDouble(tableData.getOutsourcYear1() == null ? "0" : tableData.getOutsourcYear1())));


        //获取模板
        Resource resource = resourceLoader.getResource("classpath:template\\ADCResearchProjects.docx");
        Configure scheduleTradeTableConfig = Configure.newBuilder()
                .customPolicy("researchContent", new ScheduleTradeTablePolicy(0))
                .customPolicy("researchTarget", new ScheduleTradeTablePolicy(0))
                .customPolicy("userTable",new ScheduleTradeTablePolicy(1))
                .customPolicy("progressA",new ScheduleTradeTablePolicy(2))
                .customPolicy("progressB",new ScheduleTradeTablePolicy(1)).build();
        XWPFTemplate template = XWPFTemplate.compile(resource.getInputStream()
                , scheduleTradeTableConfig).render(tableData);

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=./" + Encodes.urlEncode(fileName + ".docx"));
        response.setContentType("application/force-download");
        try (OutputStream os = response.getOutputStream()) {
            template.write(os);
            os.flush();
            template.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            Result.error("下载失败，请联系管理员");
        }


    }

    public void downLoadCatarc(HttpServletResponse response, String projectId, String fileName) throws Exception {
        CatarcData docData=new CatarcData();
        final ProjectDataEO projectDataEO = this.selectByProjectId(projectId);
        if (Objects.isNull(projectDataEO)){
            throw new AdcDaBaseException("无此项目信息，请核查");
        }
        //项目人员信息
        MemberInfoEOPage memberInfoEOPage=new MemberInfoEOPage();
        memberInfoEOPage.setProjectId(projectId);
        final List<MemberInfoEO> memberInfoEOS = memberInfoEOService.queryByList(memberInfoEOPage);

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        //-------------------------------------------文档首页信息-------------------------------------------
        //项目编号
        docData.setpCode(projectDataEO.getProjectCode());
        //项目名称
        docData.setpName(projectDataEO.getProjectName());
        //申请中心科研经费预算
        docData.setCentreBudget(String.valueOf(projectDataEO.getCenterBudgetApply()));
        //项目负责人
        String leaderId="";
        final ArrayList technicalList = JSON.parseObject(projectDataEO.getTechnicalDirector(), ArrayList.class);
        if (CollectionUtils.isNotEmpty(technicalList)) {
            Map<String, String> technicalerInfo = (Map<String, String>) technicalList.get(0);
            if (Objects.nonNull(technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"))) {
                docData.setPrincipal(technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"));
            }
            if (Objects.nonNull(technicalerInfo.get("TECHNICAL_DIRECTOR_ID"))){
                leaderId=technicalerInfo.get("TECHNICAL_DIRECTOR_ID");
            }
        }


/*        DicTypeEOPage dicTypeEOPageByLeader =new DicTypeEOPage();
        dicTypeEOPageByLeader.setDicTypeName("项目负责人");
        final List<DicTypeEO> dicTypeEOS = dicTypeEOService.queryByList(dicTypeEOPageByLeader);
        final String leaderClassId = dicTypeEOS.get(0).getId();
        final List<MemberInfoEO> leader = memberInfoEOS.stream().
                filter(item -> item.getUndertakingTypeId().equals("Z4Z673E9CG")
                        || item.getUndertakingTypeId().equals("0")).collect(Collectors.toList());*/
        //
        final UserEO userEO = userService.selectByPrimaryKey(leaderId);
        MemberInfoEOPage memberInfoPage=new MemberInfoEOPage();
        memberInfoPage.setMemberUserId(leaderId);
        final List<MemberInfoEO> memberEOS = memberInfoEOService.queryByList(memberInfoPage);
        if (Objects.nonNull(userEO)){
            if (CollectionUtils.isNotEmpty(memberEOS)){
                //所属部门
                docData.setDepartment(projectDataEO.getDeptName());
            }

            //负责人姓名
            docData.setPrincipal(memberEOS.get(0).getUsname());
            //联系人电话
            if (StringUtils.isNotBlank(userEO.getOfficePhone())){
                docData.setTel(userEO.getOfficePhone());
            }else{
                docData.setTel("        ");
            }
            //手机
            docData.setPhone(userEO.getCellPhoneNumber());
            //电子邮箱
            docData.setEmail(userEO.getEmail());

            //学历
            docData.setEducation(memberEOS.get(0).getMemberEducation());
            //性别
            docData.setSex(memberEOS.get(0).getMemberSex()=="1"?"女":"男");
            //职称
            docData.setLevel(memberEOS.get(0).getJobLevel());
            //是否留学
            docData.setAbroad(memberEOS.get(0).getStudyAbroadType()=="0"?"是":"否");
            //从事专业
            docData.setProfession(memberEOS.get(0).getMemberProfession());
        }
        //起止时间
        docData.setStartEndTime
                (format.format(projectDataEO.getStartTime())+"---"+format.format(projectDataEO.getEndTime()));


        //----------------------------项目信息表--------------------------------------
        final List<MemberInfoEO> member = memberInfoEOS.stream().
                filter(item -> item.getUndertakingTypeId().equals("9UG6XVQ4N5")
                        || item.getUndertakingTypeId().equals("1")).collect(Collectors.toList());

        List<RowRenderData> userContentList = new ArrayList<>();

        member.forEach(item->{
            RowRenderData data=RowRenderData.build(
                    item.getUsname(),
                    item.getMemberSex(),
                    item.getDeptName(),
                    item.getMemberEducation(),
                    item.getJobLevel(),
                    item.getTaskDivision(),
                    String.valueOf(item.getWorkHours())
            );
            userContentList.add(data);
        });
        ResearchContentData userContentData = new ResearchContentData();
        if (CollectionUtils.isNotEmpty(userContentList)){
            userContentData.setScheduleTradeRowRenderDataList(userContentList);
            docData.setUserTable(userContentData);
        }

        if (CollectionUtils.isNotEmpty(member)){
/*            try {
                final MemberInfoEO memberInfoEO = member.get(0);
                docData.setU11(memberInfoEO.getUsname());
                docData.setU12(memberInfoEO.getMemberSex());
                docData.setU13(memberInfoEO.getDeptName());
                docData.setU14(memberInfoEO.getMemberEducation());
                docData.setU15(memberInfoEO.getJobLevel());
                docData.setU16(memberInfoEO.getTaskDivision());
                docData.setU17(String.valueOf(memberInfoEO.getWorkHours()));
            } catch (Exception e) {
                logger.debug("第一行无数据");
            }*/

/*            try {
                final MemberInfoEO memberInfoEO = member.get(1);
                docData.setU21(memberInfoEO.getUsname());
                docData.setU22(memberInfoEO.getMemberSex());
                docData.setU23(memberInfoEO.getDeptName());
                docData.setU24(memberInfoEO.getMemberEducation());
                docData.setU25(memberInfoEO.getJobLevel());
                docData.setU26(memberInfoEO.getTaskDivision());
                docData.setU27(String.valueOf(memberInfoEO.getWorkHours()));
            } catch (Exception e) {
                logger.debug("第二行无数据");
            }*/

/*            try {
                final MemberInfoEO memberInfoEO = member.get(2);
                docData.setU31(memberInfoEO.getUsname());
                docData.setU32(memberInfoEO.getMemberSex());
                docData.setU33(memberInfoEO.getDeptName());
                docData.setU34(memberInfoEO.getMemberEducation());
                docData.setU35(memberInfoEO.getJobLevel());
                docData.setU36(memberInfoEO.getTaskDivision());
                docData.setU37(String.valueOf(memberInfoEO.getWorkHours()));
            } catch (Exception e) {
                logger.debug("第三行无数据");
            }*/

/*            try {
                final MemberInfoEO memberInfoEO = member.get(3);
                docData.setU41(memberInfoEO.getUsname());
                docData.setU42(memberInfoEO.getMemberSex());
                docData.setU43(memberInfoEO.getDeptName());
                docData.setU44(memberInfoEO.getMemberEducation());
                docData.setU45(memberInfoEO.getJobLevel());
                docData.setU46(memberInfoEO.getTaskDivision());
                docData.setU47(String.valueOf(memberInfoEO.getWorkHours()));
            } catch (Exception e) {
                logger.debug("第四行无数据");
            }*/

/*            try {
                final MemberInfoEO memberInfoEO = member.get(4);
                docData.setU51(memberInfoEO.getUsname());
                docData.setU52(memberInfoEO.getMemberSex());
                docData.setU53(memberInfoEO.getDeptName());
                docData.setU54(memberInfoEO.getMemberEducation());
                docData.setU55(memberInfoEO.getJobLevel());
                docData.setU56(memberInfoEO.getTaskDivision());
                docData.setU57(String.valueOf(memberInfoEO.getWorkHours()));
            } catch (Exception e) {
                logger.debug("第五行无数据");
            }*/

/*            try {
                final MemberInfoEO memberInfoEO = member.get(5);
                docData.setU61(memberInfoEO.getUsname());
                docData.setU62(memberInfoEO.getMemberSex());
                docData.setU63(memberInfoEO.getDeptName());
                docData.setU64(memberInfoEO.getMemberEducation());
                docData.setU65(memberInfoEO.getJobLevel());
                docData.setU66(memberInfoEO.getTaskDivision());
                docData.setU67(String.valueOf(memberInfoEO.getWorkHours()));
            } catch (Exception e) {
                logger.debug("第六行无数据");
            }*/

/*            try {
                final MemberInfoEO memberInfoEO = member.get(6);
                docData.setU71(memberInfoEO.getUsname());
                docData.setU72(memberInfoEO.getMemberSex());
                docData.setU73(memberInfoEO.getDeptName());
                docData.setU74(memberInfoEO.getMemberEducation());
                docData.setU75(memberInfoEO.getJobLevel());
                docData.setU76(memberInfoEO.getTaskDivision());
                docData.setU77(String.valueOf(memberInfoEO.getWorkHours()));
            } catch (Exception e) {
                logger.debug("第七行无数据");
            }*/

/*            try {
                final MemberInfoEO memberInfoEO = member.get(7);
                docData.setU81(memberInfoEO.getUsname());
                docData.setU82(memberInfoEO.getMemberSex());
                docData.setU83(memberInfoEO.getDeptName());
                docData.setU84(memberInfoEO.getMemberEducation());
                docData.setU85(memberInfoEO.getJobLevel());
                docData.setU86(memberInfoEO.getTaskDivision());
                docData.setU87(String.valueOf(memberInfoEO.getWorkHours()));
            } catch (Exception e) {
                logger.debug("第八行无数据");
            }*/

/*            try {
                final MemberInfoEO memberInfoEO = member.get(8);
                docData.setU91(memberInfoEO.getUsname());
                docData.setU92(memberInfoEO.getMemberSex());
                docData.setU93(memberInfoEO.getDeptName());
                docData.setU94(memberInfoEO.getMemberEducation());
                docData.setU95(memberInfoEO.getJobLevel());
                docData.setU96(memberInfoEO.getTaskDivision());
                docData.setU97(String.valueOf(memberInfoEO.getWorkHours()));
            } catch (Exception e) {
                logger.debug("第九行无数据");
            }*/

/*            try {
                final MemberInfoEO memberInfoEO = member.get(10);
                docData.setUa1(memberInfoEO.getUsname());
                docData.setUa2(memberInfoEO.getMemberSex());
                docData.setUa3(memberInfoEO.getDeptName());
                docData.setUa4(memberInfoEO.getMemberEducation());
                docData.setUa5(memberInfoEO.getJobLevel());
                docData.setUa6(memberInfoEO.getTaskDivision());
                docData.setUa7(String.valueOf(memberInfoEO.getWorkHours()));
            } catch (Exception e) {
                logger.debug("第十行无数据");
            }*/
            final Map<String, Integer> stringIntegerMap = memberInfoEOService.queryCountJobByProjectId(projectId);

            //总人数
/*
            docData.setUb1(String.valueOf(member.size()+leader.size()));
*/
            //正高
            docData.setPositive(String.valueOf(stringIntegerMap.get("senior")));
            //副高
            docData.setDeputy(String.valueOf(stringIntegerMap.get("vice_senior")));
            //中级
            //初级
            //研究生
            //大学
            docData.setUniversity(String.valueOf(stringIntegerMap.get("bachelor")));
            //大专
            docData.setJunior(String.valueOf(stringIntegerMap.get("college")));
            //博士
            docData.setPhD(String.valueOf(stringIntegerMap.get("doctor")));
            //硕士
            docData.setDegree(String.valueOf(stringIntegerMap.get("master")));

        }

        //主要研究内容

        if(StringUtils.isNotBlank(projectDataEO.getResearchContent())){
            List<RowRenderData> researchContentList = new ArrayList<>();
            final String[] rowRenderDataSplit = projectDataEO.getResearchContent().split("\\r\\n", -1);
            if (Objects.nonNull(researchContentList)) {
                for (String item : rowRenderDataSplit) {
                    RowRenderData rowRenderData = RowRenderData.build(item);
                    researchContentList.add(rowRenderData);
                }
            }
            ResearchContentData researchContentData = new ResearchContentData();
            if (CollectionUtils.isNotEmpty(researchContentList)){
                researchContentData.setScheduleTradeRowRenderDataList(researchContentList);
                docData.setResearchContent(researchContentData);
            }
        }
        //研究目标
        if (StringUtils.isNotBlank(projectDataEO.getResearchTarget())){
            List<RowRenderData> researchTargetList = new ArrayList<>();
            final String[] rowResearchTargetDataSplit = projectDataEO.getResearchTarget().split("\\r\\n", -1);
            if (Objects.nonNull(researchTargetList)) {
                for (String item : rowResearchTargetDataSplit) {
                    RowRenderData rowRenderData = RowRenderData.build(item);
                    researchTargetList.add(rowRenderData);
                }
            }
            ResearchContentData researchTargetDataSplitData = new ResearchContentData();

            if (CollectionUtils.isNotEmpty(researchTargetList)){
                researchTargetDataSplitData.setScheduleTradeRowRenderDataList(researchTargetList);
                docData.setResearchTarget(researchTargetDataSplitData);
            }
        }

        //节点检查
        WorkProgressEOPage workProgressEOPage=new WorkProgressEOPage();
        workProgressEOPage.setProjectId(projectId);
        List<WorkProgressEO> workProgressEOS = workProgressEOService.queryByList(workProgressEOPage);
        List<RowRenderData> progressDataList = new ArrayList<>();
        workProgressEOS=workProgressEOS.stream()
                .filter(item->!item.getExamineType().equals("2")).collect(Collectors.toList());
        workProgressEOS.forEach(item->{
            RowRenderData data=RowRenderData.build(format.format(item.getExamineTime())
                    ,item.getExamineContent());
            progressDataList.add(data);

        });

        ResearchContentData progressDataSplitData = new ResearchContentData();

        if (CollectionUtils.isNotEmpty(progressDataList)){
            progressDataSplitData.setScheduleTradeRowRenderDataList(progressDataList);
            docData.setProgress(progressDataSplitData);
        }

        //-----------------------------主要考核指标------------------------

        //交付物
        CheckTargetEOPage checkTargetEOPage=new CheckTargetEOPage();
        checkTargetEOPage.setProjectId(projectId);
        final List<CheckTargetEO> checkTargetEOS = checkTargetEOService.queryByList(checkTargetEOPage);
        final List<CheckTargetEO> deliverable
                = checkTargetEOS.stream()
                .filter(item -> item.getCheckTypeId().equals("SUE4G9GWQN")).collect(Collectors.toList());
        //交付物（表格改变）
/*        List<RowRenderData> deliverableList = new ArrayList<>();
        deliverable.forEach(item->{
            RowRenderData rowRenderData=RowRenderData.build(item.getExt2(),item.getExt1());
                    deliverableList.add(rowRenderData);
        });
        ResearchContentData deliverableContentData = new ResearchContentData();

        if (CollectionUtils.isNotEmpty(deliverableList)){
            deliverableContentData.setScheduleTradeRowRenderDataList(deliverableList);
            docData.setDeliverable(deliverableContentData);
        }*/

        final int deliverableSum = deliverable.stream().mapToInt(item -> Integer.parseInt(item.getExt2())).sum();
        final int deliverableExpectedSum = deliverable.stream().mapToInt(item -> Integer.parseInt(item.getExt1())).sum();
        if (Objects.nonNull(deliverableSum)){
            docData.setDeliverables(String.valueOf(deliverableSum));
        }
        if (Objects.nonNull(deliverableExpectedSum)){
            docData.setIndicators(String.valueOf(deliverableExpectedSum));
        }

        //发明专利数 checkName=发明专利数
        final double patent =
                checkTargetEOS.stream().filter(item
                        -> Objects.nonNull(item.getCheckName())
                        && item.getCheckName().equals("发明专利数")).collect(Collectors.toList())
                        .stream().mapToDouble(CheckTargetEO::getCheckNum).sum();

        docData.setPatent(String.valueOf(patent));

        //checkName=软件著作权
        final double soft =
                checkTargetEOS.stream().filter(item
                        -> Objects.nonNull(item.getCheckName())
                        && item.getCheckName().equals("软件著作权")).collect(Collectors.toList())
                        .stream().mapToDouble(CheckTargetEO::getCheckNum).sum();

        docData.setSoft(String.valueOf(soft));

        //实用新型数
        final double practical =
                checkTargetEOS.stream().filter(item
                        -> Objects.nonNull(item.getCheckName())
                        && item.getCheckName().equals("实用新型数")).collect(Collectors.toList())
                        .stream().mapToDouble(CheckTargetEO::getCheckNum).sum();

        docData.setPractical(String.valueOf(practical));
        //外观专利数
        final double exterior =
                checkTargetEOS.stream().filter(item
                        -> Objects.nonNull(item.getCheckName())
                        && item.getCheckName().equals("外观专利数")).collect(Collectors.toList())
                        .stream().mapToDouble(CheckTargetEO::getCheckNum).sum();

        docData.setExterior(String.valueOf(exterior));
        //获取模板

        final double other =
                checkTargetEOS.stream().filter(item
                        -> Objects.nonNull(item.getCheckName())
                        && item.getCheckName().equals("外观专利数")).collect(Collectors.toList())
                        .stream().mapToDouble(CheckTargetEO::getCheckNum).sum();

        docData.setOther(String.valueOf(other));

        //其他





        //-----------------------------------资金表格--------------------------------------------

        BudgetFundEOPage budgetFundEOPage=new BudgetFundEOPage();
        budgetFundEOPage.setProjectId(projectId);
        final List<BudgetFundEO> budgetFundEOS = budgetFundEOService.queryByPage(budgetFundEOPage);
        //申请中心预算
        final double centreBudget = budgetFundEOS.stream().filter(item -> item.getBudgetType().equals("申请拨款")).collect(Collectors.toList())
                .stream().mapToDouble(BudgetFundEO::getBudgetAmount).sum();
        if (Objects.nonNull(centreBudget)){
            docData.setCenter(String.valueOf(centreBudget));

        }

        //部门自筹
        final double selfBudget = budgetFundEOS.stream().filter(item -> item.getBudgetType().equals("部门自筹")).collect(Collectors.toList())
                .stream().mapToDouble(BudgetFundEO::getBudgetAmount).sum();
        if (Objects.nonNull(selfBudget)){
            docData.setSelfBudget(String.valueOf(selfBudget));
        }
        //合计
        final double sum = budgetFundEOS.stream().mapToDouble(BudgetFundEO::getBudgetAmount).sum();
        if (Objects.nonNull(sum)){
            docData.setSumSource(String.valueOf(sum));
        }




        BudgetDetailEOPage budgetDetailEOPage = new BudgetDetailEOPage();
        budgetDetailEOPage.setProjectId(projectId);
        List<BudgetDetailEO> budgetDetailEOS = budgetDetailEOService.queryByList(budgetDetailEOPage);
        //根据年度非空进行数据筛选
        budgetDetailEOS =
                budgetDetailEOS.stream()
                        .filter(item -> Objects.nonNull(item.getBudgetYear())).collect(Collectors.toList());

        //拿到年份信息
        final Set<String> yearDetail = budgetDetailEOS.stream().
                map(BudgetDetailEO::getBudgetYear).collect(Collectors.toSet());
        List<Integer> yearsListDetail = new ArrayList<>();
        yearDetail.forEach(item -> {
            yearsListDetail.add(Integer.parseInt(item));
        });

        Collections.sort(yearsListDetail);

        //t11 到t13
        if (yearsListDetail.size() == 3) {
            docData.setFundYear1(String.valueOf(yearsListDetail.get(0)));
            docData.setFundYear2(String.valueOf(yearsListDetail.get(1)));
            docData.setFundYear3(String.valueOf(yearsListDetail.get(2)));
        }
        if (yearsListDetail.size() < 3 && yearsListDetail.size() > 2) {
            docData.setFundYear1(String.valueOf(yearsListDetail.get(0)));
            docData.setFundYear2(String.valueOf(yearsListDetail.get(1)));
            docData.setFundYear3("20__");

        }
        if (yearsListDetail.size() == 1) {
            docData.setFundYear1(String.valueOf(yearsListDetail.get(0)));
            docData.setFundYear2("20__");
            docData.setFundYear3("20__");

        }

        budgetDetailEOS.forEach(item->{
            if (Objects.nonNull(docData.getFundYear1()) && !docData.getFundYear1().contains("_")
                    && item.getBudgetYear().equals(docData.getFundYear1())) {
                if (item.getBudgetDetailTypeName().equals("设备费")) {
                    docData.setEquipmentYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("设备购置费") || item.getBudgetDetailTypeName().contains("购置设备费")) {
                    docData.setPurchaseYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("试制")) {
                    docData.setTrialYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("改造租赁费") || item.getBudgetDetailTypeName().contains("租赁设备费")) {
                    docData.setLeaseYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("材料费")) {
                    docData.setMaterialYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().equals("测试化验加工费")) {
                    docData.setAssayYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("燃料动力费")) {
                    docData.setFuelYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("差旅费")) {
                    docData.setTravelYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("会议费")) {
                    docData.setConferenceYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("合作交流费")) {
                    docData.setExchangeYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }

                if (item.getBudgetDetailTypeName().contains("事务费")) {
                    docData.setServiceYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().equals("软件购置费")) {
                    docData.setSoftwarYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("劳务费")) {
                    docData.setLaborYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("咨询费")) {
                    docData.setAdvisoryYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("外协费")) {
                    docData.setExternalYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("管理费")) {
                    docData.setManYear1(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
            }

            if (Objects.nonNull(docData.getFundYear2()) && !docData.getFundYear2().contains("_")
                    && item.getBudgetYear().equals(docData.getFundYear2())) {
                if (item.getBudgetDetailTypeName().equals("设备费")) {
                    docData.setEquipmentYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("设备购置费") || item.getBudgetDetailTypeName().contains("购置设备费")) {
                    docData.setPurchaseYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("试制")) {
                    docData.setTrialYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("改造租赁费") || item.getBudgetDetailTypeName().contains("租赁设备费")) {
                    docData.setLeaseYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("材料费")) {
                    docData.setMaterialYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().equals("测试化验加工费")) {
                    docData.setAssayYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("燃料动力费")) {
                    docData.setFuelYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("差旅费")) {
                    docData.setTravelYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("会议费")) {
                    docData.setConferenceYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("合作交流费")) {
                    docData.setExchangeYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }

                if (item.getBudgetDetailTypeName().contains("事务费")) {
                    docData.setServiceYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().equals("软件购置费")) {
                    docData.setSoftwarYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("劳务费")) {
                    docData.setLaborYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("咨询费")) {
                    docData.setAdvisoryYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("外协费")) {
                    docData.setExternalYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("管理费")) {
                    docData.setManYear2(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
            }


            if (Objects.nonNull(docData.getFundYear3()) && !docData.getFundYear3().contains("_")
                    && item.getBudgetYear().equals(docData.getFundYear3())) {
                if (item.getBudgetDetailTypeName().equals("设备费")) {
                    docData.setEquipmentYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("设备购置费") || item.getBudgetDetailTypeName().contains("购置设备费")) {
                    docData.setPurchaseYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("试制")) {
                    docData.setTrialYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("改造租赁费") || item.getBudgetDetailTypeName().contains("租赁设备费")) {
                    docData.setLeaseYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("材料费")) {
                    docData.setMaterialYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().equals("测试化验加工费")) {
                    docData.setAssayYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("燃料动力费")) {
                    docData.setFuelYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("差旅费")) {
                    docData.setTravelYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("会议费")) {
                    docData.setConferenceYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("合作交流费")) {
                    docData.setExchangeYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }

                if (item.getBudgetDetailTypeName().contains("事务费")) {
                    docData.setServiceYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().equals("软件购置费")) {
                    docData.setSoftwarYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("劳务费")) {
                    docData.setLaborYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("咨询费")) {
                    docData.setAdvisoryYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("外协费")) {
                    docData.setExternalYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
                if (item.getBudgetDetailTypeName().contains("管理费")) {
                    docData.setManYear3(String.valueOf(item.getBudgetAmount()==null?"":item.getBudgetAmount()));
                }
            }

            docData.setSumFundYear1(
                    String.valueOf(
                            Double.parseDouble(StringUtils.isBlank(docData.getEquipmentYear1()) == true ? "0" : docData.getEquipmentYear1()) + Double.parseDouble(StringUtils.isBlank(docData.getPurchaseYear1()) == true ? "0" : docData.getPurchaseYear1())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getTrialYear1()) == true ? "0" : docData.getTrialYear1()) + Double.parseDouble(StringUtils.isBlank(docData.getLeaseYear1()) == true ? "0" : docData.getLeaseYear1())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getMaterialYear1()) == true ? "0" : docData.getMaterialYear1()) + Double.parseDouble(StringUtils.isBlank(docData.getAssayYear1()) == true ? "0" : docData.getAssayYear1())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getFuelYear1()) == true ? "0" : docData.getFuelYear1()) + Double.parseDouble(StringUtils.isBlank(docData.getTravelYear1()) == true ? "0" : docData.getTravelYear1())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getConferenceYear1()) == true ? "0" : docData.getConferenceYear1()) + Double.parseDouble(StringUtils.isBlank(docData.getExchangeYear1()) == true ? "0" : docData.getExchangeYear1())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getServiceYear1()) == true ? "0" : docData.getServiceYear1()) + Double.parseDouble(StringUtils.isBlank(docData.getSoftwarYear1()) == true ? "0" : docData.getSoftwarYear1())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getLaborYear1()) == true ? "0" : docData.getLaborYear1()) + Double.parseDouble(StringUtils.isBlank(docData.getAdvisoryYear1()) == true ? "0" : docData.getAdvisoryYear1())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getExternalYear1()) == true ? "0" : docData.getExternalYear1()) + Double.parseDouble(StringUtils.isBlank(docData.getManYear1()) == true ? "0" : docData.getManYear1())
                    )
            );

            docData.setSumFundYear2(
                    String.valueOf(
                            Double.parseDouble(StringUtils.isBlank(docData.getEquipmentYear2()) == true ? "0" : docData.getEquipmentYear2()) + Double.parseDouble(StringUtils.isBlank(docData.getPurchaseYear2()) == true ? "0" : docData.getPurchaseYear2())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getTrialYear2()) == true ? "0" : docData.getTrialYear2()) + Double.parseDouble(StringUtils.isBlank(docData.getLeaseYear2()) == true ? "0" : docData.getLeaseYear2())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getMaterialYear2()) == true ? "0" : docData.getMaterialYear2()) + Double.parseDouble(StringUtils.isBlank(docData.getAssayYear2()) == true ? "0" : docData.getAssayYear2())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getFuelYear2()) == true ? "0" : docData.getFuelYear2()) + Double.parseDouble(StringUtils.isBlank(docData.getTravelYear2()) == true ? "0" : docData.getTravelYear2())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getConferenceYear2()) == true ? "0" : docData.getConferenceYear2()) + Double.parseDouble(StringUtils.isBlank(docData.getExchangeYear2()) == true ? "0" : docData.getExchangeYear2())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getServiceYear2()) == true ? "0" : docData.getServiceYear2()) + Double.parseDouble(StringUtils.isBlank(docData.getSoftwarYear2()) == true ? "0" : docData.getSoftwarYear2())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getLaborYear2()) == true ? "0" : docData.getLaborYear2()) + Double.parseDouble(StringUtils.isBlank(docData.getAdvisoryYear2()) == true ? "0" : docData.getAdvisoryYear2())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getExternalYear2()) == true ? "0" : docData.getExternalYear2()) + Double.parseDouble(StringUtils.isBlank(docData.getManYear2()) == true ? "0" : docData.getManYear2())
                    )
            );

            docData.setSumFundYear3(
                    String.valueOf(
                            Double.parseDouble(StringUtils.isBlank(docData.getEquipmentYear3()) == true ? "0" : docData.getEquipmentYear3()) + Double.parseDouble(StringUtils.isBlank(docData.getPurchaseYear3()) == true ? "0" : docData.getPurchaseYear3())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getTrialYear3()) == true ? "0" : docData.getTrialYear3()) + Double.parseDouble(StringUtils.isBlank(docData.getLeaseYear3()) == true ? "0" : docData.getLeaseYear3())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getMaterialYear3()) == true ? "0" : docData.getMaterialYear3()) + Double.parseDouble(StringUtils.isBlank(docData.getAssayYear3()) == true ? "0" : docData.getAssayYear3())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getFuelYear3()) == true ? "0" : docData.getFuelYear3()) + Double.parseDouble(StringUtils.isBlank(docData.getTravelYear3()) == true ? "0" : docData.getTravelYear3())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getConferenceYear3()) == true ? "0" : docData.getConferenceYear3()) + Double.parseDouble(StringUtils.isBlank(docData.getExchangeYear3()) == true ? "0" : docData.getExchangeYear3())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getServiceYear3()) == true ? "0" : docData.getServiceYear3()) + Double.parseDouble(StringUtils.isBlank(docData.getSoftwarYear3()) == true ? "0" : docData.getSoftwarYear3())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getLaborYear3()) == true ? "0" : docData.getLaborYear3()) + Double.parseDouble(StringUtils.isBlank(docData.getAdvisoryYear3()) == true ? "0" : docData.getAdvisoryYear3())
                                    + Double.parseDouble(StringUtils.isBlank(docData.getExternalYear3()) == true ? "0" : docData.getExternalYear3()) + Double.parseDouble(StringUtils.isBlank(docData.getManYear3()) == true ? "0" : docData.getManYear3())
                    )
            );

            docData.setSumFundSum(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getSumFundYear1()) == true ? "0" : docData.getSumFundYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getSumFundYear2()) == true ? "0" : docData.getSumFundYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getSumFundYear3()) == true ? "0" : docData.getSumFundYear3())
            ));
            docData.setEquipmentSum(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getEquipmentYear1()) == true ? "0" : docData.getEquipmentYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getEquipmentYear2()) == true ? "0" : docData.getEquipmentYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getEquipmentYear3()) == true ? "0" : docData.getEquipmentYear3())
            ));
            docData.setPurchaseYear1(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getPurchaseYear1()) == true ? "0" : docData.getPurchaseYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getPurchaseYear2()) == true ? "0" : docData.getPurchaseYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getPurchaseYear3()) == true ? "0" : docData.getPurchaseYear3())
            ));
            docData.setTrialSum(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getTrialYear1()) == true ? "0" : docData.getTrialYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getTrialYear2()) == true ? "0" : docData.getTrialYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getTrialYear3()) == true ? "0" : docData.getTrialYear3())
            ));
            docData.setLeaseSum(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getLeaseYear1()) == true ? "0" : docData.getLeaseYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getLeaseYear2()) == true ? "0" : docData.getLeaseYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getLeaseYear3()) == true ? "0" : docData.getLeaseYear3())
            ));
            docData.setT71(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getMaterialYear1()) == true ? "0" : docData.getMaterialYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getMaterialYear2()) == true ? "0" : docData.getMaterialYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getMaterialYear3()) == true ? "0" : docData.getMaterialYear3())
            ));
            docData.setT81(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getAssayYear1()) == true ? "0" : docData.getAssayYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getAssayYear2()) == true ? "0" : docData.getAssayYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getAssayYear3()) == true ? "0" : docData.getAssayYear3())
            ));
            docData.setT91(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getFuelYear1()) == true ? "0" : docData.getFuelYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getFuelYear2()) == true ? "0" : docData.getFuelYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getFuelYear3()) == true ? "0" : docData.getFuelYear3())
            ));
            docData.setTa1(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getTravelYear1()) == true ? "0" : docData.getTravelYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getTravelYear2()) == true ? "0" : docData.getTravelYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getTravelYear3()) == true ? "0" : docData.getTravelYear3())
            ));
            docData.setTb1(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getConferenceYear1()) == true ? "0" : docData.getConferenceYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getConferenceYear2()) == true ? "0" : docData.getConferenceYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getConferenceYear3()) == true ? "0" : docData.getConferenceYear3())
            ));
            docData.setTc1(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getExchangeYear1()) == true ? "0" : docData.getExchangeYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getExchangeYear2()) == true ? "0" : docData.getExchangeYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getExchangeYear3()) == true ? "0" : docData.getExchangeYear3())
            ));
            docData.setServiceSum(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getServiceYear1()) == true ? "0" : docData.getServiceYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getServiceYear2()) == true ? "0" : docData.getServiceYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getServiceYear3()) == true ? "0" : docData.getServiceYear3())
            ));
            docData.setSoftwarSum(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getSoftwarYear1()) == true ? "0" : docData.getSoftwarYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getSoftwarYear2()) == true ? "0" : docData.getSoftwarYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getSoftwarYear3()) == true ? "0" : docData.getSoftwarYear3())
            ));
            docData.setLaborSum(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getLaborYear1()) == true ? "0" : docData.getLaborYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getLaborYear2()) == true ? "0" : docData.getLaborYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getLaborYear3()) == true ? "0" : docData.getLaborYear3())
            ));
            docData.setAdvisorySum(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getAdvisoryYear1()) == true ? "0" : docData.getAdvisoryYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getAdvisoryYear2()) == true ? "0" : docData.getAdvisoryYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getAdvisoryYear3()) == true ? "0" : docData.getAdvisoryYear3())
            ));
            docData.setExternalSum(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getExternalYear1()) == true ? "0" : docData.getExternalYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getExternalYear2()) == true ? "0" : docData.getExternalYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getExternalYear3()) == true ? "0" : docData.getExternalYear3())
            ));
            docData.setManSum(String.valueOf(
                    Double.parseDouble(StringUtils.isBlank(docData.getManYear1()) == true ? "0" : docData.getManYear1())
                            + Double.parseDouble(StringUtils.isBlank(docData.getManYear2()) == true ? "0" : docData.getManYear2())
                            + Double.parseDouble(StringUtils.isBlank(docData.getManYear3()) == true ? "0" : docData.getManYear3())
            ));

        });



        //--------------------------------------------项目经费测算依据-------------------------------------

        ResearchBudgetDetailEOPage researchBudgetDetailEOPage=new ResearchBudgetDetailEOPage();
        researchBudgetDetailEOPage.setResearchProjectId(projectId);
        final List<ResearchBudgetDetailEO> researchBudgetDetailEOS
                = researchBudgetDetailEOService.queryByList(researchBudgetDetailEOPage);

        //设备费分组
        final List<ResearchBudgetDetailEO> equipment = researchBudgetDetailEOS.stream()
                .filter(item -> item.getExtInfo3().equals("设备费")).collect(Collectors.toList());
        double equipmentSum;
        try {
            equipmentSum = equipment.stream().mapToDouble(ResearchBudgetDetailEO::getTotalPrice).sum();
            docData.setpSum(String.valueOf(equipmentSum));

        } catch (Exception e) {
            equipmentSum=0;
        }
        if (equipmentSum==0){
            docData.setpSum("0");
        }



        //购置设备费
        final List<ResearchBudgetDetailEO> purchase
                = equipment.stream()
                .filter(item -> item.getDetailType().equals("购置设备费")).collect(Collectors.toList());

        double purchaseSum;
        try {
            purchaseSum = purchase.stream().mapToDouble(ResearchBudgetDetailEO::getTotalPrice).sum();
            docData.setPurchaseSum(String.valueOf(purchaseSum));
        } catch (Exception e) {
            purchaseSum=0;
        }

        if (purchaseSum==0){
            docData.setPurchaseSum("0");
        }

        List<RowRenderData> purchaseContentList = new ArrayList<>();
        purchase.forEach(item->{
            final BigDecimal price = BigDecimal.valueOf(item.getUnitPrice());
            final BigDecimal Unit = BigDecimal.valueOf(Integer.parseInt(item.getUnit()));
            RowRenderData data=RowRenderData.build(
                    item.getName(),
                    item.getModel(),
                    String.valueOf(item.getUnitPrice()),
                    item.getUnit(),
                    price.multiply(Unit).toString()
            );
            purchaseContentList.add(data);
        });
        ResearchContentData purchaseContentData = new ResearchContentData();
        if (CollectionUtils.isNotEmpty(purchaseContentList)){
            purchaseContentData.setScheduleTradeRowRenderDataList(purchaseContentList);
            docData.setPurchase(purchaseContentData);
        }



        //试制设备费
        final List<ResearchBudgetDetailEO> production
                = equipment.stream()
                .filter(item -> item.getDetailType().equals("试制设备费")).collect(Collectors.toList());

        double productionSum;
        try {
            productionSum = production.stream().mapToDouble(ResearchBudgetDetailEO::getTotalPrice).sum();
            docData.setProductionSum(String.valueOf(productionSum));

        } catch (Exception e) {
            productionSum=0;
        }
        if (productionSum==0){
            docData.setProductionSum("0");
        }

        List<RowRenderData> productionContentList = new ArrayList<>();
        production.forEach(item->{
            final BigDecimal price = BigDecimal.valueOf(item.getUnitPrice());
            final BigDecimal Unit = BigDecimal.valueOf(Integer.parseInt(item.getUnit()));
            RowRenderData data=RowRenderData.build(
                    item.getName(),
                    item.getModel(),
                    String.valueOf(item.getUnitPrice()),
                    item.getUnit(),
                    price.multiply(Unit).toString()
            );
            productionContentList.add(data);
        });
        ResearchContentData productionContentData = new ResearchContentData();
        if (CollectionUtils.isNotEmpty(productionContentList)){
            productionContentData.setScheduleTradeRowRenderDataList(productionContentList);
            docData.setProduction(productionContentData);
        }



        //改造租赁费
        final List<ResearchBudgetDetailEO> retrofit
                = equipment.stream()
                .filter(item -> item.getDetailType().equals("改造租赁费")).collect(Collectors.toList());
        double retrofitSum;
        try {
            retrofitSum = retrofit.stream().mapToDouble(ResearchBudgetDetailEO::getTotalPrice).sum();
            docData.setRetrofitSum(String.valueOf(retrofitSum));
        } catch (Exception e) {
            retrofitSum=0;
        }
        if (retrofitSum==0d){
            docData.setRetrofitSum("0");
        }

        List<RowRenderData> retrofitContentList = new ArrayList<>();
        retrofit.forEach(item->{
            final BigDecimal price = BigDecimal.valueOf(item.getUnitPrice());
            final BigDecimal Unit = BigDecimal.valueOf(Integer.parseInt(item.getUnit()));
            RowRenderData data=RowRenderData.build(
                    item.getName(),
                    item.getModel(),
                    String.valueOf(item.getUnitPrice()),
                    item.getUnit(),
                    price.multiply(Unit).toString()
            );
            retrofitContentList.add(data);
        });
        ResearchContentData retrofitContentData = new ResearchContentData();
        if (CollectionUtils.isNotEmpty(retrofitContentList)){
            retrofitContentData.setScheduleTradeRowRenderDataList(retrofitContentList);
            docData.setRetrofit(retrofitContentData);
        }



        //材料费
        final List<ResearchBudgetDetailEO> material
                = researchBudgetDetailEOS.stream()
                .filter(item -> item.getExtInfo3().equals("材料费")).collect(Collectors.toList());
        double materialSum;
        try {
            materialSum = material.stream().mapToDouble(ResearchBudgetDetailEO::getTotalPrice).sum();
            docData.setMaterialSum(String.valueOf(materialSum));
        } catch (Exception e) {
            materialSum=0;
        }
        if (materialSum==0d){
            docData.setMaterialSum("0");
        }

        List<RowRenderData> materialContentList = new ArrayList<>();
        material.forEach(item->{
/*            final BigDecimal price = BigDecimal.valueOf(item.getUnitPrice());
            final BigDecimal Unit = BigDecimal.valueOf(Integer.parseInt(item.getUnit()));*/
            RowRenderData data=RowRenderData.build(
                    item.getName(),
                    item.getModel(),
                    String.valueOf(item.getUnitPrice()),
                    item.getUnit(),
                    String.valueOf(item.getTotalPrice())
            );
            materialContentList.add(data);
        });
        ResearchContentData materialContentData = new ResearchContentData();
        if (CollectionUtils.isNotEmpty(materialContentList)){
            materialContentData.setScheduleTradeRowRenderDataList(materialContentList);
            docData.setMaterial(materialContentData);
        }



        //测试化验加工费
        final List<ResearchBudgetDetailEO> assay
                = researchBudgetDetailEOS.stream()
                .filter(item -> item.getExtInfo3().contains("测试化验")).collect(Collectors.toList());
        double assaySum;
        try {
            assaySum = assay.stream().mapToDouble(ResearchBudgetDetailEO::getTotalPrice).sum();
            docData.setAssaySum(String.valueOf(assaySum));
        } catch (Exception e) {
            assaySum=0;
        }
        if (assaySum==0d){
            docData.setAssaySum("0");
        }

        List<RowRenderData> assayContentList = new ArrayList<>();
        assay.forEach(item->{
            RowRenderData data=RowRenderData.build(
                    item.getName(),
                    item.getEntrustedUnit(),
                    String.valueOf(item.getUnitPrice()),
                    item.getUnit(),
                    String.valueOf(item.getTotalPrice())
            );
            assayContentList.add(data);
        });
        ResearchContentData assayContentData = new ResearchContentData();
        if (CollectionUtils.isNotEmpty(assayContentList)){
            assayContentData.setScheduleTradeRowRenderDataList(assayContentList);
            docData.setAssay(assayContentData);
        }


        //燃料动力费
        final List<ResearchBudgetDetailEO> fuel
                = researchBudgetDetailEOS.stream()
                .filter(item -> item.getExtInfo3().contains("燃料")).collect(Collectors.toList());
        double fuelSum;
        try {
            fuelSum = fuel.stream().mapToDouble(ResearchBudgetDetailEO::getTotalPrice).sum();
            docData.setFuelSum(String.valueOf(fuelSum));
        } catch (Exception e) {
            fuelSum=0;
        }
        if (fuelSum==0d){
            docData.setFuelSum("0");
        }

        final Map<String, List<ResearchBudgetDetailEO>> fuelMap
                = fuel.stream().collect(Collectors.groupingBy(ResearchBudgetDetailEO::getName));

        if (fuelMap.containsKey("水")){
            final List<ResearchBudgetDetailEO> water = fuelMap.get("水");
            docData.setWateUnitPrice(String.valueOf(water.get(0).getUnitPrice()));
            docData.setWateCount(String.valueOf(water.get(0).getUnit()));
            docData.setWateTotal(String.valueOf(water.get(0).getTotalPrice()));
        }
        if (fuelMap.containsKey("电")){
            final List<ResearchBudgetDetailEO> electricity = fuelMap.get("电");
            docData.setElectricityPrice(String.valueOf(electricity.get(0).getUnitPrice()));
            docData.setElectricityCount(String.valueOf(electricity.get(0).getUnit()));
            docData.setElectricityTotal(String.valueOf(electricity.get(0).getTotalPrice()));
        }
        if (fuelMap.containsKey("汽油")){
            final List<ResearchBudgetDetailEO> gasoline = fuelMap.get("汽油");
            docData.setGasPrice(String.valueOf(gasoline.get(0).getUnitPrice()));
            docData.setGasCount(String.valueOf(gasoline.get(0).getUnit()));
            docData.setGasTotal(String.valueOf(gasoline.get(0).getTotalPrice()));
        }
        if (fuelMap.containsKey("柴油")){
            final List<ResearchBudgetDetailEO> diesel  = fuelMap.get("柴油");
            docData.setDieselPrice(String.valueOf(diesel.get(0).getUnitPrice()));
            docData.setDieselCount(String.valueOf(diesel.get(0).getUnit()));
            docData.setDieselTotal(String.valueOf(diesel.get(0).getTotalPrice()));
        }


        //差旅费
        final List<ResearchBudgetDetailEO> travel
                = researchBudgetDetailEOS.stream()
                .filter(item -> item.getExtInfo3().contains("差旅费")).collect(Collectors.toList());
        double travelSum;
        try {
            travelSum = travel.stream().mapToDouble(ResearchBudgetDetailEO::getTotalPrice).sum();
            docData.setTravelSum(String.valueOf(travelSum));
        } catch (Exception e) {
            travelSum=0;
        }
        if (travelSum==0d){
            docData.setTravelSum("0");
        }
        List<RowRenderData> travelContentList = new ArrayList<>();
        travel.forEach(item->{
            RowRenderData data=RowRenderData.build(
                    String.valueOf(item.getUnitPrice()),
                    item.getUnit(),
                    String.valueOf(item.getTotalPrice())
            );
            travelContentList.add(data);
        });
        ResearchContentData travelContentData = new ResearchContentData();
        if (CollectionUtils.isNotEmpty(travelContentList)){
            travelContentData.setScheduleTradeRowRenderDataList(travelContentList);
            docData.setTravel(travelContentData);
        }


        //会议费
        final List<ResearchBudgetDetailEO> conference
                = researchBudgetDetailEOS.stream()
                .filter(item -> item.getExtInfo3().contains("会议费")).collect(Collectors.toList());
        double conferenceSum;
        try {
            conferenceSum = conference.stream().mapToDouble(ResearchBudgetDetailEO::getTotalPrice).sum();
            docData.setConferenceSum(String.valueOf(conferenceSum));
        } catch (Exception e) {
            conferenceSum=0;
        }
        if (conferenceSum==0d){
            docData.setConferenceSum("0");
        }

        List<RowRenderData> conferenceContentList = new ArrayList<>();
        conference.forEach(item->{
            RowRenderData data=RowRenderData.build(
                    item.getName(),
                    item.getParticipateHost(),
                    String.valueOf(item.getPeopleNum()),
                    String.valueOf(item.getDayNum()),
                    String.valueOf(item.getCount()),
                    String.valueOf(item.getTotalPrice())
            );
            conferenceContentList.add(data);
        });
        ResearchContentData conferenceContentData = new ResearchContentData();
        if (CollectionUtils.isNotEmpty(conferenceContentList)){
            conferenceContentData.setScheduleTradeRowRenderDataList(conferenceContentList);
            docData.setConference(conferenceContentData);
        }



        //国际合作与交流费
        final List<ResearchBudgetDetailEO> exchange
                = researchBudgetDetailEOS.stream()
                .filter(item -> item.getExtInfo3().contains("交流费")).collect(Collectors.toList());
        double exchangeSum;
        try {
            exchangeSum = conference.stream().mapToDouble(ResearchBudgetDetailEO::getTotalPrice).sum();
            docData.setExchangeSum(String.valueOf(exchangeSum));
        } catch (Exception e) {
            exchangeSum=0;
        }
        if (exchangeSum==0d){
            docData.setExchangeSum("0");
        }

        List<RowRenderData> exchangeContentList = new ArrayList<>();
        exchange.forEach(item->{
            RowRenderData data=RowRenderData.build(
                    item.getName(),
                    item.getExchangePlace(),
                    String.valueOf(item.getPeopleNum()),
                    String.valueOf(item.getDayNum()),
                    String.valueOf(item.getCount()),
                    String.valueOf(item.getTotalPrice())
            );
            exchangeContentList.add(data);
        });
        ResearchContentData exchangeContentData = new ResearchContentData();
        if (CollectionUtils.isNotEmpty(exchangeContentList)){
            exchangeContentData.setScheduleTradeRowRenderDataList(exchangeContentList);
            docData.setExchange(exchangeContentData);
        }



        //事务费
        final List<ResearchBudgetDetailEO> service
                = researchBudgetDetailEOS.stream()
                .filter(item -> item.getExtInfo3().contains("事务费")).collect(Collectors.toList());
        double serviceSum;
        try {
            serviceSum = service.stream().mapToDouble(ResearchBudgetDetailEO::getTotalPrice).sum();
            docData.setService(String.valueOf(serviceSum));
        } catch (Exception e) {
            serviceSum=0;
        }
        if (serviceSum==0d){
            docData.setService("0");
        }

        try {
            final Map<String, List<ResearchBudgetDetailEO>> serviceMap
                    = fuel.stream().collect(Collectors.groupingBy(ResearchBudgetDetailEO::getDetailType));

            if (serviceMap.containsKey("出版费")){
                final List<ResearchBudgetDetailEO> publication  = fuelMap.get("出版费");
                docData.setPublicationUnit(String.valueOf(publication.get(0).getUnit()));
                docData.setPublicationPrice(String.valueOf(publication.get(0).getUnitPrice()));
                docData.setPublicationCount(String.valueOf(publication.get(0).getCount()));
                docData.setPublicationTotal(String.valueOf(publication.get(0).getTotalPrice()));
            }
            if (serviceMap.containsKey("资料费")){
                final List<ResearchBudgetDetailEO> dataFee  = fuelMap.get("资料费");
                docData.setPublicationUnit(String.valueOf(dataFee.get(0).getUnit()));
                docData.setPublicationPrice(String.valueOf(dataFee.get(0).getUnitPrice()));
                docData.setPublicationCount(String.valueOf(dataFee.get(0).getCount()));
                docData.setPublicationTotal(String.valueOf(dataFee.get(0).getTotalPrice()));
            }
            if (serviceMap.containsKey("文献检索费")){
                final List<ResearchBudgetDetailEO> literature   = fuelMap.get("文献检索费");
                docData.setPublicationUnit(String.valueOf(literature.get(0).getUnit()));
                docData.setPublicationPrice(String.valueOf(literature.get(0).getUnitPrice()));
                docData.setPublicationCount(String.valueOf(literature.get(0).getCount()));
                docData.setPublicationTotal(String.valueOf(literature.get(0).getTotalPrice()));
            }
            if (serviceMap.containsKey("专业通信费")){
                final List<ResearchBudgetDetailEO> communication   = fuelMap.get("专业通信费");
                docData.setPublicationUnit(String.valueOf(communication.get(0).getUnit()));
                docData.setPublicationPrice(String.valueOf(communication.get(0).getUnitPrice()));
                docData.setPublicationCount(String.valueOf(communication.get(0).getCount()));
                docData.setPublicationTotal(String.valueOf(communication.get(0).getTotalPrice()));
            }
            if (serviceMap.containsKey("专利申请及其他知识产权事务等")){
                final List<ResearchBudgetDetailEO> intellectual  = fuelMap.get("专利申请及其他知识产权事务等");
                docData.setPublicationUnit(String.valueOf(intellectual.get(0).getUnit()));
                docData.setPublicationPrice(String.valueOf(intellectual.get(0).getUnitPrice()));
                docData.setPublicationCount(String.valueOf(intellectual.get(0).getCount()));
                docData.setPublicationTotal(String.valueOf(intellectual.get(0).getTotalPrice()));
            }
        } catch (Exception e) {
            logger.debug("无事务费信息");
        }


        //软件购置费
        final List<ResearchBudgetDetailEO> software
                = researchBudgetDetailEOS.stream()
                .filter(item -> item.getExtInfo3().contains("软件购置费")).collect(Collectors.toList());
        double softwareSum;
        try {
            softwareSum = software.stream().mapToDouble(ResearchBudgetDetailEO::getTotalPrice).sum();
            docData.setSoftwareSum(String.valueOf(softwareSum));
        } catch (Exception e) {
            softwareSum=0;
        }
        if (softwareSum==0d){
            docData.setSoftwareSum("0");
        }

        List<RowRenderData> softwareContentList = new ArrayList<>();
        software.forEach(item->{
            RowRenderData data=RowRenderData.build(
                    StringUtils.isBlank(item.getName())==true?"":item.getName(),
                    StringUtils.isBlank(item.getUnit())==true?"":item.getUnit(),
                    String.valueOf(item.getUnitPrice()),
                    String.valueOf(item.getCount()),
                    String.valueOf(item.getTotalPrice())
            );
            softwareContentList.add(data);
        });
        ResearchContentData softwareContentData = new ResearchContentData();
        if (CollectionUtils.isNotEmpty(softwareContentList)){
            softwareContentData.setScheduleTradeRowRenderDataList(softwareContentList);
            docData.setSoftware(softwareContentData);
        }

        Resource resource = resourceLoader.getResource("classpath:template\\CATARC.docx");


        Configure scheduleTradeTableConfig = Configure.newBuilder()
                .customPolicy("researchTarget",new ScheduleTradeTablePolicy(1))
                .customPolicy("researchContent",new ScheduleTradeTablePolicy(1))
                .customPolicy("progress",new ScheduleTradeTablePolicy(2))
                .customPolicy("deliverable",new ScheduleTradeTablePolicy(2))
                .customPolicy("userTable",new ScheduleTradeTablePolicy(1))
                .customPolicy("purchase",new ScheduleTradeTablePolicy(1))
                .customPolicy("production",new ScheduleTradeTablePolicy(1))
                .customPolicy("retrofit",new ScheduleTradeTablePolicy(1))
                .customPolicy("material",new ScheduleTradeTablePolicy(1))
                .customPolicy("assay",new ScheduleTradeTablePolicy(1))
                .customPolicy("travel",new ScheduleTradeTablePolicy(1))
                .customPolicy("conference",new ScheduleTradeTablePolicy(1))
                .customPolicy("exchange",new ScheduleTradeTablePolicy(1))
                .customPolicy("software",new ScheduleTradeTablePolicy(1))
                .build();
        XWPFTemplate template = XWPFTemplate.compile(resource.getInputStream()
                , scheduleTradeTableConfig).render(docData);
//预期交付物	交付物的技术指标
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=./" + Encodes.urlEncode(fileName + ".docx"));
        response.setContentType("application/force-download");
        try (OutputStream os = response.getOutputStream()) {
            template.write(os);
            os.flush();
            template.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            Result.error("下载失败，请联系管理员");
        }

    }


    /**
     * 立项签约撤回
     *
     * @param projectDataEO
     */
    public void projectRecall(ProjectDataEO projectDataEO) {
        if (ObjectUtil.isNull(projectDataEO)){
           return;
        }
        try {
                 this.updateProjectStatusByProjectId(projectDataEO.getId(),"待审核");

                } catch (Exception e) {
                    log.error(e.getMessage());
                    throw new AdcDaBaseException("提交失败");
                }
            }
    /**
     * 立项签约删除
     *
     * @param projectDataEO
     */
    public void projectDelete(ProjectDataEO projectDataEO) {
        if (ObjectUtil.isNull(projectDataEO)){
            return;
        }
        try {
            String projectId=projectDataEO.getId();
            String checkTypeId="";

            //预算表
            budgetFundHistoryEODao.deleteByProjectId(projectId);
            //预算明细
            budgetDetailHistoryEODao.deleteByProjectId(projectId);
            //工作安排
            workProgressHistoryEODao.deleteByProjectId(projectId);
            //考核指标
            checkTargetHistoryEODao.deleteByProjectId(projectId,checkTypeId);
            //项目成员
            memberInfoHistoryEODao.deleteByProjectId(projectId);

            this.updateProjectStatusByProjectId(projectId,"待发起");

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AdcDaBaseException("提交失败");

        }




    }

}
