package com.adc.da.project.controller;

import com.adc.da.login.util.UserUtils;
import com.adc.da.project.dao.OrgProDao;
import com.adc.da.project.detail.FatherData;
import com.adc.da.project.detail.PlanTradeDetailData;
import com.adc.da.project.detail.ScheduleResearchDetailData;
import com.adc.da.project.detail.ScheduleTradeDetailData;
import com.adc.da.project.entity.ProjectPlanEO;
import com.adc.da.project.entity.ProjectScheduleEO;
import com.adc.da.project.poi.XWPFTemplate;
import com.adc.da.project.poi.config.Configure;
import com.adc.da.project.poi.datas.RowRenderData;
import com.adc.da.project.policy.PlanTradeTablePolicy;
import com.adc.da.project.policy.ScheduleResearchTablePolicy;
import com.adc.da.project.policy.ScheduleTradeTablePolicy;
import com.adc.da.project.service.ProjectPlanEOService;
import com.adc.da.project.service.ProjectScheduleEOService;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/${restPath}/project/downLoadReportController")
@Api(description = "|下载周报、月报、季报|")
public class DownLoadReportController {

    private static final Logger logger = LoggerFactory.getLogger(DownLoadReportController.class);

    @Autowired
    private ProjectPlanEOService projectPlanEOService;

    @Autowired
    private ProjectScheduleEOService projectScheduleEOService;

    @Autowired
    private UserEODao userEODao;

    @Autowired
    private OrgProDao orgProDao;

    @Autowired
    ResourceLoader resourceLoader;

