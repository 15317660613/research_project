package com.adc.da.oa.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-09-26
 */
@Getter
@Setter
public class ClaimVO {

    /**
     *
     */
    private Set<String> idSet;

    /**
     * 描述
     */
    private String remark;

    /**
     * 人力投入
     */
    private Integer personInput;

    /**
     * 负责人处于第一个位置
     */
    private List<ClaimMemberVO> claimMemberVOList;

    /*
    部门列表
     */
    private List<ClaimDeptVO>   claimDeptVOList;

}
