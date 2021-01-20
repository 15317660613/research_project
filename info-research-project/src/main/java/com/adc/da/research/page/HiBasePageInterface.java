package com.adc.da.research.page;

import com.adc.da.research.entity.HiBaseInterface;

/**
 * 实例 分页
 *
 *
 * * 流程实例key
 * * 变更内容标记
 */
public interface HiBasePageInterface extends HiBaseInterface {

    void setProcBusinessKeyOperator(String procBusinessKeyOperator);

    String getProcBusinessKeyOperator();

    void setMaskOperator(String mask);

    String getMaskOperator();

}
