package cn.dsscm.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dsscm.pojo.Bill;
import cn.dsscm.pojo.Provider;
import cn.dsscm.pojo.User;
import cn.dsscm.service.BillService;
import cn.dsscm.service.ProviderService;
import cn.dsscm.tools.Constants;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/sys/bill")
public class BillController extends BaseController {
    private Logger logger = Logger.getLogger(BillController.class);

    @Resource
    private BillService billService;
    @Resource
    private ProviderService providerService;

    @RequestMapping(value = "/list.html")
    public String getBillList(
            Model model,
            @RequestParam(value = "queryProductName", required = false) String queryProductName,
            @RequestParam(value = "queryProviderId", required = false) String queryProviderId,
            @RequestParam(value = "queryIsPayment", required = false) String queryIsPayment,
            @RequestParam(value = "pageIndex", required = false) String pageIndex) {
        logger.info("getBillList ---- > queryProductName: " + queryProductName);
        logger.info("getUserList ---- > queryProviderId: " + queryProviderId);
        logger.info("getUserList ---- > queryIsPayment: " + queryIsPayment);
        logger.info("getUserList ---- > pageIndex: " + pageIndex);
        Integer _queryProviderId = null;
        Integer _queryIsPayment = null;
        PageInfo<Bill> bpi = null;
        List<Provider> providerList = null;
        // 设置页面容量
        int pageSize = Constants.pageSize;
        // 当前页码
        int currentPageNo = 1;

        if (queryProductName == null) {
            queryProductName = "";
        }
        if (queryProviderId != null && !queryProviderId.equals("")) {
            _queryProviderId = Integer.parseInt(queryProviderId);
        }
        if (queryIsPayment != null && !queryIsPayment.equals("")) {
            _queryIsPayment = Integer.parseInt(queryIsPayment);
        }

        if (pageIndex != null) {
            try {
                currentPageNo = Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                return "redirect:/syserror.html";
            }
        }

        try {
            bpi = billService.getBillList(queryProductName,
                    _queryProviderId, _queryIsPayment,currentPageNo,pageSize);
            providerList = providerService.getProviderList();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        model.addAttribute("pi", bpi);
        model.addAttribute("providerList", providerList);
        model.addAttribute("queryProductName", queryProductName);
        model.addAttribute("queryProviderId", queryProviderId);
        model.addAttribute("queryIsPayment", queryIsPayment);
        return "billlist";
    }

    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String addBill(@ModelAttribute("bill") Bill bill) {
        return "billadd";
    }

    @RequestMapping(value = "/addsave.html", method = RequestMethod.POST)
    public String addBillSave(Bill bill, HttpSession session) {
        bill.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION))
                .getId());
        bill.setCreationDate(new Date());
        try {
            if (billService.add(bill)) {
                return "redirect:/sys/bill/list.html";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "billadd";
    }

    @RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
    public String getBillById(@PathVariable String id, Model model,
                              HttpServletRequest request) {
        Bill bill = new Bill();
        try {
            bill = billService.getBillById(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(bill);
        return "billmodify";
    }

    @RequestMapping(value = "/modifysave.html", method = RequestMethod.POST)
    public String modifyBillSave(Bill bill, HttpSession session) {
        bill.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION))
                .getId());
        bill.setModifyDate(new Date());
        try {
            if (billService.modify(bill)) {
                return "redirect:/sys/bill/list.html";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "billmodify";
    }

    @RequestMapping(value = "/providerlist.json", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public List<Provider> getProviderList() {
        List<Provider> providerList = null;
        try {
            providerList = providerService.getProviderList();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.debug("providerList size: " + providerList.size());
        return providerList;
    }

    @RequestMapping(value = "/delbill.json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> delBill(@RequestParam String id) { // 返回类型改为 Map<String, String>
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (id == null || id.isEmpty()) {
            resultMap.put("delResult", "notexist");
        } else {
            try {
                if (billService.deleteBillById(Integer.parseInt(id)))
                    resultMap.put("delResult", "true");
                else
                    resultMap.put("delResult", "false");
            } catch (NumberFormatException e) {
                logger.error("Error parsing bill ID for deletion: " + id, e); // 记录错误
                resultMap.put("delResult", "error_parse");
            } catch (Exception e) {
                logger.error("Error deleting bill with ID: " + id, e); // 记录错误
                resultMap.put("delResult", "error_db");
            }
        }
        return resultMap;
    }
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable String id, Model model,
                       HttpServletRequest request) {
        logger.debug("view id===================== " + id);
        Bill bill = new Bill();
        try {
            bill = billService.getBillById(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        model.addAttribute(bill);
        return "billview";
    }

}
