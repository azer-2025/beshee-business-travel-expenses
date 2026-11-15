package com.bishe.system.domain.travel;

import java.math.BigDecimal;
import java.util.Date;
import com.bishe.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

public class BizReimburse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long reimburseId;
    private String reimburseNo;
    private Long applyId;
    private Long userId;
    private String userName;
    private Long deptId;
    private String deptName;
    private String destination;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private BigDecimal trafficAmount;
    private BigDecimal hotelAmount;
    private BigDecimal mealAmount;
    private BigDecimal otherAmount;
    private BigDecimal totalAmount;
    private String invoiceImages;
    private String expenseDesc;
    private String status;
    private Long approverId;
    private String approverName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTime;
    private String approveComment;
    private String payStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;
    private Long payerId;
    private String payerName;
    private String payVoucher;

    public Long getReimburseId()
    {
        return reimburseId;
    }

    public void setReimburseId(Long reimburseId)
    {
        this.reimburseId = reimburseId;
    }

    public String getReimburseNo()
    {
        return reimburseNo;
    }

    public void setReimburseNo(String reimburseNo)
    {
        this.reimburseNo = reimburseNo;
    }

    public Long getApplyId()
    {
        return applyId;
    }

    public void setApplyId(Long applyId)
    {
        this.applyId = applyId;
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

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getDestination()
    {
        return destination;
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public BigDecimal getTrafficAmount()
    {
        return trafficAmount;
    }

    public void setTrafficAmount(BigDecimal trafficAmount)
    {
        this.trafficAmount = trafficAmount;
    }

    public BigDecimal getHotelAmount()
    {
        return hotelAmount;
    }

    public void setHotelAmount(BigDecimal hotelAmount)
    {
        this.hotelAmount = hotelAmount;
    }

    public BigDecimal getMealAmount()
    {
        return mealAmount;
    }

    public void setMealAmount(BigDecimal mealAmount)
    {
        this.mealAmount = mealAmount;
    }

    public BigDecimal getOtherAmount()
    {
        return otherAmount;
    }

    public void setOtherAmount(BigDecimal otherAmount)
    {
        this.otherAmount = otherAmount;
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public String getInvoiceImages()
    {
        return invoiceImages;
    }

    public void setInvoiceImages(String invoiceImages)
    {
        this.invoiceImages = invoiceImages;
    }

    public String getExpenseDesc()
    {
        return expenseDesc;
    }

    public void setExpenseDesc(String expenseDesc)
    {
        this.expenseDesc = expenseDesc;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Long getApproverId()
    {
        return approverId;
    }

    public void setApproverId(Long approverId)
    {
        this.approverId = approverId;
    }

    public String getApproverName()
    {
        return approverName;
    }

    public void setApproverName(String approverName)
    {
        this.approverName = approverName;
    }

    public Date getApproveTime()
    {
        return approveTime;
    }

    public void setApproveTime(Date approveTime)
    {
        this.approveTime = approveTime;
    }

    public String getApproveComment()
    {
        return approveComment;
    }

    public void setApproveComment(String approveComment)
    {
        this.approveComment = approveComment;
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

    public Long getPayerId()
    {
        return payerId;
    }

    public void setPayerId(Long payerId)
    {
        this.payerId = payerId;
    }

    public String getPayerName()
    {
        return payerName;
    }

    public void setPayerName(String payerName)
    {
        this.payerName = payerName;
    }

    public String getPayVoucher()
    {
        return payVoucher;
    }

    public void setPayVoucher(String payVoucher)
    {
        this.payVoucher = payVoucher;
    }
}
