package com.bishe.system.service.travel;

import java.util.List;
import com.bishe.system.domain.travel.BizFinancePayment;

public interface IBizFinancePaymentService
{
    BizFinancePayment selectBizFinancePaymentById(Long paymentId);

    List<BizFinancePayment> selectBizFinancePaymentList(BizFinancePayment payment);

    int insertBizFinancePayment(BizFinancePayment payment);

    int updateBizFinancePayment(BizFinancePayment payment);

    int executePayment(BizFinancePayment payment, Long operatorId, String operatorName);

    int deleteBizFinancePaymentById(Long paymentId);

    int deleteBizFinancePaymentByIds(Long[] paymentIds);
}
