package com.adc.da.knowledge.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <b>功能：</b>K_PAPER PaperEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Getter
@Setter
public class PaperEO extends BaseEntity {

    private String id;

    @Excel(name = "题名", orderNum = "3")
    private String name;

    @Excel(name = "发表期刊", orderNum = "2")
    private String publishedJournals;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "刊登时间", orderNum = "7", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date publishedTime;

    @Excel(name = "刊登期号", orderNum = "6")
    private String publishedIssue;

    @Excel(name = "关键词", orderNum = "4")
    private String keywords;

    @Excel(name = "索引", orderNum = "5")
    private String index;

    @Excel(name = "公司", orderNum = "8")
    private String company;

    @Excel(name = "作者", orderNum = "9")
    private String authorUserNames;

    private String deptId;

    @Excel(name = "上传人", orderNum = "11")
    private String uploadUserName;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "更新时间", orderNum = "12", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String paperAbstract;

    @Excel(name = "部门", orderNum = "10")
    private String deptName;

    private String uploadUserId;

    private String authorUserIds;

    private String securityLevel;

    private String extInfo1;

    private String extInfo2;

    private String extInfo3;

    private String extInfo4;

    private String num;

    private String extInfo5;

    private String extInfo6;

    @Excel(name = "编号", orderNum = "1")
    private String autoNumber;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id_</li>
     * <li>name -> name_</li>
     * <li>publishedJournals -> published_journals_</li>
     * <li>publishedTime -> published_time_</li>
     * <li>publishedIssue -> published_issue_</li>
     * <li>keywords -> keywords_</li>
     * <li>index -> index_</li>
     * <li>company -> company_</li>
     * <li>authorUserNames -> author_user_names_</li>
     * <li>deptId -> dept_id_</li>
     * <li>uploadUserName -> upload_user_name_</li>
     * <li>updateTime -> update_time_</li>
     * <li>paperAbstract -> paper_abstract_</li>
     * <li>deptName -> dept_name_</li>
     * <li>uploadUserId -> upload_user_id_</li>
     * <li>authorUserIds -> author_user_ids_</li>
     * <li>securityLevel -> security_level_</li>
     * <li>extInfo1 -> ext_info1_</li>
     * <li>extInfo2 -> ext_info2_</li>
     * <li>extInfo3 -> ext_info3_</li>
     * <li>extInfo4 -> ext_info4_</li>
     * <li>num -> num_</li>
     * <li>extInfo5 -> ext_info5_</li>
     * <li>extInfo6 -> ext_info6_</li>
     */

}
