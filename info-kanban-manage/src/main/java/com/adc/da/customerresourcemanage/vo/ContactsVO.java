package com.adc.da.customerresourcemanage.vo;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>DB_CONTACTS ContactsEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ContactsVO extends BaseEntity {


    /** 主键 **/
    private String id;
    /**企业id-外键  **/
    private String enterpriseId;
    /**企业名称-外键  **/
    private String enterpriseName;
    /** 姓名 **/
    private String contactsUsname;
    /** 性别 **/
    private String contactsSex;
    /** 部门 **/
    private String departName;
    /** 子部门 **/
    private String childDepartName;
    /** 职务 **/
    private String contactsPost;
    /** 手机 **/
    private String contactsPhone;
    /** 座机 **/
    private String contactsLandline;
    /** 邮箱 **/
    private String contactsEmail;
    /** 家庭住址 **/
    private String contactsHomeaddress;
    /** 生日 **/
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date contactsBirthday;
    /** 籍贯 **/
    private String contactsBirthplace;
    /** 毕业院校 **/
    private String contactsSchool;
    /** 影响力-数据字典维护 **/
    private String effectId;
    /** 影响力名称 **/
    private String effectName;
    /**亲密度-数据字典维护  **/
    private String intimacyId;
    /**亲密度名称  **/
    private String intimacyName;
    /** 录入日期 **/
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**录入人id  **/
    private String createUserId;
    /** 录入人姓名 **/
    private String createUserName;
    /** 联系人姓名 **/
    private String conUsname;
    /** 联系人部门 **/
    private String conDepartName;
    /** 联系人职务 **/
    private String conPost;
    /**联系方式  **/
    private String conPhone;
    /**联系邮箱  **/
    private String conEmail;
    /** 材料 **/
    private String stuff;
    /** 评价 **/
    private String evaluate;
    /** 市场 **/
    private String market;
    /** 法规 **/
    private String statute;
    /**  认证**/
    private String authentication;
    /**  智能**/
    private String intelligence;
    /** 采购 **/
    private String purchase;
    /**财务  **/
    private String finance;
    /**综合**/
    private  String comprehensive;
    /** 备注 **/
    private String remark;
    /**删除标志 0-正常 1-删除 **/
    private  String delFlag;

    /** 其他 **/
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;


}
