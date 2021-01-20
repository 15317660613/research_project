package com.adc.da.research.project.enumcase;

public enum RoleEnum {
    ADMIN("ZVXUGCP56D","管理员"),RSADMIN("B7QCZTEVX5","科研管理员"),SUPERADMIN("YWYC7ME75K","超级管理员");

    private String name ;
    private String id ;

    private RoleEnum( String id , String name ){
        this.name = name ;
        this.id = id ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
