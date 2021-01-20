package com.adc.da.finance.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import java.io.*;
import java.util.*;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.adc.da.file.controller.FileUploadRestController;
import com.adc.da.finance.dto.ResearchIssueDTO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.adc.da.base.web.BaseController;
import com.adc.da.finance.entity.ResearchIssueEO;
import com.adc.da.finance.page.ResearchIssueEOPage;
import com.adc.da.finance.service.ResearchIssueEOService;
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
@RequestMapping("/${restPath}/finance/researchIssue")
@Api(description = "|ResearchIssueEO|科研课题配置")
@Slf4j
public class ResearchIssueEOController extends BaseController<ResearchIssueEO>{

    @Autowired
    private ResearchIssueEOService researchIssueEOService;

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

	@ApiOperation(value = "|ResearchIssueEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("finance:researchIssue:page")
    public ResponseMessage<PageInfo<ResearchIssueEO>> page(ResearchIssueEOPage page) throws Exception {
        // 拼模糊查询
        if (StringUtils.isNotEmpty(page.getIssueName())) {
            page.setIssueName("%" + page.getIssueName() + "%");
        }
        List<ResearchIssueEO> rows = researchIssueEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ResearchIssueEO|查询")
    @GetMapping("")
//    @RequiresPermissions("finance:researchIssue:list")
    public ResponseMessage<List<ResearchIssueEO>> list(ResearchIssueEOPage page) throws Exception {
        return Result.success(researchIssueEOService.queryByList(page));
	}

    @ApiOperation(value = "|ResearchIssueEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("finance:researchIssue:get")
    public ResponseMessage<ResearchIssueEO> find(@PathVariable String id) throws Exception {
        return Result.success(researchIssueEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ResearchIssueEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("finance:researchIssue:save")
    public ResponseMessage<ResearchIssueEO> create(@RequestBody ResearchIssueEO researchIssueEO) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        researchIssueEO.setUpdateUserId(userId);
        researchIssueEO.setCreateUserId(userId);
        researchIssueEOService.save(researchIssueEO);
        return Result.success(researchIssueEO);
    }

    @ApiOperation(value = "|ResearchIssueEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("finance:researchIssue:update")
    public ResponseMessage<ResearchIssueEO> update(@RequestBody ResearchIssueEO researchIssueEO) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        researchIssueEO.setUpdateTime(new Date());
        researchIssueEO.setUpdateUserId(userId);
        researchIssueEOService.updateByPrimaryKeySelective(researchIssueEO);
        return Result.success(researchIssueEO);
    }

    @ApiOperation(value = "|ResearchIssueEO|删除")
    @DeleteMapping("/{ids}")
//    @RequiresPermissions("finance:researchIssue:delete")
    public ResponseMessage delete(@NotNull @PathVariable("ids") String[] ids) {
        List<String> idList = Arrays.asList(ids);
        researchIssueEOService.deleteLogicInBatch(idList);
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
        ExcelImportResult<ResearchIssueDTO> result = ExcelImportUtil.importExcelMore(is, ResearchIssueDTO.class, params);
        // 如果校验不通过，返回错误信息
        if (result.isVerfiyFail()) {
            log.error("excel校验不通过！");
            List<ResearchIssueDTO> errors = result.getFailList();
            StringBuilder sb = new StringBuilder();
            for (ResearchIssueDTO error : errors){
                sb.append("第"+error.getRowNum()+"行"+error.getErrorMsg());
            }
            return Result.error("r0101", sb.toString());
        }
        // 校验通过，数据入库
        List<ResearchIssueDTO> datas = result.getList();
        List<ResearchIssueEO> datasEO = beanMapper.mapList(datas, ResearchIssueEO.class);
        researchIssueEOService.batchSave(datasEO, userId);
        return Result.success();
    }

    @ApiOperation(value = "数据下载")
    @GetMapping("/download")
    public void download(HttpServletResponse response, ResearchIssueEOPage page) throws Exception {
        OutputStream os = null;
        Workbook workbook = null;
        try {
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode("科研课题配置.xlsx"));
            response.setContentType("application/force-download");
            ExportParams exportParams = new ExportParams();
            exportParams.setType(ExcelType.XSSF);

            // 拼模糊查询
            if (StringUtils.isNotEmpty(page.getIssueName())) {
                page.setIssueName("%" + page.getIssueName() + "%");
            }
            List<ResearchIssueEO> datas = researchIssueEOService.queryByList(page);
            List<ResearchIssueDTO> datasDto = new ArrayList<>();
            if (datas != null && !datas.isEmpty()) {
                datasDto = beanMapper.mapList(datas, ResearchIssueDTO.class);
            }
            workbook = ExcelExportUtil.exportExcel(exportParams, ResearchIssueDTO.class, datasDto);
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