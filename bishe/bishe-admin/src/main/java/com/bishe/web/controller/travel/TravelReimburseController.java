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
import com.bishe.system.domain.travel.BizReimburse;
import com.bishe.system.service.ISysUserService;
import com.bishe.system.service.travel.IBizReimburseService;

@RestController
@RequestMapping("/travel/reimburse")
public class TravelReimburseController extends BaseController
{
    @Autowired
    private IBizReimburseService reimburseService;

    @Autowired
    private ISysUserService userService;

    @PreAuthorize("@ss.hasPermi('travel:reimburse:list') or @ss.hasPermi('travel:approve:query') or @ss.hasPermi('travel:payment:query')")
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

    @GetMapping("/{reimburseId}")
    public AjaxResult getInfo(@PathVariable Long reimburseId)
    {
        return success(reimburseService.selectBizReimburseById(reimburseId));
    }

    @PreAuthorize("@ss.hasPermi('travel:reimburse:add')")
    @Log(title = "报销填报", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizReimburse reimburse)
    {
        if (!fillReimburseUserByUserId(reimburse))
        {
            return error("申请人不存在");
        }
        reimburse.setCreateBy(getUsername());
        return toAjax(reimburseService.insertBizReimburse(reimburse));
    }

    @PostMapping("/my")
    public AjaxResult addMy(@RequestBody BizReimburse reimburse)
    {
        fillCurrentUser(reimburse);
        reimburse.setStatus("0");
        reimburse.setPayStatus("0");
        reimburse.setCreateBy(getUsername());
        return toAjax(reimburseService.insertBizReimburse(reimburse));
    }

    @PreAuthorize("@ss.hasPermi('travel:reimburse:edit')")
    @Log(title = "报销填报", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizReimburse reimburse)
    {
        if (reimburse.getReimburseId() == null)
        {
            return error("报销ID不能为空");
        }
        if (!fillReimburseUserByUserId(reimburse))
        {
            return error("申请人不存在");
        }
        reimburse.setUpdateBy(getUsername());
        return toAjax(reimburseService.updateBizReimburse(reimburse));
    }

    @PreAuthorize("@ss.hasPermi('travel:reimburse:submit')")
    @PostMapping("/submit/{reimburseId}")
    public AjaxResult submit(@PathVariable Long reimburseId)
    {
        BizReimburse db = reimburseService.selectBizReimburseById(reimburseId);
        if (db == null)
        {
            return error("报销单不存在");
        }
        if (!"0".equals(db.getStatus()))
        {
            return error("仅草稿状态可提交");
        }
        return toAjax(reimburseService.submitBizReimburse(reimburseId, getUsername()));
    }

    @PutMapping("/my")
    public AjaxResult editMy(@RequestBody BizReimburse reimburse)
    {
        if (reimburse.getReimburseId() == null)
        {
            return error("报销ID不能为空");
        }
        BizReimburse db = reimburseService.selectBizReimburseById(reimburse.getReimburseId());
        AjaxResult checkResult = checkMyReimbursePermission(db);
        if (checkResult != null)
        {
            return checkResult;
        }
        if (!"0".equals(db.getStatus()))
        {
            return error("仅草稿状态可编辑");
        }
        fillCurrentUser(reimburse);
        reimburse.setReimburseNo(db.getReimburseNo());
        reimburse.setStatus(db.getStatus());
        reimburse.setPayStatus(db.getPayStatus());
        reimburse.setApproverId(db.getApproverId());
        reimburse.setApproverName(db.getApproverName());
        reimburse.setApproveTime(db.getApproveTime());
        reimburse.setApproveComment(db.getApproveComment());
        reimburse.setPayTime(db.getPayTime());
        reimburse.setPayerId(db.getPayerId());
        reimburse.setPayerName(db.getPayerName());
        reimburse.setPayVoucher(db.getPayVoucher());
        reimburse.setUpdateBy(getUsername());
        return toAjax(reimburseService.updateBizReimburse(reimburse));
    }

    @PostMapping("/my/submit/{reimburseId}")
    public AjaxResult submitMy(@PathVariable Long reimburseId)
    {
        BizReimburse db = reimburseService.selectBizReimburseById(reimburseId);
        AjaxResult checkResult = checkMyReimbursePermission(db);
        if (checkResult != null)
        {
            return checkResult;
        }
        if (!"0".equals(db.getStatus()))
        {
            return error("仅草稿状态可提交");
        }
        return toAjax(reimburseService.submitBizReimburse(reimburseId, getUsername()));
    }

    @PreAuthorize("@ss.hasPermi('travel:reimburse:remove')")
    @Log(title = "报销填报", businessType = BusinessType.DELETE)
    @DeleteMapping("/{reimburseIds}")
    public AjaxResult remove(@PathVariable Long[] reimburseIds)
    {
        return toAjax(reimburseService.deleteBizReimburseByIds(reimburseIds));
    }

    @DeleteMapping("/my/{reimburseId}")
    public AjaxResult removeMy(@PathVariable Long reimburseId)
    {
        BizReimburse db = reimburseService.selectBizReimburseById(reimburseId);
        AjaxResult checkResult = checkMyReimbursePermission(db);
        if (checkResult != null)
        {
            return checkResult;
        }
        if (!"0".equals(db.getStatus()))
        {
            return error("仅草稿状态可删除");
        }
        return toAjax(reimburseService.deleteBizReimburseById(reimburseId));
    }

    private void fillCurrentUser(BizReimburse reimburse)
    {
        SysUser user = getLoginUser().getUser();
        reimburse.setUserId(getUserId());
        reimburse.setUserName(getUsername());
        reimburse.setDeptId(user.getDeptId());
        reimburse.setDeptName(user.getDept() == null ? "" : user.getDept().getDeptName());
    }

    private AjaxResult checkMyReimbursePermission(BizReimburse reimburse)
    {
        if (reimburse == null)
        {
            return error("报销单不存在");
        }
        if (reimburse.getUserId() == null || !getUserId().equals(reimburse.getUserId()))
        {
            return error("无权操作他人报销单");
        }
        return null;
    }

    private boolean fillReimburseUserByUserId(BizReimburse reimburse)
    {
        if (reimburse.getUserId() == null)
        {
            return false;
        }
        SysUser user = userService.selectUserById(reimburse.getUserId());
        if (user == null || "1".equals(user.getDelFlag()))
        {
            return false;
        }
        reimburse.setUserName(user.getUserName());
        reimburse.setDeptId(user.getDeptId());
        reimburse.setDeptName(user.getDept() == null ? "" : user.getDept().getDeptName());
        return true;
    }
}
