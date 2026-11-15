package com.bishe.system.mapper.travel;

import java.util.List;
import java.util.Date;
import com.bishe.system.domain.travel.BizTravelStandard;
import org.apache.ibatis.annotations.Param;

public interface BizTravelStandardMapper
{
    BizTravelStandard selectBizTravelStandardById(Long standardId);

    List<BizTravelStandard> selectBizTravelStandardList(BizTravelStandard standard);

    int insertBizTravelStandard(BizTravelStandard standard);

    int updateBizTravelStandard(BizTravelStandard standard);

    int deleteBizTravelStandardById(Long standardId);

    int deleteBizTravelStandardByIds(Long[] standardIds);

    BizTravelStandard selectMatchedStandard(@Param("regionLevel") String regionLevel,
                                            @Param("jobLevel") String jobLevel,
                                            @Param("bizDate") Date bizDate);
}
