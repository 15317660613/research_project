package com.adc.da.knowledge.util;


import com.adc.da.file.entity.FileEO;

import com.adc.da.file.service.FileEOService;

import com.adc.da.file.store.IFileStore;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;

import com.adc.da.util.utils.FileUtil;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class FileStoreUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileStoreUtil.class);


    public static void uploadFile(MultipartFile file, String ID, FileEOService fileEOService,IFileStore iFileStore,UserEOService userEOService,List<String> whiteUrls)throws Exception{

        //        if (StringUtils.isEmpty(milepostResultId)){
//            throw new AdcDaBaseException("没有选择加入哪个目录下文件下！");
//        }
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        FileEO fileEO = null;
        InputStream is = null;

        try {
            /* 获取文件后缀 */
            String fileExtension = FileUtil.getFileExtension(file.getOriginalFilename());
            /* 校验文件类型 */

            if (!whiteUrls.contains(fileExtension)) {
                logger.error("上传文件类型不允许，请重试");
               // return Result.error("r0071", "上传文件类型不允许，请重试");
            }

            is = file.getInputStream();

            /* 设置路径*/
            String path = iFileStore.storeFile(is, fileExtension, "milePostFile");

            /* 设置文件属性 */
            fileEO = new FileEO();
            fileEO.setForeignId(ID);
            fileEO.setFileId(UUID.randomUUID10());
            fileEO.setFileName(FileUtil.getFileName(file.getOriginalFilename()));
            fileEO.setFileType(fileExtension);
            fileEO.setContentType(file.getContentType());
            fileEO.setSavePath(path);
            fileEO.setCreateTime(new Date());
            fileEO.setUserId(userId);
            fileEOService.insertSelective(fileEO);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            //return Result.error("r0072", "文件存储失败，请重试");
        } finally {

            IOUtils.closeQuietly(is);
        }

        UserEO userEO = userEOService.selectByPrimaryKey(fileEO.getUserId());
        String uploadUserName = "" ;
        if (null != userEO){
            uploadUserName = userEO.getUsname();
        }


        String fileSize = "0";
        long fileLength = file.getSize();
        long length = 0;
        if (fileLength >= 1000000) {
            length = fileLength >> 20 ;
            fileSize = String.valueOf(length) + "MB";
        } else if (fileLength < 1000000 && fileLength >= 1000) {
            length = fileLength >> 10 ;
            fileSize = String.valueOf(length) + "KB";
        } else {
            fileSize = String.valueOf(fileLength) + "B";
        }

    }
}
