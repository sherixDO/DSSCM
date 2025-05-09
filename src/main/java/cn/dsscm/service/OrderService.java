package cn.dsscm.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.PageInfo;

import cn.dsscm.pojo.Order;
import cn.dsscm.pojo.OrderDetail;
import cn.dsscm.pojo.Product;

public interface OrderService{
    /**
     * 获取订单及其下属的详情记录
     * @param condition
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws SQLException
     */
    public List<Order> getOrders(long id,String queryUserName,Integer status) throws SQLException;
    /**
     * 保存订单
     * @param order
     * @throws SQLException
     */
    public int saveOrder(Order order) throws SQLException;
    /**
     * 保存订单详情
     * @param detail
     * @param orderId
     * @throws SQLException
     */
    public int saveOrderDetail(OrderDetail detail) throws SQLException;
    /**
     * 删除订单
     * @param id
     * @throws SQLException
     */
    public boolean deleteOrder(long id) throws SQLException;
    /**
     * 删除订单详情
     * @param id
     * @throws SQLException
     */
    public int deleteOrderDetails(long[] ids) throws SQLException;

    /**
     * 更新订单
     * @param order
     * @throws SQLException
     */
    public int updateOrder(Order order) throws SQLException;
    /**
     * 更新订单详情
     * @param order
     * @throws SQLException
     */
    public int updateOrderDetails(OrderDetail detail) throws SQLException;

}
