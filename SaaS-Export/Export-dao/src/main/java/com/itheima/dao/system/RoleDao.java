package com.itheima.dao.system;

import com.itheima.domain.system.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDao {

    //查询所有角色信息，分页显示
    List<Role> findAllRoles(String companyId);

    //新建角色
    void saveRole(Role role);

    //修改角色
    void updateRole(Role role);

    //根据角色id去查询信息回显到页面上用来进行修改操作
    Role findRoleById(String id);

    //删除角色
    void deleteRole(String id);

    List<Role> findUserRoles(String id);

    void insertUserRole(@Param("userId") String userid,@Param("roleId") String roleId);


    void deleteRoleByUserId(String userid);
}
