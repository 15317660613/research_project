package com.adc.da.statistics.vo;

/**
 * 条形图
 *
 * @author Lee Kwanho 李坤澔
 *     date 2019-11-14
 **/
public interface InterfaceDataBoardGraph {
    String[] getX();

    String[] getY();

    void setX(String[] x);

    void setY(String[] y);
}
