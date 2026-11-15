package com.bishe.system.service.travel;

import java.util.List;
import java.util.Date;
import com.bishe.system.domain.travel.BizTravelStandard;

public interface IBizTravelStandardService
{
    BizTravelStandard selectBizTravelStandardById(Long standardId);

    List<BizTravelStandard> selectBizTravelStandardList(BizTravelStandard standard);

    int insertBizTravelStandard(BizTravelStandard standard);

    int updateBizTravelStandard(BizTravelStandard standard);

    int deleteBizTravelStandardById(Long standardId);

    int deleteBizTravelStandardByIds(Long[] standardIds);

    BizTravelStandard selectMatchedStandard(String regionLevel, String jobLevel, Date bizDate);
}
