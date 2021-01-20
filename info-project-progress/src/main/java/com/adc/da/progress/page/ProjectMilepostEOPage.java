package com.adc.da.progress.page;

import com.adc.da.base.page.BasePage;

import java.util.Date;
import java.util.List;

/**
 * <b>功能：</b>PR_PROJECT_MILEPOST ProjectMilepostEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectMilepostEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String projectId;
    private String projectIdOperator = "=";
    private String projectName;
    private String projectNameOperator = "=";
    private String milepostName;
    private String milepostNameOperator = "=";
    private String milepostTarget;
    private String milepostTargetOperator = "=";
    private String milepostManagerId;
    private String milepostManagerIdOperator = "=";
    private String milepostManagerName;
    private String milepostManagerNameOperator = "=";
    private String milepostBeginTime;
    private String milepostBeginTime1;
    private String milepostBeginTime2;
    private String milepostBeginTimeOperator = "=";
    private String milepostEndTime;
    private String milepostEndTime1;
    private String milepostEndTime2;
    private String milepostEndTimeOperator = "=";
    private String milepostVersion;
    private String milepostVersionOperator = "=";
    private String extInfo1;
    private String extInfo1Operator = "=";
    private String extInfo2;
    private String extInfo2Operator = "=";
    private String extInfo3;
    private String extInfo3Operator = "=";
    private String extInfo4;
    private String extInfo4Operator = "=";
    private String extInfo5;
    private String extInfo5Operator = "=";
    private String stageId;
    private String stageIdOperator = "=";
    private String finishTime;
    private String finishTime1;
    private String finishTime2;
    private String finishTimeOperator = "=";
    private String finishStatus;
    private String finishStatusOperator = "=";
    private List<String> projectIdList ;

    public List<String> getProjectIdList() {
        return projectIdList;
    }

    public void setProjectIdList(List<String> projectIdList) {
        this.projectIdList = projectIdList;
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

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectNameOperator() {
        return this.projectNameOperator;
    }

    public void setProjectNameOperator(String projectNameOperator) {
        this.projectNameOperator = projectNameOperator;
    }

    public String getMilepostName() {
        return this.milepostName;
    }

    public void setMilepostName(String milepostName) {
        this.milepostName = milepostName;
    }

    public String getMilepostNameOperator() {
        return this.milepostNameOperator;
    }

    public void setMilepostNameOperator(String milepostNameOperator) {
        this.milepostNameOperator = milepostNameOperator;
    }

    public String getMilepostTarget() {
        return this.milepostTarget;
    }

    public void setMilepostTarget(String milepostTarget) {
        this.milepostTarget = milepostTarget;
    }

    public String getMilepostTargetOperator() {
        return this.milepostTargetOperator;
    }

    public void setMilepostTargetOperator(String milepostTargetOperator) {
        this.milepostTargetOperator = milepostTargetOperator;
    }

    public String getMilepostManagerId() {
        return this.milepostManagerId;
    }

    public void setMilepostManagerId(String milepostManagerId) {
        this.milepostManagerId = milepostManagerId;
    }

    public String getMilepostManagerIdOperator() {
        return this.milepostManagerIdOperator;
    }

    public void setMilepostManagerIdOperator(String milepostManagerIdOperator) {
        this.milepostManagerIdOperator = milepostManagerIdOperator;
    }

    public String getmilepostManagerName() {
        return this.milepostManagerName;
    }

    public void setmilepostManagerName(String milepostManagerName) {
        this.milepostManagerName = milepostManagerName;
    }

    public String getmilepostManagerNameOperator() {
        return this.milepostManagerNameOperator;
    }

    public void setmilepostManagerNameOperator(String milepostManagerNameOperator) {
        this.milepostManagerNameOperator = milepostManagerNameOperator;
    }

    public String getMilepostBeginTime() {
        return this.milepostBeginTime;
    }

    public void setMilepostBeginTime(String milepostBeginTime) {
        this.milepostBeginTime = milepostBeginTime;
    }

    public String getMilepostBeginTime1() {
        return this.milepostBeginTime1;
    }

    public void setMilepostBeginTime1(String milepostBeginTime1) {
        this.milepostBeginTime1 = milepostBeginTime1;
    }

    public String getMilepostBeginTime2() {
        return this.milepostBeginTime2;
    }

    public void setMilepostBeginTime2(String milepostBeginTime2) {
        this.milepostBeginTime2 = milepostBeginTime2;
    }

    public String getMilepostBeginTimeOperator() {
        return this.milepostBeginTimeOperator;
    }

    public void setMilepostBeginTimeOperator(String milepostBeginTimeOperator) {
        this.milepostBeginTimeOperator = milepostBeginTimeOperator;
    }

    public String getMilepostEndTime() {
        return this.milepostEndTime;
    }

    public void setMilepostEndTime(String milepostEndTime) {
        this.milepostEndTime = milepostEndTime;
    }

    public String getMilepostEndTime1() {
        return this.milepostEndTime1;
    }

    public void setMilepostEndTime1(String milepostEndTime1) {
        this.milepostEndTime1 = milepostEndTime1;
    }

    public String getMilepostEndTime2() {
        return this.milepostEndTime2;
    }

    public void setMilepostEndTime2(String milepostEndTime2) {
        this.milepostEndTime2 = milepostEndTime2;
    }

    public String getMilepostEndTimeOperator() {
        return this.milepostEndTimeOperator;
    }

    public void setMilepostEndTimeOperator(String milepostEndTimeOperator) {
        this.milepostEndTimeOperator = milepostEndTimeOperator;
    }

    public String getMilepostVersion() {
        return this.milepostVersion;
    }

    public void setMilepostVersion(String milepostVersion) {
        this.milepostVersion = milepostVersion;
    }

    public String getMilepostVersionOperator() {
        return this.milepostVersionOperator;
    }

    public void setMilepostVersionOperator(String milepostVersionOperator) {
        this.milepostVersionOperator = milepostVersionOperator;
    }

    public String getExtInfo1() {
        return this.extInfo1;
    }

    public void setExtInfo1(String extInfo1) {
        this.extInfo1 = extInfo1;
    }

    public String getExtInfo1Operator() {
        return this.extInfo1Operator;
    }

    public void setExtInfo1Operator(String extInfo1Operator) {
        this.extInfo1Operator = extInfo1Operator;
    }

    public String getExtInfo2() {
        return this.extInfo2;
    }

    public void setExtInfo2(String extInfo2) {
        this.extInfo2 = extInfo2;
    }

    public String getExtInfo2Operator() {
        return this.extInfo2Operator;
    }

    public void setExtInfo2Operator(String extInfo2Operator) {
        this.extInfo2Operator = extInfo2Operator;
    }

    public String getExtInfo3() {
        return this.extInfo3;
    }

    public void setExtInfo3(String extInfo3) {
        this.extInfo3 = extInfo3;
    }

    public String getExtInfo3Operator() {
        return this.extInfo3Operator;
    }

    public void setExtInfo3Operator(String extInfo3Operator) {
        this.extInfo3Operator = extInfo3Operator;
    }

    public String getExtInfo4() {
        return this.extInfo4;
    }

    public void setExtInfo4(String extInfo4) {
        this.extInfo4 = extInfo4;
    }

    public String getExtInfo4Operator() {
        return this.extInfo4Operator;
    }

    public void setExtInfo4Operator(String extInfo4Operator) {
        this.extInfo4Operator = extInfo4Operator;
    }

    public String getExtInfo5() {
        return this.extInfo5;
    }

    public void setExtInfo5(String extInfo5) {
        this.extInfo5 = extInfo5;
    }

    public String getExtInfo5Operator() {
        return this.extInfo5Operator;
    }

    public void setExtInfo5Operator(String extInfo5Operator) {
        this.extInfo5Operator = extInfo5Operator;
    }

    public String getstageId() {
        return this.stageId;
    }

    public void setstageId(String stageId) {
        this.stageId = stageId;
    }

    public String getstageIdOperator() {
        return this.stageIdOperator;
    }

    public void setstageIdOperator(String stageIdOperator) {
        this.stageIdOperator = stageIdOperator;
    }

    public String getFinishTime() {
        return this.finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getFinishTime1() {
        return this.finishTime1;
    }

    public void setFinishTime1(String finishTime1) {
        this.finishTime1 = finishTime1;
    }

    public String getFinishTime2() {
        return this.finishTime2;
    }

    public void setFinishTime2(String finishTime2) {
        this.finishTime2 = finishTime2;
    }

    public String getFinishTimeOperator() {
        return this.finishTimeOperator;
    }

    public void setFinishTimeOperator(String finishTimeOperator) {
        this.finishTimeOperator = finishTimeOperator;
    }

    public String getFinishStatus() {
        return this.finishStatus;
    }

    public void setFinishStatus(String finishStatus) {
        this.finishStatus = finishStatus;
    }

    public String getFinishStatusOperator() {
        return this.finishStatusOperator;
    }

    public void setFinishStatusOperator(String finishStatusOperator) {
        this.finishStatusOperator = finishStatusOperator;
    }

}
