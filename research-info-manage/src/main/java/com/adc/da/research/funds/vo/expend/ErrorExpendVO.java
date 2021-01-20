package com.adc.da.research.funds.vo.expend;

import cn.afterturn.easypoi.excel.annotation.Excel;

/***
* @Description: 导入错误列表
* @Author: yanyujie
* @Date: 2020/12/1
*/
public class ErrorExpendVO {
    @Excel(name = "项目日期",orderNum = "1",width = 20)
    private String date;
    @Excel(name = "凭证号",orderNum = "2",width = 20)
    private String voucher;
    @Excel(name = "项目档案名称",orderNum = "3",width = 20)
    private String fileName;
    @Excel(name = "科目名称",orderNum = "4",width = 20)
    private String subjectName;
    @Excel(name = "金额",orderNum = "5",width = 20)
    private String amount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
