package cn.dsscm.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import cn.dsscm.pojo.User;

public interface UserMapper {
    /**
     * 通过userCode获取User
     * @param userCode
     * @return
     * @throws Exception
     */
    public User getLoginUser(@Param("userCode")String userCode)throws Exception;

    /**
     * 增加用户信息
     * @param user
     * @return
     * @throws Exception
     */
    public int add(User user)throws Exception;

    /**
     * 通过条件查询-userList
     * @param userName
     * @param userRole
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<User> getUserList(@Param("userName")String userName,@Param("userRole")Integer userRole )throws Exception;

    /**
     * 通过条件查询-用户表记录数
     * @param userName
     * @param userRole
     * @return
     * @throws Exception
     */
    public int getUserCount(@Param("userName")String userName,@Param("userRole")Integer userRole)throws Exception;

    /**
     * 通过userId删除user
     * @param delId
     * @return
     * @throws Exception
     */
    public int deleteUserById(@Param("id")Integer delId)throws Exception;


    /**
     * 通过userId获取user
     * @param id
     * @return
     * @throws Exception
     */
    public User getUserById(@Param("id")Integer id)throws Exception;

    /**
     * 修改用户信息
     * @param user
     * @return
     * @throws Exception
     */
    public int modify(User user)throws Exception;


    /**
     * 修改当前用户密码
     * @param id
     * @param pwd
     * @return
     * @throws Exception
     */
    public int updatePwd(@Param("id")Integer id,@Param("userPassword")String pwd)throws Exception;

}
