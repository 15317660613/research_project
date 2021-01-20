package com.adc.da.event.vo;

public class ParmVO {
    private String eventId;
    private String[] receiveUsersId;
    private String eventReceiveId ;
    private String queryFlag;


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String[] getReceiveUsersId() {
        return receiveUsersId;
    }

    public void setReceiveUsersId(String[] receiveUsersId) {
        this.receiveUsersId = receiveUsersId;
    }

    public String getEventReceiveId() {
        return eventReceiveId;
    }

    public void setEventReceiveId(String eventReceiveId) {
        this.eventReceiveId = eventReceiveId;
    }

    public String getQueryFlag() {
        return queryFlag;
    }

    public void setQueryFlag(String queryFlag) {
        this.queryFlag = queryFlag;
    }
}
