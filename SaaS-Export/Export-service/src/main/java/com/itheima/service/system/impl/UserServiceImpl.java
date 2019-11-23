package com.itheima.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.commons.utils.Encrypt;
import com.itheima.dao.system.UserDao;
import com.itheima.domain.system.Dept;
import com.itheima.domain.system.Role;
import com.itheima.domain.system.User;
import com.itheima.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    //查询所有用户，需要分页显示
    @Override
    public PageInfo findAllUser(String companyId, int page, int size) {

        PageHelper.startPage(page, size);

        List<User> list = userDao.findAllUser(companyId);

        PageInfo pageInfo = new PageInfo(list);

        return pageInfo;
    }


    @Override
    public void saveUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setPassword(Encrypt.md5(user.getPassword(),user.getEmail()));
        System.out.println(user);
        userDao.saveUser(user);
    }

    @Override
    public void updateUser(User user) {
        user.setPassword(Encrypt.md5(user.getPassword(),user.getEmail()));
        userDao.updateUser(user);
    }

    @Override
    public User findUserByUserId(String id) {
        return userDao.findUserByUserId(id);
    }

    @Override
    public void deleteUser(String id) {
        userDao.deleteUser(id);
    }

    @Override
    public List<Role> findUserRoles(String id) {
        return userDao.findUserRoles(id);
    }

    //登录验证，根据email去查询用户是否存在
    @Override
    public User findByEmail(String email) {
        System.out.println(1111);
        User user =  userDao.findByEmail(email);
        return user;
    }



}
