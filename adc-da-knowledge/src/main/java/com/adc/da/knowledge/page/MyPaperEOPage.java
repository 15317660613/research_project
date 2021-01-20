package com.adc.da.knowledge.page;

import com.adc.da.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * <b>功能：</b>K_PAPER PaperEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Getter
@Setter
public class MyPaperEOPage extends BasePage {

    private String id;
    private String name;
    private String publishedJournals;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date publishedTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startPublishedTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endPublishedTime;
    private String publishedIssue;
    private String keywords;
    private String index;
    private String company;
    private String authorUserNames;
    private String deptId;
    private String uploadUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String paperAbstract;
    private String deptName;
    private String uploadUserId;
    private String authorUserIds;
    private String securityLevel;
    private String num;
    private List<String> deptIdList;

}
