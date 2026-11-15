package com.bishe.system.domain.travel;

import java.math.BigDecimal;
import java.util.Date;
import com.bishe.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

public class BizTripApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long applyId;
    private String applyNo;
    private Long userId;
    private String userName;
    private Long deptId;
    private String deptName;
    private String destination;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private Integer tripDays;
    private String tripReason;
    private BigDecimal budgetAmount;
    private String status;
    private String approvalStep;
    private Long approverId;
    private String approverName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approveTime;
    private String approveComment;

    public Long getApplyId()
    {
        return applyId;
    }

    public void setApplyId(Long applyId)
    {
        this.applyId = applyId;
    }

    public String getApplyNo()
    {
        return applyNo;
    }

    public void setApplyNo(String applyNo)
    {
        this.applyNo = applyNo;
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

    public Integer getTripDays()
    {
        return tripDays;
    }

    public void setTripDays(Integer tripDays)
    {
        this.tripDays = tripDays;
    }

    public String getTripReason()
    {
        return tripReason;
    }

    public void setTripReason(String tripReason)
    {
        this.tripReason = tripReason;
    }

    public BigDecimal getBudgetAmount()
    {
        return budgetAmount;
    }

    public void setBudgetAmount(BigDecimal budgetAmount)
    {
        this.budgetAmount = budgetAmount;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getApprovalStep()
    {
        return approvalStep;
    }

    public void setApprovalStep(String approvalStep)
    {
        this.approvalStep = approvalStep;
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
}
