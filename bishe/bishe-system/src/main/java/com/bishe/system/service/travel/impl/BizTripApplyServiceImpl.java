package com.bishe.system.service.travel.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.common.utils.DateUtils;
import com.bishe.common.utils.StringUtils;
import com.bishe.common.utils.uuid.IdUtils;
import com.bishe.system.domain.travel.BizApprovalLog;
import com.bishe.system.domain.travel.BizTripApply;
import com.bishe.system.mapper.travel.BizTripApplyMapper;
import com.bishe.system.service.travel.IBizApprovalLogService;
import com.bishe.system.service.travel.IBizTripApplyService;

@Service
public class BizTripApplyServiceImpl implements IBizTripApplyService
{
    @Autowired
    private BizTripApplyMapper applyMapper;

    @Autowired
    private IBizApprovalLogService approvalLogService;

    @Override
    public BizTripApply selectBizTripApplyById(Long applyId)
    {
        return applyMapper.selectBizTripApplyById(applyId);
    }

    @Override
    public List<BizTripApply> selectBizTripApplyList(BizTripApply apply)
    {
        return applyMapper.selectBizTripApplyList(apply);
    }

    @Override
    public int insertBizTripApply(BizTripApply apply)
    {
        if (StringUtils.isEmpty(apply.getApplyNo()))
        {
            apply.setApplyNo("SQ" + DateUtils.dateTimeNow() + IdUtils.fastSimpleUUID().substring(0, 6));
        }
        if (StringUtils.isEmpty(apply.getStatus()))
        {
            apply.setStatus("0");
        }
        fillTripDays(apply);
        return applyMapper.insertBizTripApply(apply);
    }

    @Override
    public int updateBizTripApply(BizTripApply apply)
    {
        fillTripDays(apply);
        return applyMapper.updateBizTripApply(apply);
    }

    @Override
    public int submitBizTripApply(Long applyId, String username)
    {
        BizTripApply apply = applyMapper.selectBizTripApplyById(applyId);
        if (apply == null)
        {
            return 0;
        }
        apply.setStatus("1");
        apply.setApprovalStep("待审批");
        apply.setUpdateBy(username);
        return applyMapper.updateBizTripApply(apply);
    }

    @Override
    public int auditBizTripApply(Long applyId, String action, String comment, Long approverId, String approverName)
    {
        BizTripApply apply = applyMapper.selectBizTripApplyById(applyId);
        if (apply == null)
        {
            return 0;
        }
        apply.setApproverId(approverId);
        apply.setApproverName(approverName);
        apply.setApproveTime(DateUtils.getNowDate());
        apply.setApproveComment(comment);
        apply.setUpdateBy(approverName);
        if ("1".equals(action))
        {
            apply.setStatus("2");
            apply.setApprovalStep("审批通过");
        }
        else
        {
            apply.setStatus("3");
            apply.setApprovalStep("审批驳回");
        }
        int rows = applyMapper.updateBizTripApply(apply);

        BizApprovalLog log = new BizApprovalLog();
        log.setBizType("apply");
        log.setBizId(applyId);
        log.setStepName("出差申请审批");
        log.setAction(action);
        log.setApproverId(approverId);
        log.setApproverName(approverName);
        log.setApproveComment(comment);
        approvalLogService.insertBizApprovalLog(log);
        return rows;
    }

    @Override
    public int deleteBizTripApplyById(Long applyId)
    {
        return applyMapper.deleteBizTripApplyById(applyId);
    }

    @Override
    public int deleteBizTripApplyByIds(Long[] applyIds)
    {
        return applyMapper.deleteBizTripApplyByIds(applyIds);
    }

    private void fillTripDays(BizTripApply apply)
    {
        if (apply.getStartDate() != null && apply.getEndDate() != null)
        {
            long diff = apply.getEndDate().getTime() - apply.getStartDate().getTime();
            int days = (int) TimeUnit.MILLISECONDS.toDays(diff);
            apply.setTripDays(days >= 0 ? days + 1 : 0);
        }
    }
}
