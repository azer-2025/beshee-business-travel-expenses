package com.bishe.web.controller.travel;

import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.annotation.Log;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.core.page.TableDataInfo;
import com.bishe.common.enums.BusinessType;
import com.bishe.common.utils.StringUtils;
import com.bishe.system.domain.travel.BizEmpProfile;
import com.bishe.system.domain.travel.BizTravelStandard;
import com.bishe.system.service.ISysUserService;
import com.bishe.system.service.travel.IBizEmpProfileService;
import com.bishe.system.service.travel.IBizTravelStandardService;

@RestController
@RequestMapping("/travel/standard")
public class TravelStandardController extends BaseController
{
    @Autowired
    private IBizTravelStandardService standardService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private IBizEmpProfileService profileService;

    @PreAuthorize("@ss.hasPermi('travel:standard:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizTravelStandard standard)
    {
        startPage();
        List<BizTravelStandard> list = standardService.selectBizTravelStandardList(standard);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('travel:standard:query')")
    @GetMapping("/{standardId}")
    public AjaxResult getInfo(@PathVariable Long standardId)
    {
        return success(standardService.selectBizTravelStandardById(standardId));
    }

    @PreAuthorize("@ss.hasPermi('travel:standard:query')")
    @GetMapping("/match")
    public AjaxResult match(@RequestParam String regionLevel, @RequestParam String jobLevel)
    {
        BizTravelStandard standard = standardService.selectMatchedStandard(regionLevel, jobLevel, new Date());
        return success(standard);
    }

    @GetMapping("/myMatch")
    public AjaxResult myMatch(@RequestParam String regionLevel)
    {
        String jobLevel = resolveCurrentUserJobLevel();
        BizTravelStandard standard = standardService.selectMatchedStandard(regionLevel, jobLevel, new Date());
        AjaxResult ajax = success();
        ajax.put("regionLevel", regionLevel);
        ajax.put("jobLevel", jobLevel);
        ajax.put("standard", standard);
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('travel:standard:add')")
    @Log(title = "差旅标准", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizTravelStandard standard)
    {
        standard.setCreateBy(getUsername());
        return toAjax(standardService.insertBizTravelStandard(standard));
    }

    @PreAuthorize("@ss.hasPermi('travel:standard:edit')")
    @Log(title = "差旅标准", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizTravelStandard standard)
    {
        standard.setUpdateBy(getUsername());
        return toAjax(standardService.updateBizTravelStandard(standard));
    }

    @PreAuthorize("@ss.hasPermi('travel:standard:remove')")
    @Log(title = "差旅标准", businessType = BusinessType.DELETE)
    @DeleteMapping("/{standardIds}")
    public AjaxResult remove(@PathVariable Long[] standardIds)
    {
        return toAjax(standardService.deleteBizTravelStandardByIds(standardIds));
    }

    private String resolveCurrentUserJobLevel()
    {
        BizEmpProfile profile = profileService.selectBizEmpProfileByUserId(getUserId());
        if (profile != null && StringUtils.isNotEmpty(profile.getJobLevel()))
        {
            return profile.getJobLevel();
        }
        return resolveJobLevelByPost();
    }

    private String resolveJobLevelByPost()
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
