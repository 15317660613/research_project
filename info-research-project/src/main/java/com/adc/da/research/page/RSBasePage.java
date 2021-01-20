package com.adc.da.research.page;

import com.adc.da.base.page.BasePage;
import lombok.Getter;
import lombok.Setter;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-08-30
 */
@Getter
@Setter
public class RSBasePage extends BasePage {
    /**
     * 项目id
     */
    private String researchProjectId;

    /**
     *
     */
    private String researchProjectIdOperator = "=";
}
