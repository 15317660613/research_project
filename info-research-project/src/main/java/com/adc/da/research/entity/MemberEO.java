package com.adc.da.research.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <b>功能：</b>RS_PROJECT_MEMBER MemberEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 *
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>id -> id_</li>
 * <li>researchProjectId -> research_project_id_</li>
 * <li>memberName -> member_name_</li>
 * <li>memberNameId -> member_name_id_</li>
 * <li>leaderFlag -> leader_flag_</li>
 * <li>jobLevel -> job_level_</li>
 * <li>jobLevelId -> job_level_id_</li>
 * <li>memberSex -> member_sex_</li>
 * <li>memberEducation -> member_education_</li>
 * <li>memberEducationId -> member_education_id_</li>
 * <li>memberDegree -> member_degree_</li>
 * <li>memberDegreeId -> member_degree_id_</li>
 * <li>mainTaskDescription -> main_task_description_</li>
 * <li>studyAbroadType -> study_abroad_type_</li>
 * <li>memberProfession -> member_profession_</li>
 * <li>memberCard -> member_card_</li>
 * <li>memberTelephone -> member_telephone_</li>
 * <li>memberMobilePhone -> member_mobile_phone_</li>
 * <li>memberEmail -> member_email_</li>
 * <li>extInfo1 -> ext_info_1_</li>
 * <li>extInfo2 -> ext_info_2_</li>
 * <li>extInfo3 -> ext_info_3_</li>
 * <li>extInfo4 -> ext_info_4_</li>
 * <li>extInfo5 -> ext_info_5_</li>
 * <li>extInfo6 -> ext_info_6_</li>
 */
@Setter
@Getter
public class MemberEO extends BaseEntity {

    /**
     *
     */
    private String id;

    /**
     *
     */
    private String researchProjectId;

    /**
     *
     */
    private String memberName;

    /**
     *
     */
    private String memberNameId;

    /**
     * 负责人 1 ,  非负责人 0
     */
    private Integer leaderFlag = 0;

    /**
     * 职级
     */
    private String jobLevel;

    /**
     * 职级 id 数据字典
     */
    private String jobLevelId;

    /**
     * 1为男 0为女1
     */
    private Integer memberSex = 1;

    /**
     * 学历
     */
    private String memberEducation;

    /**
     * 学历 id 数据字典
     */
    private String memberEducationId;

    /**
     * 学位
     */
    private String memberDegree;

    /**
     * 学位 id 数据字典
     */
    private String memberDegreeId;

    /**
     * 主要工作
     */
    private String mainTaskDescription;

    /**
     * 是为 1 ， 否 为 0
     */
    private Integer studyAbroadType = 0;

    /**
     *
     */
    private String memberProfession;

    /**
     *
     */
    private String memberCard;

    /**
     *
     */
    private String memberTelephone;

    /**
     *
     */
    private String memberMobilePhone;

    /**
     *
     */
    private String memberEmail;

    /**
     * 排序字段
     */
    private Integer extInfo1;

    /**
     *
     */
    private String extInfo2;

    /**
     *
     */
    private String extInfo3;

    /**
     *
     */
    private String extInfo4;

    /**
     *
     */
    private String extInfo5;

    /**
     *
     */
    private String extInfo6;

}
