package com.adc.da.smallprogram.vo;

import lombok.Data;
import java.util.Date;

@Data
public class ScheduleSupportVO {

    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date finishedTime;
    private String id;
    /** 标题 **/
    private String title;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private String address;
    private Integer status;
    private String createUserId;
    private String detail;
    private String receiveUserId;
    private String receiveUserName;
    /** 收藏 **/
    private Integer collected;
    private String createUserName;
    /** 置顶 **/
    private Integer top;
    private String extInfo;
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;
    private String dateSection;
    private Integer type;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /** 处理意见 **/
    private Integer opinion;
    private Integer delFlag;
}