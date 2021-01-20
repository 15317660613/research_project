package com.adc.da.finance.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

/**
 * @ClassName MyCostManagementEOPage
 * @Description: TODO
 * @Author 丁强
 * @Date 2020/4/24
 * @Version V1.0
 **/
@Data
public class MyCostManagementEOPage extends BasePage {
    private String orgName;

    private String status;

    private String userId;
}
