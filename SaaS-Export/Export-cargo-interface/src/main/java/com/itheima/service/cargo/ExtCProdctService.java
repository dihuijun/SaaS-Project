package com.itheima.service.cargo;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.ExtCproduct;
import com.itheima.domain.cargo.ExtCproductExample;

public interface ExtCProdctService {

    //查询所有附件，分页显示
    PageInfo findAll(ExtCproductExample example,int page,int size);

    //保存附件信息
    void save(ExtCproduct extCproduct);

    //修改附件信息
    void update(ExtCproduct extCproduct);

    //通过id进行查询
    ExtCproduct findById(String id);

    //删除
    void delete(String id);





}
