package com.bishe.system.domain.travel;

import java.math.BigDecimal;
import java.util.Date;
import com.bishe.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

public class BizFinancePayment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long paymentId;
    private String paymentNo;
    private Long reimburseId;
    private Long userId;
    private String userName;
    private BigDecimal payAmount;
    private String payChannel;
    private String payAccount;
    private String payStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;
    private String voucherImage;
    private Long operatorId;
    private String operatorName;

    public Long getPaymentId()
    {
        return paymentId;
    }

    public void setPaymentId(Long paymentId)
    {
        this.paymentId = paymentId;
    }

    public String getPaymentNo()
    {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo)
    {
        this.paymentNo = paymentNo;
    }

    public Long getReimburseId()
    {
        return reimburseId;
    }

    public void setReimburseId(Long reimburseId)
    {
        this.reimburseId = reimburseId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public BigDecimal getPayAmount()
    {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount)
    {
        this.payAmount = payAmount;
    }

    public String getPayChannel()
    {
        return payChannel;
    }

    public void setPayChannel(String payChannel)
    {
        this.payChannel = payChannel;
    }

    public String getPayAccount()
    {
        return payAccount;
    }

    public void setPayAccount(String payAccount)
    {
        this.payAccount = payAccount;
    }

    public String getPayStatus()
    {
        return payStatus;
    }

    public void setPayStatus(String payStatus)
    {
        this.payStatus = payStatus;
    }

    public Date getPayTime()
    {
        return payTime;
    }

    public void setPayTime(Date payTime)
    {
        this.payTime = payTime;
    }

    public String getVoucherImage()
    {
        return voucherImage;
    }

    public void setVoucherImage(String voucherImage)
    {
        this.voucherImage = voucherImage;
    }

    public Long getOperatorId()
    {
        return operatorId;
    }

    public void setOperatorId(Long operatorId)
    {
        this.operatorId = operatorId;
    }

    public String getOperatorName()
    {
        return operatorName;
    }

    public void setOperatorName(String operatorName)
    {
        this.operatorName = operatorName;
    }
}
