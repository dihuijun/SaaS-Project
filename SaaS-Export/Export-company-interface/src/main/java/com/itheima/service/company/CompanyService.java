package com.itheima.service.company;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.company.Company;

public interface CompanyService {

    //查询所有企业
    PageInfo findAllCompanys(String page, String size);

    //新增企业
    void saveCompany(Company company);

    //根据用户id搜索用户
    Company findCompanyById(String id);

    //修改用户
    void update(Company company);

    //删除用户
    void deleteCompany(String id);
}
