package com.itheima.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.system.RoleDao;
import com.itheima.domain.system.Role;
import com.itheima.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public PageInfo findAllRoles(String companyId,int page, int size) {

        PageHelper.startPage(page,size);

        List<Role> list = roleDao.findAllRoles(companyId);

        PageInfo pageInfo = new PageInfo(list);

        return pageInfo;
    }

    @Override
    public void saveRole(Role role) {
        role.setId(UUID.randomUUID().toString());
        roleDao.saveRole(role);
    }

    @Override
    public void updateRole(Role role) {
        roleDao.updateRole(role);
    }

    @Override
    public Role findRoleById(String id) {
        return roleDao.findRoleById(id);
    }

    //删除角色
    @Override
    public void deleteRole(String id) {
         roleDao.deleteRole(id);
    }

    @Override
    public List<Role> findAllRolesById(String companyId) {
        return roleDao.findAllRoles(companyId);
    }

    @Override
    public List<Role> findUserRoles(String id) {
        return roleDao.findUserRoles(id);
    }

    //修改用户拥有的角色
    @Override
    public void changeRole(String userid, String[] roleIds) {
        //先删除之前拥有的
        roleDao.deleteRoleByUserId(userid);

        //插入新的
        for (String roleId : roleIds) {
            roleDao.insertUserRole(userid,roleId);
        }
    }
}
