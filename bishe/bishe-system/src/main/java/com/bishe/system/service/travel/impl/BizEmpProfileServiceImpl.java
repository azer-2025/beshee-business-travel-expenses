package com.bishe.system.service.travel.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.system.domain.travel.BizEmpProfile;
import com.bishe.system.mapper.travel.BizEmpProfileMapper;
import com.bishe.system.service.travel.IBizEmpProfileService;

@Service
public class BizEmpProfileServiceImpl implements IBizEmpProfileService
{
    @Autowired
    private BizEmpProfileMapper profileMapper;

    @Override
    public BizEmpProfile selectBizEmpProfileById(Long profileId)
    {
        return profileMapper.selectBizEmpProfileById(profileId);
    }

    @Override
    public BizEmpProfile selectBizEmpProfileByUserId(Long userId)
    {
        return profileMapper.selectBizEmpProfileByUserId(userId);
    }

    @Override
    public List<BizEmpProfile> selectBizEmpProfileList(BizEmpProfile profile)
    {
        return profileMapper.selectBizEmpProfileList(profile);
    }

    @Override
    public int insertBizEmpProfile(BizEmpProfile profile)
    {
        return profileMapper.insertBizEmpProfile(profile);
    }

    @Override
    public int updateBizEmpProfile(BizEmpProfile profile)
    {
        return profileMapper.updateBizEmpProfile(profile);
    }

    @Override
    public int deleteBizEmpProfileById(Long profileId)
    {
        return profileMapper.deleteBizEmpProfileById(profileId);
    }

    @Override
    public int deleteBizEmpProfileByIds(Long[] profileIds)
    {
        return profileMapper.deleteBizEmpProfileByIds(profileIds);
    }
}
