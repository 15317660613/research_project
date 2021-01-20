package com.adc.da.industymeeting.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.result.ExcelImportResult;
import com.adc.da.excel.poi.excel.entity.result.ExcelVerifyHanlderErrorResult;
import com.adc.da.file.controller.FileUploadRestController;
import com.adc.da.industymeeting.dto.CommunicationFrequencyDTO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.adc.da.base.web.BaseController;
import com.adc.da.industymeeting.entity.CommunicationFrequencyEO;
import com.adc.da.industymeeting.page.CommunicationFrequencyEOPage;
import com.adc.da.industymeeting.service.CommunicationFrequencyEOService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/${restPath}/industymeeting/communicationFrequency")
@Api(description = "|CommunicationFrequencyEO|交流频次表")
@Slf4j
public class CommunicationFrequencyEOController extends BaseController<CommunicationFrequencyEO>{

    @Autowired
    private CommunicationFrequencyEOService communicationFrequencyEOService;

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

	@ApiOperation(value = "|CommunicationFrequencyEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("industymeeting:communicationFrequency:page")
    public ResponseMessage<PageInfo<CommunicationFrequencyEO>> page(CommunicationFrequencyEOPage page) throws Exception {
        List<CommunicationFrequencyEO> rows = communicationFrequencyEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|CommunicationFrequencyEO|查询")
    @GetMapping("")
//    @RequiresPermissions("industymeeting:communicationFrequency:list")
    public ResponseMessage<List<CommunicationFrequencyEO>> list(CommunicationFrequencyEOPage page) throws Exception {
        return Result.success(communicationFrequencyEOService.queryByList(page));
	}

    @ApiOperation(value = "|CommunicationFrequencyEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("industymeeting:communicationFrequency:get")
    public ResponseMessage<CommunicationFrequencyEO> find(@PathVariable String id) throws Exception {
        return Result.success(communicationFrequencyEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|CommunicationFrequencyEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("industymeeting:communicationFrequency:save")
    public ResponseMessage<CommunicationFrequencyEO> create(@RequestBody CommunicationFrequencyEO communicationFrequencyEO) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        communicationFrequencyEOService.save(communicationFrequencyEO, userId);
        return Result.success(communicationFrequencyEO);
    }

    @ApiOperation(value = "|CommunicationFrequencyEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("industymeeting:communicationFrequency:update")
    public ResponseMessage<CommunicationFrequencyEO> update(@RequestBody CommunicationFrequencyEO communicationFrequencyEO) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        communicationFrequencyEO.setUpdateTime(new Date());
        communicationFrequencyEO.setUpdateUserId(userId);
        communicationFrequencyEOService.updateByPrimaryKeySelective(communicationFrequencyEO);
        return Result.success(communicationFrequencyEO);
    }

//    @ApiOperation(value = "|CommunicationFrequencyEO|删除")
//    @DeleteMapping("/{ids}")
//    @RequiresPermissions("industymeeting:communicationFrequency:delete")
//    public ResponseMessage delete(@PathVariable String[] ids) throws Exception {
//        communicationFrequencyEOService.deleteLogicInBatch(id);
//        logger.info("delete from COMMUNICATION_FREQUENCY where id = {}", id);
//        return Result.success();
//    }

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
        // 解析excel，并返回校验信息
        ExcelImportResult<CommunicationFrequencyDTO> result = ExcelImportUtil.importExcelVerify(is, CommunicationFrequencyDTO.class, params);
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
        List<CommunicationFrequencyDTO> datas = result.getList();
        for (int i = 0; i < datas.size(); i++) {
            try {
                if (datas.get(i).getFrequencyStr().length() > 10) {
                    throw new AdcDaBaseException();
                }
            }
            catch (Exception e) {
                 return Result.error("第" + (i+1) + "行有错误，交流频次不能大于10位");
            }
        }
        List<CommunicationFrequencyEO> datasEO = beanMapper.mapList(datas, CommunicationFrequencyEO.class);
        communicationFrequencyEOService.batchSave(datasEO, userId);
        return Result.success();
    }

//    @ApiOperation(value = "数据下载")
//    @GetMapping("/download")
//    public void download(HttpServletResponse response) throws Exception {
//        OutputStream os = null;
//        Workbook workbook = null;
//        try {
//            response.setHeader(
//                    "Content-Disposition",
//                    "attachment; filename=" + Encodes.urlEncode("预算数据.xlsx"));
//            response.setContentType("application/force-download");
//            ExportParams exportParams = new ExportParams();
//            exportParams.setType(ExcelType.XSSF);
//            List<CommunicationFrequencyEO> datas = communicationFrequencyEOService.queryByList(new CommunicationFrequencyEOPage());
//            List<CommunicationFrequencyDTO> datasDto = new ArrayList<>();
//            if (datas != null && !datas.isEmpty()) {
//                datasDto = beanMapper.mapList(datas, CommunicationFrequencyDTO.class);
//            }
//            workbook = ExcelExportUtil.exportExcel(exportParams, CommunicationFrequencyDTO.class, datasDto);
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
}