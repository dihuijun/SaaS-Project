package com.itheima.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.system.DeptDao;
import com.itheima.domain.system.Dept;
import com.itheima.service.system.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptDao deptDao;

    //需要分页展示
    @Override
    public PageInfo findAllDept(String companyId, int page, int size) {

        PageHelper.startPage(page, size);

        List<Dept> list = deptDao.findAllDept(companyId);

        PageInfo pageInfo = new PageInfo(list);

        return pageInfo;


    }

    @Override
    public List<Dept> findById(String deptId) {
        return deptDao.findById(deptId);
    }

    //保存新的部门
    @Override
    public void saveDept(Dept dept) {
        dept.setId(UUID.randomUUID().toString());
        deptDao.saveDept(dept);
    }

    //查询所有部门，但是不分页显示
    @Override
    public List<Dept> findAll(String companyId) {
        return deptDao.findAllDept(companyId);
    }

    //删除部门，级联删除
    @Override
    public void deleteDept(String id) {
        deptDao.deleteDept(id);
    }

    //修改部门
    @Override
    public void update(Dept dept) {
        deptDao.updateDept(dept);
    }

    //根据id查询部门
    @Override
    public Dept findDeptById(String id) {
        return deptDao.findDeptById(id);
    }
}
