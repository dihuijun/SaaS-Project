package com.itheima.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.system.ModuleDao;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.service.system.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class ModuleServiceImpl implements ModuleService {
    @Autowired
    private ModuleDao moduleDao;

    //分页查询所有
    public PageInfo findAll(int page, int size) {
        PageHelper.startPage(page, size);
        List<Module> list = moduleDao.findAll();
        return new PageInfo(list);
    }

    //根据id进行查询
    public Module findById(String id) {
        return moduleDao.findById(id);
    }

    //保存
    public int save(Module module) {
        module.setId(UUID.randomUUID().toString());
        return moduleDao.save(module);
    }

    //更新
    public int update(Module module) {
        return moduleDao.update(module);
    }

    //删除
    public int delete(String id) {
        return moduleDao.delete(id);
    }

    public List<Module> findAll() {
        return moduleDao.findAll();
    }

    @Override
    public List<Module> findModuleByRoleId(String roleId) {
        return moduleDao.findModuleByRoleId(roleId);
    }

    @Override
    public void insertRoleModule(String roleid, String moduleIds) {
        moduleDao.deleteByRoleId(roleid);

        String[] modules = moduleIds.split(",");

        for (String moduleId : modules) {
            moduleDao.insertRoleModule(roleid,moduleId);
        }

    }

    @Override
    public List<Module> findModuleByUserId(User user) {
        //SaaS管理员 degree=0 belong=0
        if (user.getDegree()==0){
            List<Module> moduleList = moduleDao.findByDegree(user.getDegree());
            return moduleList;
        }
        if (user.getDegree()==1){
            List<Module> moduleList = moduleDao.findByDegree(user.getDegree());
            return moduleList;
        }else{
            //0代表SaaS管理员，1代表企业管理员，如果这两个都不是的话那么就是企业用户
            String userId = user.getId();
            List<Module> moduleList = moduleDao.findModuleByUserId(userId);
            return moduleList;


        }

    }
}
