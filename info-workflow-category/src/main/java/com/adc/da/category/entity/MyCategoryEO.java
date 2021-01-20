package com.adc.da.category.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.workflow.vo.ActivitiProcessDefinitionVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * <b>功能：</b>ACT_TS_CATEGORY CategoryEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-12 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MyCategoryEO extends BaseEntity {

    private String id;

    private String name;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer delFlag;

    private String extInfo1;

    private String extInfo2;

    private List<ActivitiProcessDefinitionVO> deploymentList;

}
