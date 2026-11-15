package com.bishe.system.domain.travel;

import java.util.Date;
import com.bishe.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

public class BizCompanyNews extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long newsId;
    private String newsTitle;
    private String newsType;
    private String coverImage;
    private String newsContent;
    private String publishStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    public Long getNewsId()
    {
        return newsId;
    }

    public void setNewsId(Long newsId)
    {
        this.newsId = newsId;
    }

    public String getNewsTitle()
    {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle)
    {
        this.newsTitle = newsTitle;
    }

    public String getNewsType()
    {
        return newsType;
    }

    public void setNewsType(String newsType)
    {
        this.newsType = newsType;
    }

    public String getCoverImage()
    {
        return coverImage;
    }

    public void setCoverImage(String coverImage)
    {
        this.coverImage = coverImage;
    }

    public String getNewsContent()
    {
        return newsContent;
    }

    public void setNewsContent(String newsContent)
    {
        this.newsContent = newsContent;
    }

    public String getPublishStatus()
    {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus)
    {
        this.publishStatus = publishStatus;
    }

    public Date getPublishTime()
    {
        return publishTime;
    }

    public void setPublishTime(Date publishTime)
    {
        this.publishTime = publishTime;
    }
}
