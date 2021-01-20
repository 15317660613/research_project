package com.adc.da.industymeeting.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.*;
import java.util.*;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;

import com.adc.da.excel.poi.excel.entity.result.ExcelVerifyHanlderErrorResult;
import com.adc.da.file.controller.FileUploadRestController;
import com.adc.da.industymeeting.dto.ReceivableIncomeDTO;
import com.adc.da.industymeeting.dto.ReceivableIncomeFiledDTO;
import com.adc.da.industymeeting.dto.ReceivableIncomeFiledOutputDTO;
import com.adc.da.industymeeting.entity.ReceivableIncomeEO;
import com.adc.da.industymeeting.handler.ReceivableIncomeFiledHandler;
import com.adc.da.industymeeting.page.ReceivableIncomeEOPage;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.industymeeting.entity.ReceivableIncomeFiledEO;
import com.adc.da.industymeeting.page.ReceivableIncomeFiledEOPage;
import com.adc.da.industymeeting.service.ReceivableIncomeFiledEOService;

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
@RequestMapping("/${restPath}/industymeeting/receivableIncomeFiled")
@Api(description = "|ReceivableIncomeFiledEO|应收&进账管理-归档数据")
@Slf4j
public class ReceivableIncomeFiledEOController extends BaseController<ReceivableIncomeFiledEO>{

    @Autowired
    private ReceivableIncomeFiledEOService receivableIncomeFiledEOService;
    private static final Logger logger = LoggerFactory.getLogger(ReceivableIncomeFiledEO.class);
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

	@ApiOperation(value = "|ReceivableIncomeFiledEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("industymeeting:receivableIncomeFiled:page")
    public ResponseMessage<PageInfo<ReceivableIncomeFiledEO>> page(ReceivableIncomeFiledEOPage page) throws Exception {
        List<ReceivableIncomeFiledEO> rows = receivableIncomeFiledEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ReceivableIncomeFiledEO|查询")
    @GetMapping("")
//    @RequiresPermissions("industymeeting:receivableIncomeFiled:list")
    public ResponseMessage<List<ReceivableIncomeFiledEO>> list(ReceivableIncomeFiledEOPage page) throws Exception {
        return Result.success(receivableIncomeFiledEOService.queryByList(page));
	}

    @ApiOperation(value = "|ReceivableIncomeFiledEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("industymeeting:receivableIncomeFiled:get")
    public ResponseMessage<ReceivableIncomeFiledEO> find(@PathVariable String id) throws Exception {
        return Result.success(receivableIncomeFiledEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ReceivableIncomeFiledEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("industymeeting:receivableIncomeFiled:save")
    public ResponseMessage<ReceivableIncomeFiledEO> create(@RequestBody ReceivableIncomeFiledEO receivableIncomeFiledEO) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        receivableIncomeFiledEOService.save(receivableIncomeFiledEO, userId);
        return Result.success(receivableIncomeFiledEO);
    }

    @ApiOperation(value = "|ReceivableIncomeFiledEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("industymeeting:receivableIncomeFiled:update")
    public ResponseMessage<ReceivableIncomeFiledEO> update(@RequestBody ReceivableIncomeFiledEO receivableIncomeFiledEO) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        receivableIncomeFiledEO.setUpdateTime(new Date());
        receivableIncomeFiledEO.setUpdateUserId(userId);
        receivableIncomeFiledEOService.updateByPrimaryKeySelective(receivableIncomeFiledEO);
        return Result.success(receivableIncomeFiledEO);
    }

    @ApiOperation(value = "|ReceivableIncomeFiledEO|删除")
    @DeleteMapping("/{ids}")
//    @RequiresPermissions("industymeeting:receivableIncomeFiled:delete")
    public ResponseMessage delete(@NotNull @PathVariable("ids") String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        receivableIncomeFiledEOService.deleteLogicInBatch(idList);
        return Result.success();
    }

    @ApiOperation(value = "|ReceivableIncomeFiledEO|清空")
    @DeleteMapping("/deleteAll")
//    @RequiresPermissions("industymeeting:receivableIncomeFiled:delete")
    public ResponseMessage deleteAll() {
        receivableIncomeFiledEOService.deleteAll();
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
        params.setHeadRows(1);
        params.setVerifyHandler(new ReceivableIncomeFiledHandler());
        // 解析excel，并返回校验信息
        ExcelImportResult<ReceivableIncomeFiledDTO> result = ExcelImportUtil.importExcelMore(is, ReceivableIncomeFiledDTO.class, params);
        // 如果校验不通过，返回错误信息
        // 如果校验不通过，返回错误信息
        if (result.isVerfiyFail()) {
            List<Integer> rowindexs = new ArrayList<>();
            logger.error("excel校验不通过！");
            List<ReceivableIncomeFiledDTO> errors = result.getFailList();
            StringBuilder sb = new StringBuilder();
            for (ReceivableIncomeFiledDTO error : errors) {
                rowindexs.add( error.getRowNum() + 1);
//                sb.append("第 " + error.getRowNum() + "、");
            }
            sb.append("第 ");
            sb.append(StringUtils.join(rowindexs, '、'));
            sb.append(" 行必填项为空或数据不合法，请检查！");
            throw new AdcDaBaseException(sb.toString());
        }
        // 校验通过，数据入库
        List<ReceivableIncomeFiledDTO> datas = result.getList();
        if (CollectionUtils.isEmpty(datas)){
            return Result.success();
        }
        List<ReceivableIncomeFiledEO> datasEO = beanMapper.mapList(datas, ReceivableIncomeFiledEO.class);
        receivableIncomeFiledEOService.batchSave(datasEO, userId);
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
                    "attachment; filename=" + Encodes.urlEncode("归档数据导出.xlsx"));
            response.setContentType("application/force-download");
            ExportParams exportParams = new ExportParams();
            exportParams.setType(ExcelType.XSSF);
            ReceivableIncomeFiledEOPage page = new ReceivableIncomeFiledEOPage();
            page.setOrderBy("update_time");
            page.setOrder("desc");
            List<ReceivableIncomeFiledEO> datas = receivableIncomeFiledEOService.queryByList(page);
            workbook = ExcelExportUtil.exportExcel(exportParams, ReceivableIncomeFiledEO.class, datas);
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