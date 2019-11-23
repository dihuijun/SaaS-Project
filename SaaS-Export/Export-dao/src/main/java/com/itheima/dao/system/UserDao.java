package com.itheima.dao.system;

import com.itheima.domain.system.Dept;
import com.itheima.domain.system.Role;
import com.itheima.domain.system.User;

import java.util.List;

public interface UserDao {

    //查询所有用户
    List<User> findAllUser(String companyId);

    //新建用户
    void saveUser(User user);

    //修改用户
    void updateUser(User user);


    //根据id去查询用户
    User findUserByUserId(String id);

    //删除用户
    void deleteUser(String id);

    //根据id去查询角色信息
    List<Role> findUserRoles(String id);

    //根据email去查询用户，用来登录的验证
    User findByEmail(String email);


}
