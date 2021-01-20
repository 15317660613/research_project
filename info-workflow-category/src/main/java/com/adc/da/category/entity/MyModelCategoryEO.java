package com.adc.da.category.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <b>功能：</b>ACT_TR_MODEL_CATEGORY ModelCategoryEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-12 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MyModelCategoryEO extends BaseEntity {
    private String categoryName;

    private String categoryId;

    private String procDefKey;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    private String extInfo1;

    private String extInfo2;

}
