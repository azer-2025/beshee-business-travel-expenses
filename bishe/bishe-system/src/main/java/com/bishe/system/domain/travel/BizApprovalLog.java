package com.bishe.system.domain.travel;

import com.bishe.common.core.domain.BaseEntity;

public class BizApprovalLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long logId;
    private String bizType;
    private Long bizId;
    private String stepName;
    private String action;
    private Long approverId;
    private String approverName;
    private String approveComment;

    public Long getLogId()
    {
        return logId;
    }

    public void setLogId(Long logId)
    {
        this.logId = logId;
    }

    public String getBizType()
    {
        return bizType;
    }

    public void setBizType(String bizType)
    {
        this.bizType = bizType;
    }

    public Long getBizId()
    {
        return bizId;
    }

    public void setBizId(Long bizId)
    {
        this.bizId = bizId;
    }

    public String getStepName()
    {
        return stepName;
    }

    public void setStepName(String stepName)
    {
        this.stepName = stepName;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
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

    public String getApproveComment()
    {
        return approveComment;
    }

    public void setApproveComment(String approveComment)
    {
        this.approveComment = approveComment;
    }
}
