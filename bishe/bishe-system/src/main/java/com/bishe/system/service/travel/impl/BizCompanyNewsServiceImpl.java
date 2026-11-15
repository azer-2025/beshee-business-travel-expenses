package com.bishe.system.service.travel.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bishe.common.utils.DateUtils;
import com.bishe.system.domain.travel.BizCompanyNews;
import com.bishe.system.mapper.travel.BizCompanyNewsMapper;
import com.bishe.system.service.travel.IBizCompanyNewsService;

@Service
public class BizCompanyNewsServiceImpl implements IBizCompanyNewsService
{
    @Autowired
    private BizCompanyNewsMapper newsMapper;

    @Override
    public BizCompanyNews selectBizCompanyNewsById(Long newsId)
    {
        return newsMapper.selectBizCompanyNewsById(newsId);
    }

    @Override
    public List<BizCompanyNews> selectBizCompanyNewsList(BizCompanyNews news)
    {
        return newsMapper.selectBizCompanyNewsList(news);
    }

    @Override
    public List<BizCompanyNews> selectPublishedBizCompanyNewsList(BizCompanyNews news)
    {
        return newsMapper.selectPublishedBizCompanyNewsList(news);
    }

    @Override
    public int insertBizCompanyNews(BizCompanyNews news)
    {
        if ("0".equals(news.getPublishStatus()) && news.getPublishTime() == null)
        {
            news.setPublishTime(DateUtils.getNowDate());
        }
        return newsMapper.insertBizCompanyNews(news);
    }

    @Override
    public int updateBizCompanyNews(BizCompanyNews news)
    {
        if ("0".equals(news.getPublishStatus()) && news.getPublishTime() == null)
        {
            news.setPublishTime(DateUtils.getNowDate());
        }
        return newsMapper.updateBizCompanyNews(news);
    }

    @Override
    public int deleteBizCompanyNewsById(Long newsId)
    {
        return newsMapper.deleteBizCompanyNewsById(newsId);
    }

    @Override
    public int deleteBizCompanyNewsByIds(Long[] newsIds)
    {
        return newsMapper.deleteBizCompanyNewsByIds(newsIds);
    }
}
