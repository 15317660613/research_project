package com.adc.da.budget.constant;

/**
 * 导入提示信息常量类
 *
 * @author qichunxu
 */
public class Import {
    public static final String SUCCESS = "导入成功！";
    
    public static final String FAILURE = "导入的项目参与人员有不合法的人员！";
    
    public static final String FAILURE_DATE_REASON = "导入的项目日期不合法！";
    public static final String FAILURE_ORG_REASON = "导入的组织不合法！";
    public static final String FAILURE_BUSINESS_TYPE_REASON = "导入的业务类型！";
    
    public static final String FAILURE_NAME_REASON = "导入的项目名称不合法！";
    public static final String FAILURE_BUSINESS_NAME_REASON = "导入的所属业务不合法！";
    public static final String FAILURE_LEADER_REASON = "导入的负责人名不合法！";
    public static final String FAILURE_MEMBERS_REASON = "导入的组员人名不合法！";
    
    public static final String WARN_LEADER_REASON = "导入成功,负责人存在重名！";

    public static final String FAILURE_PROJECT_REASON = "当前导入的项目已存在！";

    private Import() {
    }
}
