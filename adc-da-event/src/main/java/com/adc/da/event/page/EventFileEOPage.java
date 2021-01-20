package com.adc.da.event.page;

import com.adc.da.base.page.BasePage;


/**
 * <b>功能：</b>WR_EVENT_FILE EventFileEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-12 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class EventFileEOPage extends BasePage {

    private String eventId;
    private String eventIdOperator = "=";
    private String fileId;
    private String fileIdOperator = "=";

    public String getEventId() {
        return this.eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventIdOperator() {
        return this.eventIdOperator;
    }

    public void setEventIdOperator(String eventIdOperator) {
        this.eventIdOperator = eventIdOperator;
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

}
