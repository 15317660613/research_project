package com.adc.da.budget.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.constant.Role;
import com.adc.da.budget.dto.BudgetDto;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.UserWithProjects;
import com.adc.da.budget.page.BudgetEOPage;
import com.adc.da.budget.page.TreeCustomPage;
import com.adc.da.budget.query.QueryVO;
import com.adc.da.budget.query.budget.BudgetQuery;
import com.adc.da.budget.repository.UserWithProjectsRepository;
import com.adc.da.budget.service.BudgetEOService;
import com.adc.da.budget.service.BudgetTreeService;
import com.adc.da.budget.service.UserProjectCustomService;
import com.adc.da.budget.vo.BudgetVO;
import com.adc.da.budget.vo.ListTreeVO;
import com.adc.da.budget.vo.TaskStatusVO;
import com.adc.da.budget.vo.UserProjectCustomVO;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.excel.poi.excel.entity.result.ExcelImportResult;
import com.adc.da.excel.poi.excel.entity.result.ExcelVerifyHanlderErrorResult;
import com.adc.da.file.controller.FileUploadRestController;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.*;
import com.adc.da.util.utils.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/budget/budget")
@Api(tags = "|BudgetEO|")
@Slf4j
public class BudgetEOController extends BaseController<BudgetEO> {

    @Autowired
    private BudgetTreeService budgetTreeService;

    @Autowired
    private BudgetEOService budgetEOService;

    @Autowired
    private UserEOService userEOService;

    @Autowired
    private UserProjectCustomService userProjectCustomService;

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository ;
    private static final String LOGIN_EXPIRED = "登陆可能过期，请登录！";
    private static final String OLNY_OPERATE_BUDGET_ADMIN = "只有经营类业务管理员可以编辑或创建经营类业务！";

    // 上传文件类型允许白名单
    private List<String> whiteUrls = new ArrayList<>();

