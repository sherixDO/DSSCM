package cn.dsscm.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.PageInfo;

import cn.dsscm.pojo.Product;


public interface ProductService {
    /**
     * 根据ID查询商品
     * @param id
     * @return
     * @throws SQLException
     */
    public Product findById(Long id) throws SQLException;
    /**
     * 根据分类,名称查询商品
     * @param id
     * @return
     * @throws SQLException
     */
    public PageInfo<Product> getProducts(Long t1id,
                                         Long t2id,String name,Integer currentPageNo,
                                         Integer pageSize) throws SQLException;
    /**
     * 删除一款商品
     * @param id
     * @throws SQLException
     */
    public int delete(Long id) throws SQLException;
    /**
     * 保存一款商品
     * @param product
     * @return
     * @throws SQLException
     */
    public int save(Product product) throws SQLException;
    /**
     * 更新一款商品
     * @param product
     * @return
     * @throws SQLException
     */
    public int update(Product product) throws SQLException;

}
