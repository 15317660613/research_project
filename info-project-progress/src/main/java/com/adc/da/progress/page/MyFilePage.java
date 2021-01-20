package com.adc.da.progress.page;


import com.adc.da.base.page.BasePage;
import lombok.Data;

import java.util.List;

@Data
public class MyFilePage extends BasePage {

    private  String stageId ;

    private  String milepostId ;

    private List<String> milepostIdList ;

    private List<String> milepostResultIdList ;


}
