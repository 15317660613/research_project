package com.adc.da.budget.vo;

import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Task;
import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 */
@Data
public class TreeBudgetVO {

    private String id;
    private String name;
    private int type;
    private List<TreeTaskVO> treeTaskVOList;
    private List<TreeProjectVO> treeProjectVOList;
}
