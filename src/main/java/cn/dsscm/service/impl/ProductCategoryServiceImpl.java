package cn.dsscm.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.dsscm.dao.ProductCategoryMapper;
import cn.dsscm.pojo.Product;
import cn.dsscm.pojo.ProductCategory;
import cn.dsscm.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{
    @Resource
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public ProductCategory findById(long id) throws SQLException {
        return productCategoryMapper.findById(id);
    }

//	@Override
//	public PageInfo<ProductCategory> getProductCategories(String name,Long parentId,Integer currentPageNo,
//			Integer pageSize,Integer type) throws SQLException {
//		// 开启分页
//		PageHelper.startPage(currentPageNo, pageSize);
//		List<ProductCategory> list =productCategoryMapper.getProductCategories(name,parentId,type);
//		PageInfo<ProductCategory> pi = new PageInfo<ProductCategory>(list);
//		return pi;
//	}

    @Override
    public List<ProductCategory> getRootCategories(Long parentId) throws SQLException {
        return productCategoryMapper.getRootCategories(parentId);
    }

    @Override
    public int delete(long id) throws SQLException {
        return productCategoryMapper.delete(id);
    }

    @Override
    public int save(ProductCategory productCategory) throws SQLException {
        return productCategoryMapper.save(productCategory);
    }

    @Override
    public int update(ProductCategory productCategory) throws SQLException {
        return productCategoryMapper.update(productCategory);
    }

    @Override
    public List<ProductCategory> findProductCategories() {
        return productCategoryMapper.findProductCategories();
    }

}
