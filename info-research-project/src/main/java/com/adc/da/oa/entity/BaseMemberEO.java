package com.adc.da.oa.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;
import java.util.Map;

/**
 * describe:
 * 用于project存储
 *
 * @author 李坤澔
 *     date 2019-09-26
 */
@Getter
@Setter
//@Builder
public class BaseMemberEO {
    /**
     * "mapList": [
     * {
     * "name": "马啸",
     * "id": "MUYD6KHFWE"
     * }]
     */
    private List<Map<String, String>> mapList;

    private List<Map<String,String>> userIdDeptNameMapList;

    /**
     * 负责人Id
     */
    private String projectLeaderId;

    /**
     * 负责人名称
     */
    private String projectLeader;

    private String projectAdminId ;
    private String projectAdminName ;


    /**
     * "projectMemberNames": "马啸,赵津,赵峰",
     */
    private String projectMemberNames;

    /**
     * "memberNames": [
     * "马啸",
     * "赵津",
     * "赵峰"
     * ],
     */
    private String[] memberNames;

    /**
     * "projectMemberIds": [
     * "MUYD6KHFWE",
     * "ZR45VBWBGU",
     * "WCHZ395XUU"
     * ],
     */
    private String[] projectMemberIds;
}
