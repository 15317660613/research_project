package com.adc.da.research.project.page;

import com.adc.da.base.page.BasePage;

import java.util.Date;

/**
 * <b>功能：</b>RS_IMPLEMENTATION_PROC ImplementationProcEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ImplementationProcEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String projectId;
    private String projectIdOperator = "=";
    private String proceeStageId;
    private String proceeStageIdOperator = "=";
    private String startTime;
    private String startTime1;
    private String startTime2;
    private String startTimeOperator = "=";
    private String endTime;
    private String endTime1;
    private String endTime2;
    private String endTimeOperator = "=";
    private String status;
    private String statusOperator = "=";
    private String delFlag;
    private String delFlagOperator = "=";
    private String ext1;
    private String ext1Operator = "=";
    private String ext2;
    private String ext2Operator = "=";
    private String ext3;
    private String ext3Operator = "=";
    private String ext4;
    private String ext4Operator = "=";
    private String ext5;
    private String ext5Operator = "=";

    private String stageName;
    private String fileId;
    private String fileName;

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdOperator() {
        return this.idOperator;
    }

    public void setIdOperator(String idOperator) {
        this.idOperator = idOperator;
    }

    public String getProjectId() {
        return this.projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectIdOperator() {
        return this.projectIdOperator;
    }

    public void setProjectIdOperator(String projectIdOperator) {
        this.projectIdOperator = projectIdOperator;
    }

    public String getProceeStageId() {
        return this.proceeStageId;
    }

    public void setProceeStageId(String proceeStageId) {
        this.proceeStageId = proceeStageId;
    }

    public String getProceeStageIdOperator() {
        return this.proceeStageIdOperator;
    }

    public void setProceeStageIdOperator(String proceeStageIdOperator) {
        this.proceeStageIdOperator = proceeStageIdOperator;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime1() {
        return this.startTime1;
    }

    public void setStartTime1(String startTime1) {
        this.startTime1 = startTime1;
    }

    public String getStartTime2() {
        return this.startTime2;
    }

    public void setStartTime2(String startTime2) {
        this.startTime2 = startTime2;
    }

    public String getStartTimeOperator() {
        return this.startTimeOperator;
    }

    public void setStartTimeOperator(String startTimeOperator) {
        this.startTimeOperator = startTimeOperator;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime1() {
        return this.endTime1;
    }

    public void setEndTime1(String endTime1) {
        this.endTime1 = endTime1;
    }

    public String getEndTime2() {
        return this.endTime2;
    }

    public void setEndTime2(String endTime2) {
        this.endTime2 = endTime2;
    }

    public String getEndTimeOperator() {
        return this.endTimeOperator;
    }

    public void setEndTimeOperator(String endTimeOperator) {
        this.endTimeOperator = endTimeOperator;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusOperator() {
        return this.statusOperator;
    }

    public void setStatusOperator(String statusOperator) {
        this.statusOperator = statusOperator;
    }

    public String getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlagOperator() {
        return this.delFlagOperator;
    }

    public void setDelFlagOperator(String delFlagOperator) {
        this.delFlagOperator = delFlagOperator;
    }

    public String getExt1() {
        return this.ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt1Operator() {
        return this.ext1Operator;
    }

    public void setExt1Operator(String ext1Operator) {
        this.ext1Operator = ext1Operator;
    }

    public String getExt2() {
        return this.ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt2Operator() {
        return this.ext2Operator;
    }

    public void setExt2Operator(String ext2Operator) {
        this.ext2Operator = ext2Operator;
    }

    public String getExt3() {
        return this.ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public String getExt3Operator() {
        return this.ext3Operator;
    }

    public void setExt3Operator(String ext3Operator) {
        this.ext3Operator = ext3Operator;
    }

    public String getExt4() {
        return this.ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }

    public String getExt4Operator() {
        return this.ext4Operator;
    }

    public void setExt4Operator(String ext4Operator) {
        this.ext4Operator = ext4Operator;
    }

    public String getExt5() {
        return this.ext5;
    }

    public void setExt5(String ext5) {
        this.ext5 = ext5;
    }

    public String getExt5Operator() {
        return this.ext5Operator;
    }

    public void setExt5Operator(String ext5Operator) {
        this.ext5Operator = ext5Operator;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
