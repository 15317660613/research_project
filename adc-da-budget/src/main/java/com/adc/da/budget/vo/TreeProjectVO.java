package com.adc.da.budget.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 */
@Data
public class TreeProjectVO {

    private String id;
    private String name;
    private List<TreeTaskVO> treeTaskVOList;
}
