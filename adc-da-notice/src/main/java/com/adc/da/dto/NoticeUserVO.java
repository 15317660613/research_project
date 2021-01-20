package com.adc.da.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NoticeUserVO {

    /**
     * 公告信息主键
     */
    private String id;

    /**
     * 公告名称
     */
    private String noticeName;

    /**
     * 公告内容
     */
    private String noticeContent;

    /**
     * 公告文件id
     */
    private String noticeFileId;

    /**
     * 公告类型id
     */
    private String noticeTypeId;

    /**
     * 创建时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人id
     */
    private String createUserId;

    /**
     * 删除标志  0 正常 1删除
     */
    private String delFlag;

    /**
     * 公告文件名称
     */
    private String noticeFileName;

    /**
     * 是否所有人可见 1-全部可见
     */
    private String noticeIsLook;

    /**
     * 接收人id集合，逗号隔开
     */
    private String receiveUserIds;

    /**
     * 接收人姓名集合，逗号隔开
     */
    private String receiveUserNames;

    /**
     * 其他
     */
    private String ext1;

    /**
     * 其他
     */
    private String ext2;

    /**
     * 其他
     */
    private String ext3;

    /**
     * 其他
     */
    private String ext4;

    /**
     * 其他
     */
    private String ext5;

    /**
     * 数据字典维护：公告通知，资料 Name
     */
    private String noticeTypeName;
    /**
     * 公告id
     */
    private String noticeId;

    /**
     * 接收人id
     */
    private String receiveUserId;

    /**
     * 接收人操作状态 0未忽略 1已忽略/查看
     */
    private String operationStatus;
}
