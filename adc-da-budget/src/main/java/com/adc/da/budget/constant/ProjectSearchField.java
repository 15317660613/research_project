package com.adc.da.budget.constant;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-09-30
 */
public class ProjectSearchField {

    private ProjectSearchField() {
        throw new IllegalStateException("Utility class");
    }

    public static final String CREATE_USER_ID = "createUserId";

    public static final String PROJECT_LEADER_ID = "projectLeaderId";

    public static final String PROJECT_TYPE = "projectType";

    public static final String CONTRACT_AMOUNT = "contractAmount";

    public static final String PROJECT_MEMBER_NAMES = "projectMemberNames";

    public static final String MEMBER_NAMES = "memberNames";

    public static final String DEL_FLAG = "delFlag";

    public static final String BUDGET_ID = "budgetId";

    public static final String PROJECT_LEADER = "projectLeader";

    public static final String FINISHED_STATUS = "finishedStatus";

    public static final String CREATE_TIME = "createTime";

    public static final String PROJECT_START_TIME = "projectStartTime";

    public static final String PROJECT_END_TIME = "projectEndTime";

    public static final String PROJECT_BEGIN_TIME = "projectBeginTime";

    public static final String PERSON_INPUT = "personInput";

    public static final String NAME = "name";

    public static final String CONTRACT_NO = "contractNo";

    public static final String BUDGET_DOMAIN_ID = "budgetDomainId";

    /**
     * task
     */
    public static final String PRIORITY = "priority";

    public static final String PLAN_START_TIME = "planStartTime";

    public static final String PLAN_END_TIME = "planEndTime";

    public static final String MODIFY_TIME = "modifyTime";

    public static final String TASK_STATUS = "taskStatus";

    public static final String SEARCH_BUDGET_ID = "searchBudgetId";

    public static final String NOT_LIKE = "not like";

    public static final String LIKE = "like";

    public static final String AND = "and";

    public static final String EQUAL = "=";

    public static final String NOT_EQUAL = "!=";
//    public static final String NOT_EQUAL = "ne";

//    public static final String GREATER_THAN = ">";
    public static final String GREATER_THAN = "gt";

//    public static final String GREATER_THAN_OR_EQUAL = ">=";
    public static final String GREATER_THAN_OR_EQUAL = "gte";

//    public static final String LESS_THAN = "<";
    public static final String LESS_THAN = "lt";

//    public static final String LESS_THAN_OR_EQUAL = "<=";
    public static final String LESS_THAN_OR_EQUAL = "lte";

    public static String convertOperator(String opt) {
        switch (opt) {
            case ProjectSearchField.GREATER_THAN_OR_EQUAL:
                return ">=";
            case ProjectSearchField.GREATER_THAN:
                return ">";
            case ProjectSearchField.LESS_THAN:
                return "<";
            case ProjectSearchField.LESS_THAN_OR_EQUAL:
                return "<=";
            case ProjectSearchField.NOT_EQUAL:
                return "!=";
            case ProjectSearchField.LIKE:
                return ProjectSearchField.LIKE;
            case ProjectSearchField.NOT_LIKE:
                return ProjectSearchField.NOT_LIKE;
            default:
                return "=";
        }
    }

}
