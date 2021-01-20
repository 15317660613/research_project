package com.adc.da.research.entity;

/**
 * 实例
 *
 *
 *  流程实例key
 *  结项内容标记
 */
public interface EndBaseInterface {

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
     * @param procBusinessKey
     */
    void setResearchProjectId(String procBusinessKey);

    /**
     *
     * @return
     */
    String getResearchProjectId();

}
