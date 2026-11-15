package com.bishe.system.mapper.travel;

import java.util.List;
import com.bishe.system.domain.travel.BizCompanyNews;

public interface BizCompanyNewsMapper
{
    BizCompanyNews selectBizCompanyNewsById(Long newsId);

    List<BizCompanyNews> selectBizCompanyNewsList(BizCompanyNews news);

    List<BizCompanyNews> selectPublishedBizCompanyNewsList(BizCompanyNews news);

    int insertBizCompanyNews(BizCompanyNews news);

    int updateBizCompanyNews(BizCompanyNews news);

    int deleteBizCompanyNewsById(Long newsId);

    int deleteBizCompanyNewsByIds(Long[] newsIds);
}
