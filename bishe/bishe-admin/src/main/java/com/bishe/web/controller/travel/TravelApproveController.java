package com.bishe.web.controller.travel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.annotation.Log;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.enums.BusinessType;
import com.bishe.system.domain.travel.BizAuditForm;
import com.bishe.system.service.travel.IBizTripApplyService;
import com.bishe.system.service.travel.IBizReimburseService;

@RestController
@RequestMapping("/travel/approve")
public class TravelApproveController extends BaseController
{
    @Autowired
    private IBizTripApplyService applyService;

    @Autowired
    private IBizReimburseService reimburseService;

    @PreAuthorize("@ss.hasPermi('travel:approve:query')")
    @GetMapping("/ping")
    public AjaxResult ping()
    {
        return success("ok");
    }

    @PreAuthorize("@ss.hasPermi('travel:approve:audit')")
    @Log(title = "业务审批", businessType = BusinessType.UPDATE)
    @PostMapping("/audit")
    public AjaxResult audit(@RequestBody BizAuditForm form)
    {
        if ("apply".equals(form.getBizType()))
        {
            return toAjax(applyService.auditBizTripApply(form.getBizId(), form.getAction(), form.getComment(), getUserId(), getUsername()));
        }
        if ("reimburse".equals(form.getBizType()))
        {
            return toAjax(reimburseService.auditBizReimburse(form.getBizId(), form.getAction(), form.getComment(), getUserId(), getUsername()));
        }
        return error("不支持的审批业务类型");
    }
}
