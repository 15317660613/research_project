package com.adc.da.listener.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-07-03
 */
@Data
public class InvoiceEO {

    /**
     * 开票时间
     */
    private Date dateReceive;

    /**
     * 收款时间
     */
    private Date dateInvoice;

    /**
     * 开票金额
     */
    private BigDecimal invoiceAmount;

}
