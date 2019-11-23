package com.itheima.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Dept;
import com.itheima.domain.system.Role;
import com.itheima.domain.system.User;
import com.itheima.service.system.DeptService;
import com.itheima.service.system.RoleService;
import com.itheima.service.system.UserService;
import com.itheima.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        PageInfo pageInfo = userService.findAllUser(companyId, page, size);
        request.setAttribute("page", pageInfo);
        return "system/user/user-list";
    }

    //新建用户跳转
    @RequestMapping("/toAdd")
    public String toAdd() {
        //根据管理员的companyId去查询其属于哪个公司，然后查询其下的所有部门
        List<Dept> list = deptService.findAll(companyId);
        request.setAttribute("deptList", list);
        return "system/user/user-add";
    }

    //新建用户和修改用户
    @RequestMapping("/edit")
    public String edit(User user) {
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        if (StringUtils.isEmpty(user.getId())) {
            userService.saveUser(user);
        } else {
            userService.updateUser(user);
        }
        return "redirect:/system/user/list.do";
    }

    //修改用户跳转
    @RequestMapping("/toUpdate")
    public String toUpdate(String id) {
        //将当前管理员所登录的公司查询出来所有的部门回显到下拉列表中供用户进行选择
        List<Dept> deptList = deptService.findAll(companyId);
        request.setAttribute("deptList", deptList);

        //将前台传过来的id用来查询，回显到user-update页面
        User user = userService.findUserByUserId(id);
        request.setAttribute("user", user);
        return "system/user/user-update";
    }

    //删除用户
    @RequestMapping("/delete")
    public String delete(String id) {
        userService.deleteUser(id);
        return "redirect:/system/user/list.do";
    }

    //分配角色跳转功能
    @RequestMapping("/roleList")
    public String roleList(String id) {
        System.out.println(id);

        //根据id去查询此id已经存在了哪些角色，然后回显到页面上
        List<Role> userRoles = roleService.findUserRoles(id);
        //System.out.println("陈建“+"+userRoles);

        //查询所有的角色
        List<Role> roleList = roleService.findAllRolesById(companyId);

        //根据id去查询是哪个用户
        User user = userService.findUserByUserId(id);

        String userRoleStr = "";

        for (Role userRole : userRoles) {
            userRoleStr = userRoleStr + userRole.getId() + ",";
        }


        request.setAttribute("user", user);
        request.setAttribute("roleList", roleList);
        request.setAttribute("userRoleStr", userRoleStr);
        return "system/user/user-role";
    }

    //修改用户拥有的模块
    @RequestMapping("/changeRole")
    public String changeRole(String[] roleIds,String userid){
        roleService.changeRole(userid,roleIds);

        return "redirect:/system/user/list.do";
    }

}
