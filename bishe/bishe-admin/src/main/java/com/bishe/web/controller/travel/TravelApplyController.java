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
import com.bishe.common.core.domain.entity.SysUser;
import com.bishe.common.core.page.TableDataInfo;
import com.bishe.common.enums.BusinessType;
import com.bishe.system.domain.travel.BizTripApply;
import com.bishe.system.service.ISysUserService;
import com.bishe.system.service.travel.IBizTripApplyService;

@RestController
@RequestMapping("/travel/apply")
public class TravelApplyController extends BaseController
{
    @Autowired
    private IBizTripApplyService applyService;

    @Autowired
    private ISysUserService userService;

    @PreAuthorize("@ss.hasPermi('travel:apply:list') or @ss.hasPermi('travel:approve:query')")
    @GetMapping("/list")
    public TableDataInfo list(BizTripApply apply)
    {
        startPage();
        List<BizTripApply> list = applyService.selectBizTripApplyList(apply);
        return getDataTable(list);
    }

    @GetMapping("/myList")
    public TableDataInfo myList(BizTripApply apply)
    {
        startPage();
        apply.setUserId(getUserId());
        List<BizTripApply> list = applyService.selectBizTripApplyList(apply);
        return getDataTable(list);
    }

    @GetMapping("/{applyId}")
    public AjaxResult getInfo(@PathVariable Long applyId)
    {
        return success(applyService.selectBizTripApplyById(applyId));
    }

    @PreAuthorize("@ss.hasPermi('travel:apply:add')")
    @Log(title = "出差申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizTripApply apply)
    {
        if (!fillApplyUserByUserId(apply))
        {
            return error("申请人不存在");
        }
        apply.setCreateBy(getUsername());
        return toAjax(applyService.insertBizTripApply(apply));
    }

    @PostMapping("/my")
    public AjaxResult addMy(@RequestBody BizTripApply apply)
    {
        fillCurrentUser(apply);
        apply.setStatus("0");
        apply.setCreateBy(getUsername());
        return toAjax(applyService.insertBizTripApply(apply));
    }

    @PreAuthorize("@ss.hasPermi('travel:apply:edit')")
    @Log(title = "出差申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizTripApply apply)
    {
        if (apply.getApplyId() == null)
        {
            return error("申请ID不能为空");
        }
        if (!fillApplyUserByUserId(apply))
        {
            return error("申请人不存在");
        }
        apply.setUpdateBy(getUsername());
        return toAjax(applyService.updateBizTripApply(apply));
    }

    @PreAuthorize("@ss.hasPermi('travel:apply:submit')")
    @PostMapping("/submit/{applyId}")
    public AjaxResult submit(@PathVariable Long applyId)
    {
        BizTripApply db = applyService.selectBizTripApplyById(applyId);
        if (db == null)
        {
            return error("申请不存在");
        }
        if (!"0".equals(db.getStatus()))
        {
            return error("仅草稿状态可提交");
        }
        return toAjax(applyService.submitBizTripApply(applyId, getUsername()));
    }

    @PutMapping("/my")
    public AjaxResult editMy(@RequestBody BizTripApply apply)
    {
        if (apply.getApplyId() == null)
        {
            return error("申请ID不能为空");
        }
        BizTripApply db = applyService.selectBizTripApplyById(apply.getApplyId());
        AjaxResult checkResult = checkMyApplyPermission(db);
        if (checkResult != null)
        {
            return checkResult;
        }
        if (!"0".equals(db.getStatus()))
        {
            return error("仅草稿状态可编辑");
        }
        fillCurrentUser(apply);
        apply.setApplyNo(db.getApplyNo());
        apply.setStatus(db.getStatus());
        apply.setApprovalStep(db.getApprovalStep());
        apply.setApproverId(db.getApproverId());
        apply.setApproverName(db.getApproverName());
        apply.setApproveTime(db.getApproveTime());
        apply.setApproveComment(db.getApproveComment());
        apply.setUpdateBy(getUsername());
        return toAjax(applyService.updateBizTripApply(apply));
    }

    @PostMapping("/my/submit/{applyId}")
    public AjaxResult submitMy(@PathVariable Long applyId)
    {
        BizTripApply db = applyService.selectBizTripApplyById(applyId);
        AjaxResult checkResult = checkMyApplyPermission(db);
        if (checkResult != null)
        {
            return checkResult;
        }
        if (!"0".equals(db.getStatus()))
        {
            return error("仅草稿状态可提交");
        }
        return toAjax(applyService.submitBizTripApply(applyId, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('travel:apply:remove')")
    @Log(title = "出差申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{applyIds}")
    public AjaxResult remove(@PathVariable Long[] applyIds)
    {
        return toAjax(applyService.deleteBizTripApplyByIds(applyIds));
    }

    @DeleteMapping("/my/{applyId}")
    public AjaxResult removeMy(@PathVariable Long applyId)
    {
        BizTripApply db = applyService.selectBizTripApplyById(applyId);
        AjaxResult checkResult = checkMyApplyPermission(db);
        if (checkResult != null)
        {
            return checkResult;
        }
        if (!"0".equals(db.getStatus()))
        {
            return error("仅草稿状态可删除");
        }
        return toAjax(applyService.deleteBizTripApplyById(applyId));
    }

    private void fillCurrentUser(BizTripApply apply)
    {
        SysUser user = getLoginUser().getUser();
        apply.setUserId(getUserId());
        apply.setUserName(getUsername());
        apply.setDeptId(user.getDeptId());
        apply.setDeptName(user.getDept() == null ? "" : user.getDept().getDeptName());
    }

    private AjaxResult checkMyApplyPermission(BizTripApply apply)
    {
        if (apply == null)
        {
            return error("申请不存在");
        }
        if (apply.getUserId() == null || !getUserId().equals(apply.getUserId()))
        {
            return error("无权操作他人申请");
        }
        return null;
    }

    private boolean fillApplyUserByUserId(BizTripApply apply)
    {
        if (apply.getUserId() == null)
        {
            return false;
        }
        SysUser user = userService.selectUserById(apply.getUserId());
        if (user == null || "1".equals(user.getDelFlag()))
        {
            return false;
        }
        apply.setUserName(user.getUserName());
        apply.setDeptId(user.getDeptId());
        apply.setDeptName(user.getDept() == null ? "" : user.getDept().getDeptName());
        return true;
    }
}
