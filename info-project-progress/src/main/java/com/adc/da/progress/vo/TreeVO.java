package com.adc.da.progress.vo;

import lombok.Data;

@Data
public class TreeVO {

    public TreeVO(String id, String name,String value, String parentId, int type) {
        this.id = id;
        this.name = name;
        this.value= value;
        this.parentId = parentId;
        this.type = type;
    }

    private String id ;

    private String name ;

    private String value ;

    private String parentId ;

    private int type;

}
