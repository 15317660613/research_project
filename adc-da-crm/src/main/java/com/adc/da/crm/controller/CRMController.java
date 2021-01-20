package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.adc.da.base.page.BasePage;
import com.adc.da.base.page.Pager;
import com.adc.da.crm.annotation.MatchField;
import com.adc.da.crm.page.AdcFormDataVOPage;
import com.adc.da.crm.entity.excel.BlockInfo;
import com.adc.da.crm.entity.excel.CompareInfo;
import com.adc.da.crm.entity.excel.ContractInfo;
import com.adc.da.crm.entity.excel.DeptInfo;
import com.adc.da.crm.entity.excel.GroupInfo;
import com.adc.da.crm.service.*;
import com.adc.da.crm.util.FormId2BeanNameEnum;
import com.adc.da.crm.util.SpringContextUtils;
import com.adc.da.crm.vo.PageVO;
import com.adc.da.form.entity.AdcFormEO;
import com.adc.da.form.service.CustomFormService;
import com.adc.da.util.utils.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.form.entity.AdcFormDataEO;
import com.adc.da.form.page.AdcFormDataEOPage;
import com.adc.da.form.vo.AdcFormDataVO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;

@RestController
@RequestMapping("/${restPath}/crm/content")
@Api(description = "|content|")
public class CRMController extends BaseController<AdcFormDataEO> {
    private static final Logger logger = LoggerFactory.getLogger(CRMController.class);
    
    @Autowired
    private CRMService crmService;
    
    @Autowired
    CustomFormService customFormService;
    
    @Autowired
    private ProjectInfoEOService projectInfoEOService;
    
    @Autowired
    private ProjectClosureApprovalEOService projectClosureApprovalEOService;
    
    @Autowired
    private ProjectEstablishApprovalEOService projectEstablishApprovalEOService;
    
    @Autowired
    private ProjectTargetBottomEOService projectTargetBottomEOService;
    
    @Autowired
    private ContractBaseEOService contractBaseEOService;
    
    @Autowired
    private SalesVlaueEOService salesVlaueEOService;
    
    @Autowired
    private BTravelApprovalEOService bTravelApprovalEOService;
    
    @Autowired
    private ContractApprovalEOService contractApprovalEOService;
    
    @Autowired
    private CustomerRecordEOService customerRecordEOService;
    
    @Autowired
    private InvoiceApprovalEOService invoiceApprovalEOService;
    
    @Autowired
    SpringContextHolder springContextHolder;
    
    @Autowired
    BeanMapper beanMapper;
    
    @ApiOperation(value = "|content|通过表单id查询表单内容列表")
    @GetMapping({ "/{formId}" })
    public ResponseMessage<PageInfo<AdcFormDataVO>> getById(@NotNull @PathVariable("formId") String formId,
        AdcFormDataEOPage adcFormDataEOPage) {
        try {
            AdcFormDataVOPage adcFormDataVOPage = beanMapper.map(adcFormDataEOPage, AdcFormDataVOPage.class);
            adcFormDataVOPage.setFormId(formId);
            List<AdcFormDataEO> rows = this.crmService.queryPageByFormId(adcFormDataVOPage);
            adcFormDataEOPage.getPager().setRowCount(crmService.queryByCount(adcFormDataVOPage));
            return Result.success(this.beanMapper
                .mapPage(this.getPageInfo(adcFormDataEOPage.getPager(), rows), AdcFormDataVO.class));
        } catch (Exception var8) {
            logger.error("表单数据集管理异常", var8);
            return Result.error("表单数据集管理异常");
        }
    }
    
