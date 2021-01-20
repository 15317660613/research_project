package com.adc.da.budget.vo;

import com.adc.da.budget.entity.ChildrenTask;
import lombok.Data;

import java.text.Collator;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class TreeTaskVO implements Comparable<TreeTaskVO> {

    private String id;

    private String value;

    private String name;

    private int type;

    private String parentId;

    private List<TreeTaskVO> treeTaskVOList;

    private List<ChildrenTask> childrenTaskList;

    /** 负责人  可能是 业务负责人 可能是项目负责人 也可能是 任务负责人*/
    private String projectLeaderId;

    //项目名
    private String projectId ;
    //业务名
    private String budgetId ;



    public TreeTaskVO(String id, String value, String name, int type, String parentId, String projectLeaderId,
            String budgetId ,String projectId) {
        this.id = id;
        this.value = value;
        this.name = name.replace("&amp;&amp;","&").replace("&amp;amp;","&").replace("&amp;","&");
        this.type = type;
        this.parentId = parentId;
        this.projectLeaderId = projectLeaderId;
        this.budgetId = budgetId ;
        this.projectId = projectId ;
    }




    @Override
    public int compareTo(TreeTaskVO o) {
        return Collator.getInstance(java.util.Locale.CHINA).compare(this.getName(), o.getName());
    }
}
