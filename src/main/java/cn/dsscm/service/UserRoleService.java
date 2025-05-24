package cn.dsscm.service;

import cn.dsscm.pojo.Role;
import java.util.List;

public interface UserRoleService {

    boolean assignRole(Integer userId, Integer roleId) throws Exception;

    List<Role> getUserRoles(Integer userId) throws Exception;

    boolean deleteRolesByUserId(Integer userId) throws Exception;
}