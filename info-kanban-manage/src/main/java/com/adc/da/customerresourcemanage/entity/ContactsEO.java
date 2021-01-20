package com.adc.da.customerresourcemanage.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * <b>功能：</b>DB_CONTACTS ContactsEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ContactsEO extends BaseEntity {


    /** 主键 **/
    private String id;
    /**企业id-外键  **/
    private String enterpriseId;
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
    /**亲密度-数据字典维护  **/
    private String intimacyId;
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

    /** 录入日期 **/
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**更新人id  **/
    private String updateUserId;
    /** 更新人姓名 **/
    private String updateUserName;

    /**企业名称-外键  **/
    @Transient
    private String enterpriseName;
    /**企业领域  **/
    @Transient
    private String enterpriseDomain;
    /**企业省份  **/
    @Transient
    private String enterpriseProvince;
    /**企业地址  **/
    @Transient
    private String enterpriseAddress;
    /** 影响力名称 **/
    @Transient
    private String effectName;
    /**亲密度名称  **/
    @Transient
    private String intimacyName;



    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>ext5 -> ext5</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>id -> id</li>
     * <li>enterpriseId -> enterprise_id</li>
     * <li>contactsUsname -> contacts_usname</li>
     * <li>contactsSex -> contacts_sex</li>
     * <li>departName -> depart_name</li>
     * <li>childDepartName -> child_depart_name</li>
     * <li>contactsPost -> contacts_post</li>
     * <li>contactsPhone -> contacts_phone</li>
     * <li>contactsLandline -> contacts_landline</li>
     * <li>contactsEmail -> contacts_email</li>
     * <li>contactsHomeaddress -> contacts_homeaddress</li>
     * <li>contactsBirthday -> contacts_birthday</li>
     * <li>contactsBirthplace -> contacts_birthplace</li>
     * <li>contactsSchool -> contacts_school</li>
     * <li>effectId -> effect_id</li>
     * <li>intimacyId -> intimacy_id</li>
     * <li>createTime -> create_time</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>conUsname -> con_usname</li>
     * <li>conDepartName -> con_depart_name</li>
     * <li>conPost -> con_post</li>
     * <li>conPhone -> con_phone</li>
     * <li>conEmail -> con_email</li>
     * <li>stuff -> stuff</li>
     * <li>evaluate -> evaluate</li>
     * <li>market -> market</li>
     * <li>statute -> statute</li>
     * <li>authentication -> authentication</li>
     * <li>intelligence -> intelligence</li>
     * <li>purchase -> purchase</li>
     * <li>finance -> finance</li>
     * <li>remark -> remark</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "ext5": return "ext5";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "id": return "id";
            case "enterpriseId": return "enterprise_id";
            case "contactsUsname": return "contacts_usname";
            case "contactsSex": return "contacts_sex";
            case "departName": return "depart_name";
            case "childDepartName": return "child_depart_name";
            case "contactsPost": return "contacts_post";
            case "contactsPhone": return "contacts_phone";
            case "contactsLandline": return "contacts_landline";
            case "contactsEmail": return "contacts_email";
            case "contactsHomeaddress": return "contacts_homeaddress";
            case "contactsBirthday": return "contacts_birthday";
            case "contactsBirthplace": return "contacts_birthplace";
            case "contactsSchool": return "contacts_school";
            case "effectId": return "effect_id";
            case "intimacyId": return "intimacy_id";
            case "createTime": return "create_time";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "conUsname": return "con_usname";
            case "conDepartName": return "con_depart_name";
            case "conPost": return "con_post";
            case "conPhone": return "con_phone";
            case "conEmail": return "con_email";
            case "stuff": return "stuff";
            case "evaluate": return "evaluate";
            case "market": return "market";
            case "statute": return "statute";
            case "authentication": return "authentication";
            case "intelligence": return "intelligence";
            case "purchase": return "purchase";
            case "finance": return "finance";
            case "remark": return "remark";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "delFalg" : return "del_falg";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>ext5 -> ext5</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>id -> id</li>
     * <li>enterprise_id -> enterpriseId</li>
     * <li>contacts_usname -> contactsUsname</li>
     * <li>contacts_sex -> contactsSex</li>
     * <li>depart_name -> departName</li>
     * <li>child_depart_name -> childDepartName</li>
     * <li>contacts_post -> contactsPost</li>
     * <li>contacts_phone -> contactsPhone</li>
     * <li>contacts_landline -> contactsLandline</li>
     * <li>contacts_email -> contactsEmail</li>
     * <li>contacts_homeaddress -> contactsHomeaddress</li>
     * <li>contacts_birthday -> contactsBirthday</li>
     * <li>contacts_birthplace -> contactsBirthplace</li>
     * <li>contacts_school -> contactsSchool</li>
     * <li>effect_id -> effectId</li>
     * <li>intimacy_id -> intimacyId</li>
     * <li>create_time -> createTime</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>con_usname -> conUsname</li>
     * <li>con_depart_name -> conDepartName</li>
     * <li>con_post -> conPost</li>
     * <li>con_phone -> conPhone</li>
     * <li>con_email -> conEmail</li>
     * <li>stuff -> stuff</li>
     * <li>evaluate -> evaluate</li>
     * <li>market -> market</li>
     * <li>statute -> statute</li>
     * <li>authentication -> authentication</li>
     * <li>intelligence -> intelligence</li>
     * <li>purchase -> purchase</li>
     * <li>finance -> finance</li>
     * <li>remark -> remark</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "ext5": return "ext5";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "id": return "id";
            case "enterprise_id": return "enterpriseId";
            case "contacts_usname": return "contactsUsname";
            case "contacts_sex": return "contactsSex";
            case "depart_name": return "departName";
            case "child_depart_name": return "childDepartName";
            case "contacts_post": return "contactsPost";
            case "contacts_phone": return "contactsPhone";
            case "contacts_landline": return "contactsLandline";
            case "contacts_email": return "contactsEmail";
            case "contacts_homeaddress": return "contactsHomeaddress";
            case "contacts_birthday": return "contactsBirthday";
            case "contacts_birthplace": return "contactsBirthplace";
            case "contacts_school": return "contactsSchool";
            case "effect_id": return "effectId";
            case "intimacy_id": return "intimacyId";
            case "create_time": return "createTime";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "con_usname": return "conUsname";
            case "con_depart_name": return "conDepartName";
            case "con_post": return "conPost";
            case "con_phone": return "conPhone";
            case "con_email": return "conEmail";
            case "stuff": return "stuff";
            case "evaluate": return "evaluate";
            case "market": return "market";
            case "statute": return "statute";
            case "authentication": return "authentication";
            case "intelligence": return "intelligence";
            case "purchase": return "purchase";
            case "finance": return "finance";
            case "remark": return "remark";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "del_falg" : return "delFalg";
            default: return null;
        }
    }
    


}
