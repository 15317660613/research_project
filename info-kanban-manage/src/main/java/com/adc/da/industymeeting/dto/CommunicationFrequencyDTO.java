package com.adc.da.industymeeting.dto;

import com.adc.da.excel.annotation.Excel;
import com.adc.da.util.utils.StringUtils;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CommunicationFrequencyDTO {

    @Excel(name = "省份", orderNum = "1")
    private String province;

    @Excel(name = "高层交流次数", orderNum = "2")
    private String frequencyStr;

    private long frequency;

    public void setFrequencyStr(String frequencyStr) {
        this.frequencyStr = frequencyStr;
        if (StringUtils.isEmpty(frequencyStr)) {
            this.frequency = 0;
        } else {
            BigDecimal bd = new BigDecimal(frequencyStr);
            this.frequency = bd.longValue();
        }
    }
}