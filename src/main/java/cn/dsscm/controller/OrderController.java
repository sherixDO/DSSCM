package cn.dsscm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dsscm.pojo.Order;
import cn.dsscm.pojo.Product;
import cn.dsscm.service.OrderService;
import cn.dsscm.tools.Constants;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/sys/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/list.html")
    public String getOrderList(
            Model model,
            @RequestParam(value = "queryname", required = false) String queryUserName,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        // 设置页面容量
        int pageSize = Constants.pageSize;
        // 页码为空默认分第一页
        if (null == pageIndex) {
            pageIndex = 1;
        }
        PageInfo pi = null;
        List<Order> oolist= null;
        try {
            List<Order> olist = orderService.getOrders(0L, queryUserName, status);
            pi = new PageInfo();
            oolist = new ArrayList<Order>();
            System.out.println("size "+olist.size());
            System.out.println("from = "+(pageIndex-1)*pageSize);
            int pages = olist.size()%pageSize==0 ? olist.size()/pageSize : olist.size()/pageSize+1;
            int end = pageIndex<pages ? pageIndex*pageSize : (pageIndex-1)*pageSize + olist.size()%5;

            for(int i=(pageIndex-1)*pageSize; i<end ; i++){
                System.out.println("========="+i);
                oolist.add(olist.get(i));
            }
            pi.setPageNum(pageIndex);
            pi.setPrePage(pageIndex-1);
            pi.setNextPage(pageIndex+1);
            pi.setPageSize(pageSize);
            pi.setPages(pages);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] statuslist = {"请选择","待审核","审核通过","配货","卖家已发货","已收货"};
        model.addAttribute("oolist", oolist);
        model.addAttribute("pi", pi);
        model.addAttribute("queryname", queryUserName);
        model.addAttribute("status", status);
        model.addAttribute("statuslist", statuslist);
        return "orderlist";
    }

    @RequestMapping(value = "/delorder.json", method = RequestMethod.GET)
    @ResponseBody
    public Object deluser(@RequestParam String id) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(id)) {
            resultMap.put("delResult", "notexist");
        } else {
            try {
                if (orderService.deleteOrder(Integer.parseInt(id)))
                    resultMap.put("delResult", "true");
                else
                    resultMap.put("delResult", "false");
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

}
