package com.bishe.system.service.travel.impl;

import java.util.List;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bishe.common.utils.DateUtils;
import com.bishe.common.utils.StringUtils;
import com.bishe.common.utils.uuid.IdUtils;
import com.bishe.system.domain.travel.BizApprovalLog;
import com.bishe.system.domain.travel.BizFinancePayment;
import com.bishe.system.domain.travel.BizReimburse;
import com.bishe.system.mapper.travel.BizFinancePaymentMapper;
import com.bishe.system.service.travel.IBizApprovalLogService;
import com.bishe.system.service.travel.IBizFinancePaymentService;
import com.bishe.system.service.travel.IBizReimburseService;

@Service
public class BizFinancePaymentServiceImpl implements IBizFinancePaymentService
{
    @Autowired
    private BizFinancePaymentMapper paymentMapper;

    @Autowired
    private IBizReimburseService reimburseService;

    @Autowired
    private IBizApprovalLogService approvalLogService;

    @Override
    public BizFinancePayment selectBizFinancePaymentById(Long paymentId)
    {
        return paymentMapper.selectBizFinancePaymentById(paymentId);
    }

    @Override
    public List<BizFinancePayment> selectBizFinancePaymentList(BizFinancePayment payment)
    {
        return paymentMapper.selectBizFinancePaymentList(payment);
    }

    @Override
    public int insertBizFinancePayment(BizFinancePayment payment)
    {
        if (StringUtils.isEmpty(payment.getPaymentNo()))
        {
            payment.setPaymentNo("ZF" + DateUtils.dateTimeNow() + IdUtils.fastSimpleUUID().substring(0, 6));
        }
        return paymentMapper.insertBizFinancePayment(payment);
    }

    @Override
    public int updateBizFinancePayment(BizFinancePayment payment)
    {
        return paymentMapper.updateBizFinancePayment(payment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int executePayment(BizFinancePayment payment, Long operatorId, String operatorName)
    {
        if (payment == null || payment.getReimburseId() == null)
        {
            return 0;
        }

        if (StringUtils.isEmpty(payment.getPaymentNo()))
        {
            payment.setPaymentNo("ZF" + DateUtils.dateTimeNow() + IdUtils.fastSimpleUUID().substring(0, 6));
        }
        if (StringUtils.isEmpty(payment.getPayStatus()) || (!"1".equals(payment.getPayStatus()) && !"2".equals(payment.getPayStatus())))
        {
            payment.setPayStatus("1");
        }
        if (payment.getPayTime() == null)
        {
            payment.setPayTime(DateUtils.getNowDate());
        }
        payment.setOperatorId(operatorId);
        payment.setOperatorName(operatorName);

        BizReimburse reimburse = reimburseService.selectBizReimburseById(payment.getReimburseId());
        if (reimburse == null)
        {
            return 0;
        }
        if (!"2".equals(reimburse.getStatus()))
        {
            return 0;
        }
        if ("1".equals(reimburse.getPayStatus()))
        {
            return 0;
        }

        if (payment.getPayAmount() == null || payment.getPayAmount().compareTo(BigDecimal.ZERO) <= 0)
        {
            payment.setPayAmount(reimburse.getTotalAmount());
        }
        if (StringUtils.isEmpty(payment.getPayChannel()))
        {
            payment.setPayChannel("bank");
        }
        if (StringUtils.isEmpty(payment.getPayAccount()))
        {
            payment.setPayAccount("-");
        }
        payment.setUserId(reimburse.getUserId());
        payment.setUserName(reimburse.getUserName());

        int rows = paymentMapper.insertBizFinancePayment(payment);
        if (rows == 0)
        {
            throw new IllegalStateException("支付流水写入失败");
        }

        int markRows = reimburseService.markBizReimbursePaid(payment.getReimburseId(), operatorId, operatorName,
                payment.getVoucherImage(), payment.getPayStatus());
        if (markRows == 0)
        {
            throw new IllegalStateException("报销单支付状态更新失败");
        }

        BizApprovalLog log = new BizApprovalLog();
        log.setBizType("reimburse");
        log.setBizId(payment.getReimburseId());
        log.setStepName("财务支付");
        log.setAction("1".equals(payment.getPayStatus()) ? "1" : "2");
        log.setApproverId(operatorId);
        log.setApproverName(operatorName);
        String payResult = "1".equals(payment.getPayStatus()) ? "支付成功" : "支付失败";
        log.setApproveComment(payResult + "，方式：" + payment.getPayChannel() + "，金额：" + payment.getPayAmount());
        int logRows = approvalLogService.insertBizApprovalLog(log);
        if (logRows == 0)
        {
            throw new IllegalStateException("支付日志写入失败");
        }
        return rows;
    }

    @Override
    public int deleteBizFinancePaymentById(Long paymentId)
    {
        return paymentMapper.deleteBizFinancePaymentById(paymentId);
    }

    @Override
    public int deleteBizFinancePaymentByIds(Long[] paymentIds)
    {
        return paymentMapper.deleteBizFinancePaymentByIds(paymentIds);
    }
}
