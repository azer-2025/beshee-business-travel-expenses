package com.bishe.web.controller.travel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.core.domain.entity.SysRole;
import com.bishe.common.core.domain.entity.SysUser;
import com.bishe.common.utils.StringUtils;
import com.bishe.system.service.ISysRoleService;
import com.bishe.system.service.ISysUserService;

@RestController
@RequestMapping("/travel/common")
public class TravelCommonController extends BaseController
{
    private static final String EMPLOYEE_ROLE_KEY = "employee";

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysUserService userService;

    @PreAuthorize("@ss.hasPermi('travel:profile:list') or @ss.hasPermi('travel:approve:query') or @ss.hasPermi('travel:apply:list') or @ss.hasPermi('travel:reimburse:list')")
    @GetMapping("/employeeUsers")
    public AjaxResult employeeUsers(@RequestParam(value = "userName", required = false) String userName)
    {
        Long roleId = getEmployeeRoleId();
        if (roleId == null)
        {
            return success(Collections.emptyList());
        }

        SysUser roleUser = new SysUser();
        roleUser.setRoleId(roleId);
        roleUser.setStatus("0");
        if (StringUtils.isNotEmpty(userName))
        {
            roleUser.setUserName(userName);
        }

        List<SysUser> allocatedUsers = userService.selectAllocatedList(roleUser);
        if (allocatedUsers == null || allocatedUsers.isEmpty())
        {
            return success(Collections.emptyList());
        }

        SysUser allQuery = new SysUser();
        allQuery.setStatus("0");
        List<SysUser> allUsers = userService.selectUserList(allQuery);
        Map<Long, SysUser> userMap = allUsers.stream().collect(Collectors.toMap(SysUser::getUserId, item -> item, (a, b) -> a));

        List<SysUser> result = new ArrayList<>(allocatedUsers.size());
        for (SysUser item : allocatedUsers)
        {
            SysUser merged = userMap.get(item.getUserId());
            if (merged != null)
            {
                item.setDeptId(merged.getDeptId());
                item.setDept(merged.getDept());
                item.setAvatar(merged.getAvatar());
                if (StringUtils.isEmpty(item.getNickName()))
                {
                    item.setNickName(merged.getNickName());
                }
            }
            result.add(item);
        }
        return success(result);
    }

    private Long getEmployeeRoleId()
    {
        SysRole roleQuery = new SysRole();
        roleQuery.setRoleKey(EMPLOYEE_ROLE_KEY);
        roleQuery.setStatus("0");
        List<SysRole> roles = roleService.selectRoleList(roleQuery);
        if (roles == null || roles.isEmpty())
        {
            return null;
        }
        return roles.get(0).getRoleId();
    }
}
