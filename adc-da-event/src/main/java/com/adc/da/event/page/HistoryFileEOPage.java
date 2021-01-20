package com.adc.da.event.page;

import com.adc.da.base.page.BasePage;

import java.util.Date;

/**
 * <b>功能：</b>WR_HISTORY_FILE HistoryFileEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class HistoryFileEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String fileId;
    private String fileIdOperator = "=";
    private String filePath;
    private String filePathOperator = "=";
    private String editorId;
    private String editorIdOperator = "=";
    private String editorName;
    private String editorNameOperator = "=";
    private String editTime;
    private String editTime1;
    private String editTime2;
    private String editTimeOperator = "=";

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

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileIdOperator() {
        return this.fileIdOperator;
    }

    public void setFileIdOperator(String fileIdOperator) {
        this.fileIdOperator = fileIdOperator;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePathOperator() {
        return this.filePathOperator;
    }

    public void setFilePathOperator(String filePathOperator) {
        this.filePathOperator = filePathOperator;
    }

    public String getEditorId() {
        return this.editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public String getEditorIdOperator() {
        return this.editorIdOperator;
    }

    public void setEditorIdOperator(String editorIdOperator) {
        this.editorIdOperator = editorIdOperator;
    }

    public String getEditorName() {
        return this.editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public String getEditorNameOperator() {
        return this.editorNameOperator;
    }

    public void setEditorNameOperator(String editorNameOperator) {
        this.editorNameOperator = editorNameOperator;
    }

    public String getEditTime() {
        return this.editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getEditTime1() {
        return this.editTime1;
    }

    public void setEditTime1(String editTime1) {
        this.editTime1 = editTime1;
    }

    public String getEditTime2() {
        return this.editTime2;
    }

    public void setEditTime2(String editTime2) {
        this.editTime2 = editTime2;
    }

    public String getEditTimeOperator() {
        return this.editTimeOperator;
    }

    public void setEditTimeOperator(String editTimeOperator) {
        this.editTimeOperator = editTimeOperator;
    }

}
