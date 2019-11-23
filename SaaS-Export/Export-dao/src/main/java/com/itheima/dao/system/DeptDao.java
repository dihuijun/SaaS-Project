package com.itheima.dao.system;

import com.itheima.domain.system.Dept;

import java.util.List;

public interface DeptDao {

    //查询所有部门
    List<Dept> findAllDept(String companyId);

    //顺带查询其上属部门
    List<Dept> findById(String deptId);


    //新增部门
    void saveDept(Dept dept);

    //删除部门
    void deleteDept(String id);

    //修改部门
    void updateDept(Dept dept);

    //根据id查询部门
    Dept findDeptById(String id);
}
