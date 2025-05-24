package cn.dsscm.dao;

import cn.dsscm.pojo.Role;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface UserRoleMapper {

    int assignRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    List<Role> getRolesByUserId(@Param("userId") Integer userId);

    int deleteRolesByUserId(@Param("userId") Integer userId);
}