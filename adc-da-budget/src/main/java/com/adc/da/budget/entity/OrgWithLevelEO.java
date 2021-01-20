package com.adc.da.budget.entity;

import com.adc.da.sys.entity.OrgEO;
import lombok.Getter;
import lombok.Setter;

/**
 * describe:
 * 继承OrgEO，新增关于树结构的Level和Leaf属性
 *
 * @author 李坤澔
 *     date 2019-05-31
 */
@Getter
@Setter
public class OrgWithLevelEO extends OrgEO {
    /**
     * 组织所在树深度，组织根节点的Level为1，该属性由递归sql中的Level提供，根节点取的是parentId 为 0的记录
     */
    private int level;

    /**
     * 是否为叶子节点 1为叶子节点,该属性由oracle自带的函数提供 connect_by_isLeaf
     */
    private int leaf;

    private String userId;
}
