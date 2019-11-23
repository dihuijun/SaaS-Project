package com.itheima.service.packing;

import com.github.pagehelper.PageInfo;

public interface PackingService {

    //查询所有的报运单，分页显示
    PageInfo findAll(int page,int size);


}
