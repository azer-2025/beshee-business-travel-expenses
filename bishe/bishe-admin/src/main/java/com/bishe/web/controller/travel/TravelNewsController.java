package com.bishe.web.controller.travel;

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
import org.springframework.web.bind.annotation.RestController;
import com.bishe.common.annotation.Log;
import com.bishe.common.core.controller.BaseController;
import com.bishe.common.core.domain.AjaxResult;
import com.bishe.common.core.page.TableDataInfo;
import com.bishe.common.enums.BusinessType;
import com.bishe.system.domain.travel.BizCompanyNews;
import com.bishe.system.service.travel.IBizCompanyNewsService;

@RestController
@RequestMapping("/travel/news")
public class TravelNewsController extends BaseController
{
    @Autowired
    private IBizCompanyNewsService newsService;

    @PreAuthorize("@ss.hasPermi('travel:news:list')")
    @GetMapping("/list")
    public TableDataInfo list(BizCompanyNews news)
    {
        startPage();
        List<BizCompanyNews> list = newsService.selectBizCompanyNewsList(news);
        return getDataTable(list);
    }

    @GetMapping("/published/list")
    public TableDataInfo publishedList(BizCompanyNews news)
    {
        startPage();
        List<BizCompanyNews> list = newsService.selectPublishedBizCompanyNewsList(news);
        return getDataTable(list);
    }

    @GetMapping("/{newsId}")
    public AjaxResult getInfo(@PathVariable Long newsId)
    {
        return success(newsService.selectBizCompanyNewsById(newsId));
    }

    @PreAuthorize("@ss.hasPermi('travel:news:add')")
    @Log(title = "公司资讯", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BizCompanyNews news)
    {
        news.setCreateBy(getUsername());
        return toAjax(newsService.insertBizCompanyNews(news));
    }

    @PreAuthorize("@ss.hasPermi('travel:news:edit')")
    @Log(title = "公司资讯", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BizCompanyNews news)
    {
        news.setUpdateBy(getUsername());
        return toAjax(newsService.updateBizCompanyNews(news));
    }

    @PreAuthorize("@ss.hasPermi('travel:news:remove')")
    @Log(title = "公司资讯", businessType = BusinessType.DELETE)
    @DeleteMapping("/{newsIds}")
    public AjaxResult remove(@PathVariable Long[] newsIds)
    {
        return toAjax(newsService.deleteBizCompanyNewsByIds(newsIds));
    }
}
