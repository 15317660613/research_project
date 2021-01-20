package com.adc.da.research.project.page;

import com.adc.da.base.page.BasePage;

/**
 * <b>功能：</b>RS_WORK_PROGRESS WorkProgressEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class WorkProgressEOPage extends BasePage {

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
    private String id;
    private String idOperator = "=";
    private String projectId;
    private String projectIdOperator = "=";
    private String examineType;
    private String examineTypeOperator = "=";
    private String examineContent;
    private String examineContentOperator = "=";
    private String examineTime;
    private String examineTime1;
    private String examineTime2;
    private String examineTimeOperator = "=";
    private String examineDescription;
    private String examineDescriptionOperator = "=";
    private String actualExamineTime;
    private String actualExamineTime1;
    private String actualExamineTime2;
    private String actualExamineTimeOperator = "=";
    private String rectifyDescription;
    private String rectifyDescriptionOperator = "=";
    private String createUserId;
    private String createUserIdOperator = "=";
    private String createUserName;
    private String createUserNameOperator = "=";
    private String createTime;
    private String createTime1;
    private String createTime2;
    private String createTimeOperator = "=";
    private String modifyTime;
    private String modifyTime1;
    private String modifyTime2;
    private String modifyTimeOperator = "=";
    private String approveComment;
    private String approvecommentOperator="=";
    private String checkMethod;
    private String checkMethodOperator="=";
    private String file;
    private String fileOperator="=";

    private String fileArr;
    private String checkFileArr;
    private String processId;
    private String checkUser;
    private String checkResult;
    private String checkUserId;
    private String reviewRemark;//评审备注

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

    public String getExamineType() {
        return this.examineType;
    }

    public void setExamineType(String examineType) {
        this.examineType = examineType;
    }

    public String getExamineTypeOperator() {
        return this.examineTypeOperator;
    }

    public void setExamineTypeOperator(String examineTypeOperator) {
        this.examineTypeOperator = examineTypeOperator;
    }

    public String getExamineContent() {
        return this.examineContent;
    }

    public void setExamineContent(String examineContent) {
        this.examineContent = examineContent;
    }

    public String getExamineContentOperator() {
        return this.examineContentOperator;
    }

    public void setExamineContentOperator(String examineContentOperator) {
        this.examineContentOperator = examineContentOperator;
    }

    public String getExamineTime() {
        return this.examineTime;
    }

    public void setExamineTime(String examineTime) {
        this.examineTime = examineTime;
    }

    public String getExamineTime1() {
        return this.examineTime1;
    }

    public void setExamineTime1(String examineTime1) {
        this.examineTime1 = examineTime1;
    }

    public String getExamineTime2() {
        return this.examineTime2;
    }

    public void setExamineTime2(String examineTime2) {
        this.examineTime2 = examineTime2;
    }

    public String getExamineTimeOperator() {
        return this.examineTimeOperator;
    }

    public void setExamineTimeOperator(String examineTimeOperator) {
        this.examineTimeOperator = examineTimeOperator;
    }

    public String getExamineDescription() {
        return this.examineDescription;
    }

    public void setExamineDescription(String examineDescription) {
        this.examineDescription = examineDescription;
    }

    public String getExamineDescriptionOperator() {
        return this.examineDescriptionOperator;
    }

    public void setExamineDescriptionOperator(String examineDescriptionOperator) {
        this.examineDescriptionOperator = examineDescriptionOperator;
    }

    public String getActualExamineTime() {
        return this.actualExamineTime;
    }

    public void setActualExamineTime(String actualExamineTime) {
        this.actualExamineTime = actualExamineTime;
    }

    public String getActualExamineTime1() {
        return this.actualExamineTime1;
    }

    public void setActualExamineTime1(String actualExamineTime1) {
        this.actualExamineTime1 = actualExamineTime1;
    }

    public String getActualExamineTime2() {
        return this.actualExamineTime2;
    }

    public void setActualExamineTime2(String actualExamineTime2) {
        this.actualExamineTime2 = actualExamineTime2;
    }

    public String getActualExamineTimeOperator() {
        return this.actualExamineTimeOperator;
    }

    public void setActualExamineTimeOperator(String actualExamineTimeOperator) {
        this.actualExamineTimeOperator = actualExamineTimeOperator;
    }

    public String getRectifyDescription() {
        return this.rectifyDescription;
    }

    public void setRectifyDescription(String rectifyDescription) {
        this.rectifyDescription = rectifyDescription;
    }

    public String getRectifyDescriptionOperator() {
        return this.rectifyDescriptionOperator;
    }

    public void setRectifyDescriptionOperator(String rectifyDescriptionOperator) {
        this.rectifyDescriptionOperator = rectifyDescriptionOperator;
    }

    public String getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserIdOperator() {
        return this.createUserIdOperator;
    }

    public void setCreateUserIdOperator(String createUserIdOperator) {
        this.createUserIdOperator = createUserIdOperator;
    }

    public String getCreateUserName() {
        return this.createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateUserNameOperator() {
        return this.createUserNameOperator;
    }

    public void setCreateUserNameOperator(String createUserNameOperator) {
        this.createUserNameOperator = createUserNameOperator;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime1() {
        return this.createTime1;
    }

    public void setCreateTime1(String createTime1) {
        this.createTime1 = createTime1;
    }

    public String getCreateTime2() {
        return this.createTime2;
    }

    public void setCreateTime2(String createTime2) {
        this.createTime2 = createTime2;
    }

    public String getCreateTimeOperator() {
        return this.createTimeOperator;
    }

    public void setCreateTimeOperator(String createTimeOperator) {
        this.createTimeOperator = createTimeOperator;
    }

    public String getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyTime1() {
        return this.modifyTime1;
    }

    public void setModifyTime1(String modifyTime1) {
        this.modifyTime1 = modifyTime1;
    }

    public String getModifyTime2() {
        return this.modifyTime2;
    }

    public void setModifyTime2(String modifyTime2) {
        this.modifyTime2 = modifyTime2;
    }

    public String getModifyTimeOperator() {
        return this.modifyTimeOperator;
    }

    public void setModifyTimeOperator(String modifyTimeOperator) {
        this.modifyTimeOperator = modifyTimeOperator;
    }

    public String getApproveComment() {
        return approveComment;
    }

    public void setApproveComment(String approveComment) {
        this.approveComment = approveComment;
    }

    public String getApprovecommentOperator() {
        return approvecommentOperator;
    }

    public void setApprovecommentOperator(String approvecommentOperator) {
        this.approvecommentOperator = approvecommentOperator;
    }

    public String getCheckMethod() {
        return checkMethod;
    }

    public void setCheckMethod(String checkMethod) {
        this.checkMethod = checkMethod;
    }

    public String getCheckMethodOperator() {
        return checkMethodOperator;
    }

    public void setCheckMethodOperator(String checkMethodOperator) {
        this.checkMethodOperator = checkMethodOperator;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileOperator() {
        return fileOperator;
    }

    public void setFileOperator(String fileOperator) {
        this.fileOperator = fileOperator;
    }


    public String getFileArr() {
        return fileArr;
    }

    public void setFileArr(String fileArr) {
        this.fileArr = fileArr;
    }

    public String getCheckFileArr() {
        return checkFileArr;
    }

    public void setCheckFileArr(String checkFileArr) {
        this.checkFileArr = checkFileArr;
    }

    public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(String checkUserId) {
        this.checkUserId = checkUserId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getReviewRemark() {
        return reviewRemark;
    }

    public void setReviewRemark(String reviewRemark) {
        this.reviewRemark = reviewRemark;
    }
}
