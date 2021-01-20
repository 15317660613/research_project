package com.adc.da.industymeeting.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.file.controller.FileUploadRestController;
import com.adc.da.industymeeting.dto.BudgetManagementInfoNewDTO;
import com.adc.da.industymeeting.dto.BudgetManagementInfoOutputDTO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.adc.da.base.web.BaseController;
import com.adc.da.industymeeting.entity.BudgetManagementInfoEO;
import com.adc.da.industymeeting.page.BudgetManagementInfoEOPage;
import com.adc.da.industymeeting.service.BudgetManagementInfoEOService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/${restPath}/industymeeting/budgetManagementInfo")
@Api(description = "|BudgetManagementInfoEO|预算管理")
@Slf4j
public class BudgetManagementInfoEOController extends BaseController<BudgetManagementInfoEO> {

    @Autowired
    private BudgetManagementInfoEOService budgetManagementInfoEOService;

    // 上传文件类型允许白名单
    private List<String> whiteUrls = new ArrayList<>();

    @Autowired
    private BeanMapper beanMapper;

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

	@ApiOperation(value = "|BudgetManagementInfoEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("industymeeting:budgetManagementInfo:page")
    public ResponseMessage<PageInfo<BudgetManagementInfoEO>> page(BudgetManagementInfoEOPage page) throws Exception {
        if (StringUtils.isEmpty(page.getSql_filter())){
            page.setSql_filter(" order by info.update_time desc, info.id asc");
        }
        List<BudgetManagementInfoEO> rows = budgetManagementInfoEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BudgetManagementInfoEO|查询")
    @GetMapping("")
//    @RequiresPermissions("industymeeting:budgetManagementInfo:list")
    public ResponseMessage<List<BudgetManagementInfoEO>> list(BudgetManagementInfoEOPage page) throws Exception {
        return Result.success(budgetManagementInfoEOService.queryByList(page));
	}

    @ApiOperation(value = "|BudgetManagementInfoEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("industymeeting:budgetManagementInfo:get")
    public ResponseMessage<BudgetManagementInfoEO> find(@PathVariable String id) throws Exception {
        return Result.success(budgetManagementInfoEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BudgetManagementInfoEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("industymeeting:budgetManagementInfo:save")
    public ResponseMessage<BudgetManagementInfoEO> create(@RequestBody BudgetManagementInfoEO budgetManagementInfoEO) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        budgetManagementInfoEO.setUpdateUserId(userId);
        budgetManagementInfoEO.setCreateUserId(userId);
        return Result.success(budgetManagementInfoEOService.save(budgetManagementInfoEO));
    }

    @ApiOperation(value = "|BudgetManagementInfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("industymeeting:budgetManagementInfo:update")
    public ResponseMessage<BudgetManagementInfoEO> update(@RequestBody BudgetManagementInfoEO budgetManagementInfoEO) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        budgetManagementInfoEO.setUpdateTime(new Date());
        budgetManagementInfoEO.setUpdateUserId(userId);
        budgetManagementInfoEOService.updateByPrimaryKeySelective(budgetManagementInfoEO);
        return Result.success(budgetManagementInfoEO);
    }

    @ApiOperation(value = "|BudgetManagementInfoEO|删除")
    @DeleteMapping("/{ids}")
//    @RequiresPermissions("industymeeting:budgetManagementInfo:delete")
    public ResponseMessage delete(@NotNull @PathVariable("ids") String[] ids) {
        List<String> idList = Arrays.asList(ids);
        budgetManagementInfoEOService.deleteLogicInBatch(idList);
        return Result.success();
    }

    @ApiOperation(value = "|BudgetManagementInfoEO|清空")
    @DeleteMapping("")
//    @RequiresPermissions("industymeeting:budgetManagementInfo:empty")
    public ResponseMessage empty() {
        budgetManagementInfoEOService.empty();
        return Result.success();
    }

    @ApiOperation(value = "数据上传")
    @PostMapping("/upload")
    public ResponseMessage uploadData(@RequestParam("file") MultipartFile file) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

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
        params.setNeedVerfiy(true);
        // 解析excel，并返回校验信息
        ExcelImportResult<BudgetManagementInfoNewDTO> result = ExcelImportUtil.importExcelMore(is, BudgetManagementInfoNewDTO.class, params);
        // 如果校验不通过，返回错误信息
        if (result.isVerfiyFail()) {
            log.error("excel校验不通过！");
            List<BudgetManagementInfoNewDTO> errors = result.getFailList();
            StringBuilder sb = new StringBuilder();
            for (BudgetManagementInfoNewDTO error : errors){
                sb.append("第"+error.getRowNum()+"行"+error.getErrorMsg());
            }
            return Result.error("r0101", sb.toString());
        }
        // 校验通过，数据入库
        List<BudgetManagementInfoNewDTO> datas = result.getList();
        List<BudgetManagementInfoEO> datasEO = beanMapper.mapList(datas, BudgetManagementInfoEO.class);
        budgetManagementInfoEOService.batchSave(datasEO, userId);
        return Result.success();
    }

//    @ApiOperation(value = "数据上传")
//    @PostMapping("/upload")
//    public ResponseMessage uploadData(@RequestParam("file") MultipartFile file) throws Exception {
//        String userId = UserUtils.getUserId();
//        if (StringUtils.isEmpty(userId)) {
//            throw new AdcDaBaseException("登陆可能过期，请登录！");
//        }
//
//        InputStream is = null;
//        String fileExtension = FileUtil.getFileExtension(file.getOriginalFilename());
//
//        if (!whiteUrls.contains(fileExtension)) {
//            log.error("上传文件类型不允许，请重试");
//            return Result.error("r0071", "上传文件类型不允许，请重试");
//        }
//        // 获取文件输入流
//        is = file.getInputStream();
//
//        // 导入参数设置，默认即可
//        ImportParams params = new ImportParams();
//        // 解析excel，并返回校验信息
//        ExcelImportResult<BudgetManagementInfoDTO> result = ExcelImportUtil.importExcelVerify(is, BudgetManagementInfoDTO.class, params);
//        // 如果校验不通过，返回错误信息
//        if (result.isVerfiyFail()) {
//            log.error("excel校验不通过！");
//            List<ExcelVerifyHanlderErrorResult> errors = result.getErrors();
//            StringBuilder sb = new StringBuilder();
//            for (ExcelVerifyHanlderErrorResult error : errors) {
//                sb.append("[").append(error.getRowNum()).append("行")
//                        .append(error.getColumnNum()).append("列]").append(error.getMsg())
//                        .append("/t/n");
//            }
//            return Result.error("r0101", sb.toString());
//        }
//        // 校验通过，数据入库
//        List<BudgetManagementInfoDTO> datas = result.getList();
//        for (BudgetManagementInfoDTO dto : datas) {
//            if (StringUtils.isEmpty(dto.getHeadquarters()) || StringUtils.isEmpty(dto.getDepartment())) {
//                return Result.error("本部或部门有空项");
//            }
//        }
//        List<BudgetManagementInfoEO> datasEO = beanMapper.mapList(datas, BudgetManagementInfoEO.class);
//        budgetManagementInfoEOService.batchSave(datasEO, userId);
//        return Result.success();
//    }

    @ApiOperation(value = "数据下载")
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
            BudgetManagementInfoEOPage page = new BudgetManagementInfoEOPage();
            page.setOrderBy("update_time");
            page.setOrder("desc");
            List<BudgetManagementInfoEO> datas = budgetManagementInfoEOService.queryByList(page);
            List<BudgetManagementInfoOutputDTO> datasDto = new ArrayList<>();
            if (datas != null && !datas.isEmpty()) {
                datasDto = beanMapper.mapList(datas, BudgetManagementInfoOutputDTO.class);
            }
            workbook = ExcelExportUtil.exportExcel(exportParams, BudgetManagementInfoOutputDTO.class, datasDto);
            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new AdcDaBaseException("下载数据文件失败，请重试");
        } finally {
            IOUtils.closeQuietly(os);
        }
    }
}