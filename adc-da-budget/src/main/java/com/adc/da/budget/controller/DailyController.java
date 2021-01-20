package com.adc.da.budget.controller;

import com.adc.da.base.page.BasePage;
import com.adc.da.budget.dto.DailyConditionDTO;
import com.adc.da.budget.dto.DailyConditionRequestDTO;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.Daily;
import com.adc.da.budget.page.DailyPage;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.service.BudgetEOService;
import com.adc.da.budget.service.DailyService;
import com.adc.da.budget.vo.DailyExportStasticsVO;
import com.adc.da.budget.vo.DailyExportVO;
import com.adc.da.budget.vo.DailyVO;
import com.adc.da.crm.entity.CustomerRecordEO;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/Daily")
@Api("|Daily|")
public class DailyController {
    /**
     * 单表的增删改
     */
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    @Autowired
    private DailyService dailyService;
    @Autowired
    private UserEODao userEODao;

    /**
     * 新增
     *
     * @param
     * @return
     */
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "|Daily|新增")
    @RequiresPermissions("fin:daily:save")
    public ResponseMessage<Daily> save(@RequestBody @Valid DailyVO dailyVO)   {
        Daily daily = dailyService.save(dailyVO, UserUtils.getUserId());

        return Result.success(daily);
    }


    /**
     * 批量新增
     *
     * @param
     * @return
     */
    @PostMapping(value = "/saveList",consumes = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "|Daily|批量新增")
    @RequiresPermissions("fin:daily:save")
    public ResponseMessage<List<Daily>> saveList(@RequestBody @Valid List<DailyVO> dailyVOList) throws Exception {
        if(null == UserUtils.getUserId()){
            return Result.error("登陆可能已过期!");
        }

        return Result.success(dailyService.saveList(dailyVOList, UserUtils.getUserId()));
    }

    /**
     * 批量修改状态
     *
     * @param
     * @return
     */
    @PostMapping(value = "/updateDailyState",consumes = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "|Daily|批量修改状态")
    //@RequiresPermissions("fin:daily:save")
    public ResponseMessage<List<Daily>> updateDailyState(@RequestBody @Valid List<DailyVO> dailyVOList)   {
        if(null == UserUtils.getUserId()){
            return Result.error("登陆可能已过期!");
        }
        return Result.success(dailyService.updateDailyState(dailyVOList));
    }
    @ApiOperation(value = "|Daily|批量修改状态")
    @GetMapping(value = "/queryTask/{id}",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<Object> queryTask(@PathVariable("id") String id)   {
        return Result.success(dailyService.queryTask(id));
    }

    /**
     * 批量已阅（审批）
     *
     * @param
     * @return
     */
    @PostMapping(value = "/approveDailyBatch",consumes = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "|Daily|批量已阅（审批）")  // 前端不需要jsonStringfy()
    public ResponseMessage<String> approveDailyBatch(@RequestBody String[] dailyIdList)throws Exception{
        dailyService.approveDailyBatch(Arrays.asList(dailyIdList));
        return Result.success();
    }



    /**
     * 修改
     *
     * @param dailyVO
     * @return
     */
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "|Daily|修改")
    @RequiresPermissions("fin:daily:update")
    public ResponseMessage<Daily> update(@RequestBody @Valid DailyVO dailyVO) {
        return Result.success(dailyService.update(dailyVO));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    @ApiOperation(value = "|Daily|删除")
    @RequiresPermissions("fin:daily:delete")
    public ResponseMessage delete(@PathVariable(value = "ids") String ids) {
        dailyService.delete(ids);
        return Result.success(ids);
    }


    /**
     * 删除
     */
    @Deprecated
    @DeleteMapping("/delete/all")
    @ApiOperation(value = "|Daily|清空数据,删除已改为软删除，慎用此方法")
    public ResponseMessage deleteAll() {
        dailyService.deleteAll();
        return Result.success();
    }


    /**
     * 批量更新日报
     */
//    @Deprecated
    @GetMapping("/update/all")
    @ApiOperation(value = "|Daily|")
    public ResponseMessage updateAll() {
        Iterable<Daily> dailyIterable = dailyService.queryBuilds();
        if (dailyIterable.iterator().hasNext()) {
            dailyService.saveAll(dailyIterable);
        }
        return Result.success("更新成功！");
    }


    /**
     * 批量更新日报
     */
//    @Deprecated
    @GetMapping("/moveAll")
    @ApiOperation(value = "|Daily|")
    public ResponseMessage moveAll() {

        dailyService.moveAll();

        return Result.success("更新成功！");
    }



    /**
     * 查找单个
     */
    @GetMapping("/findOne/{id}")
    @ApiOperation(value = "|Daily|单个查找")
    @RequiresPermissions("fin:daily:get")
    public ResponseMessage<Daily> findOne(@PathVariable String id) {
        Daily daily = dailyService.findOne(id);
        if(daily.getDailyCreateTime() == null){
            daily.setDailyCreateTime(daily.getCreateTime());
        }
        return Result.success(daily);
    }

    /**
     * 查询全部
     */
    @GetMapping("/findAll")
    @ApiOperation(value = "|Daily|查询全部")
    @RequiresPermissions("fin:daily:list")
    public ResponseMessage<List<Daily>> findAll() {


        Iterable<Daily> iterable = dailyService.findAll();
        Iterator<Daily> it = iterable.iterator();
        List<Daily> list = new ArrayList<>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        List<UserEO> userEOList = userEODao.getUserWithRolesListByJobWeight(6999);
        Set<String> userIdSet = new HashSet<>();
        for (UserEO userEO : userEOList) {
            userIdSet.add(userEO.getUsid());
        }
        dailyService.updateCreateTime(list);
        dailyService.updateApproveUserName(list, userIdSet);
        return Result.success(list);
    }


    @ApiOperation(value = "|Daily|分页查询")
    @PostMapping("/page")
    public ResponseMessage<PageDTO> page(@RequestBody BasePage basePage) {
        int page = 1;
        int pageSize = 10;
        if(basePage.getPage() != null){
            page = basePage.getPage();
        }
        if(basePage.getPageSize() != null){
            pageSize = basePage.getPageSize();
        }
        PageDTO pageDTO = dailyService.queryByPage(page, pageSize);
        List<UserEO> userEOList = userEODao.getUserWithRolesListByJobWeight(6999);
        Set<String> userIdSet = new HashSet<>();
        for (UserEO userEO : userEOList) {
            userIdSet.add(userEO.getUsid());
        }
        dailyService.updateApproveUserName(pageDTO.getDataList(), userIdSet);
        dailyService.updateCreateTime(pageDTO.getDataList());
        return Result.success(pageDTO);
    }


    @ApiOperation(value = "|Daily|分页查询")
    @PostMapping("/newQueryByPage")
    public ResponseMessage<PageDTO> newQueryByPage(@RequestBody BasePage basePage) {
        PageDTO pageDTO = dailyService.newQueryByPage(basePage.getPage(), basePage.getPageSize());
        dailyService.updateCreateTime(pageDTO.getDataList());
        List<UserEO> userEOList = userEODao.getUserWithRolesListByJobWeight(6999);
        Set<String> userIdSet = new HashSet<>();
        for (UserEO userEO : userEOList) {
            userIdSet.add(userEO.getUsid());
        }
        dailyService.updateApproveUserName(pageDTO.getDataList(), userIdSet);
        return Result.success(pageDTO);
    }


    @ApiOperation(value = "|Daily|分页个人日报查询")
    @PostMapping("/getLoginUserDailyByPage")
    public ResponseMessage<PageDTO> getLoginUserDailyByPage(@RequestBody BasePage basePage) {

        PageDTO pageDTO = dailyService.queryLoginUserDailyByPage(basePage.getPage(), basePage.getPageSize());
        dailyService.updateCreateTime(pageDTO.getDataList());
        List<UserEO> userEOList = userEODao.getUserWithRolesListByJobWeight(6999);
        Set<String> userIdSet = new HashSet<>();
        for (UserEO userEO : userEOList) {
            userIdSet.add(userEO.getUsid());
        }
        dailyService.updateApproveUserName(pageDTO.getDataList(), userIdSet);
        return Result.success(pageDTO);
    }



    @ApiOperation(value = "|Daily|分页查询")
    @PostMapping("/page4Tips")
    public ResponseMessage<PageDTO> page4Tips(@RequestBody DailyPage dailyPage) {

        PageDTO pageDTO = dailyService.queryByPage4Tips(dailyPage);

        dailyService.updateCreateTime(pageDTO.getDataList());
        List<UserEO> userEOList = userEODao.getUserWithRolesListByJobWeight(6999);
        Set<String> userIdSet = new HashSet<>();
        for (UserEO userEO : userEOList) {
            userIdSet.add(userEO.getUsid());
        }
        dailyService.updateApproveUserName(pageDTO.getDataList(), userIdSet);
        return Result.success(pageDTO);
    }

    @ApiOperation(value = "|Daily|分页查询某个业务下日报")
    @PostMapping("/page4TipsInByType")
    public ResponseMessage<PageDTO> page4TipsInBudget(@RequestBody DailyPage dailyPage) {
        switch (dailyPage.getPageType()){
            case 0:
                if (StringUtils.isEmpty(dailyPage.getBudgetId())){
                    throw  new AdcDaBaseException("参数错误，业务id未传");
                }
                break;
            case 1:
                if (StringUtils.isEmpty(dailyPage.getProjectId())){
                    throw  new AdcDaBaseException("参数错误，项目id未传");
                }
                break;
            case 2:
                if (StringUtils.isEmpty(dailyPage.getTaskId())){
                    throw  new AdcDaBaseException("参数错误，任务id未传");
                }
                break;
            default:
                throw  new AdcDaBaseException("查询参数异常，没有选择查询类型！");
        }
        PageDTO pageDTO = dailyService.queryByPage4TipsByType(dailyPage);
        dailyService.updateCreateTime(pageDTO.getDataList());
        List<UserEO> userEOList = userEODao.getUserWithRolesListByJobWeight(6999);
        Set<String> userIdSet = new HashSet<>();
        for (UserEO userEO : userEOList) {
            userIdSet.add(userEO.getUsid());
        }
        dailyService.updateApproveUserName(pageDTO.getDataList(), userIdSet);
        return Result.success(pageDTO);
    }



    @ApiOperation(value = "|Daily|条件分页查询")
    @PostMapping("/conditionPage")
    public ResponseMessage<PageDTO> conditionPage(@RequestBody DailyConditionRequestDTO dailyConditionRequestDTO){
        DailyConditionDTO dailyConditionDTO = new DailyConditionDTO();
        String id = null;
        if((id = dailyConditionRequestDTO.getBudgetId()) != null) {
            dailyConditionDTO.setBudgetIds(Lists.newArrayList(new String[]{id})); }
        if((id = dailyConditionRequestDTO.getProjectId()) != null) {
            dailyConditionDTO.setProjectIds(Lists.newArrayList(new String[]{id})); }
        if((id = dailyConditionRequestDTO.getTaskId()) != null) {
            dailyConditionDTO.setTaskIds(Lists.newArrayList(new String[]{id})); }
        if((id = dailyConditionRequestDTO.getChildrenTaskId()) != null) {
            dailyConditionDTO.setChildrenTaskIds(Lists.newArrayList(new String[]{id})); }
        dailyConditionDTO.setPage(dailyConditionRequestDTO.getPage());
        dailyConditionDTO.setSize(dailyConditionRequestDTO.getSize());
        dailyConditionDTO.setDailyCreateTimeBegin(dailyConditionRequestDTO.getDailyCreateTimeBegin());
        dailyConditionDTO.setDailyCreateTimeEnd(dailyConditionRequestDTO.getDailyCreateTimeEnd());
        return Result.success(dailyService.queryByConditionPage(dailyConditionDTO));
    }



    /**
     * 导入
     */
    @PostMapping("/import")
    @ApiOperation(value = "|Daily|导入")
    @RequiresPermissions("fin:daily:import")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) {

        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        try {
            InputStream is = file.getInputStream();
            ImportParams params = new ImportParams();
            dailyService.excelImport(is, params);
            return Result.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.error("导入失败");
        }
    }

    @ApiOperation(value = "|Daily|导出")
    @GetMapping("/export")
    @RequiresPermissions("fin:daily:export")
    public ResponseMessage export(HttpServletResponse response, String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            fileName = "数据表格.xlsx";
        } else {
            if (!fileName.contains(".xlsx")) {
                fileName += ".xlsx";
            }
        }
        OutputStream os = null;
        Workbook workbook = null;

        try {
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");
            ExportParams params = new ExportParams();
            params.setType(ExcelType.XSSF);
            workbook = dailyService.excelExport(params);
            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        } finally {
            IOUtils.closeQuietly(os);
        }
        return Result.success();
    }

    @ApiOperation(value = "|Daily|导出")
    @PostMapping(value = "/exportExcelByPage4TipsByType")
    //@RequiresPermissions("fin:daily:export")
    public ResponseMessage exportExcelByPage4TipsByType(HttpServletResponse response, DailyPage dailyPage) {
        String fileName = "日报数据";
        switch (dailyPage.getPageType()){
            case 0:
                if (StringUtils.isEmpty(dailyPage.getBudgetId())) {
                    throw  new AdcDaBaseException("参数错误，业务Id未传");
                }
                break;
            case 1:
                if (StringUtils.isEmpty(dailyPage.getProjectId())) {
                    throw  new AdcDaBaseException("参数错误，项目Id未传");
                }
                break;
            case 2:
                if (StringUtils.isEmpty(dailyPage.getTaskId())) {
                    throw new AdcDaBaseException("参数错误，任务Id未传");
                }
                break;
            default:
                throw  new AdcDaBaseException("查询参数异常，没有选择查询类型！");
        }
        if (!fileName.contains(".xlsx")) {
            fileName += ".xlsx";
        }
        OutputStream os = null;
        Workbook workbook = null;

        try {
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");
            ExportParams params = new ExportParams();
            params.setType(ExcelType.XSSF);
            workbook = dailyService.exportExcelByPage4TipsByType(dailyPage,params);
            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        } finally {
            IOUtils.closeQuietly(os);
        }
        return Result.success();
    }

    // 当前人的的子任务
    @ApiOperation(value = "|Daily|创建日报可选子任务的列表")
    @GetMapping("/daily/children")
    public ResponseMessage<List<Map<String, String>>> dailyTaskList(String projectId) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        return Result.success(dailyService.queryChildrenDaily(userId, projectId));
    }

    @ApiOperation(value = "|Daily|查询客户和分公司")
    @GetMapping("/branch")
    public ResponseMessage<List<CustomerRecordEO>> queryBranch() throws Exception {
        return Result.success(dailyService.queryBranch());
    }

    @ApiOperation("我的日程数")
    @GetMapping(
            value = {"/task/listsize"},
            produces = {"application/json"}
    )
    public ResponseMessage<Long> getActivitiTaskNumber(String assignee) {
        if (StringUtils.isEmpty(assignee)) {
            return Result.error("至少输入一个查询条件");
        } else {
            String userId = UserUtils.getUserId();
            if (StringUtils.isEmpty(userId)) {
                throw new AdcDaBaseException("登陆可能过期，请登录！");
            }
            long dailyCount = dailyService.countByUserId(userId);
            return Result.success(dailyCount);
        }
    }

    @ApiOperation(value = "|Daily|分页查询已删除日报")
    @PostMapping("/pageByDelFlag")
    public ResponseMessage<PageDTO> pageByDelFlag(@RequestBody BasePage basePage) {
        int page = 1;
        int pageSize = 10;
        if(basePage.getPage() != null){
            page = basePage.getPage();
        }
        if(basePage.getPageSize() != null){
            pageSize = basePage.getPageSize();
        }
        PageDTO pageDTO = dailyService.queryByPageDelFlag(page, pageSize);
        dailyService.updateCreateTime(pageDTO.getDataList());
        List<UserEO> userEOList = userEODao.getUserWithRolesListByJobWeight(6999);
        Set<String> userIdSet = new HashSet<>();
        for (UserEO userEO : userEOList) {
            userIdSet.add(userEO.getUsid());
        }
        dailyService.updateApproveUserName(pageDTO.getDataList(), userIdSet);
        return Result.success(pageDTO);
    }

    @ApiOperation(value = "|Daily|刷新数据")
    @PostMapping("/refreshDaily")
    public ResponseMessage<String> refreshDaily() {
        dailyService.refreshDaily();
        return Result.success("刷新成功！");
    }

    @GetMapping("/dailyStatisticExport")
    @ApiOperation(value = "|Daily|日报统计", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage exportDailyData(String startDate,String endDate) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getResponse();
        OutputStream out = null;
        Workbook workbook = null;
        if (null == startDate || null == endDate) {
            throw new AdcDaBaseException("请指定时间范围");
        }
        try {
            workbook = dailyService.getDailyStatisticWorkbook(startDate,endDate);
            // 文件名
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode("日报统计.xls"));
            response.setContentType("application/force-download");
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            throw new AdcDaBaseException("导出失败，请重试");
        } finally {
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(out);
        }
        return Result.success("导出成功");
    }
    //getDailyStatisticWorkbookNew
    @GetMapping("/getDailyStatisticWorkbookNew")
    @ApiOperation(value = "|Daily|日报统计", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage getDailyStatisticWorkbookNew(String startDate,String endDate,HttpServletResponse servletResponse) {
        OutputStream out = null;
        Workbook workbook = null;
        if (null == startDate || null == endDate) {
            throw new AdcDaBaseException("请指定时间范围");
        }
        try {
            List<DailyExportStasticsVO> dailyExportStasticsVOList = dailyService.getDailyStatisticWorkbookNew(startDate, endDate);
            ExportParams params = new ExportParams();
            params.setType(ExcelType.XSSF);
            workbook = ExcelExportUtil.exportExcel(params, DailyExportStasticsVO.class, dailyExportStasticsVOList);
            // 文件名
            servletResponse.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode("日报统计.xls"));
            servletResponse.setContentType("application/force-download");
            out = servletResponse.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            throw new AdcDaBaseException("导出失败，请重试");
        } finally {
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(out);
        }
        return Result.success("导出成功");
    }
    @GetMapping("/dailyDistributionQuery")
    @ApiOperation(value = "|Daily|日报分布查询")
    public ResponseMessage dailyDistribution(@RequestParam(value = "userIds",required = false)List<String> userIds,
                                             @RequestParam(value = "deptIds",required = false)List<String> deptIds,
                                             @RequestParam(value = "startDate",required = false)String startDate,
                                             @RequestParam(value = "endDate",required = false)String endDate,
                                             @RequestParam(value = "businessName",required = false)String businessName,
                                             @RequestParam(value = "projectName",required = false)String projectName,
                                             @RequestParam(value = "pageSize",required = true)int pageSize,
                                             @RequestParam(value = "pageNo",required = true)int pageNo) {
        String dailyDistribution = dailyService.getDailyDistributionOld(userIds,deptIds, startDate, endDate,businessName,projectName,pageSize,pageNo);

        return Result.success(dailyDistribution);
    }


    @GetMapping("/dailyDistributionExport")
    @ApiOperation(value = "|Daily|日报分布导出", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage dailyDistribution(@RequestParam(value = "userIds",required = false)List<String> userIds,
                                             @RequestParam(value = "deptIds",required = false)List<String> deptIds,
                                             @RequestParam(value = "startDate",required = false)String startDate,
                                             @RequestParam(value = "endDate",required = false)String endDate,
                                             @RequestParam(value = "businessName",required = false)String businessName,
                                             @RequestParam(value = "projectName",required = false)String projectName

    ) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getResponse();
        OutputStream out = null;
        Workbook workbook = null;

        try {
            workbook = dailyService.getDailyDistributionExcel(userIds,deptIds, startDate, endDate,businessName,projectName);
            // 文件名
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode("日报分布.xls"));
            response.setContentType("application/force-download");
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            throw new AdcDaBaseException("导出失败，请重试");
        } finally {
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(out);
        }
        return Result.success("导出成功");

    }
    @GetMapping("/exportDailyDistributionNewTem")
    @ApiOperation(value = "|Daily|日报分布导出")
    public ResponseMessage exportDailyDistributionNewTem(@RequestParam(value = "userIds",required = false)List<String> userIds,
                                                        @RequestParam(value = "deptIds",required = false)List<String> deptIds,
                                                        @RequestParam(value = "startDate",required = false)String startDate,
                                                        @RequestParam(value = "endDate",required = false)String endDate,
                                                        @RequestParam(value = "businessName",required = false)String businessName,
                                                        @RequestParam(value = "projectName",required = false)String projectName,
                                                         HttpServletResponse response) {

        OutputStream out = null;
        Workbook workbook = null;
        try {
            workbook = dailyService.exportDailyDistributionNewTem(userIds,deptIds, startDate, endDate,businessName,projectName);
             // 文件名
            response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode("日报分布.xls"));
            response.setContentType("application/force-download");
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            throw new AdcDaBaseException("导出失败，请重试");
         } finally {
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(out);
        }
        return Result.success("导出成功");
    }
    @GetMapping("/getDailyDistributionNewTem")
    @ApiOperation(value = "|Daily|日报分布查询")
    public ResponseMessage getDailyDistributionNewTem(@RequestParam(value = "userIds",required = false)List<String> userIds,
                                             @RequestParam(value = "deptIds",required = false)List<String> deptIds,
                                             @RequestParam(value = "startDate",required = false)String startDate,
                                             @RequestParam(value = "endDate",required = false)String endDate,
                                             @RequestParam(value = "businessName",required = false)String businessName,
                                             @RequestParam(value = "projectName",required = false)String projectName,
                                             @RequestParam(value = "pageSize",required = true)int pageSize,
                                             @RequestParam(value = "pageNo",required = true)int pageNo) {
        String dailyDistribution = dailyService.getDailyDistributionNewTem(userIds,deptIds, startDate, endDate,businessName,projectName,pageSize,pageNo);

        return Result.success(dailyDistribution);
    }
    @GetMapping("/refreshDailyProjectId")
    @ApiOperation(value = "|Daily|刷数据")
    public void refreshDailyProjectId(){
        dailyService.refreshDailyProjectId();
    }

}
