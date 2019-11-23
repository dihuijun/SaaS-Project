package com.itheima.web.controller.company;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.company.Company;
import com.itheima.service.company.CompanyService;
import com.itheima.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController {

    @Reference
    private CompanyService companyService;

    //列表查询
    @RequestMapping("/list")
    public String list(String page,String size){
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(size)) {
            size = "5";
        }
        PageInfo pageInfo = companyService.findAllCompanys(page, size);
        request.setAttribute("page",pageInfo);


        return "company/company-list";
    }

    //跳转到新建企业的页面
    @RequestMapping(value = "/toAdd",name = "新建页面跳转")
    public String toAdd(){
        return "/company/company-add";
    }

    /*@RequestMapping(value = "/edit",name = "保存企业信息")
    public String edit(Company company){
        companyService.saveCompany(company);
        return "redirect:/company/list.do";
    }*/

    //跳转到修改企业的界面
    @RequestMapping(value = "/toUpdate",name = "修改企业的跳转功能")
    public String toUpdate(String id){
        Company company = companyService.findCompanyById(id);
        request.setAttribute("company",company);
        return "company/company-update";

    }

    //处理新建和修改企业的方法
    @RequestMapping("/edit")
    public String edit(Company company){
        if (StringUtils.isEmpty(company.getId())){
            companyService.saveCompany(company);
        }else {
            companyService.update(company);
        }
        return "redirect:/company/list.do";
    }

    //删除企业
    @RequestMapping("/delete")
    public String delete(String id){
        companyService.deleteCompany(id);
        return "redirect:/company/list.do";
    }

}
