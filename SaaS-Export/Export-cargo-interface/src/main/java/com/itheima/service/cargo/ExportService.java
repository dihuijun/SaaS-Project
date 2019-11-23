package com.itheima.service.cargo;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.Export;
import com.itheima.domain.cargo.ExportExample;
import com.itheima.domain.vo.ExportResult;

import java.util.List;

public interface ExportService {

    //根据id查询
    Export findById(String id);

    //保存
    void save(Export export);

    //修改
    void update(Export export);

    //删除
    void delete(String id);

    //分页查询
    PageInfo findAll(ExportExample example,int page,int size);

    void updateE(ExportResult result);
}
