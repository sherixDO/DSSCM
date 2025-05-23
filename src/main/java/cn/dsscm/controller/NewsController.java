package cn.dsscm.controller;

import cn.dsscm.pojo.News;
import cn.dsscm.pojo.User;
import cn.dsscm.service.NewsService;
import cn.dsscm.tools.Constants;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sys/news")
public class NewsController {
    private static final Logger logger = Logger.getLogger(NewsController.class);

    @Resource
    private NewsService newsService;

    @RequestMapping("/list.html")
    public String getNewsList(Model model,
                              @RequestParam(value = "queryTitle", required = false) String queryTitle,
                              @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex) {
        logger.info("getNewsList ---- > queryTitle: " + queryTitle);
        logger.info("getNewsList ---- > pageIndex: " + pageIndex);

        PageInfo<News> pageInfo = null;
        try {
            pageInfo = newsService.getNewsList(queryTitle, null, pageIndex, 10);
        } catch (Exception e) {
            logger.error("getNewsList error", e);
        }
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("pi", pageInfo); // 添加这一行，使得rollpage.jsp可以访问到pi对象
        model.addAttribute("queryTitle", queryTitle);
        return "newslist";
    }

    @GetMapping("/add.html")
    public String addNewsPage(@ModelAttribute("news") News news) {
        return "newsadd";
    }

    @PostMapping("/addsave.html")
    public String saveNews(News news, HttpSession session) {
        User currentUser = (User) session.getAttribute(Constants.USER_SESSION);
        if (currentUser == null) {
            return "redirect:/login.html";
        }
        news.setCreatedBy(currentUser.getId());
        news.setCreationDate(new Date());
        news.setModifyBy(currentUser.getId());
        news.setModifyDate(new Date());

        try {
            if (newsService.add(news)) {
                return "redirect:/sys/news/list.html";
            }
        } catch (Exception e) {
            logger.error("saveNews error", e);
        }
        return "newsadd";
    }

    @GetMapping("/view/{id}")
    public String viewNews(@PathVariable Integer id, Model model) {
        News news = null;
        try {
            news = newsService.getNewsById(id);
        } catch (Exception e) {
            logger.error("viewNews error", e);
        }
        model.addAttribute("news", news);
        return "newsview";
    }

    @GetMapping("/modify/{id}")
    public String modifyNewsPage(@PathVariable Integer id, Model model) {
        News news = null;
        try {
            news = newsService.getNewsById(id);
        } catch (Exception e) {
            logger.error("modifyNewsPage error", e);
        }
        model.addAttribute("news", news);
        return "newsmodify";
    }

    @PostMapping("/modifysave.html")
    public String saveModifiedNews(News news, HttpSession session) {
        User currentUser = (User) session.getAttribute(Constants.USER_SESSION);
        if (currentUser == null) {
            return "redirect:/login.html";
        }
        news.setModifyBy(currentUser.getId());
        news.setModifyDate(new Date());

        try {
            if (newsService.modify(news)) {
                return "redirect:/sys/news/list.html";
            }
        } catch (Exception e) {
            logger.error("saveModifiedNews error", e);
        }
        return "newsmodify";
    }

    /**
     * 处理新闻删除请求
     */
    @RequestMapping(value = "/delete.json", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> deleteNews(@RequestParam Integer id) {
        Map<String, Object> resultMap = new HashMap<>();
        logger.info("开始处理新闻删除请求，ID: " + id);

        if (id == null) {
            logger.error("删除失败：ID为空");
            resultMap.put("delResult", "false");
            return resultMap;
        }

        try {
            boolean result = newsService.deleteNewsById(id);
            logger.info("删除结果: " + result);
            resultMap.put("delResult", result ? "true" : "false");
        } catch (Exception e) {
            logger.error("删除新闻时发生错误", e);
            resultMap.put("delResult", "false");
        }

        return resultMap;
    }
}
