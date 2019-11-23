package com.itheima.dao.system;

import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ModuleDao {

    //根据id查询
    Module findById(String moduleId);

    //根据id删除
    int delete(String moduleId);

    //添加用户
    int save(Module module);

    //更新用户
    int update(Module module);

    //查询全部
    List<Module> findAll();

    //通过角色查找模块信息
    List<Module> findModuleByRoleId(String roleId);


    void deleteByRoleId(String roleid);

    void insertRoleModule(@Param("roleId") String roleid, @Param("moduleId") String moduleId);

    //如果0和1都没有找到的话，那么就用当前登录的用户去查找其拥有的模块
    List<Module> findModuleByUserId(String  userId);

    //根据degree查询模块
    List<Module> findByDegree(Integer belong);
}
