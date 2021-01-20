package com.adc.da.research.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <b>功能：</b>RS_PROJECT_DOC ProjectDocEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 * /**
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>id -> id_</li>
 * <li>researchProjectId -> research_project_id_</li>
 * <li>docType -> doc_type_</li>
 * <li>fileId -> file_id_</li>
 * <li>extInfo2 -> ext_info_2_</li>
 * <li>extInfo3 -> ext_info_3_</li>
 * <li>extInfo4 -> ext_info_4_</li>
 * <li>extInfo5 -> ext_info_5_</li>
 * <li>extInfo6 -> ext_info_6_</li>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectDocEO extends BaseEntity {

    /**
     * 主键
     */
    private String id;

    /**
     * 项目id
     */
    private String researchProjectId;

    /**
     * 文件类型（对应的页面）
     */
    private String docType;

    /**
     * 文件id
     */
    private String fileId;

    /**
     * 预留
     */
    private String extInfo2;

    /**
     * 预留
     */
    private String extInfo3;

    /**
     * 预留
     */
    private String extInfo4;

    /**
     * 预留
     */
    private String extInfo5;

    /**
     * 预留
     */
    private String extInfo6;

}
