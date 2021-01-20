package com.adc.da.budget.controller;

import com.adc.da.budget.entity.FileEO;
import com.adc.da.budget.service.FileEPService;
import com.adc.da.file.store.IFileStore;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.FileUtil;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * //TODO 添加类/接口功能描述
 *
 * @author qichunxu
 * @date 2019-04-23
 */
@RestController("fileUploadRestController-ebiz")
@RequestMapping({"/${restPath}/budget/file"})
@Api("文件上传")
public class FileUploadRestController {
    private static final Logger logger =
            LoggerFactory.getLogger(com.adc.da.file.controller.FileUploadRestController.class);
    @Autowired
    private FileEPService fileEOService;
    @Autowired
    private IFileStore iFileStore;
    private List<String> whiteUrls = new ArrayList();


    @PostConstruct
    public void init() {
        try {
            InputStream is = com.adc.da.file.controller.FileUploadRestController.class.
                                getClassLoader().getResourceAsStream("white/uploadWhite.txt");
            Throwable var2 = null;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {

                Throwable var4 = null;

                try {
                    String line;
                    try {
                        while((line = reader.readLine()) != null) {
                            if (!"".equals(line)) {
                                this.whiteUrls.add(line);
                            }
                        }
                    } catch (Throwable var29) {
                        var4 = var29;
                        throw var29;
                    }
                } finally {
                    if (reader != null) {
                        if (var4 != null) {
                            try {
                                reader.close();
                            } catch (Throwable var28) {
                                var4.addSuppressed(var28);
                            }
                        } else {
                            reader.close();
                        }
                    }

                }
            } catch (Throwable var31) {
                var2 = var31;
                throw var31;
            } finally {
                if (is != null) {
                    if (var2 != null) {
                        try {
                            is.close();
                        } catch (Throwable var27) {
                            var2.addSuppressed(var27);
                        }
                    } else {
                        is.close();
                    }
                }

            }
        } catch (Exception var33) {
            logger.error("读取文件上传白名单异常", var33);
        }

    }

    @ApiOperation("|FileEO|上传")
    @PostMapping({"/upload"})
    public ResponseMessage<FileEO> upload(String filePath, String userId, String projectId,
                                          @RequestParam("file") MultipartFile file) throws Exception {
        InputStream is = null;

        ResponseMessage var7;
        try {
            String fileExtension = FileUtil.getFileExtension(file.getOriginalFilename());
            if (this.whiteUrls.contains(fileExtension)) {
                is = file.getInputStream();
                String path = this.iFileStore.storeFile(is, fileExtension, filePath);
                FileEO fileEO = new FileEO();
                fileEO.setFileId(UUID.randomUUID());
                fileEO.setFileName(FileUtil.getFileName(file.getOriginalFilename()));
                fileEO.setFileType(fileExtension);
                fileEO.setContentType(file.getContentType());
                fileEO.setSavePath(path);
                fileEO.setCreateTime(new Date());
                fileEO.setUserId(userId);
                fileEO.setProjectId(projectId);
                this.fileEOService.insertSelective(fileEO);
                return Result.success(fileEO);
            }

            logger.error("上传文件类型不允许，请重试");
            var7 = Result.error("r0071", "上传文件类型不允许，请重试");
        } catch (IOException var11) {
            logger.error(var11.getMessage(), var11);
            var7 = Result.error("r0072", "文件存储失败，请重试");
            return var7;
        } finally {
            IOUtils.closeQuietly(is);
        }

        return var7;
    }
}
