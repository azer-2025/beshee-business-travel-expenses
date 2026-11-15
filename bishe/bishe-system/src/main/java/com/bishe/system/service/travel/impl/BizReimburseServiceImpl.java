package com.bishe.system.service.travel.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.common.utils.DateUtils;
import com.bishe.common.utils.StringUtils;
import com.bishe.common.utils.uuid.IdUtils;
import com.bishe.system.domain.travel.BizApprovalLog;
import com.bishe.system.domain.travel.BizReimburse;
import com.bishe.system.mapper.travel.BizReimburseMapper;
import com.bishe.system.service.travel.IBizApprovalLogService;
import com.bishe.system.service.travel.IBizReimburseService;

@Service
public class BizReimburseServiceImpl implements IBizReimburseService
{
    @Autowired
    private BizReimburseMapper reimburseMapper;

    @Autowired
    private IBizApprovalLogService approvalLogService;

    @Override
    public BizReimburse selectBizReimburseById(Long reimburseId)
    {
        return reimburseMapper.selectBizReimburseById(reimburseId);
    }

    @Override
    public List<BizReimburse> selectBizReimburseList(BizReimburse reimburse)
    {
        return reimburseMapper.selectBizReimburseList(reimburse);
    }

    @Override
    public int insertBizReimburse(BizReimburse reimburse)
    {
        if (StringUtils.isEmpty(reimburse.getReimburseNo()))
        {
            reimburse.setReimburseNo("BX" + DateUtils.dateTimeNow() + IdUtils.fastSimpleUUID().substring(0, 6));
        }
        if (StringUtils.isEmpty(reimburse.getStatus()))
        {
            reimburse.setStatus("0");
        }
        if (StringUtils.isEmpty(reimburse.getPayStatus()))
        {
            reimburse.setPayStatus("0");
        }
        fillTotalAmount(reimburse);
        return reimburseMapper.insertBizReimburse(reimburse);
    }

    @Override
    public int updateBizReimburse(BizReimburse reimburse)
    {
        fillTotalAmount(reimburse);
        return reimburseMapper.updateBizReimburse(reimburse);
    }

    @Override
    public int submitBizReimburse(Long reimburseId, String username)
    {
        BizReimburse reimburse = reimburseMapper.selectBizReimburseById(reimburseId);
        if (reimburse == null)
        {
            return 0;
        }
        reimburse.setStatus("1");
        reimburse.setUpdateBy(username);
        return reimburseMapper.updateBizReimburse(reimburse);
    }

    @Override
    public int auditBizReimburse(Long reimburseId, String action, String comment, Long approverId, String approverName)
    {
        BizReimburse reimburse = reimburseMapper.selectBizReimburseById(reimburseId);
        if (reimburse == null)
        {
            return 0;
        }
        reimburse.setApproverId(approverId);
        reimburse.setApproverName(approverName);
        reimburse.setApproveTime(DateUtils.getNowDate());
        reimburse.setApproveComment(comment);
        reimburse.setUpdateBy(approverName);
        if ("1".equals(action))
        {
            reimburse.setStatus("2");
            reimburse.setPayStatus("0");
        }
        else
        {
            reimburse.setStatus("4");
        }
        int rows = reimburseMapper.updateBizReimburse(reimburse);

        BizApprovalLog log = new BizApprovalLog();
        log.setBizType("reimburse");
        log.setBizId(reimburseId);
        log.setStepName("报销审批");
        log.setAction(action);
        log.setApproverId(approverId);
        log.setApproverName(approverName);
        log.setApproveComment(comment);
        approvalLogService.insertBizApprovalLog(log);
        return rows;
    }

    @Override
    public int markBizReimbursePaid(Long reimburseId, Long payerId, String payerName, String voucherImage, String payStatus)
    {
        BizReimburse reimburse = reimburseMapper.selectBizReimburseById(reimburseId);
        if (reimburse == null)
        {
            return 0;
        }
        reimburse.setPayerId(payerId);
        reimburse.setPayerName(payerName);
        reimburse.setPayVoucher(voucherImage);
        reimburse.setPayStatus(payStatus);
        reimburse.setPayTime(DateUtils.getNowDate());
        reimburse.setUpdateBy(payerName);
        if ("1".equals(payStatus))
        {
            reimburse.setStatus("3");
        }
        return reimburseMapper.updateBizReimburse(reimburse);
    }

    @Override
    public int deleteBizReimburseById(Long reimburseId)
    {
        return reimburseMapper.deleteBizReimburseById(reimburseId);
    }

    @Override
    public int deleteBizReimburseByIds(Long[] reimburseIds)
    {
        return reimburseMapper.deleteBizReimburseByIds(reimburseIds);
    }

    private void fillTotalAmount(BizReimburse reimburse)
    {
        BigDecimal total = BigDecimal.ZERO;
        total = total.add(reimburse.getTrafficAmount() == null ? BigDecimal.ZERO : reimburse.getTrafficAmount());
        total = total.add(reimburse.getHotelAmount() == null ? BigDecimal.ZERO : reimburse.getHotelAmount());
        total = total.add(reimburse.getMealAmount() == null ? BigDecimal.ZERO : reimburse.getMealAmount());
        total = total.add(reimburse.getOtherAmount() == null ? BigDecimal.ZERO : reimburse.getOtherAmount());
        reimburse.setTotalAmount(total);
    }
}
