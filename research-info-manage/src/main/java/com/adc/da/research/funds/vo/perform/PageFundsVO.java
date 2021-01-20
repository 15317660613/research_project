package com.adc.da.research.funds.vo.perform;

import java.util.List;

/**
 * 包装有总数信息的FundsPerformVO类
 *
 * @Auther: yanyujie
 * @Date: 2020/12/01
 * @Description:
 */
public class PageFundsVO {

    Long size;
    List<FundsPerformVO> fundsPerformVOS;

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public List<FundsPerformVO> getFundsPerformVOS() {
        return fundsPerformVOS;
    }

    public void setFundsPerformVOS(List<FundsPerformVO> fundsPerformVOS) {
        this.fundsPerformVOS = fundsPerformVOS;
    }
}
