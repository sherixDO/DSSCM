package cn.dsscm.service.impl;

import cn.dsscm.dao.UserRoleMapper;
import cn.dsscm.pojo.Role;
import cn.dsscm.service.UserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public boolean assignRole(Integer userId, Integer roleId) throws Exception {
        return userRoleMapper.assignRole(userId, roleId) > 0;
    }

    @Override
    public List<Role> getUserRoles(Integer userId) throws Exception {
        return userRoleMapper.getRolesByUserId(userId);
    }

    @Override
    public boolean deleteRolesByUserId(Integer userId) throws Exception {
        return userRoleMapper.deleteRolesByUserId(userId) > 0;
    }
}