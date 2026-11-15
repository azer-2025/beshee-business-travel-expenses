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
import com.bishe.common.core.domain.model.LoginUser;
import com.bishe.common.core.page.TableDataInfo;
import com.bishe.common.enums.BusinessType;
import com.bishe.common.utils.StringUtils;
import com.bishe.framework.web.service.TokenService;
import com.bishe.system.domain.travel.BizEmpProfile;
import com.bishe.system.service.ISysUserService;
import com.bishe.system.service.travel.IBizEmpProfileService;

@RestController
@RequestMapping("/travel/profile")
public class TravelProfileController extends BaseController
{
    @Autowired
    private IBizEmpProfileService profileService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @PreAuthorize("@ss.hasPermi('travel:profile:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizEmpProfile profile)
    {
        startPage();
        List<BizEmpProfile> list = profileService.selectBizEmpProfileList(profile);
        return getDataTable(list);
    }

    @GetMapping("/my")
    public AjaxResult myInfo()
    {
        Long userId = getUserId();
        BizEmpProfile profile = profileService.selectBizEmpProfileByUserId(userId);
        if (profile == null)
        {
            profile = new BizEmpProfile();
            SysUser user = getLoginUser().getUser();
            profile.setUserId(userId);
            profile.setRealName(user.getNickName());
            profile.setPhone(user.getPhonenumber());
            profile.setEmail(user.getEmail());
            profile.setAvatarUrl(user.getAvatar());
            profile.setDeptId(user.getDeptId());
            profile.setDeptName(user.getDept() == null ? "" : user.getDept().getDeptName());
            profile.setJobLevel(resolveCurrentUserJobLevel());
            profile.setStatus("0");
        }
        else
        {
            SysUser user = getLoginUser().getUser();
            if (StringUtils.isEmpty(profile.getAvatarUrl()))
            {
                profile.setAvatarUrl(user.getAvatar());
            }
            if (StringUtils.isEmpty(profile.getJobLevel()))
            {
                profile.setJobLevel(resolveCurrentUserJobLevel());
            }
        }
        return success(profile);
    }

    @PostMapping("/my")
    public AjaxResult saveMy(@RequestBody BizEmpProfile profile)
    {
        Long userId = getUserId();
        BizEmpProfile db = profileService.selectBizEmpProfileByUserId(userId);
        SysUser user = getLoginUser().getUser();
        profile.setUserId(userId);
        profile.setDeptId(user.getDeptId());
        profile.setDeptName(user.getDept() == null ? "" : user.getDept().getDeptName());
        int rows;
        if (db == null)
        {
            profile.setJobLevel(resolveCurrentUserJobLevel());
            profile.setCreateBy(getUsername());
            if (profile.getStatus() == null)
            {
                profile.setStatus("0");
            }
            rows = profileService.insertBizEmpProfile(profile);
        }
        else
        {
            profile.setJobLevel(StringUtils.isEmpty(db.getJobLevel()) ? resolveCurrentUserJobLevel() : db.getJobLevel());
            profile.setProfileId(db.getProfileId());
            profile.setUpdateBy(getUsername());
            rows = profileService.updateBizEmpProfile(profile);
        }
        if (rows > 0 && StringUtils.isNotEmpty(profile.getAvatarUrl()))
        {
            userService.updateUserAvatar(userId, profile.getAvatarUrl());
            LoginUser loginUser = getLoginUser();
            if (loginUser != null && loginUser.getUser() != null)
            {
                loginUser.getUser().setAvatar(profile.getAvatarUrl());
                tokenService.setLoginUser(loginUser);
            }
        }
        return toAjax(rows);
    }

    @PreAuthorize("@ss.hasPermi('travel:profile:query')")
    @GetMapping("/{profileId}")
    public AjaxResult getInfo(@PathVariable Long profileId)
    {
        return success(profileService.selectBizEmpProfileById(profileId));
    }

    @PreAuthorize("@ss.hasPermi('travel:profile:add')")
    @Log(title = "员工档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizEmpProfile profile)
    {
        if (StringUtils.isEmpty(profile.getJobLevel()))
        {
            profile.setJobLevel("1");
        }
        profile.setCreateBy(getUsername());
        return toAjax(profileService.insertBizEmpProfile(profile));
    }

    @PreAuthorize("@ss.hasPermi('travel:profile:edit')")
    @Log(title = "员工档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizEmpProfile profile)
    {
        if (StringUtils.isEmpty(profile.getJobLevel()))
        {
            profile.setJobLevel("1");
        }
        profile.setUpdateBy(getUsername());
        return toAjax(profileService.updateBizEmpProfile(profile));
    }

    @PreAuthorize("@ss.hasPermi('travel:profile:remove')")
    @Log(title = "员工档案", businessType = BusinessType.DELETE)
    @DeleteMapping("/{profileIds}")
    public AjaxResult remove(@PathVariable Long[] profileIds)
    {
        return toAjax(profileService.deleteBizEmpProfileByIds(profileIds));
    }

    private String resolveCurrentUserJobLevel()
    {
        String postGroup = userService.selectUserPostGroup(getUsername());
        if (StringUtils.isEmpty(postGroup))
        {
            return "1";
        }
        String text = postGroup.toLowerCase();
        if (containsAny(text, "董事长", "总经理", "ceo", "高管", "高级管理"))
        {
            return "4";
        }
        if (containsAny(text, "经理", "项目经理", "部长"))
        {
            return "3";
        }
        if (containsAny(text, "主管", "组长", "hr", "人力"))
        {
            return "2";
        }
        return "1";
    }

    private boolean containsAny(String text, String... keywords)
    {
        for (String keyword : keywords)
        {
            if (text.contains(keyword.toLowerCase()))
            {
                return true;
            }
        }
        return false;
    }
}
