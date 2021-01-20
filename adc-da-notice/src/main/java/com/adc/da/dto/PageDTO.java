package com.adc.da.dto;

import lombok.Data;

import java.util.List;

/**
 * 分页DTO
 * created by chenhaidong 2018/11/29
 */
@Data
public class PageDTO {
    /**
     * 总条数
     */
    private long count;
    /**
     * 数据列表
     */
    private List dataList;
    /**
     * 页数
     */
    private int page;
    /**
     * 每页条数
     */
    private int size;

    public PageDTO() {
    }

    public PageDTO(long count, List dataList, int page, int size) {
        this.count = count;
        this.dataList = dataList;
        this.page = page;
        this.size = size;
    }
}