    @ApiOperation(value = "|ProjectPlanEO|根据部门及项目类型下载月计划")
    @PostMapping(value = "/downLoadMonthReport")
    //@RequiresPermissions("order:projectPlan:update")
    public void downLoadMonthReport(HttpServletResponse response, String processInstanceId, String fileName) throws Exception {
        String classType = "0";
        String planTitle = "";
        String scheduleTitle = "";

        String userId = UserUtils.getUserId();
        UserEO userEO = userEODao.getUserWithRoles(userId);
        List<OrgEO> orgEOList = userEO.getOrgEOList();

        String deptName = getMainOrgName(orgEOList);

        //---------------------------------schedule的行业工作------------------------------------------------------------

        List<ProjectScheduleEO> scheduleTradeProjectScheduleEOlist = projectScheduleEOService
                .selectByDeptProTypeAndProcessInstId(userId, classType, "行业工作", processInstanceId);
        List<RowRenderData> scheduleTradeRowRenderDataList = new ArrayList<>();

        for (int i = 1; i <= scheduleTradeProjectScheduleEOlist.size(); i++) {
            ProjectScheduleEO projectScheduleEO = scheduleTradeProjectScheduleEOlist.get(i - 1);
            scheduleTitle = projectScheduleEO.getYear().substring(projectScheduleEO.getYear().lastIndexOf("^^") + 2) + " 年 " +
                    projectScheduleEO.getMonth().substring(projectScheduleEO.getMonth().lastIndexOf("^^") + 2) +
                    " 月重点工作项目执行情况一览表";
            if (null != projectScheduleEO.getMark() && projectScheduleEO.getMark().indexOf("^^") >= 0) {
                projectScheduleEO.setMark(projectScheduleEO.getMark().substring(projectScheduleEO.getMark().lastIndexOf("^^") + 2));
            }
            if ( null != projectScheduleEO.getProjectName() && projectScheduleEO.getProjectName().indexOf("^^") >= 0) {
                projectScheduleEO
                        .setProjectName(projectScheduleEO.getProjectName().substring(projectScheduleEO.getProjectName().lastIndexOf("^^") + 2));
            }
            if (null != projectScheduleEO.getCompletion() && projectScheduleEO.getCompletion().indexOf("^^") >= 0) {
                projectScheduleEO.setCompletion(projectScheduleEO.getCompletion().substring(projectScheduleEO.getCompletion().lastIndexOf("^^") + 2));
            }
            if (null != projectScheduleEO.getImplementation() && projectScheduleEO.getImplementation().indexOf("^^") >= 0) {
                projectScheduleEO.setImplementation(projectScheduleEO.getImplementation()
                        .substring(projectScheduleEO.getImplementation().lastIndexOf("^^") + 2));
            }

            RowRenderData row = RowRenderData.build(
                    String.valueOf(i),
                    projectScheduleEO.getMark(),
                    projectScheduleEO.getProjectName(),
                    projectScheduleEO.getCompletion(),
                    projectScheduleEO.getImplementation()
            );
            scheduleTradeRowRenderDataList.add(row);

        }

        //---------------------------------schedule的科研创新------------------------------------------------------------

        List<ProjectScheduleEO> scheduleResearchProjectScheduleEOlist = projectScheduleEOService
                .selectByDeptProTypeAndProcessInstId(userId, classType, "科研与技术创新", processInstanceId);
        List<RowRenderData> scheduleResearchRowRenderDataList = new ArrayList<>();

        for (int i = 1; i <= scheduleResearchProjectScheduleEOlist.size(); i++) {
            ProjectScheduleEO projectScheduleEO = scheduleResearchProjectScheduleEOlist.get(i - 1);
            if (null != projectScheduleEO.getMark() && projectScheduleEO.getMark().indexOf("^^") >= 0) {
                projectScheduleEO.setMark(projectScheduleEO.getMark().substring(projectScheduleEO.getMark().lastIndexOf("^^") + 2));
            }
            if (null != projectScheduleEO.getProjectName() && projectScheduleEO.getProjectName().indexOf("^^") >= 0) {
                projectScheduleEO
                        .setProjectName(projectScheduleEO.getProjectName().substring(projectScheduleEO.getProjectName().lastIndexOf("^^") + 2));
            }
            if (null != projectScheduleEO.getCompletion() && projectScheduleEO.getCompletion().indexOf("^^") >= 0) {
                projectScheduleEO.setCompletion(projectScheduleEO.getCompletion().substring(projectScheduleEO.getCompletion().lastIndexOf("^^") + 2));
            }
            if (null != projectScheduleEO.getImplementation() && projectScheduleEO.getImplementation().indexOf("^^") >= 0) {
                projectScheduleEO.setImplementation(projectScheduleEO.getImplementation()
                        .substring(projectScheduleEO.getImplementation().lastIndexOf("^^") + 2));
            }
            RowRenderData row = RowRenderData.build(
                    String.valueOf(i),
                    projectScheduleEO.getMark(),
                    projectScheduleEO.getProjectName(),
                    projectScheduleEO.getCompletion(),
                    projectScheduleEO.getImplementation()
            );
            scheduleResearchRowRenderDataList.add(row);
        }

        //--------------------------------------------这部分是查plan表的行业工作----------------------------------------------------------------------------------------
        List<ProjectPlanEO> planProjectPlanEOlist = projectPlanEOService.selectByDeptAndProType(userId, classType, "行业工作", processInstanceId);
        List<RowRenderData> planRowRenderDataList = new ArrayList<>();

        for (int i = 1; i <= planProjectPlanEOlist.size(); i++) {

            ProjectPlanEO projectPlanEO = planProjectPlanEOlist.get(i - 1);

            planTitle = projectPlanEO.getYear().substring(projectPlanEO.getYear().lastIndexOf("^^") + 2) + " 年 " +
                    projectPlanEO.getMonth().substring(projectPlanEO.getMonth().lastIndexOf("^^") + 2) +
                    " 月重点工作项目计划表";
            if (null != projectPlanEO.getMark() &&projectPlanEO.getMark().indexOf("^^") >= 0) {
                projectPlanEO.setMark(projectPlanEO.getMark().substring(projectPlanEO.getMark().lastIndexOf("^^") + 2));
            }
            if (null != projectPlanEO.getProjectName() &&projectPlanEO.getProjectName().indexOf("^^") >= 0) {
                projectPlanEO.setProjectName(projectPlanEO.getProjectName().substring(projectPlanEO.getProjectName().lastIndexOf("^^") + 2));
            }
            if (null != projectPlanEO.getProjectContent() && projectPlanEO.getProjectContent().indexOf("^^") >= 0) {
                projectPlanEO.setProjectContent(projectPlanEO.getProjectContent().substring(projectPlanEO.getProjectContent().lastIndexOf("^^") + 2));
            }
            if (null != projectPlanEO.getProgressTarget() && projectPlanEO.getProgressTarget().indexOf("^^") >= 0) {
                projectPlanEO.setProgressTarget(projectPlanEO.getProgressTarget().substring(projectPlanEO.getProgressTarget().lastIndexOf("^^") + 2));
            }
            if (null != projectPlanEO.getFinishTime() && projectPlanEO.getFinishTime().indexOf("^^") >= 0) { //完成时间
                projectPlanEO.setFinishTime(projectPlanEO.getFinishTime().substring(projectPlanEO.getFinishTime().lastIndexOf("^^") + 2));
            }
            if (null != projectPlanEO.getMajorPerson() && projectPlanEO.getMajorPerson().indexOf("^^") >= 0) { //负责人
                projectPlanEO.setMajorPerson(projectPlanEO.getMajorPerson().substring(projectPlanEO.getMajorPerson().lastIndexOf("^^") + 2));
            }
            if (null != projectPlanEO.getParticipant() && projectPlanEO.getParticipant().indexOf("^^") >= 0) { //参与人
                projectPlanEO.setParticipant(projectPlanEO.getParticipant().substring(projectPlanEO.getParticipant().lastIndexOf("^^") + 2));
            }
            if (null != projectPlanEO.getCooperationDepartment() && projectPlanEO.getCooperationDepartment().indexOf("^^") >= 0) { //合作部门
                projectPlanEO.setCooperationDepartment(projectPlanEO.getCooperationDepartment()
                        .substring(projectPlanEO.getCooperationDepartment().lastIndexOf("^^") + 2));
            }
            RowRenderData row = RowRenderData.build(
                    String.valueOf(i),
                    projectPlanEO.getMark(),
                    projectPlanEO.getProjectName(),
                    projectPlanEO.getProjectContent(),
                    projectPlanEO.getProgressTarget(),
                    projectPlanEO.getFinishTime(),
                    projectPlanEO.getMajorPerson(),
                    projectPlanEO.getParticipant(),
                    projectPlanEO.getCooperationDepartment()
            );

            planRowRenderDataList.add(row);

        }

        Resource resource = resourceLoader.getResource("classpath:doctemplate\\doc_template_month.docx");
//        //打开doc
//        File DOCFile = resource.getFile();

        ScheduleTradeDetailData scheduleTradeDetailData = new ScheduleTradeDetailData();
        scheduleTradeDetailData.setScheduleTradeRowRenderDataList(scheduleTradeRowRenderDataList);

        ScheduleResearchDetailData scheduleResearchDetailData = new ScheduleResearchDetailData();
        scheduleResearchDetailData.setScheduleResearchRowRenderDataList(scheduleResearchRowRenderDataList);

        PlanTradeDetailData planTradeDetailData = new PlanTradeDetailData();
        planTradeDetailData.setPlanTradeRowRenderDataList(planRowRenderDataList);

        FatherData fatherData = new FatherData();
        fatherData.setDept(deptName);
        fatherData.setSchedule_title(scheduleTitle);
        fatherData.setPlan_title(planTitle);
        fatherData.setTrade_remark(
                scheduleTradeProjectScheduleEOlist.get(0).getRemark().substring(
                        scheduleTradeProjectScheduleEOlist.get(0).getRemark().lastIndexOf("^^") + 2)
        );
        fatherData.setResearch_remark(
                scheduleResearchProjectScheduleEOlist.get(0).getRemark().substring(
                        scheduleResearchProjectScheduleEOlist.get(0).getRemark().lastIndexOf("^^") + 2)
        );

        fatherData.setScheduleTradeTable(scheduleTradeDetailData);
        fatherData.setTrade_count(String.valueOf(scheduleTradeRowRenderDataList.size()));

        fatherData.setScheduleResearchTable(scheduleResearchDetailData);
        fatherData.setResearch_count(String.valueOf(scheduleResearchRowRenderDataList.size()));

        fatherData.setPlanTradeTable(planTradeDetailData);
        fatherData.setPlan_count(String.valueOf(planRowRenderDataList.size()));

        //tagName 与 规则名除了policy 之外必须相等
        Configure scheduleTradeTableConfig = Configure.newBuilder().customPolicy("ScheduleTradeTable", new ScheduleTradeTablePolicy(1))
                .customPolicy("ScheduleResearchTable", new ScheduleResearchTablePolicy(0))
                .customPolicy("PlanTradeTable", new PlanTradeTablePolicy(1)).build();

        XWPFTemplate template = XWPFTemplate.compile(resource.getInputStream(), scheduleTradeTableConfig).render(fatherData);


        //下载数据
        doDownLoad(template, response , fileName);
    }

