package com.adc.da.budget.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 信息化模块树
 */
@Data
public class TreeNode {
    /**
     * 节点id
     */
    private String id;

    /**
     * 节点子id集
     */
    private List<TreeNode> child;

    public TreeNode(String id) {

        this.id = id;
        this.child = new ArrayList<>();
    }
}
