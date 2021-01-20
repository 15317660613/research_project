package com.adc.da.finance.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.adc.da.base.entity.BaseEntity;
import com.adc.da.util.utils.StringUtils;
import lombok.Data;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Date;

/**
 * <b>功能：</b>F_SALARY_MANAGEMENT SalaryManagementEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class SalaryManagementEO extends BaseEntity implements IExcelModel, IExcelDataModel {

    private String errorMsg;

    private int rowNum;


    private String id;

    private String orgId;

    @Excel(name = "*部门", orderNum = "0")
    private String orgName;

    @Excel(name = "*年", orderNum = "1")
    private Integer year;

    @Excel(name = "*月", orderNum = "2")
    private Integer month;

    @Excel(name = "科目名称", orderNum = "3")
    private String subjectName;

    @Excel(name = "对方科目", orderNum = "4")
    private String otherSubjectName;

    @Excel(name = "*金额（元）", orderNum = "5", type = 10)
    private Double amount;

    @Excel(name = "*所属业务", orderNum = "6")
    private String businessName;

    private String businessId;

    private String updateUserId;

    private String updateUserName;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Integer delFlag;

    private Integer extInfo;

    private Integer extInfo1;

    private String extInfo2;

    private String extInfo3;

    private String extInfo4;

    private String extInfo5;

    private String orgInitial;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>orgId -> org_id</li>
     * <li>orgName -> org_name</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     * <li>subjectName -> subject_name</li>
     * <li>otherSubjectName -> other_subject_name</li>
     * <li>amount -> amount</li>
     * <li>businessName -> business_name</li>
     * <li>businessId -> business_id</li>
     * <li>updateUserId -> update_user_id</li>
     * <li>updateUserName -> update_user_name</li>
     * <li>updateTime -> update_time</li>
     * <li>extInfo -> ext_info</li>
     * <li>extInfo1 -> ext_info1</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     * <li>extInfo4 -> ext_info4</li>
     * <li>extInfo5 -> ext_info5</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
        case "id":
            return "id";
        case "orgId":
            return "org_id";
        case "orgName":
            return "org_name";
        case "year":
            return "year";
        case "month":
            return "month";
        case "subjectName":
            return "subject_name";
        case "otherSubjectName":
            return "other_subject_name";
        case "amount":
            return "amount";
        case "businessName":
            return "business_name";
        case "businessId":
            return "business_id";
        case "updateUserId":
            return "update_user_id";
        case "updateUserName":
            return "update_user_name";
        case "updateTime":
            return "update_time";
        case "delFlag":
            return "del_flag";
        case "extInfo":
            return "ext_info";
        case "extInfo1":
            return "ext_info1";
        case "extInfo2":
            return "ext_info2";
        case "extInfo3":
            return "ext_info3";
        case "extInfo4":
            return "ext_info4";
        case "extInfo5":
            return "ext_info5";
        case "orgInitial":
            return "org_initial";
        default:
            return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>org_id -> orgId</li>
     * <li>org_name -> orgName</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     * <li>subject_name -> subjectName</li>
     * <li>other_subject_name -> otherSubjectName</li>
     * <li>amount -> amount</li>
     * <li>business_name -> businessName</li>
     * <li>business_id -> businessId</li>
     * <li>update_user_id -> updateUserId</li>
     * <li>update_user_name -> updateUserName</li>
     * <li>update_time -> updateTime</li>
     * <li>ext_info -> extInfo</li>
     * <li>ext_info1 -> extInfo1</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     * <li>ext_info4 -> extInfo4</li>
     * <li>ext_info5 -> extInfo5</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
        case "id":
            return "id";
        case "org_id":
            return "orgId";
        case "org_name":
            return "orgName";
        case "year":
            return "year";
        case "month":
            return "month";
        case "subject_name":
            return "subjectName";
        case "other_subject_name":
            return "otherSubjectName";
        case "amount":
            return "amount";
        case "business_name":
            return "businessName";
        case "business_id":
            return "businessId";
        case "update_user_id":
            return "updateUserId";
        case "update_user_name":
            return "updateUserName";
        case "update_time":
            return "updateTime";
        case "del_flag":
            return "delFlag";
        case "ext_info":
            return "extInfo";
        case "ext_info1":
            return "extInfo1";
        case "ext_info2":
            return "extInfo2";
        case "ext_info3":
            return "extInfo3";
        case "ext_info4":
            return "extInfo4";
        case "ext_info5":
            return "extInfo5";
        case "org_initial":
            return "orgInitial";
        default:
            return null;
        }
    }

    /**
     * 获取汉字的首字母大写
     */
    public String getFirstSpell() {
        StringBuffer pybf = new StringBuffer();
        if (StringUtils.isEmpty(this.getOrgName())) {
            return "z";
        } else {
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            char firstChar = this.getOrgName().charAt(0);
            if (firstChar > 128) { //如果已经是字母就不用转换了
                try {
                    //获取当前汉字的全拼
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(firstChar, defaultFormat);
                    if (temp != null && temp.length > 0) {
                        pybf.append(temp[0]);// 取首字母
                    } else {
                        pybf.append(firstChar);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                if (firstChar >= 'a' && firstChar <= 'z') {
                    firstChar -= 32;
                }
                pybf.append(firstChar);
            }
        }
        return pybf.toString();
    }

}
