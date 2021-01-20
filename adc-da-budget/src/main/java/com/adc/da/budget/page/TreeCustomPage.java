package com.adc.da.budget.page;

import com.adc.da.budget.vo.UserProjectCustomVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TreeCustomPage {

    private String keyword;

    private Integer type;


    private String status;

    /**
     * 页大小
     */
    private Integer pageSize = 10;

    /**
     * 页码
     */
    private Integer pageNo = 1;

    /**
     * 关闭节点的id ， 关闭后的节点不会计入总数统计
     * @see UserProjectCustomVO#getId()
     */
    private List<String> closeIdList = new ArrayList<>();

    /**
     * 是否采用业务分页
     */
    private boolean pageByBusiness = false;
}
