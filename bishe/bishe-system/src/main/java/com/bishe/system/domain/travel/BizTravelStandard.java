package com.bishe.system.domain.travel;

import java.math.BigDecimal;
import java.util.Date;
import com.bishe.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

public class BizTravelStandard extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long standardId;
    private String regionLevel;
    private String jobLevel;
    private BigDecimal trafficLimit;
    private BigDecimal hotelLimit;
    private BigDecimal mealLimit;
    private BigDecimal otherLimit;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectiveEnd;

    public Long getStandardId()
    {
        return standardId;
    }

    public void setStandardId(Long standardId)
    {
        this.standardId = standardId;
    }

    public String getRegionLevel()
    {
        return regionLevel;
    }

    public void setRegionLevel(String regionLevel)
    {
        this.regionLevel = regionLevel;
    }

    public String getJobLevel()
    {
        return jobLevel;
    }

    public void setJobLevel(String jobLevel)
    {
        this.jobLevel = jobLevel;
    }

    public BigDecimal getTrafficLimit()
    {
        return trafficLimit;
    }

    public void setTrafficLimit(BigDecimal trafficLimit)
    {
        this.trafficLimit = trafficLimit;
    }

    public BigDecimal getHotelLimit()
    {
        return hotelLimit;
    }

    public void setHotelLimit(BigDecimal hotelLimit)
    {
        this.hotelLimit = hotelLimit;
    }

    public BigDecimal getMealLimit()
    {
        return mealLimit;
    }

    public void setMealLimit(BigDecimal mealLimit)
    {
        this.mealLimit = mealLimit;
    }

    public BigDecimal getOtherLimit()
    {
        return otherLimit;
    }

    public void setOtherLimit(BigDecimal otherLimit)
    {
        this.otherLimit = otherLimit;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Date getEffectiveStart()
    {
        return effectiveStart;
    }

    public void setEffectiveStart(Date effectiveStart)
    {
        this.effectiveStart = effectiveStart;
    }

    public Date getEffectiveEnd()
    {
        return effectiveEnd;
    }

    public void setEffectiveEnd(Date effectiveEnd)
    {
        this.effectiveEnd = effectiveEnd;
    }
}
