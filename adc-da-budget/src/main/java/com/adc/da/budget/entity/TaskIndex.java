package com.adc.da.budget.entity;

import lombok.Data;

/**
 * created by chenhaidong 2018/12/5
 */
@Data
public class TaskIndex {
    private int index;
    private String[] taskNames;

    public TaskIndex(int index, String[] taskNames) {
        this.index = index;
        this.taskNames = taskNames;
    }

    public TaskIndex() {
    }
}
