package com.adc.da.statistics.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>功能：</b>ST_CONTRACT_AMOUNT ContractAmountEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-15 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 * /**
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>projectId -> project_id_</li>
 * <li>contractNo -> contract_no_</li>
 * <li>businessId -> business_id_</li>
 * <li>contractAmount -> contract_amount_</li>
 * <li>createTime -> create_time_</li>
 * <li>projectBeginTime -> project_begin_time_</li>
 * <li>extInfo01 -> ext_info_01_</li>
 */
@Setter
@Getter
@Builder
public class ContractAmountEO extends BaseEntity {
    /***/
    private String projectId;

    /***/
    private String contractNo;

    /***/
    private String businessId;

    /***/
    private String deptId;

    /***/
    private Double contractAmount;

    /**
     * 合同签订时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 合同生效时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectBeginTime;

    /** 用于已删除 项目的标识 */
    private String extInfo01;

    /**
     * 合同名称
     */
    private String extInfo02;

    /**
     * 业务方
     */
    private String extInfo03;

    /**
     * 统计字段-非数据库
     */
    private BigDecimal sumValue;

    @Tolerate
    public ContractAmountEO() {
        //new ContractAmountEO()
    }
}
