package com.adc.da.finance.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * <b>功能：</b>F_COST_RECEIVER CostReceiverEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class CostReceiverVO extends BaseEntity {

    private String id;

    private String orgId;

    private String orgName;

    private List<String> userIdList;

    private List<String> userNameList;
}
