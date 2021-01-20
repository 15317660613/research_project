package com.adc.da.budget.dto;

import lombok.Data;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.util.List;

@Data
public class PPTDataDTO {

    /**
     * 图标标题
     */
    private String title;

    /**
     * label数组
     */
    private List<String> names;

    /**
     * 分组标题+折线标题（数组最后一个元素为折线标题）
     */
    private List<String> groupTitle;

    /**
     * 柱状图分组数据
     */
    private List<List<String>> groupValue;

    /**
     * 非分组数据
     */
    private List<String> values;
    /**
     * 幻灯片对象
     */
    private XMLSlideShow pptx;

    public PPTDataDTO(XMLSlideShow pptx,String title, List<String> names, List<String> groupTitle, List<List<String>> groupValue, List<String> values) {
        this.pptx = pptx;
        this.title = title;
        this.names = names;
        this.groupTitle = groupTitle;
        this.groupValue = groupValue;
        this.values= values;
    }

}
