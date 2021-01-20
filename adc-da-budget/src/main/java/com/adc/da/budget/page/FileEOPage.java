package com.adc.da.budget.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

/**
 * //TODO 添加类/接口功能描述
 *
 * @author qichunxu
 * @date 2019-04-23
 */
@Data
public class FileEOPage  extends BasePage {
    private String userId;
    private String userIdOperator = "=";
    private String url;
    private String urlOperator = "=";
    private String savePath;
    private String savePathOperator = "=";
    private String remark;
    private String remarkOperator = "=";
    private String lastUpdateTime;
    private String lastUpdateTime1;
    private String lastUpdateTime2;
    private String lastUpdateTimeOperator = "=";
    private String fileType;
    private String fileTypeOperator = "=";
    private String fileName;
    private String fileNameOperator = "=";
    private String createTime;
    private String createTime1;
    private String createTime2;
    private String createTimeOperator = "=";
    private String contentType;
    private String contentTypeOperator = "=";
    private String fileId;
    private String fileIdOperator = "=";
    private String projectId;
    private String projectIdOperator = "=";
}
