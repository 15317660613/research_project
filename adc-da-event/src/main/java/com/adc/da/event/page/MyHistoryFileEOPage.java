package com.adc.da.event.page;

import com.adc.da.base.page.BasePage;

import java.util.Date;

/**
 * <b>功能：</b>WR_HISTORY_FILE HistoryFileEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class MyHistoryFileEOPage extends BasePage {

    private String id;
    private String fileId;
    private String filePath;
    private String editorId;
    private String editorName;
    private String editTime;
    private String startEditTime;
    private String endEditTime;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getEditorId() {
        return this.editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public String getEditorName() {
        return this.editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }


    public String getEditTime() {
        return this.editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getStartEditTime() {
        return startEditTime;
    }

    public void setStartEditTime(String startEditTime) {
        this.startEditTime = startEditTime;
    }

    public String getEndEditTime() {
        return endEditTime;
    }

    public void setEndEditTime(String endEditTime) {
        this.endEditTime = endEditTime;
    }

}

