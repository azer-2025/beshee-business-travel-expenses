package com.bishe.system.mapper.travel;

import java.util.List;
import com.bishe.system.domain.travel.BizEmpProfile;

public interface BizEmpProfileMapper
{
    BizEmpProfile selectBizEmpProfileById(Long profileId);

    BizEmpProfile selectBizEmpProfileByUserId(Long userId);

    List<BizEmpProfile> selectBizEmpProfileList(BizEmpProfile profile);

    int insertBizEmpProfile(BizEmpProfile profile);

    int updateBizEmpProfile(BizEmpProfile profile);

    int deleteBizEmpProfileById(Long profileId);

    int deleteBizEmpProfileByIds(Long[] profileIds);
}
