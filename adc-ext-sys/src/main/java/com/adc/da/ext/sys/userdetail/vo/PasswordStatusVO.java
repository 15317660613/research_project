package com.adc.da.ext.sys.userdetail.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordStatusVO {
    Boolean passwordUpdatingNeeded;
    Integer remainingDays;
}
