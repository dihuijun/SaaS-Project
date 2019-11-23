package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.company.CompanyDao;
import com.itheima.domain.company.Company;
import com.itheima.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    //查询所有企业
    @Override
    public PageInfo findAllCompanys(String page, String size) {
        //1、PageHelper.startPage
        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(size));
        //2、紧跟着List，findAll
        List<Company> list = companyDao.findAllCompany();

        //3、new PageInfo(list)
        return new PageInfo(list);
    }

    //保存新建的企业
    @Override
    public void saveCompany(Company company) {
        company.setId(UUID.randomUUID().toString());
        companyDao.saveCompany(company);
    }

    //根据id搜索企业
    @Override
    public Company findCompanyById(String id) {
        return companyDao.findCompanyById(id);
    }

    //修改企业信息
    @Override
    public void update(Company company) {
        companyDao.update(company);
    }

    //删除企业
    @Override
    public void deleteCompany(String id) {
        companyDao.deleteCompany(id);
    }


}