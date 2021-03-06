package com.itheima.service.cargo;

import com.github.pagehelper.PageInfo;

import com.itheima.domain.cargo.ContractProduct;
import com.itheima.domain.cargo.ContractProductExample;
import com.itheima.domain.vo.ContractProductVo;

import java.util.List;

public interface ContractProductService {

    //分页查询所有
    PageInfo findAll(ContractProductExample example, int page, int size);

    //新增货物
    void save(ContractProduct contractProduct);

    //通过id进行查询
    ContractProduct findById(String id);

    //更新合同信息
    void update(ContractProduct contractProduct);

    //通过id进行删除
    void delete(String id);
    //通过船期查询货物列表
    List<ContractProductVo> findByShipTime(String inputDate);














}
