package com.itheima.service.cargo;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.ProductMsg;
import com.itheima.domain.cargo.ProductMsgExample;

import java.util.List;

public interface ProductMsgService {
    //查询所有货物的货号和详细信息分页显示
    PageInfo findAllProduct(ProductMsgExample example, int page, int size);

    //新增货号和详细信息
    void saveProductMsg(ProductMsg productMsg);

    //根据货号查询货物的详细信息
    ProductMsg findByProductId(String productNo);

    //修改货物的详细信息
    void updateProductMsg(ProductMsg productMsg);

    //删除
    void delete(String productNo);

    //查询所有货号和货物详情不分页显示
    List<ProductMsg> findAll();
}
