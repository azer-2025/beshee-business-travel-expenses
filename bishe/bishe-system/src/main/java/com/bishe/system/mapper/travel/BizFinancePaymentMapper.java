package com.bishe.system.mapper.travel;

import java.util.List;
import com.bishe.system.domain.travel.BizFinancePayment;

public interface BizFinancePaymentMapper
{
    BizFinancePayment selectBizFinancePaymentById(Long paymentId);

    List<BizFinancePayment> selectBizFinancePaymentList(BizFinancePayment payment);

    int insertBizFinancePayment(BizFinancePayment payment);

    int updateBizFinancePayment(BizFinancePayment payment);

    int deleteBizFinancePaymentById(Long paymentId);

    int deleteBizFinancePaymentByIds(Long[] paymentIds);
}
