package com.itheima.service.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Dept;
import org.w3c.dom.ls.LSInput;

import java.util.List;


public interface DeptService {

    //查询所有部门，根据SaaS管理员登录时候的companyID(分页形式)
    PageInfo findAllDept(String companyId, int page, int size);

    //在查询企业的部门的时候同时将其上属部门也查询出来
    List<Dept> findById(String deptId);

    //新增部门
    void saveDept(Dept dept);

    //查询所有部门（不分页）
    List<Dept> findAll(String companyId);

    //删除部门
    void deleteDept(String id);

    //修改部门
    void update(Dept dept);

    //根据id查询部门
    Dept findDeptById(String id);
}
