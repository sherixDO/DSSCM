package cn.dsscm.service.impl;
import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.dsscm.dao.UserMapper;
import cn.dsscm.pojo.User;
import cn.dsscm.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User login(String userCode, String userPassword) throws Exception {
        // TODO Auto-generated method stub
        User user = null;
        user = userMapper.getLoginUser(userCode);
        //匹配密码
        if(null != user){
            if(!user.getUserPassword().equals(userPassword))
                user = null;
        }
        return user;
    }

    @Override
    public boolean add(User user) throws Exception {
        // TODO Auto-generated method stub
        boolean flag = false;
        if(userMapper.add(user) > 0)
            flag = true;
        return flag;
    }

    @Override
    public PageInfo<User> getUserList(String queryUserName, Integer queryUserRole,
                                      Integer currentPageNo, Integer pageSize) throws Exception {
        //开启分页
        PageHelper.startPage(currentPageNo,pageSize);
        List<User> list = userMapper.getUserList(queryUserName, queryUserRole);
        PageInfo<User> pi = new PageInfo<User>(list);
        return pi;
    }

    @Override
    public int getUserCount(String queryUserName, Integer queryUserRole)
            throws Exception {
        // TODO Auto-generated method stub
        return userMapper.getUserCount(queryUserName, queryUserRole);
    }

    @Override
    public User selectUserCodeExist(String userCode) throws Exception {
        // TODO Auto-generated method stub
        return userMapper.getLoginUser(userCode);
    }

    @Override
    public boolean deleteUserById(Integer delId) throws Exception {
        // TODO Auto-generated method stub
        boolean flag = true;
        //先删除该条记录的上传附件
        User user = userMapper.getUserById(delId);
        if(user.getImgPath() != null && !user.getImgPath().equals("")){
            //删除服务器上个人证件照
            File file = new File(user.getImgPath());
            if(file.exists()){
                flag = file.delete();
            }
        }
        if(flag){
            if(userMapper.deleteUserById(delId) < 1)
                flag = false;
        }
        return flag;
    }

    @Override
    public User getUserById(Integer id) throws Exception {
        // TODO Auto-generated method stub
        return userMapper.getUserById(id);
    }

    @Override
    public boolean modify(User user) throws Exception {
        // TODO Auto-generated method stub
        boolean flag = false;
        if(userMapper.modify(user) > 0)
            flag = true;
        return flag;
    }

    @Override
    public boolean updatePwd(Integer id, String newPassword) throws Exception {
        return userMapper.updatePwd(id, newPassword) > 0;
    }

}
