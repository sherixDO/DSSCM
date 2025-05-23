package cn.dsscm.service.impl;

import cn.dsscm.dao.NewsMapper;
import cn.dsscm.pojo.News;
import cn.dsscm.service.NewsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    private static final Logger logger = Logger.getLogger(NewsServiceImpl.class);

    @Resource
    private NewsMapper newsMapper;

    @Override
    public PageInfo<News> getNewsList(String title, Integer userId, Integer pageIndex, Integer pageSize) {
        logger.info("getNewsList param: title=" + title + ", userId=" + userId + ", pageIndex=" + pageIndex + ", pageSize=" + pageSize);
        PageHelper.startPage(pageIndex, pageSize);
        List<News> newsList = newsMapper.getAllNews(title, userId);
        return new PageInfo<>(newsList);
    }

    @Override
    public boolean add(News news) {
        try {
            return newsMapper.save(news) > 0;
        } catch (Exception e) {
            logger.error("添加新闻失败", e);
            return false;
        }
    }

    @Override
    public News getNewsById(Integer id) {
        try {
            // 由于 NewsMapper.findById 方法期望的是 Long 类型的参数，需要进行转换
            return id == null ? null : newsMapper.findById(id.longValue());
        } catch (Exception e) {
            logger.error("获取新闻失败，ID: " + id, e);
            return null;
        }
    }

    @Override
    public boolean modify(News news) {
        try {
            return newsMapper.update(news) > 0;
        } catch (Exception e) {
            logger.error("修改新闻失败", e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteNewsById(Integer id) {
        try {
            if (id == null) {
                logger.error("删除新闻失败：ID为空");
                return false;
            }
            return newsMapper.delete(id.longValue()) > 0;
        } catch (Exception e) {
            logger.error("删除新闻失败，ID: " + id, e);
            throw e;
        }
    }
}
