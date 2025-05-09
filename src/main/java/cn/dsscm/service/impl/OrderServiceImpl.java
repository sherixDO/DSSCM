package cn.dsscm.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.dsscm.dao.OrderMapper;
import cn.dsscm.pojo.Order;
import cn.dsscm.pojo.OrderDetail;
import cn.dsscm.pojo.User;
import cn.dsscm.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
    @Resource
    private OrderMapper orderMapper;

    @Override
    public List<Order> getOrders(long id,String queryUserName,Integer status) throws SQLException {
        return orderMapper.getOrders(id,queryUserName,status);
    }

    @Override
    public int saveOrder(Order order) throws SQLException {
        return orderMapper.saveOrder(order);
    }

    @Override
    public int saveOrderDetail(OrderDetail detail) throws SQLException {
        return orderMapper.saveOrderDetail(detail);
    }

    @Override
    public boolean deleteOrder(long id) throws SQLException {
        return orderMapper.deleteOrder(id)==1;
    }

    @Override
    public int deleteOrderDetails(long[] ids) throws SQLException {
        return orderMapper.deleteOrderDetails(ids);
    }

    @Override
    public int updateOrder(Order order) throws SQLException {
        return orderMapper.updateOrder(order);
    }

    @Override
    public int updateOrderDetails(OrderDetail detail) throws SQLException {
        return orderMapper.updateOrderDetails(detail);
    }

}
