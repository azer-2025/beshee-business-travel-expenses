package com.bishe.web.controller.travel;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.core.page.TableDataInfo;
import com.bishe.system.domain.travel.BizApprovalLog;
import com.bishe.system.domain.travel.BizReimburse;
import com.bishe.system.service.travel.IBizApprovalLogService;
import com.bishe.system.service.travel.IBizReimburseService;

@RestController
@RequestMapping("/travel/progress")
public class TravelProgressController extends BaseController
{
    @Autowired
    private IBizReimburseService reimburseService;

    @Autowired
    private IBizApprovalLogService approvalLogService;

    @PreAuthorize("@ss.hasPermi('travel:progress:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizReimburse reimburse)
    {
        startPage();
        List<BizReimburse> list = reimburseService.selectBizReimburseList(reimburse);
        return getDataTable(list);
    }

    @GetMapping("/myList")
    public TableDataInfo myList(BizReimburse reimburse)
    {
        startPage();
        reimburse.setUserId(getUserId());
        List<BizReimburse> list = reimburseService.selectBizReimburseList(reimburse);
        return getDataTable(list);
    }

    @GetMapping("/log/{bizType}/{bizId}")
    public AjaxResult log(@PathVariable String bizType, @PathVariable Long bizId)
    {
        BizApprovalLog query = new BizApprovalLog();
        query.setBizType(bizType);
        query.setBizId(bizId);
        return success(approvalLogService.selectBizApprovalLogList(query));
    }
}
