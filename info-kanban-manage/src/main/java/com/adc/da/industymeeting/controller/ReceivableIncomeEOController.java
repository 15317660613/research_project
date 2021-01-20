package com.adc.da.industymeeting.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.adc.da.file.controller.FileUploadRestController;
import com.adc.da.industymeeting.dto.ReceivableIncomeDTO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.adc.da.base.web.BaseController;
import com.adc.da.industymeeting.entity.ReceivableIncomeEO;
import com.adc.da.industymeeting.page.ReceivableIncomeEOPage;
import com.adc.da.industymeeting.service.ReceivableIncomeEOService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/${restPath}/industymeeting/receivableIncome")
@Api(description = "|ReceivableIncomeEO|应收&进账管理-明细数据")
@Slf4j
public class ReceivableIncomeEOController extends BaseController<ReceivableIncomeEO>{

    @Autowired
    private ReceivableIncomeEOService receivableIncomeEOService;

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

	@ApiOperation(value = "|ReceivableIncomeEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("industymeeting:receivableIncome:page")
    public ResponseMessage<PageInfo<ReceivableIncomeEO>> page(ReceivableIncomeEOPage page) throws Exception {
        List<ReceivableIncomeEO> rows = receivableIncomeEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ReceivableIncomeEO|查询")
    @GetMapping("")
//    @RequiresPermissions("industymeeting:receivableIncome:list")
    public ResponseMessage<List<ReceivableIncomeEO>> list(ReceivableIncomeEOPage page) throws Exception {
        return Result.success(receivableIncomeEOService.queryByList(page));
	}

    @ApiOperation(value = "|ReceivableIncomeEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("industymeeting:receivableIncome:get")
    public ResponseMessage<ReceivableIncomeEO> find(@PathVariable String id) throws Exception {
        return Result.success(receivableIncomeEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ReceivableIncomeEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("industymeeting:receivableIncome:save")
    public ResponseMessage<ReceivableIncomeEO> create(@RequestBody ReceivableIncomeEO receivableIncomeEO) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        receivableIncomeEOService.save(receivableIncomeEO, userId);
        return Result.success(receivableIncomeEO);
    }

    @ApiOperation(value = "|ReceivableIncomeEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("industymeeting:receivableIncome:update")
    public ResponseMessage<ReceivableIncomeEO> update(@RequestBody ReceivableIncomeEO receivableIncomeEO) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        receivableIncomeEO.setUpdateTime(new Date());
        receivableIncomeEO.setUpdateUserId(userId);
        receivableIncomeEOService.updateByPrimaryKeySelective(receivableIncomeEO);
        return Result.success(receivableIncomeEO);
    }

    @ApiOperation(value = "|ReceivableIncomeEO|删除")
    @DeleteMapping("/{ids}")
//    @RequiresPermissions("industymeeting:receivableIncome:delete")
    public ResponseMessage delete(@NotNull @PathVariable("ids") String[] ids) {
        List<String> idList = Arrays.asList(ids);
        receivableIncomeEOService.deleteLogicInBatch(idList);
        return Result.success();
    }

    @ApiOperation(value = "|ReceivableIncomeEO|清空")
    @DeleteMapping("")
//    @RequiresPermissions("industymeeting:receivableIncome:empty")
    public ResponseMessage empty() {
        receivableIncomeEOService.empty();
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
        ExcelImportResult<ReceivableIncomeDTO> result = ExcelImportUtil.importExcelMore(is, ReceivableIncomeDTO.class, params);

        // 解析excel，并返回校验信息
//        ExcelImportResult<ReceivableIncomeDTO> result = ExcelImportUtil.importExcelVerify(is, ReceivableIncomeDTO.class, params);
        // 如果校验不通过，返回错误信息
        if (result.isVerfiyFail()) {
            log.error("excel校验不通过！");
            List<ReceivableIncomeDTO> errors = result.getFailList();
            StringBuilder stringBuilder = new StringBuilder();
            for (ReceivableIncomeDTO error : errors){
                stringBuilder.append("第"+error.getRowNum()+"行"+error.getErrorMsg());
            }
            return Result.error("r0101", stringBuilder.toString());
        }
        // 校验通过，数据入库
        List<ReceivableIncomeDTO> datas = result.getList();
        List<ReceivableIncomeEO> datasEO = beanMapper.mapList(datas, ReceivableIncomeEO.class);
        receivableIncomeEOService.batchSave(datasEO, userId);
        return Result.success();
    }

    @ApiOperation(value = "数据下载")
    @GetMapping("/download")
    public void download(HttpServletResponse response) throws Exception {
        OutputStream os = null;
        Workbook workbook = null;
        try {
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode("应收&进账-明细数据.xlsx"));
            response.setContentType("application/force-download");
            ExportParams exportParams = new ExportParams();
            exportParams.setType(ExcelType.XSSF);
            ReceivableIncomeEOPage page = new ReceivableIncomeEOPage();
            page.setOrderBy("update_time");
            page.setOrder("desc");
            List<ReceivableIncomeEO> datas = receivableIncomeEOService.queryByList(page);
            // 做下excel数据转换
            for (ReceivableIncomeEO receivableIncomeEO : datas) {
//                receivableIncomeEO.setProject(receivableIncomeEO.getProjectName());
                receivableIncomeEO.setCompany(receivableIncomeEO.getCompanyName());
            }
            List<ReceivableIncomeDTO> datasDto = new ArrayList<>();
            if (datas != null && !datas.isEmpty()) {
                datasDto = beanMapper.mapList(datas, ReceivableIncomeDTO.class);
            }
//            for (ReceivableIncomeDTO dto: datasDto) {
//                dto.setAmountReceivableStr(new BigDecimal(dto.getAmountReceivable().toString()).toString());
//                dto.setWeeklyArrivalStr(new BigDecimal(dto.getWeeklyArrival().toString()).toString());
//            }
            workbook = ExcelExportUtil.exportExcel(exportParams, ReceivableIncomeDTO.class, datasDto);
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

//    @ApiOperation(value = "数据下载")
//    @GetMapping("/download")
//    public void download(HttpServletResponse response) throws Exception {
//        OutputStream os = null;
//        Workbook workbook = null;
//        try {
//            response.setHeader(
//                    "Content-Disposition",
//                    "attachment; filename=" + Encodes.urlEncode("应收&进账-明细数据.xlsx"));
//            response.setContentType("application/force-download");
//            ExportParams exportParams = new ExportParams();
//            exportParams.setType(ExcelType.XSSF);
//            ReceivableIncomeEOPage page = new ReceivableIncomeEOPage();
//            page.setOrderBy("update_time");
//            page.setOrder("desc");
//            List<ReceivableIncomeEO> datas = receivableIncomeEOService.queryByList(page);
//            // 做下excel数据转换
//            for (ReceivableIncomeEO receivableIncomeEO : datas) {
//                receivableIncomeEO.setProject(receivableIncomeEO.getProjectName());
//                receivableIncomeEO.setCompany(receivableIncomeEO.getCompanyName());
//            }
//            List<ReceivableIncomeOutputDTO> datasDto = new ArrayList<>();
//            if (datas != null && !datas.isEmpty()) {
//                datasDto = beanMapper.mapList(datas, ReceivableIncomeOutputDTO.class);
//            }
//            for (ReceivableIncomeOutputDTO dto: datasDto) {
//                dto.setAmountReceivableStr(new BigDecimal(dto.getAmountReceivable().toString()).toString());
//                dto.setWeeklyArrivalStr(new BigDecimal(dto.getWeeklyArrival().toString()).toString());
//            }
//            workbook = ExcelExportUtil.exportExcel(exportParams, ReceivableIncomeOutputDTO.class, datasDto);
//            os = response.getOutputStream();
//            workbook.write(os);
//            os.flush();
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//            throw new AdcDaBaseException("下载数据文件失败，请重试");
//        } finally {
//            IOUtils.closeQuietly(os);
//        }
//    }

    @ApiOperation(value = "|ReceivableIncomeEO|企业应收账款top 10")
    @GetMapping("/accountReceivableByEnterprise")
    public ResponseMessage<List<ReceivableIncomeEO>> accountReceivableByEnterprise (@RequestParam("topNum") Integer topNum) throws Exception{
        if (StringUtils.isEmpty(topNum)){
            topNum = 10;
        }
        return Result.success(receivableIncomeEOService.accountReceivableByEnterprise(topNum));
    }

    @ApiOperation(value = "|ReceivableIncomeEO|部门应收账款占比TOP10")
    @GetMapping("/accountReceivableByDepart")
    public ResponseMessage<List<ReceivableIncomeEO>> accountReceivableByDepart (@RequestParam("topNum") Integer topNum) throws Exception{
        if (StringUtils.isEmpty(topNum)){
            topNum = 10;
        }
        return Result.success(receivableIncomeEOService.accountReceivableByDepart(topNum));
    }


    @ApiOperation(value = "|ReceivableIncomeEO|各公司经营情况")
    @GetMapping("/getPartBOperationStatistics")
    public ResponseMessage getPartBOperationStatistics() throws Exception{
        return  Result.success(receivableIncomeEOService.getPartBOperationStatistics());
    }


    @ApiOperation(value = "|ReceivableIncomeEO|各公司经营情况用于Pad")
    @GetMapping("/getPadPartBOperationStatistics")
    public ResponseMessage getPadPartBOperationStatistics() throws Exception{
        return  Result.success(receivableIncomeEOService.getPadPartBOperationStatistics());
    }
}