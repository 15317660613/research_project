package com.adc.da.research.personnel.dto;

import com.adc.da.research.personnel.entity.ExpertUserInfoEO;

import java.util.List;

public class BindUserInfoDto {
    private String id;
    private String expertGroupName;
    private String remark;
    //已绑定的用户
    private List<ExpertUserInfoEO> bindUserInfoList;
    //未绑定的用户
    private List<ExpertUserInfoEO> unBindUserInfoList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpertGroupName() {
        return expertGroupName;
    }

    public void setExpertGroupName(String expertGroupName) {
        this.expertGroupName = expertGroupName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ExpertUserInfoEO> getBindUserInfoList() {
        return bindUserInfoList;
    }

    public void setBindUserInfoList(List<ExpertUserInfoEO> bindUserInfoList) {
        this.bindUserInfoList = bindUserInfoList;
    }

    public List<ExpertUserInfoEO> getUnBindUserInfoList() {
        return unBindUserInfoList;
    }

    public void setUnBindUserInfoList(List<ExpertUserInfoEO> unBindUserInfoList) {
        this.unBindUserInfoList = unBindUserInfoList;
    }
}
