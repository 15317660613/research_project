package com.adc.da.research.entity;

/**
 * 实例
 *
 *
 * * 流程实例key
 * * 变更内容标记
 */
public interface HiBaseInterface {

    /**
     *
     * @param procBusinessKey
     */
    void setProcBusinessKey(String procBusinessKey);

    /**
     *
     * @return
     */
    String getProcBusinessKey();

    /**
     *
     * @param mask
     */
    void setMask(String mask);

    /**
     *
     * @return
     */
    String getMask();
}