    //下载季度计划
    @ApiOperation(value = "|ProjectPlanEO|根据部门及项目类型下载季度计划")
    @PostMapping(value = "/downLoadSeasonReport")
    //@RequiresPermissions("order:projectPlan:update")
    public void downLoadSeasonReport(HttpServletResponse response, String processInstanceId, String fileName
    ) throws IOException {

        String classType = "1";

        String planTitle = "";

        String scheduleTitle = "";

        String userId = UserUtils.getUserId();

        UserEO userEO = userEODao.getUserWithRoles(userId);
        List<OrgEO> orgEOList = userEO.getOrgEOList();

        String deptName = getMainOrgName(orgEOList);

        //---------------------------------schedule的行业工作------------------------------------------------------------

        List<ProjectScheduleEO> scheduleTradeProjectScheduleEOlist = projectScheduleEOService
                .selectByDeptProTypeAndProcessInstId(userId, classType, "行业工作", processInstanceId);

        List<RowRenderData> scheduleTradeRowRenderDataList = new ArrayList<>();

        for (int i = 1; i <= scheduleTradeProjectScheduleEOlist.size(); i++) {

            ProjectScheduleEO projectScheduleEO = scheduleTradeProjectScheduleEOlist.get(i - 1);

            scheduleTitle = projectScheduleEO.getYear().substring(projectScheduleEO.getYear().lastIndexOf("^^") + 2) + " 年 " +
                    projectScheduleEO.getMonth().substring(projectScheduleEO.getMonth().lastIndexOf("^^") + 2) +
                    " 季度重点工作项目执行情况一览表";

            if (null != projectScheduleEO.getMark() && projectScheduleEO.getMark().indexOf("^^") >= 0) {
                projectScheduleEO.setMark(projectScheduleEO.getMark().substring(projectScheduleEO.getMark().lastIndexOf("^^") + 2));
            }

            if (null != projectScheduleEO.getProjectName() && projectScheduleEO.getProjectName().indexOf("^^") >= 0) {
                projectScheduleEO
                        .setProjectName(projectScheduleEO.getProjectName().substring(projectScheduleEO.getProjectName().lastIndexOf("^^") + 2));
            }

            if (null != projectScheduleEO.getCompletion() && projectScheduleEO.getCompletion().indexOf("^^") >= 0) {
                projectScheduleEO.setCompletion(projectScheduleEO.getCompletion().substring(projectScheduleEO.getCompletion().lastIndexOf("^^") + 2));
            }

            if (null != projectScheduleEO.getImplementation() && projectScheduleEO.getImplementation().indexOf("^^") >= 0) {
                projectScheduleEO.setImplementation(projectScheduleEO.getImplementation()
                        .substring(projectScheduleEO.getImplementation().lastIndexOf("^^") + 2));
            }

            RowRenderData row = RowRenderData.build(
                    String.valueOf(i),
                    projectScheduleEO.getMark(),
                    projectScheduleEO.getProjectName(),
                    projectScheduleEO.getCompletion(),
                    projectScheduleEO.getImplementation()
            );
            scheduleTradeRowRenderDataList.add(row);

        }

        //--------------------------------------------这部分是查plan表的行业工作----------------------------------------------------------------------------------------
        List<ProjectPlanEO> planProjectPlanEOlist = projectPlanEOService.selectByDeptAndProType(userId, classType, "行业工作", processInstanceId);
        List<RowRenderData> planRowRenderDataList = new ArrayList<>();

        for (int i = 1; i <= planProjectPlanEOlist.size(); i++) {

            ProjectPlanEO projectPlanEO = planProjectPlanEOlist.get(i - 1);

            planTitle = projectPlanEO.getYear().substring(projectPlanEO.getYear().lastIndexOf("^^") + 2) + " 年 " +
                    projectPlanEO.getMonth().substring(projectPlanEO.getMonth().lastIndexOf("^^") + 2) +
                    " 季度重点工作项目计划表";

            if (null != projectPlanEO.getMark() && projectPlanEO.getMark().indexOf("^^") >= 0) {
                projectPlanEO.setMark(projectPlanEO.getMark().substring(projectPlanEO.getMark().lastIndexOf("^^") + 2));
            }

            if (null != projectPlanEO.getProjectName() && projectPlanEO.getProjectName().indexOf("^^") >= 0) {
                projectPlanEO.setProjectName(projectPlanEO.getProjectName().substring(projectPlanEO.getProjectName().lastIndexOf("^^") + 2));
            }

            if (null != projectPlanEO.getProjectContent() && projectPlanEO.getProjectContent().indexOf("^^") >= 0) {
                projectPlanEO.setProjectContent(projectPlanEO.getProjectContent().substring(projectPlanEO.getProjectContent().lastIndexOf("^^") + 2));
            }

            if (null != projectPlanEO.getProgressTarget() && projectPlanEO.getProgressTarget().indexOf("^^") >= 0) {
                projectPlanEO.setProgressTarget(projectPlanEO.getProgressTarget().substring(projectPlanEO.getProgressTarget().lastIndexOf("^^") + 2));
            }
            if (null != projectPlanEO.getFinishTime() && projectPlanEO.getFinishTime().indexOf("^^") >= 0) { //完成时间
                projectPlanEO.setFinishTime(projectPlanEO.getFinishTime().substring(projectPlanEO.getFinishTime().lastIndexOf("^^") + 2));
            }

            if (null != projectPlanEO.getMajorPerson() && projectPlanEO.getMajorPerson().indexOf("^^") >= 0) { //负责人
                projectPlanEO.setMajorPerson(projectPlanEO.getMajorPerson().substring(projectPlanEO.getMajorPerson().lastIndexOf("^^") + 2));
            }

            RowRenderData row = RowRenderData.build(
                    String.valueOf(i),
                    projectPlanEO.getMark(),
                    projectPlanEO.getProjectName(),
                    projectPlanEO.getProjectContent(),
                    projectPlanEO.getProgressTarget(),
                    projectPlanEO.getFinishTime(),
                    projectPlanEO.getMajorPerson()
            );

            planRowRenderDataList.add(row);

        }

//        ClassPathResource resource = new ClassPathResource("application.yml");
//        InputStream inputStream = resource.getInputStream();

        Resource resource = resourceLoader.getResource("classpath:doctemplate\\doc_template_season.docx");

//        //打开doc
//        File DOCFile = null;
//        try {
//            DOCFile = resource.getFile();
//        } catch (IOException e) {
//            logger.error(e.getMessage(), e);
//            Result.error("模板" + resource.getFilename() + "丢失，下载失败，请联系管理员");
//        }

        ScheduleTradeDetailData scheduleTradeDetailData = new ScheduleTradeDetailData();
        scheduleTradeDetailData.setScheduleTradeRowRenderDataList(scheduleTradeRowRenderDataList);

        PlanTradeDetailData planTradeDetailData = new PlanTradeDetailData();
        planTradeDetailData.setPlanTradeRowRenderDataList(planRowRenderDataList);

        FatherData fatherData = new FatherData();
        fatherData.setDept(deptName);
        fatherData.setSchedule_title(scheduleTitle);
        fatherData.setPlan_title(planTitle);
        fatherData.setTrade_remark(
                scheduleTradeProjectScheduleEOlist.get(0).getRemark().substring(
                        scheduleTradeProjectScheduleEOlist.get(0).getRemark().lastIndexOf("^^") + 2)
        );

        fatherData.setScheduleTradeTable(scheduleTradeDetailData);
        fatherData.setTrade_count(String.valueOf(scheduleTradeRowRenderDataList.size()));

        fatherData.setPlanTradeTable(planTradeDetailData);
        fatherData.setPlan_count(String.valueOf(planRowRenderDataList.size()));

        Configure scheduleTradeTableConfig = Configure.newBuilder().customPolicy("ScheduleTradeTable", new ScheduleTradeTablePolicy(1))
                .customPolicy("PlanTradeTable", new PlanTradeTablePolicy(1)).build();

        XWPFTemplate template = XWPFTemplate.compile(resource.getInputStream(), scheduleTradeTableConfig).render(fatherData);

        doDownLoad(template, response , fileName);

    }

