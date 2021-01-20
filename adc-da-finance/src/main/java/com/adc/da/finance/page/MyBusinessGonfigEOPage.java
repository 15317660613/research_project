package com.adc.da.finance.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

/**
 * @ClassName MyBusinessGonfigEOPage
 * @Description: TODO
 * @Author 丁强
 * @Date 2020/4/26
 * @Version V1.0
 **/
@Data
public class MyBusinessGonfigEOPage extends BasePage {
    private String userId;

    private String bgName;

    private String bgType;
}
