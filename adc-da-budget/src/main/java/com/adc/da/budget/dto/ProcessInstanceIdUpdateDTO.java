package com.adc.da.budget.dto;

import lombok.Data;

import java.util.List;

/**
 * created by chenhaidong 2018/11/19
 */
public class ProcessInstanceIdUpdateDTO {
    private List<String> idList;
    private String processInstanceId;

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
