//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserEPEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3658632939727891047L;
    private String avatar;
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

    private Float workTime = 0.0f;
}
