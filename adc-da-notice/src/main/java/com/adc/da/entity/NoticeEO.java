package com.adc.da.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.*;

import java.util.Date;

/**
 * <b>功能：</b>Notice NoticeEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-03-23 <br>
 * <b>版权所有：<b>版权归天津卡达克数据技术中心所有。<br>
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeEO extends BaseEntity {


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
}
