package com.itheima.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.Role;
import com.itheima.service.system.ModuleService;
import com.itheima.service.system.RoleService;
import com.itheima.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    //分页显示所有的角色
    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ){
         PageInfo pageInfo = roleService.findAllRoles(companyId, page, size);
         request.setAttribute("page",pageInfo);
         return "system/role/role-list";
    }

    //新建角色跳转
    @RequestMapping("/toAdd")
    public String toAdd(){
       return "system/role/role-add";
    }

    @RequestMapping("/edit")
    public String edit(Role role){
        role.setCompanyId(companyId);
        role.setCompanyName(companyName);

        if (StringUtils.isEmpty(role.getId())){
            roleService.saveRole(role);
        }else {
            roleService.updateRole(role);
        }
        return "redirect:/system/role/list.do";
    }

    //修改角色跳转
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        Role role = roleService.findRoleById(id);
        request.setAttribute("role", role);
        return "system/role/role-update";
    }

    @RequestMapping("/delete")
    public String delete(String id){
        roleService.deleteRole(id);
        return "redirect:/system/role/list.do";
    }

    //给角色分配菜单跳转方法
    @RequestMapping("/roleModule")
    public  String roleModule(String roleid){
        Role role = roleService.findRoleById(roleid);
        request.setAttribute("role",role);
        return "system/role/role-module";
    }

    @Autowired
    private ModuleService moduleService;

    @RequestMapping("/initModuleData")
    public @ResponseBody List<Map> initModuleData(String roleId){
        //查询所有模块
        List<Module> moduleList = moduleService.findAll();

        //通过roleId拿到roleId的信息
        List<Module> roleModule =  moduleService.findModuleByRoleId(roleId);

        //构造zNodes
        List<Map> zNodes = new ArrayList<Map>();

        for (Module module : moduleList) {
            Map map = new HashMap();
            map.put("id",module.getId());
            map.put("pId",module.getParentId());
            map.put("name",module.getName());

            if (roleModule.contains(module)){
                map.put("checked",true);
            }
            zNodes.add(map);
        }

        return zNodes;
    }

    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleid,String moduleIds){

        System.out.println(moduleIds);
        moduleService.insertRoleModule(roleid,moduleIds);


        return "redirect:/system/role/list.do";
    }

}
