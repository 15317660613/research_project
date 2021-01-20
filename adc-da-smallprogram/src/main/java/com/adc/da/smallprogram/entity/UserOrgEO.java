package com.adc.da.smallprogram.entity;

import lombok.Data;

import java.util.Comparator;

@Data
public class UserOrgEO {
    private String usname;
    private String password;
    private Integer delFlag;
    private String account;
    private String usid;
    private String extInfo;
    private String userCode;
    private String officePhone;
    private String cellPhoneNumber;
    private String homeAddress;
    private String postalCode;
    private String email;
    private String contactAddress;
    private String createTime;
    private String updateTime;
    private String userState;

    private String orgId;
    private String orgName;
    private String orgABB;
    private String userNum; //机构下员工人数


}
