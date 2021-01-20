package com.adc.da.research.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <b>功能：</b>RS_PROJECT_INFO InfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Getter
@Setter
@Builder
public class FormInfoVO {

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 所属部门名称
     */
    private String deptName;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 所属部门ID
     */
    private String deptId;

    /**
     * 开始时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectBeginTime;

    /**
     * 结束时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectEndTime;

    /**
     * 创建人Id
     */
    private String projectLeaderId;

    /**
     * 创建人
     */
    private String projectLeaderName;

}
