package com.itheima.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Dept;
import com.itheima.service.system.DeptService;
import com.itheima.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    //查询当前管理员登录的企业的所有部门以及部门对应的上属部门
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        PageInfo pageInfo = deptService.findAllDept(companyId, page, size);
        request.setAttribute("page", pageInfo);
        return "system/dept/dept-list";
    }

    //新增部门,用于页面的跳转
    @RequestMapping(value = "/toAdd", name = "用于页面的跳转")
    public String toAdd() {
        List<Dept> list = deptService.findAll(companyId);
        request.setAttribute("deptList", list);
        return "system/dept/dept-add";
    }


    @RequestMapping("/edit")
    public String edit(Dept dept) {
        dept.setCompanyId(companyId);
        dept.setCompanyName(companyName);

        if (StringUtils.isEmpty(dept.getId())){
            deptService.saveDept(dept);
        }else {
            deptService.update(dept);
        }

        return "redirect:/system/dept/list.do";
    }

    //删除部门
    @RequestMapping("/delete")
    public String delete(String id){
        deptService.deleteDept(id);
        return "redirect:/system/dept/list.do";
    }

    //修改部门,用于页面的跳转
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        Dept dept = deptService.findDeptById(id);
        request.setAttribute("dept",dept);
        //因为现在有管理员在登录，所以要查询出来当前管理员所在公司的所有部门
        List<Dept> list = deptService.findAll(companyId);
        request.setAttribute("deptList", list);

        return "system/dept/dept-update";

    }


}
