package cn.dsscm.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.dsscm.service.UserRoleService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.dsscm.pojo.Role;
import cn.dsscm.pojo.User;
import cn.dsscm.service.RoleService;
import cn.dsscm.service.UserService;
import cn.dsscm.tools.Constants;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/sys/user")
public class UserController extends BaseController {
    private Logger logger = Logger.getLogger(UserController.class);

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;

    @RequestMapping(value = "/list.html")
    public String getUserList(
            Model model,
            @RequestParam(value = "queryname", required = false) String queryUserName,
            @RequestParam(value = "queryUserRole", required = false) Integer queryUserRole,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex) {
        logger.info("getUserList ---- > queryUserName: " + queryUserName);
        logger.info("getUserList ---- > queryUserRole: " + queryUserRole);
        logger.info("getUserList ---- > pageIndex: " + pageIndex);
        PageInfo<User> upi = null;
        List<Role> roleList = null;
        // 设置页面容量
        int pageSize = Constants.pageSize;
        // 页码为空默认分第一页
        if (null == pageIndex) {
            pageIndex = 1;
        }
        if (queryUserName == null) {
            queryUserName = "";
        }
        try {
            upi = userService.getUserList(queryUserName, queryUserRole,
                    pageIndex, pageSize);
            roleList = roleService.getRoleList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("pi", upi);
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryUserName);
        model.addAttribute("queryUserRole", queryUserRole);
        return "userlist";
    }

    @RequestMapping(value = "/add.html", method = RequestMethod.GET)
    public String addUser(@ModelAttribute("user") User user) {
        return "useradd";
    }

