package com.adc.da.budget.controller;

import com.adc.da.budget.entity.FileEO;
import com.adc.da.budget.service.FileEPService;
import com.adc.da.file.store.IFileStore;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;

import static org.h2.util.IOUtils.copy;

/**
 * //TODO 添加类/接口功能描述
 *
 * @author qichunxu
 * @date 2019-04-23
 */
@RestController("fileDownloadRestController-ebiz")
@RequestMapping({"/${restPath}/budget/file"})
@Api("文件下载New")
public class FileDownloadRestController {
    private static final Logger logger =
            LoggerFactory.getLogger(com.adc.da.file.controller.FileDownloadRestController.class);
    @Autowired
    private FileEPService fileEOService;
    @Autowired
    private IFileStore iFileStore;

    @ApiOperation("|FileEO|详情")
    @GetMapping({"/{fileId}"})
    public ResponseMessage<FileEO> getById(@NotNull @PathVariable("fileId") String fileId) throws Exception {
        return Result.success(this.fileEOService.selectByPrimaryKey(fileId));
    }

    @ApiOperation("|FileEO|下载")
    @GetMapping({"/{fileId}/download"})
    public void downFile(HttpServletResponse response,
                         @NotNull @PathVariable("fileId") String fileId, String fileName) throws Exception {
        if (StringUtils.isEmpty(fileId)) {
            throw new AdcDaBaseException("FileId不能为空");
        } else {
            FileEO sysFileEO = this.fileEOService.selectByPrimaryKey(fileId);
            if (sysFileEO == null) {
                throw new AdcDaBaseException("FileId[" + fileId + "]不存在");
            } else {
                InputStream is = null;
                ServletOutputStream os = null;

                try {
                    if (StringUtils.isEmpty(fileName)) {
                        fileName = sysFileEO.getFileName() + "." + sysFileEO.getFileType();
                    }
                    response.setHeader("Content-Disposition",
                            "attachment; filename=" + Encodes.urlEncode(StringUtils.trim(fileName)));
                    response.setContentType(sysFileEO.getContentType());
                    is = this.iFileStore.loadFile(sysFileEO.getSavePath());
                    os = response.getOutputStream();
                    IOUtils.copy(is, os);
                    os.flush();
                } catch (IOException var11) {
                    logger.error(var11.getMessage(), var11);
                    throw new AdcDaBaseException("下载文件失败，请重试");
                } finally {
                    IOUtils.closeQuietly(is);
                    IOUtils.closeQuietly(os);
                }

            }
        }
    }
}
