package com.adc.da.customerresourcemanage.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
public class ContactsDto extends BaseEntity {


    /**企业名称-外键  **/
    @Excel(name = "企业名称",orderNum = "1")
    private String enterpriseName;
    /** 姓名 **/
    @Excel(name = "姓名",orderNum = "2")
    private String contactsUsname;
    /** 部门 **/
    @Excel(name = "部门",orderNum = "3")
    private String departName;
    /** 子部门 **/
    @Excel(name = "子部门",orderNum = "4")
    private String childDepartName;
    /** 职务 **/
    @Excel(name = "职务",orderNum = "5")
    private String contactsPost;
    /** 性别 **/
    @Excel(name = "性别",orderNum = "6")
    private String contactsSex;
    /** 手机 **/
    @Excel(name = "手机",orderNum = "7")
    private String contactsPhone;
    /** 座机 **/
    @Excel(name = "座机",orderNum = "8")
    private String contactsLandline;
    /** 邮箱 **/
    @Excel(name = "邮箱",orderNum = "9")
    private String contactsEmail;
    /** 家庭住址 **/
    @Excel(name = "家庭地址",orderNum = "10")
    private String contactsHomeaddress;
    /** 生日 **/
    @Excel(name = "生日",orderNum = "11",format = "yyyy/MM/dd")
    private Date contactsBirthday;
    /** 籍贯 **/
    @Excel(name = "籍贯",orderNum = "12")
    private String contactsBirthplace;
    /** 毕业院校 **/
    @Excel(name = "毕业院校",orderNum = "13")
    private String contactsSchool;
    /** 影响力名称 **/
    @Excel(name = "影响力",orderNum = "14")
    private String effectName;
    /**亲密度名称  **/
    @Excel(name = "亲密度",orderNum = "15")
    private String intimacyName;
    /** 录入日期 **/
    @Excel(name = "录入时间",orderNum = "16")
    private Date createTime;
    /** 录入人姓名 **/
    @Excel(name = "录入人",orderNum = "17")
    private String createUserName;
    /** 材料 **/
    @Excel(name = "材料",orderNum = "18",groupName = "客户分类",fixedIndex = 17)
    private String stuff;
    /** 评价 **/
    @Excel(name = "评价",orderNum = "19",groupName = "客户分类",fixedIndex = 18)
    private String evaluate;
    /** 市场 **/
    @Excel(name = "市场",orderNum = "20",groupName = "客户分类",fixedIndex = 19)
    private String market;
    /** 法规 **/
    @Excel(name = "法规",orderNum = "21",groupName = "客户分类",fixedIndex = 20)
    private String statute;
    /**  认证**/
    @Excel(name = "认证",orderNum = "22",groupName = "客户分类",fixedIndex = 21)
    private String authentication;
    /**  智能**/
    @Excel(name = "智能",orderNum = "23",groupName = "客户分类",fixedIndex = 22)
    private String intelligence;
    /**综合**/
    @Excel(name = "综合",orderNum = "24",groupName = "客户分类",fixedIndex = 23)
    private  String comprehensive;
    /** 采购 **/
    @Excel(name = "采购",orderNum = "25",groupName = "客户分类",fixedIndex = 24)
    private String purchase;
    /**财务  **/
    @Excel(name = "财务",orderNum = "26",groupName = "客户分类",fixedIndex = 25)
    private String finance;
    /** 联系人姓名 **/
    @Excel(name = "联系人姓名",orderNum = "27",fixedIndex = 26)
    private String conUsname;
    /** 联系人部门 **/
    @Excel(name = "联系人部门",orderNum = "28")
    private String conDepartName;
    /** 联系人职务 **/
    @Excel(name = "联系人职务",orderNum = "29")
    private String conPost;
    /**联系方式  **/
    @Excel(name = "联系方式",orderNum = "30")
    private String conPhone;
    /**联系邮箱  **/
    @Excel(name = "联系邮箱",orderNum = "31")
    private String conEmail;
    /** 备注 **/
    @Excel(name = "备注",orderNum = "32")
    private String remark;





}