    //下载季度计划
    @ApiOperation(value = "|ProjectPlanEO|根据部门及项目类型下载周计划")
    @PostMapping(value = "/downLoadWeekReport")
    //@RequiresPermissions("order:projectPlan:update")
    public void downLoadWeekReport(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String fileName = request.getParameter("fileName");

        HashMap<String, String> hashMap = new HashMap<>();
        String s = request.getParameter("variable_name");
        String[] nameArr = s.split("#");

        for (String name : nameArr) {
            hashMap.put(name, request.getParameter(name));
        }

        Resource resource = resourceLoader.getResource("classpath:doctemplate\\doc_template_week.docx");
        //打开doc
//        File DOCFile = resource.getFile();
        XWPFTemplate template = XWPFTemplate.compile(resource.getInputStream()).render(hashMap);



        doDownLoad(template, response,fileName);
    }

    private String getMainOrgName(List<OrgEO> orgEOList) {

        String majorDeptName = null;
        String MyDeptName = null;
        for (OrgEO o : orgEOList) {
            if (o.getName().contains("本部")) {
                majorDeptName = o.getName();
                break;
            } else if (o.getName().contains("部")) {
                MyDeptName = o.getName();
            }
        }
        if (null != majorDeptName) {
            return majorDeptName;
        } else {
            return MyDeptName;
        }

    }

    private void doDownLoad(XWPFTemplate template, HttpServletResponse response,String fileName) {

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=./" + Encodes.urlEncode(fileName + ".docx")); // 这里应该是type + 月份
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

}