    // 文件上传
    @RequestMapping(value = "/addsave.html", method = RequestMethod.POST)
    public String addUserSave(
            User user,
            HttpSession session,
            HttpServletRequest request,
            @RequestParam(value = "attachs", required = false) MultipartFile[] attachs) {
        System.out.println("--------------进入添加用户方法---------");
        String idPicPath = null;
        String workPicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = request.getSession().getServletContext()
                .getRealPath("statics" + File.separator + "uploadfiles");
        logger.info("uploadFile path ============== > " + path);
        for (int i = 0; i < attachs.length; i++) {
            MultipartFile attach = attachs[i];
            if (!attach.isEmpty()) {
                if (i == 0) {
                    errorInfo = "uploadFileError";
                } else if (i == 1) {
                    errorInfo = "uploadWpError";
                }
                String oldFileName = attach.getOriginalFilename();// 原文件名
                logger.info("uploadFile oldFileName ============== > "
                        + oldFileName);
                String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
                logger.debug("uploadFile prefix============> " + prefix);
                int filesize = 500000;
                logger.debug("uploadFile size============> " + attach.getSize());
                if (attach.getSize() > filesize) {// 上传大小不得超过 500k
                    request.setAttribute(errorInfo, " * 上传大小不得超过 500k");
                    flag = false;
                } else if (prefix.equalsIgnoreCase("jpg")
                        || prefix.equalsIgnoreCase("png")
                        || prefix.equalsIgnoreCase("jpeg")
                        || prefix.equalsIgnoreCase("pneg")) {// 上传图片格式不正确
                    String fileName = System.currentTimeMillis()
                            + RandomUtils.nextInt(1000000) + "_Personal.jpg";
                    logger.debug("new fileName======== " + attach.getName());
                    File targetFile = new File(path, fileName);
                    if (!targetFile.exists()) {
                        targetFile.mkdirs();
                    }
                    // 保存
                    try {
                        attach.transferTo(targetFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                        request.setAttribute(errorInfo, " * 上传失败！");
                        flag = false;
                    }
                    if (i == 0) {
                        idPicPath = path + File.separator + fileName;
                    } else if (i == 1) {
                        workPicPath = path + File.separator + fileName;
                    }
                    logger.debug("idPicPath: " + idPicPath);
                    logger.debug("workPicPath: " + workPicPath);

                } else {
                    request.setAttribute(errorInfo, " * 上传图片格式不正确");
                    flag = false;
                }
            }
        }
        if (flag) {
            user.setCreatedBy(((User) session
                    .getAttribute(Constants.USER_SESSION)).getId());
            user.setCreationDate(new Date());
            user.setImgPath(idPicPath);
            try {
                if (userService.add(user)) {
                    return "redirect:/sys/user/list.html";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "useradd";
    }

    @RequestMapping(value = "/modify/{id}", method = RequestMethod.GET)
    public String getUserById(@PathVariable String id, Model model,
                              HttpServletRequest request) {
        User user = new User();
        try {
            user = userService.getUserById(Integer.parseInt(id));
            if (user.getImgPath() != null && !"".equals(user.getImgPath())) {
                String[] paths = user.getImgPath().split("\\" + File.separator);
                logger.debug("view picPath paths[paths.length-1]============ "
                        + paths[paths.length - 1]);
                user.setImgPath(request.getContextPath()
                        + "/statics/uploadfiles/" + paths[paths.length - 1]);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(user);
        return "usermodify";
    }

    @RequestMapping(value = "/modifysave.html", method = RequestMethod.POST)
    public String modifyUserSave(
            User user,
            HttpSession session,
            HttpServletRequest request,
            @RequestParam(value = "attachs", required = false) MultipartFile[] attachs) {
        logger.debug("modifyUserSave id===================== " + user.getId());
        String idPicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = request.getSession().getServletContext()
                .getRealPath("statics" + File.separator + "uploadfiles");
        logger.info("uploadFile path ============== > " + path);
        if (attachs != null) {
            for (int i = 0; i < attachs.length; i++) {
                MultipartFile attach = attachs[i];
                if (!attach.isEmpty()) {
                    if (i == 0) {
                        errorInfo = "uploadFileError";
                    } else if (i == 1) {
                        errorInfo = "uploadWpError";
                    }
                    String oldFileName = attach.getOriginalFilename();// 原文件名
                    logger.info("uploadFile oldFileName ============== > "
                            + oldFileName);
                    String prefix = FilenameUtils.getExtension(oldFileName);// 原文件后缀
                    logger.debug("uploadFile prefix============> " + prefix);
                    int filesize = 500000;
                    logger.debug("uploadFile size============> "
                            + attach.getSize());
                    if (attach.getSize() > filesize) {// 上传大小不得超过 500k
                        request.setAttribute(errorInfo, " * 上传大小不得超过 500k");
                        flag = false;
                    } else if (prefix.equalsIgnoreCase("jpg")
                            || prefix.equalsIgnoreCase("png")
                            || prefix.equalsIgnoreCase("jpeg")
                            || prefix.equalsIgnoreCase("pneg")) {// 上传图片格式不正确
                        String fileName = System.currentTimeMillis()
                                + RandomUtils.nextInt(1000000)
                                + "_Personal.jpg";
                        logger.debug("new fileName======== " + attach.getName());
                        File targetFile = new File(path, fileName);
                        if (!targetFile.exists()) {
                            targetFile.mkdirs();
                        }
                        // 保存
                        try {
                            attach.transferTo(targetFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                            request.setAttribute(errorInfo, " * 上传失败！");
                            flag = false;
                        }
                        if (i == 0) {
                            idPicPath = path + File.separator + fileName;
                        }
                        logger.debug("idPicPath: " + idPicPath);

                    } else {
                        request.setAttribute(errorInfo, " * 上传图片格式不正确");
                        flag = false;
                    }
                }
            }
        }
        if (flag) {
            user.setModifyBy(((User) session
                    .getAttribute(Constants.USER_SESSION)).getId());
            user.setModifyDate(new Date());
            user.setImgPath(idPicPath);
            try {
                if (userService.modify(user)) {
                    return "redirect:/sys/user/list.html";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "usermodify";
    }

    @RequestMapping(value = "/ucexist.json")
    @ResponseBody
    public Object userCodeIsExit(@RequestParam String userCode) {
        logger.debug("userCodeIsExit userCode===================== " + userCode);
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(userCode)) {
            resultMap.put("userCode", "exist");
        } else {
            User user = null;
            try {
                user = userService.selectUserCodeExist(userCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null != user)
                resultMap.put("userCode", "exist");
            else
                resultMap.put("userCode", "noexist");
        }
        return JSONArray.toJSONString(resultMap);
    }

    @RequestMapping(value = "/rolelist.json", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
    @ResponseBody
    public List<Role> getRoleList() {
        List<Role> roleList = null;
        try {
            roleList = roleService.getRoleList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("roleList size: " + roleList.size());
        return roleList;
    }

    @RequestMapping(value = "/deluser.json", method = RequestMethod.GET)
    @ResponseBody
    public Object deluser(@RequestParam String id) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(id)) {
            resultMap.put("delResult", "notexist");
        } else {
            try {
                if (userService.deleteUserById(Integer.parseInt(id)))
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

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable String id, Model model,
                       HttpServletRequest request) {
        logger.debug("view id===================== " + id);
        User user = new User();
        try {
            user = userService.getUserById(Integer.parseInt(id));
            if (user.getImgPath() != null && !"".equals(user.getImgPath())) {
                String[] paths = user.getImgPath().split("\\" + File.separator);
                logger.debug("view picPath paths[paths.length-1]============ "
                        + paths[paths.length - 1]);
                user.setImgPath(request.getContextPath()
                        + "/statics/uploadfiles/" + paths[paths.length - 1]);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(user);
        return "userview";
    }

    @RequestMapping(value = "/pwdmodify.html", method = RequestMethod.GET)
    public String pwdModify(HttpSession session) {
        if (session.getAttribute(Constants.USER_SESSION) == null) {
            return "redirect:/user/login.html";
        }
        return "pwdmodify";
    }

    @RequestMapping(value = "/pwdmodify.json", method = RequestMethod.POST)
    @ResponseBody
    public Object getPwdByUserId(@RequestParam String oldpassword,
                                 HttpSession session) {
        logger.debug("getPwdByUserId oldpassword ===================== "
                + oldpassword);
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (null == session.getAttribute(Constants.USER_SESSION)) {// session过期
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {// 旧密码输入为空
            resultMap.put("result", "error");
        } else {
            String sessionPwd = ((User) session
                    .getAttribute(Constants.USER_SESSION)).getUserPassword();
            if (oldpassword.equals(sessionPwd)) {
                resultMap.put("result", "true");
            } else {// 旧密码输入不正确
                resultMap.put("result", "false");
            }
        }
        return JSONArray.toJSONString(resultMap);
    }

    @RequestMapping(value = "/pwdsave.html")
    public String pwdSave(
            @RequestParam(value = "newpassword") String newPassword,
            HttpSession session, HttpServletRequest request) {
        boolean flag = false;
        Object o = session.getAttribute(Constants.USER_SESSION);
        if (o != null && !StringUtils.isNullOrEmpty(newPassword)) {
            try {
                flag = userService.updatePwd(((User) o).getId(), newPassword);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (flag) {
                request.setAttribute(Constants.SYS_MESSAGE,
                        "修改密码成功,请退出并使用新密码重新登录！");
                session.removeAttribute(Constants.USER_SESSION);// session注销
                return "login";
            } else {
                request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
            }
        } else {
            request.setAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
        }
        return "pwdmodify";
    }
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @ResponseBody
    public Object updatePassword(@RequestParam("id") Integer id,
                                 @RequestParam("password") String password,
                                 HttpSession session) {
        Map<String, String> resultMap = new HashMap<>();

        // Log the received parameters
        logger.info("updatePassword - Received ID: " + id + ", Password: " + password);

        // Validate session
        User user = (User) session.getAttribute(Constants.USER_SESSION);
        if (user == null) {
            logger.error("updatePassword - Session expired or user not logged in.");
            resultMap.put("result", "sessionerror");
            return resultMap;
        }

        // Check if the ID matches the logged-in user
        if (!user.getId().equals(id)) {
            logger.error("updatePassword - User ID mismatch. Logged-in user ID: " + user.getId());
            resultMap.put("result", "id_mismatch");
            return resultMap;
        }

        // Validate password
        if (password == null || password.trim().isEmpty() || password.length() < 6) {
            logger.error("updatePassword - Invalid password. Password must be at least 6 characters.");
            resultMap.put("result", "password_invalid");
            return resultMap;
        }

        // Attempt to update the password
        try {
            boolean isUpdated = userService.updatePwd(id, password);
            if (isUpdated) {
                logger.info("updatePassword - Password updated successfully for user ID: " + id);
                resultMap.put("result", "success");
            } else {
                logger.error("updatePassword - Password update failed for user ID: " + id);
                resultMap.put("result", "failure");
            }
        } catch (Exception e) {
            logger.error("updatePassword - Exception occurred while updating password: ", e);
            resultMap.put("result", "error");
        }

        return resultMap;
    }
    @Resource
    private UserRoleService userRoleService;

    @RequestMapping("/user/toAssignRole")
    public String toAssignRole(@RequestParam("id") Integer userId, Model model) throws Exception {
        List<Role> allRoles = roleService.getRoleList(); // 所有可选角色
        List<Role> userRoles = userRoleService.getUserRoles(userId); // 用户已有角色

        model.addAttribute("userId", userId);
        model.addAttribute("allRoles", allRoles);
        model.addAttribute("userRoles", userRoles);

        return "user/assignRole"; // JSP 页面路径
    }

    @RequestMapping("/user/assignRole")
    public String assignRole(@RequestParam("userId") Integer userId,
                             @RequestParam("roleId") Integer roleId) throws Exception {
        userRoleService.deleteRolesByUserId(userId); // 清空旧角色
        userRoleService.assignRole(userId, roleId);  // 重新赋值
        return "redirect:/user/list";
    }
}
