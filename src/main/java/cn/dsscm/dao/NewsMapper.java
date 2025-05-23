package cn.dsscm.dao;

import cn.dsscm.pojo.News;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewsMapper {
    int save(News news);
    
    int update(News news);
    
    int delete(Long id);
    
    News findById(Long id);
    
    List<News> getAllNews(@Param("title") String title, @Param("userId") Integer userId);
    
    long getNewsRowCount(@Param("title") String title, @Param("userId") Integer userId);
}
