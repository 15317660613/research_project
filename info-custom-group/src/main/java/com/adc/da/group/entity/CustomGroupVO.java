package com.adc.da.group.entity;

import lombok.Data;

/**
 * 用于接收创建参数
 */

@Data
public class CustomGroupVO {
    private String[] userIds;

    private String groupName;

}
