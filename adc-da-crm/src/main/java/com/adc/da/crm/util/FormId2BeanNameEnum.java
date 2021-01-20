package com.adc.da.crm.util;


import org.apache.commons.lang3.StringUtils;

/**
 * @author ZhengZhiwei
 * @description
 * @date create at 9:04 2018/12/5
 * @modified by
 */
public enum FormId2BeanNameEnum {
    CUSTOMERRECORDVO("6585961543", "CustomerRecordEO"),
    BTRAVELAPPROVALVO("5040454881", "BTravelApprovalEO"),
    PROJECTINFOEO("0624047537", "ProjectInfoEO"),
    PROJECTTARGETBOTTOMVO("6732855066", "ProjectTargetBottomVO"),
    PROJECTESTABLISHAPPROVALEO("1247577493", "ProjectEstablishApprovalEO"),
    PROJECTCLOSUREAPPROVALEO("6851928354", "ProjectClosureApprovalEO"),
    CONTRACTBASEEO("2654090261", "ContractBaseEO"),
    CONTRACTAPPROVALVO("9514007022", "ContractApprovalEO"),
    INVOICEAPPROVALVO("7884284877", "InvoiceApprovalEO"),
    SALESVLAUEEO("0432824341", "SalesVlaueEO");


    private String formId;
    private String beanName;

    FormId2BeanNameEnum(String formId, String beanName) {
        this.formId = formId;
        this.beanName = beanName;
    }

    //getBeanName
    public static String getBeanName(String formId) {
        for (FormId2BeanNameEnum c : FormId2BeanNameEnum.values()) {
            if (StringUtils.equals(c.getFormId(), formId)) {
                return c.getBeanName();
            }
        }
        return null;
    }


    public String getFormId() {
        return formId;
    }

    public String getBeanName() {
        return beanName;
    }
}
