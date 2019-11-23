package com.itheima.dao.company;

import com.itheima.domain.company.Company;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyDao {
    //查询所有企业
    List<Company> findAllCompany();

    //保存新的企业
    void saveCompany(Company company);

    //根据用户id查找
    Company findCompanyById(String id);

    //修改企业
    void update(Company company);

    //删除企业
    void deleteCompany(String id);

    //查询所有记录的条数
    Integer findAllConpanyTotal();

    //查询当前页记录
    List<Company> findRows(@Param("index") int index, @Param("size") int size);
}
