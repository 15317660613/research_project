package com.adc.da.research.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <b>功能：</b>RS_PROJECT_MENU MenuEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>id -> id_</li>
 * <li>name -> name_</li>
 * <li>parentId -> parent_id_</li>
 * <li>href -> href_</li>
 * <li>icon -> icon_</li>
 * <li>remarks -> remarks_</li>
 * <li>sort -> sort_</li>
 * <li>delFlag -> del_flag_</li>
 * <li>extInfo1 -> ext_info_1_</li>
 * <li>extInfo2 -> ext_info_2_</li>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MenuEO extends BaseEntity {

    private String id;

    private String name;

    private String parentId;

    private String href;

    private String icon;

    private String remarks;

    /**
     * 排序
     */
    private String sort;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 是否位叶子，1为是 ，0为否
     */
    private boolean leafFlag;

    /**
     * 空闲字段，预留删除
     */
    private boolean delFlag;

    /**
     * 预留
     */
    private String extInfo1;

    /**
     * 预留
     */
    private String extInfo2;

}
