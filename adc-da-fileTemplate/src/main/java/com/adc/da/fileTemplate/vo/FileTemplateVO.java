package com.adc.da.fileTemplate.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileTemplateVO{
    private String id;
    private String tempName;
    private String tempCode;
    private String tempTypeId;
    private String fileId;
    private String version;
    private Integer state;
    private String createUserId;
    private String createUserName;
    private String htmlUrl;


    private String dicTypeId;
    private String dicId;
    private String dicTypeCode;
    private String dicTypeName;

    private String fileName;
    private String fileType;
    private String savePath;
    private String url;
    private String fileSize;
}
