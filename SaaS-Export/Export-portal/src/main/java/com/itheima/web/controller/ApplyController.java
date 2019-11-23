package com.itheima.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.company.Company;
import com.itheima.service.company.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplyController {

    @Reference
    private CompanyService companyService;

    @RequestMapping("/apply")
    public @ResponseBody String apply(Company company){
        try{
            company.setState(0);
            companyService.saveCompany(company);
            return "1";
        }catch (Exception e){
            return "2";
        }



    }

}
