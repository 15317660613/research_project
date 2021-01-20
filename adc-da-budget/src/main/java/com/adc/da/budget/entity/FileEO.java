package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * //TODO 添加类/接口功能描述
 *
 * @author qichunxu
 * @date 2019-04-23
 */
@Data
public class FileEO extends BaseEntity {
    private String userId;
    private String url;
    private String savePath;
    private String remark;
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date lastUpdateTime;
    private String fileType;
    private String fileName;
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createTime;
    private String contentType;
    private String fileId;
    private String fileSize;
    private String uploadUser;
    private String projectId;
}
