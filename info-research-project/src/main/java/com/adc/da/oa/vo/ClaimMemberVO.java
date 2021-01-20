package com.adc.da.oa.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-09-26
 */
@Getter
@Setter
public class ClaimMemberVO {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    private Integer userType = 0 ; // 0 是普通成员  1 是负责人  2是管理员

}
