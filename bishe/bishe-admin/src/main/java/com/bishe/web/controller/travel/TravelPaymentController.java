package com.bishe.web.controller.travel;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.annotation.Log;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.core.page.TableDataInfo;
import com.bishe.common.enums.BusinessType;
import com.bishe.system.domain.travel.BizFinancePayment;
import com.bishe.system.service.travel.IBizFinancePaymentService;

@RestController
@RequestMapping("/travel/payment")
public class TravelPaymentController extends BaseController
{
    @Autowired
    private IBizFinancePaymentService paymentService;

    @PreAuthorize("@ss.hasPermi('travel:payment:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizFinancePayment payment)
    {
        startPage();
        List<BizFinancePayment> list = paymentService.selectBizFinancePaymentList(payment);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('travel:payment:query')")
    @GetMapping("/{paymentId}")
    public AjaxResult getInfo(@PathVariable Long paymentId)
    {
        return success(paymentService.selectBizFinancePaymentById(paymentId));
    }

    @PreAuthorize("@ss.hasPermi('travel:payment:add')")
    @Log(title = "财务支付", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizFinancePayment payment)
    {
        payment.setCreateBy(getUsername());
        return toAjax(paymentService.insertBizFinancePayment(payment));
    }

    @PreAuthorize("@ss.hasPermi('travel:payment:edit')")
    @Log(title = "财务支付", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizFinancePayment payment)
    {
        payment.setUpdateBy(getUsername());
        return toAjax(paymentService.updateBizFinancePayment(payment));
    }

    @PreAuthorize("@ss.hasPermi('travel:payment:pay')")
    @Log(title = "财务支付", businessType = BusinessType.UPDATE)
    @PostMapping("/execute")
    public AjaxResult execute(@RequestBody BizFinancePayment payment)
    {
        return toAjax(paymentService.executePayment(payment, getUserId(), getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('travel:payment:remove')")
    @Log(title = "财务支付", businessType = BusinessType.DELETE)
    @DeleteMapping("/{paymentIds}")
    public AjaxResult remove(@PathVariable Long[] paymentIds)
    {
        return toAjax(paymentService.deleteBizFinancePaymentByIds(paymentIds));
    }
}
