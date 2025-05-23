package cn.dsscm.service;

import cn.dsscm.pojo.News;
import com.github.pagehelper.PageInfo;

public interface NewsService {
    /**
     * 获取新闻列表
     * @param title 标题搜索关键词
     * @param userId 创建者ID
     * @param pageIndex 页码
     * @param pageSize 每页记录数
     * @return 包含分页信息的新闻列表
     */
    PageInfo<News> getNewsList(String title, Integer userId, Integer pageIndex, Integer pageSize);
    
    /**
     * 添加新闻
     * @param news 新闻对象
     * @return 添加是否成功
     */
    boolean add(News news);
    
    /**
     * 根据ID获取新闻
     * @param id 新闻ID
     * @return 新闻对象
     */
    News getNewsById(Integer id);
    
    /**
     * 修改新闻
     * @param news 新闻对象
     * @return 修改是否成功
     */
    boolean modify(News news);
    
    /**
     * 根据ID删除新闻
     * @param id 新闻ID
     * @return 删除是否成功
     */
    boolean deleteNewsById(Integer id);
}