    @PostConstruct
    public void init() {
        // 读取文件
        InputStream is = FileUploadRestController.class.getClassLoader().getResourceAsStream("white/uploadWhite.txt");
        if (is != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!"".equals(line)) {
                        whiteUrls.add(line);
                    }
                }
            } catch (Exception e) {
                log.error("读取文件上传白名单异常", e);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    log.error("FileInputStream关闭异常", e);
                }
            }
        }
    }

    @ApiOperation(value = "|BudgetEO|查询")
    @GetMapping("")
    //@RequiresPermissions("fin:budget:list")
    public ResponseMessage<List<BudgetEO>> listAll() {
        return Result.success(budgetEOService.queryAll());
    }

    @ApiOperation(value = "|BudgetEO|查询-用于数据回显")
    @GetMapping("/listForForm")
    //@RequiresPermissions("fin:budget:list")
    public ResponseMessage<List<BudgetEO>> listForForm() {
        return Result.success(budgetEOService.listForForm());
    }

    @ApiOperation(value = "|BudgetEO|分页查询")
    @PostMapping("/page")
    //@RequiresPermissions("fin:budget:list")
    public ResponseMessage<PageInfo<BudgetEO>> list(HttpServletRequest request,
        @RequestBody BudgetEOPage page) throws Exception {
        String userId = (String) request.getSession().getAttribute(RequestUtils.LOGIN_USER_ID);
        if (StringUtils.isNotEmpty(userId)) {
            page.setDeptIdOperator("IN");
            page.setProjectNameOperator("LIKE");
            page.setOrderBy("CREATE_TIME");
            page.setOrder("DESC");
        }
        List<BudgetEO> rows = budgetEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|BudgetEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("fin:budget:get")
    @RequiresAuthentication
    public ResponseMessage<BudgetVO> find(@PathVariable @NotNull String id) {
        return Result.success(budgetEOService.findOne(id));
    }

    @ApiOperation(value = "|BudgetEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("fin:budget:save")
    public ResponseMessage<BudgetEO> create(HttpServletRequest request,
        @RequestBody @Valid BudgetEO budgetEO) throws Exception {
        UserEO loginUserEO = UserUtils.getUser();
        if (null!= loginUserEO && StringUtils.isEmpty(loginUserEO.getUsid())) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }
//        //如果是经营类项目，判断添加者是不是经营类业务管理员，不是则不让添加
//        if (StringUtils.equals(budgetEO.getProperty(),"0")){
//                Subject subject = SecurityUtils.getSubject();
//                if (!subject.hasRole(Role.OPERATE_BUDGET_ADMIN) ) {
//                    throw new AdcDaBaseException(OLNY_OPERATE_BUDGET_ADMIN);
//                }
//            budgetEO.setBusinessAdminId(loginUserEO.getUsid());
//            budgetEO.setBusinessAdminName(loginUserEO.getUsname());
//        }
        if (budgetEOService.selectByPmAndBudgetName(null, budgetEO.getPm(), budgetEO.getProjectName()) > 0) {
            return Result.error("新增业务时，业务经理相同情况下，业务名称不允许相同!");
        }
        UserEO userEO = userEOService.getUserWithRoles(budgetEO.getPm());
        List<OrgEO> orgList = userEO.getOrgEOList();
        OrgEO orgEO = orgList.get(orgList.size() - 1);
        budgetEO.setDeptId(orgEO.getId());
        budgetEO.setDeptName(orgEO.getName());
        budgetEO.setCreateTime(new Date());
        if (orgList.size() > 1) {budgetEO.setProjectTeam(orgList.get(0).getId());}
        budgetEO.setCurrentYear(DateUtils.dateToString(new Date(), "yyyy"));
        return Result.success(budgetEOService.save(budgetEO));
    }

    @ApiOperation(value = "有验证的Excel导入")
    @PostMapping("/excelImportBudgetVerify")
    public ResponseMessage excelImportVerify
        (@RequestParam("file") MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        List<ExcelVerifyHanlderErrorResult> errors = budgetEOService.ExcelImportVerify(is, params);
        ResponseMessage result = Result.success();
        if (errors != null && !errors.isEmpty()) {
            result.setMessage(errors.toString());
        }
        return result;
    }

    @ApiOperation(value = "|BudgetEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("fin:budget:update")
    public ResponseMessage<BudgetEO> update(@RequestBody BudgetEO budgetEO) throws Exception {
        budgetEO.setUpdateTime(new Date());
        UserEO loginUserEO = UserUtils.getUser();
        if (null!= loginUserEO && StringUtils.isEmpty(loginUserEO.getUsid())) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }
        if (budgetEOService.selectByPmAndBudgetName(budgetEO.getId(), budgetEO.getPm(), budgetEO.getProjectName()) > 0) {
            return Result.error("修改业务时，业务经理相同情况下，业务名称不允许相同!");
        }
        Subject subject = SecurityUtils.getSubject();
        BudgetEO old = budgetEOService.selectByPrimaryKey(budgetEO.getId());
        //经营类 0 ， 日常事务1 科研2
        /**
         * 2.	经营类业务的管理员，从OA同步后，默认为空，可由经营类业务经理自行添加。
         * 3.	科研类业务管理员默认为空，如后期有需求，可由超级管理员进行人员补充。
         * 4.	日常事务类业务管理员目前系统中的均默认为空，可由业务经理自行添加。
         */
//        if (StringUtils.equals(old.getProperty(),"0")){
//            if (!StringUtils.equals(old.getPm(),loginUserEO.getUsid())
////                    &&!StringUtils.equals(old.getBusinessAdminId(),budgetEO.getBusinessAdminId())
//                    &&!subject.hasRole(Role.SUPER_ADMIN) ){
//                throw  new AdcDaBaseException("只有业务经理和超级管理员能修改业务管理员！");
//            }
//        }else if (StringUtils.equals(old.getProperty(),"1")){
//
//        }else if (StringUtils.equals(old.getProperty(),"2")){
//
//        }
        //只有超级管理员、主任、项目管理员、业务创建人以及业务经理可以修改业务
        if (!StringUtils.equals(loginUserEO.getUsid(), old.getPm())
            && !StringUtils.equals(loginUserEO.getUsid(), old.getCreateUserId())
            && !subject.hasRole(Role.ZHU_REN)
            && !subject.hasRole(Role.SUPER_ADMIN)
            && !subject.hasRole(Role.PROJECT_ADMIN)
//            && !subject.hasRole(Role.OPERATE_BUDGET_ADMIN)
                //存在业务管理员 且又与登录人不相等
        &&!StringUtils.equals(loginUserEO.getUsid(), old.getBusinessAdminId())) {
            throw new AdcDaBaseException("您无权修改！");
        }
        //如果业务所属部门变更修改部门信息
        if (StringUtils.isNotEmpty(budgetEO.getPm())) {
            UserEO userEO = userEOService.getUserWithRoles(budgetEO.getPm());
            List<OrgEO> orgEOList = userEO.getOrgEOList();
            if (CollectionUtils.isNotEmpty(orgEOList)) {
                OrgEO dept = orgEOList.get(orgEOList.size()-1);
                budgetEO.setDeptId(dept.getId());
                budgetEO.setDeptName(dept.getName());
                budgetEO.setDomainId(old.getDomainId());
            }
        }
        // budgetEOService.setPM(budgetEO);

        if (StringUtils.isNotEmpty(budgetEO.getBusinessAdminId())
                && StringUtils.isNotEmpty(old.getBusinessAdminId())
                && !StringUtils.equals(budgetEO.getBusinessAdminId(),old.getBusinessAdminId())) {
            budgetEOService.updateUserBudgetAdminNew(budgetEO, old);
            userProjectCustomService.upadteUserProjectCustomEOByBudgetId(budgetEO.getBusinessAdminId(),budgetEO.getId());
        }
        if (StringUtils.isNotEmpty(budgetEO.getPm())
                && StringUtils.isNotEmpty(old.getPm())
                &&!StringUtils.equals(budgetEO.getPm(),old.getPm())) {
            budgetEOService.updateUserBudgetPMNew(budgetEO, old);
        }
        budgetEOService.updateByPrimaryKey(budgetEO);
        return Result.success(budgetEO);
    }

    @ApiOperation(value = "|BudgetEO|批量删除")
    @DeleteMapping("/{ids}")
    //@RequiresPermissions("fin:budget:delete")
    public ResponseMessage delete(@NotNull @PathVariable("ids") String[] ids) {
        List<String> idList = Arrays.asList(ids);
        budgetEOService.deleteInBatch(idList);
        return Result.success();
    }

    @ApiOperation(value = "预算数据上传")
    @PostMapping("/upload")
    public ResponseMessage uploadData(@RequestParam("file") MultipartFile file)
        throws Exception {
        InputStream is = null;

        String fileExtension = FileUtil.getFileExtension(file.getOriginalFilename());

        if (!whiteUrls.contains(fileExtension)) {
            log.error("上传文件类型不允许，请重试");
            return Result.error("r0071", "上传文件类型不允许，请重试");
        }
        // 获取文件输入流
        is = file.getInputStream();

        // 导入参数设置，默认即可
        ImportParams params = new ImportParams();
        // 解析excel，并返回校验信息
        ExcelImportResult<BudgetDto> result = ExcelImportUtil.importExcelVerify(is, BudgetDto.class, params);
        // 如果校验不通过，返回错误信息
        if (result.isVerfiyFail()) {
            log.error("excel校验不通过！");
            List<ExcelVerifyHanlderErrorResult> errors = result.getErrors();
            StringBuilder sb = new StringBuilder();
            for (ExcelVerifyHanlderErrorResult error : errors) {
                sb.append("[").append(error.getRowNum()).append("行")
                  .append(error.getColumnNum()).append("列]").append(error.getMsg())
                  .append("/t/n");
            }
            return Result.error("r0101", sb.toString());
        }
        // 校验通过，数据入库
        List<BudgetDto> datas = result.getList();
        List<BudgetEO> datasEO = new ArrayList<>();
        for (BudgetDto dto : datas) {
            BudgetEO eo = new BudgetEO();
            BeanUtils.copyProperties(dto, eo);
            eo.setId(UUID.randomUUID());
            datasEO.add(eo);
        }

        return Result.success();
    }

    @ApiOperation(value = "预算数据下载")
    @GetMapping("/download")
    public void download(HttpServletResponse response) throws Exception {
        OutputStream os = null;
        Workbook workbook = null;
        try {
            response.setHeader(
                "Content-Disposition",
                "attachment; filename=" + Encodes.urlEncode("预算数据.xlsx"));
            response.setContentType("application/force-download");
            ExportParams exportParams = new ExportParams();
            exportParams.setType(ExcelType.XSSF);
            BudgetEOPage budgetEOPage = new BudgetEOPage();
            String userId = UserUtils.getUserId();
            if (StringUtils.isEmpty(userId)) {
                throw new AdcDaBaseException("登陆可能过期，请登录！");
            }
            budgetEOPage.setQ(userId);
            List<BudgetEO> datas = budgetEOService.queryByList(budgetEOPage);
            List<BudgetDto> datasDto = new ArrayList<>();
            if (datas != null && !datas.isEmpty()) {
                for (BudgetEO eo : datas) {
                    BudgetDto dto = new BudgetDto();
                    BeanUtils.copyProperties(eo, dto);
                    datasDto.add(dto);
                }
            }
            workbook = ExcelExportUtil.exportExcel(exportParams, BudgetDto.class, datasDto);
            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new AdcDaBaseException("下载预算数据文件失败，请重试");
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    @ApiOperation(value = "业务部门下所有成员")
    @GetMapping("/{id}/users")
    public ResponseMessage<List<Map<String, String>>> getUserByBudget(@PathVariable("id") String id) throws Exception {
        return Result.success(budgetEOService.queryUserByBudgetId(id));
    }

    @ApiOperation(value = "中心所有成员")
    @GetMapping("/users")
    public ResponseMessage<List<Map<String, String>>> queryUserOrg() throws Exception {
        return Result.success(budgetEOService.queryUserOrg());
    }

    @ApiOperation(value = "我的业务")
    @PostMapping("/myBudget")
    public ResponseMessage<PageInfo<BudgetEO>> getMyBudget(@RequestBody BudgetEOPage page) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        page.setOrderBy("CREATE_TIME");
        page.setOrder("DESC");
        page.setPm(userId);
        page.setQ(userId);
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("请登录！");
        }
        List<BudgetEO> rows = budgetEOService.getMyBudget(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "信息化树")
    @GetMapping("/tree")
    public ResponseMessage<List<ListTreeVO>> getListTreeVO(
            @RequestParam(value = "keyword", required = false)String keyword,
            @RequestParam(required = false) Integer type,
            @RequestParam(value = "property",required = false) String property) {
        //默认
        try {
            //status==main代表查询首页树 默认main
            List<ListTreeVO> treeVOList = budgetTreeService.getListTree(keyword, type, "main", property, false);
            Collections.sort(treeVOList, new Comparator<ListTreeVO>() {
                public int compare(ListTreeVO o1,ListTreeVO o2) {
                    return o1.getFirstSpell()
                            .compareTo( o2.getFirstSpell());
                }
            });
            return Result.success(treeVOList);
        } catch (AdcDaBaseException e) {
            return Result.error("-1", e.getMessage(), null);
        }
    }

    @ApiOperation(value = "隐藏操作信息化树")
    @GetMapping("/treeCustom")
    public ResponseMessage<List<UserProjectCustomVO>> getCustomListTreeVO(
            @RequestParam(value = "keyword", required = false)String keyword,
            @RequestParam(required = false) Integer type,
            @RequestParam(value = "status", required = false)String status) {
        /**
         * status  all全部   0隐藏  1显示
         */
        try {
            List<UserProjectCustomVO> customTreeVOList =
                    budgetTreeService.getUserProjectCustomTree(keyword, type,status);
            Collections.sort(customTreeVOList, new Comparator<UserProjectCustomVO>() {
                public int compare(UserProjectCustomVO o1,UserProjectCustomVO o2) {
                    return o1.getFirstSpell()
                            .compareTo( o2.getFirstSpell());
                }
            });
            return Result.success(customTreeVOList);
        } catch (AdcDaBaseException e) {
            return Result.error("-1", e.getMessage(), null);
        }
    }

    @ApiOperation(value = "隐藏操作信息化树")
    @PostMapping("/treeCustomPage")
    public ResponseMessage<PageInfo<UserProjectCustomVO>> postCustomListTreeVOPage(
        @RequestBody  TreeCustomPage treeCustomPage) {

        /*
         * status  all全部   0隐藏  1显示
         */
        try {
            return Result.success(budgetTreeService.getCustomTreePage(treeCustomPage));
        } catch (AdcDaBaseException e) {
            return Result.error("-1", e.getMessage(), null);
        }
    }
    @ApiOperation(value = "修改任务的负责人为空的数据")
    @GetMapping("/updateTaskApproveUser")
    public ResponseMessage updateTaskApproveUser (){
        try {
            budgetTreeService.updateTaskApproveUser();
            return Result.success();
        } catch (Exception e) {
            return Result.error("-1", e.getMessage(), null);
        }
    }

    @ApiOperation(value = "修改信息化显示或者隐藏状态")
    @PutMapping(value = "/userProjectCustomInsertUpdate",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<Void> userProjectCustomInsertUpdate(
            @RequestBody @Valid UserProjectCustomVO userProjectCustomVO) throws Exception{
        try {
            budgetTreeService.userProjectCustomInsertUpdate(userProjectCustomVO);
            return Result.success("1","更新成功",null);
        }catch (AdcDaBaseException e){
            return Result.error("-1",e.getMessage(),null);
        }


    }
    @ApiOperation(value = "业务查询页")
    @PostMapping("/findByPage")
    public ResponseMessage<PageInfo<BudgetEO>> findByPage(@RequestBody BudgetQuery page) {
        // 把前台时间查询做下转换
        if (page.getCreateTimes() != null && page.getCreateTimes().size() > 0) {
            for (QueryVO queryVO : page.getCreateTimes()) {
                Date date = DateUtils.stringToDate(queryVO.getName(), "yyyy-MM-dd");
                queryVO.setName(DateUtils.dateToString(date, "yyyy-MM-dd"));
            }
        }
        if (page.getUpdateTimes() != null && page.getUpdateTimes().size() > 0) {
            for (QueryVO queryVO : page.getUpdateTimes()) {
                Date date = DateUtils.stringToDate(queryVO.getName(), "yyyy-MM-dd");
                queryVO.setName(DateUtils.dateToString(date, "yyyy-MM-dd"));
            }
        }
        if (page.getCycleBegins() != null && page.getCycleBegins().size() > 0) {
            for (QueryVO queryVO : page.getCycleBegins()) {
                Date date = DateUtils.stringToDate(queryVO.getName(), "yyyy-MM-dd");
                queryVO.setName(DateUtils.dateToString(date, "yyyy-MM-dd"));
            }
        }
        if (page.getCycleEnds() != null && page.getCycleEnds().size() > 0) {
            for (QueryVO queryVO : page.getCycleEnds()) {
                Date date = DateUtils.stringToDate(queryVO.getName(), "yyyy-MM-dd");
                queryVO.setName(DateUtils.dateToString(date, "yyyy-MM-dd"));
            }
        }
        List<BudgetEO> rows = budgetEOService.findByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "机构树")
    @GetMapping("/orgTree")
    public ResponseMessage<List<ListTreeVO>> getOrgTreeVO(@RequestParam(value = "keyword", required = false)
        String keyword, @RequestParam(required = false) Integer type) {
        return Result.success(budgetTreeService.getOrgTreeVO(keyword, type));
    }

    @ApiOperation(value = "业务下的项目-条件查询")
    @GetMapping("/getProjectsByTips")
    public ResponseMessage<List<Project>> getProjects(@RequestParam("bussinessId") String bussinessId,
        @RequestParam(value = "projectName", required = false) String projectName,
        @RequestParam(value = "projectStatus", required = false) String projectStatus,
        @RequestParam(value = "projectManager", required = false) String projectManager) {
        return Result.success(budgetEOService.getProjects(bussinessId, projectName,
            projectStatus, projectManager));
    }

    @ApiOperation(value = "业务下的任务状态")
    @GetMapping("/{id}/getTaskStatus")
    public ResponseMessage<List<TaskStatusVO>> getTaskStatus(@PathVariable("id") String id) {
        return Result.success(budgetEOService.getTaskStatus(id));
    }

    @ApiOperation(value = "业务下的人力统计")
    @GetMapping("/{id}/{year}/getBusinessYearData")
    public ResponseMessage<String[]> getPersonInput(@PathVariable("id") String id, @PathVariable("year") String year) {
        return Result.success(budgetEOService.getPersonInput(id, year));
    }

    @ApiOperation(value = "所属业务")
    @GetMapping("/getBelongBudget")
    public ResponseMessage<List<BudgetEO>> getPersonInput() {
        return Result.success(budgetEOService.getBelongBudget());
    }

    @ApiOperation(value = "人力投入")
    @GetMapping("/getManDayByMonth/{budgetId}")
    public ResponseMessage<Map<Integer, Double>> getManDayByMonth(@PathVariable String budgetId) {
        return Result.success(budgetEOService.getManDayByMonth(budgetId));
    }

    @ApiOperation(value = "根据业务属性查询业务（0-经营类业务，1-非经营类业务,2-科研类业务）")
    @GetMapping("/getBudgetList")
    public ResponseMessage<List<BudgetEO>> getBudgetList(String[] property) {
        return Result.success(budgetEOService.queryAllBudget(property));
    }

    @ApiOperation(value = "重新构建某个用户的业务树关系")
    @GetMapping("/createUserWithProjectByUserId")
    public ResponseMessage<String> createUserWithProjectByUserId(String userId) {
        return Result.success(budgetEOService.createUserWithProjectByUserId(userId));
    }

    @ApiOperation(value = "清空用户的业务树关系，用于测试")
    //@GetMapping("/resizeTree")
    public ResponseMessage<String> resizeTree(String userId) {
        UserWithProjects userWithProjects = new UserWithProjects();
        userWithProjects.setUserId(userId);
        userWithProjectsRepository.save(userWithProjects);
        return Result.success("清空完毕！");
    }


    @ApiOperation(value = "移动旧业务下的东西到新业务中去")
    @GetMapping("/moveOldToNewBudget")
    public ResponseMessage<String> moveOldToNewBudget(String oldBudgetId , String newBudgetId) {
        budgetEOService.moveOldToNewBudget(oldBudgetId,newBudgetId);
        return Result.success();
    }

    @ApiOperation(value = "根据业务名称模糊查询经营类业务")
    @GetMapping("/findAllBudgetNameLikeAndByType")
    public ResponseMessage<List<BudgetEO>> findAllBudgetNameLikeAndByType(String budgetName) {
        return Result.success(budgetEOService.findAllBudgetNameLikeAndByType(budgetName));
    }
}
