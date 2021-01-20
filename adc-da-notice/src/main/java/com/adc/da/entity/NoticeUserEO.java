package com.adc.da.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

@Data
public class NoticeUserEO extends BaseEntity {

    /**
     * 公告用户表主键
     */
    private String id;

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