    @ApiOperation(value = "|crmInsert|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("crm:projectInfo:save")
    public ResponseMessage<AdcFormDataVO> create(HttpServletRequest request, @RequestBody AdcFormDataVO adcFormDataVO)
        throws Exception {
        if (adcFormDataVO == null || adcFormDataVO.getFormId() == null) {
            return Result.error("表单id错误！");
        }
        //根据formId获取相应的service名称
        String serviceName = this.crmService.queryServiceNameByFormId(adcFormDataVO.getFormId());
        if (StringUtils.isEmpty(serviceName)) {
            return Result.error("表单错误！");
        }
        //封装数据
        //获取当前登录用户
        HttpSession session = request.getSession();
        String userId = session.getAttribute("LOGIN_USER_ID").toString();
        //封装ct_form 数据
        String formTitle = adcFormDataVO.getFormTitle().replaceAll("&#40;", "(").replaceAll("&#41;", ")");
        String formContent = adcFormDataVO.getFormContent().replaceAll("&#39;", "'");
        adcFormDataVO.setFormTitle(formTitle);
        adcFormDataVO.setFormContent(formContent);
        AdcFormDataEO adcFormDataEO = getAdcFormDataEO(adcFormDataVO);
        if (adcFormDataEO == null) { return null; }
        adcFormDataEO.setCreateName(userId);
        //根据formId区分调用不同的service根据
        Object obj = (springContextHolder.getBean(serviceName));
        Method method = obj.getClass().getMethod("save", AdcFormDataEO.class);
        AdcFormDataEO resEO = (AdcFormDataEO) method.invoke(obj, adcFormDataEO);
        AdcFormDataVO resVo = (AdcFormDataVO) this.beanMapper.map(resEO, AdcFormDataVO.class);
        resVo.setId(adcFormDataEO.getId());
        
        return Result.success(resVo);
    }
    
    private AdcFormDataEO getAdcFormDataEO(AdcFormDataVO adcFormDataVO) throws Exception {
        AdcFormDataEO adcFormDataEO = (AdcFormDataEO) this.beanMapper.map(adcFormDataVO, AdcFormDataEO.class);
        //补充表单数据
        AdcFormEO adcFormEO = customFormService.selectByPrimaryKey((String) adcFormDataVO.getFormId());
        if (adcFormEO == null || adcFormEO.getColumnName() == null || adcFormEO.getColumnID() == null ||
            adcFormEO.getColumnName().trim().isEmpty()) { return null; }
        adcFormDataEO.setAdcFormEO(adcFormEO);
        return adcFormDataEO;
    }
    
    @ApiOperation("|AdcFormDataEO|修改")
    @PutMapping(
        consumes = { "application/json;charset=UTF-8" }
    )
    public ResponseMessage<AdcFormDataVO> update(HttpServletRequest request, @RequestBody AdcFormDataVO adcFormDataVO) {
        if (adcFormDataVO == null || adcFormDataVO.getFormId() == null) {
            return Result.error("表单id错误！");
        }
        //根据formId获取相应的service名称
        String serviceName = this.crmService.queryServiceNameByFormId(adcFormDataVO.getFormId());
        if (StringUtils.isEmpty(serviceName)) {
            return Result.error("表单错误！");
        }
        
        //获取当前登录用户
        HttpSession session = request.getSession();
        if (session.getAttribute("LOGIN_USER_ID") == null) { return Result.error("用户未登录！"); }
        String userId = session.getAttribute("LOGIN_USER_ID").toString();
//        String formTitle = adcFormDataVO.getFormTitle().replaceAll("&#40;", "(").replaceAll("&#41;", ")");
        String formContent = adcFormDataVO.getFormContent().replaceAll("&#39;", "'");
//        adcFormDataVO.setFormTitle(formTitle);
        adcFormDataVO.setFormContent(formContent);
        
        try {
            AdcFormDataEO adcFormDataEO = getAdcFormDataEO(adcFormDataVO);
            if (adcFormDataEO == null) { return null; }
            Object obj = (springContextHolder.getBean(serviceName));
            Method method = obj.getClass().getMethod("update", String.class, AdcFormDataEO.class);
            int res = (int) method.invoke(obj, userId, adcFormDataEO);
            if (res > 0) {
                return Result.success(adcFormDataVO);
            } else {
                return Result.error("删除失败");
            }
            
        } catch (Exception var5) {
            logger.error("表单数据集管理异常", var5);
            return Result.error("表单数据集管理异常");
        }
    }
    
    @SuppressWarnings("resource")
    @ApiOperation(value = "|excel|导出")
    @GetMapping("/export")
    public ResponseMessage excelExport(HttpServletResponse response, String fileName, String type, String year,
        String month) {
        if (StringUtils.isEmpty(fileName)) {
            fileName = "数据表格.xlsx";
        } else {
            if (!fileName.contains(".xlsx")) {
                fileName += ".xlsx";
            }
        }
        OutputStream os = null;
        Workbook workbook = new XSSFWorkbook();
        
        try {
            response.setHeader(
                "Content-Disposition",
                "attachment; filename=" + Encodes.urlEncode(fileName));
            //response.setContentType("application/force-download");
            response.setContentType("application/x-download");
            response.setCharacterEncoding("utf-8");
            // 设置导出参数为07版本Excel
            ExportParams params = new ExportParams();
            params.setType(ExcelType.XSSF);
            switch (type) {
                case "1":
                    List<ContractInfo> list1 = new ArrayList<>();
                    list1 = crmService.excelExport("1");
                    workbook = ExcelExportUtil.exportExcel(params, ContractInfo.class, list1);
                    break;
                case "2":
                    List<ContractInfo> list2 = new ArrayList<>();
                    list2 = crmService.excelExport("2");
                    workbook = ExcelExportUtil.exportExcel(params, ContractInfo.class, list2);
                    break;
                case "3":
                    List<GroupInfo> list3 = new ArrayList<>();
                    list3 = crmService.excelExport3("3");
                    workbook = ExcelExportUtil.exportExcel(params, GroupInfo.class, list3);
                    break;
                case "4":
                    List<GroupInfo> list4 = new ArrayList<>();
                    list4 = crmService.excelExport3("4");
                    workbook = ExcelExportUtil.exportExcel(params, GroupInfo.class, list4);
                    break;
                case "5":
                    List<BlockInfo> list5 = new ArrayList<>();
                    list5 = crmService.excelExport5("5");
                    workbook = ExcelExportUtil.exportExcel(params, BlockInfo.class, list5);
                    break;
                case "6":
                    List<BlockInfo> list6 = new ArrayList<>();
                    list6 = crmService.excelExport5("6");
                    workbook = ExcelExportUtil.exportExcel(params, BlockInfo.class, list6);
                    break;
                case "7":
                    List<DeptInfo> list7 = new ArrayList<>();
                    list7 = crmService.excelExport7("7");
                    workbook = ExcelExportUtil.exportExcel(params, DeptInfo.class, list7);
                    break;
                case "8":
                    List<DeptInfo> list8 = new ArrayList<>();
                    list8 = crmService.excelExport7("8");
                    workbook = ExcelExportUtil.exportExcel(params, DeptInfo.class, list8);
                    break;
                case "9":
                    Calendar cal = Calendar.getInstance();
                    if (year == null || "".equals(year.trim())) {
                        year = String.valueOf(cal.get(Calendar.YEAR));
                    }
                    if (month == null || "".equals(month.trim())) {
                        month = String.valueOf(cal.get(Calendar.MONTH) + 1);
                    }
                    Map m = crmService.excelExport9(month, year);
                    workbook = (Workbook) m.get("workbook");
                    break;
                case "10":
                    Calendar calendar = Calendar.getInstance();
                    year = String.valueOf(calendar.get(Calendar.YEAR));
                    List<CompareInfo> list10 = new ArrayList<>();
                    list10 = crmService.excelExport10("12", year);
                    workbook = ExcelExportUtil.exportExcel(params, CompareInfo.class, list10);
                    break;
                default:
                    break;
            }
            
            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        } finally {
            IOUtils.closeQuietly(os);
        }
        return Result.success();
    }
    
    @SuppressWarnings("resource")
    @ApiOperation(value = "|excel|报表数据查询")
    @GetMapping("/excelData")
    public ResponseMessage excelData(HttpServletResponse response, String type, String year, String month) {
        try {
            switch (type) {
                case "1":
                    List<ContractInfo> list1 = new ArrayList<>();
                    list1 = crmService.excelExport("1");
                    return Result.success(list1);
                case "2":
                    List<ContractInfo> list2 = new ArrayList<>();
                    list2 = crmService.excelExport("2");
                    return Result.success(list2);
                case "3":
                    List<GroupInfo> list3 = new ArrayList<>();
                    list3 = crmService.excelExport3("3");
                    return Result.success(list3);
                case "4":
                    List<GroupInfo> list4 = new ArrayList<>();
                    list4 = crmService.excelExport3("4");
                    return Result.success(list4);
                case "5":
                    List<BlockInfo> list5 = new ArrayList<>();
                    list5 = crmService.excelExport5("5");
                    return Result.success(list5);
                case "6":
                    List<BlockInfo> list6 = new ArrayList<>();
                    list6 = crmService.excelExport5("6");
                    return Result.success(list6);
                case "7":
                    List<DeptInfo> list7 = new ArrayList<>();
                    list7 = crmService.excelExport7("7");
                    return Result.success(list7);
                case "8":
                    List<DeptInfo> list8 = new ArrayList<>();
                    list8 = crmService.excelExport7("8");
                    return Result.success(list8);
                case "9":
                    Calendar cal = Calendar.getInstance();
                    if (year == null || "".equals(year.trim())) {
                        year = String.valueOf(cal.get(Calendar.YEAR));
                    }
                    if (month == null || "".equals(month.trim())) {
                        month = String.valueOf(cal.get(Calendar.MONTH) + 1);
                    }
                    Map m = crmService.excelExport9(month, year);
                    return Result.success((List) m.get("returnList"));
                case "10":
                    Calendar calendar = Calendar.getInstance();
                    year = String.valueOf(calendar.get(Calendar.YEAR));
                    List<CompareInfo> list10 = new ArrayList<>();
                    list10 = crmService.excelExport10("12", year);
                    return Result.success(list10);
                default:
                    return Result.error("未找到数据！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.success(e.getMessage());
        }
    }
    
    @DeleteMapping("/{formId}/{id}")
    @ApiOperation(value = "|CRMController|删除")
    public ResponseMessage delete(@PathVariable("formId") String formId, @PathVariable("id") String id)
        throws Exception {
        if (formId == null) {
            return Result.error("表单id错误！");
        }
        //根据formId获取相应的service名称
        String serviceName = this.crmService.queryServiceNameByFormId(formId);
        if (StringUtils.isEmpty(serviceName)) {
            return Result.error("表单错误！");
        }
        
        // 获取bean,注意这里用实现类的接口强转去获得目标bean的代理对象，才能成功执行下面的反射方法
        Object obj = (springContextHolder.getBean(serviceName));
        Method method = obj.getClass().getMethod("delete", String.class);
        int res = (int) method.invoke(obj, id);
        if (res > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
    
    /**
     * 获取列表内容
     * 2018-12-20新增搜索功能
     *
     * @param formId      表单id
     * @param basePage    基础参数
     * @param searchField 搜索字段
     * @return
     * @author Lee Kwanho 李坤澔
     * date 2018-12-20
     **/
    @GetMapping("/forms")
    @ApiOperation(value = "|CRMController|获取列表内容")
    public ResponseMessage<PageVO<Object>> forms(String formId, BasePage basePage, String searchField)
        throws Exception {
        String beanName = FormId2BeanNameEnum.getBeanName(formId);
        
        //从bean获取Service， 反射获取service 的queryByList和queryByCount方法，返回结果
        Object serviceBean = springContextHolder.getBean(SpringContextUtils.getServiceBeanName(beanName));
        Method queryByListMethod = serviceBean.getClass().getMethod("queryCRMListByPage", BasePage.class);
        Class pageClass = Class.forName(SpringContextUtils.getPageClassName(beanName));
        BasePage basePage1 = (BasePage) pageClass.newInstance();
        basePage1.setPage(basePage.getPage());
        basePage1.setPageSize(basePage.getPageSize());
        List<Object> sourceList = (List<Object>) queryByListMethod.invoke(serviceBean, basePage1);
        
        PageInfo<Object> pageInfo;
        /*
         *  模糊搜索实现
         */
        if (searchField != null) {
            List<Object> result = new ArrayList<>();
            for (Object o : sourceList) {
                if (o.toString().contains(searchField)) {
                    result.add(o);
                }
            }
            pageInfo = setPagerInfo(basePage1.getPager(), result);
            
        } else {
            pageInfo = setPagerInfo(basePage1.getPager(), sourceList);
            
        }
        
        //获取表头数据
        List<Map<String, String>> tableHeadList = getTableHead(beanName);
        PageVO<Object> pageVO = new PageVO<>();
        pageVO.setPageInfo(pageInfo);
        pageVO.setTableHeadList(tableHeadList);
        return Result.success(pageVO);
    }
    
    /**
     * 用于重新拼接Page信息
     * @param pager
     * @param rows
     * @return
     * @author Lee Kwanho 李坤澔
     * date 2018-12-20
     **/
    private PageInfo<Object> setPagerInfo(Pager pager, List<Object> rows) {
        /*
         * 搜索后下列参数会有变化，因此需要重新设置
         */
        int count = pager.getRowCount();
        int pageSize = pager.getPageSize();
        int pageCount = count / pageSize;
        
        PageInfo<Object> pageInfo = new PageInfo<>();
        pageInfo.setList(rows);
        pageInfo.setCount((long) count);
        pageInfo.setPageSize(pageSize);
        pageInfo.setPageCount((long) pageCount);
        
        pageInfo.setPageNo(pager.getPageId());
        return pageInfo;
    }
    
    private List<Map<String, String>> getTableHead(String beanName) throws ClassNotFoundException {
        List<Map<String, String>> tableHeadList = new ArrayList<>();
        Class tableHeadClazz = Class.forName(SpringContextUtils.getEntityClassName(beanName));
        Field[] tableHeadField = tableHeadClazz.getDeclaredFields();
        for (Field filed : tableHeadField) {
            if (filed.isAnnotationPresent(MatchField.class)) {
                MatchField matchField = filed.getAnnotation(MatchField.class);
                Map<String, String> column = new HashMap<>();
                column.put("name", filed.getName());
                column.put("value", matchField.value());
                tableHeadList.add(column);
            }
        }
        return tableHeadList;
    }
}
