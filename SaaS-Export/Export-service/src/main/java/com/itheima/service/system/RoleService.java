package com.itheima.service.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Role;

import java.util.List;

public interface RoleService {

    //分页显示角色信息
    PageInfo findAllRoles(String companyId, int page, int size);

    //新建角色
    void saveRole(Role role);

    //修改角色
    void updateRole(Role role);

    //根据角色id去查询信息，用于回显数据进行修改
    Role findRoleById(String id);

    //删除角色
    void deleteRole(String id);

    //查询所有角色
    List<Role> findAllRolesById(String companyId);

    List<Role> findUserRoles(String id);

    //修改用户拥有的角色
    void changeRole(String userid, String[] roleIds);
}
