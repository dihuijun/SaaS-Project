package com.itheima.service.cargo;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.Contract;
import com.itheima.domain.cargo.ContractExample;

import java.util.List;

public interface ContractService {

    //分页查询
    PageInfo findAll(ContractExample example,int page,int size);

    //保存合同信息
    void save(Contract contract);

    //通过id查询
    Contract findById(String id);

    //删除
    void delete(String id);

    //修改
    void update(Contract contract);


}
