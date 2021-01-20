package com.adc.da.fileTemplate.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

@Data
public class FileTemplateTableVOPage extends BasePage {

    private String tempTypeId;

    private String tempName;
    private String tempCode;

    private String dicTypeCode;
    private String dicTypeName;

    private String dicTypeId;
    private String fileName;
}
