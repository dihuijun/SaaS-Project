package com.itheima.service.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Dept;
import com.itheima.domain.system.Role;
import com.itheima.domain.system.User;

import java.util.List;

public interface UserService {

    //查询所有用户
    PageInfo findAllUser(String companyId, int page, int size);



    //新建用户
    void saveUser(User user);

    //修改用户
    void updateUser(User user);

    //根据id去查询用户
    User findUserByUserId(String id);

    //删除用户
    void deleteUser(String id);

    //根据id去查询所有的角色信息回显
    List<Role> findUserRoles(String id);

    //登录验证，根据email查询用户
    User findByEmail(String email);

}
