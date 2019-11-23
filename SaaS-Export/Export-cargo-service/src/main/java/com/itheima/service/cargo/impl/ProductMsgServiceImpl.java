package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.cargo.ProductMessageDao;
import com.itheima.dao.cargo.ProductMsgDao;
import com.itheima.domain.cargo.ProductMessage;
import com.itheima.domain.cargo.ProductMessageExample;
import com.itheima.domain.cargo.ProductMsg;
import com.itheima.domain.cargo.ProductMsgExample;
import com.itheima.service.cargo.ProductMessageService;
import com.itheima.service.cargo.ProductMsgService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ProductMsgServiceImpl implements ProductMsgService {

    @Autowired
    private ProductMsgDao productMsgDao;

    //查询所有的货号和货物详细信息，分页显示
    @Override
    public PageInfo findAllProduct(ProductMsgExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<ProductMsg> list = productMsgDao.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    //新增货号和详细信息
    @Override
    public void saveProductMsg(ProductMsg productMsg) {
        productMsgDao.insert(productMsg);
    }

    //根据id去查询货号和货物详细信息
    @Override
    public ProductMsg findByProductId(String productNo) {
        return productMsgDao.selectByPrimaryKey(productNo);
    }

    //修改
    @Override
    public void updateProductMsg(ProductMsg productMsg) {
        productMsgDao.updateByPrimaryKeySelective(productMsg);
    }
    //删除
    @Override
    public void delete(String productNo) {
        productMsgDao.deleteByPrimaryKey(productNo);
    }

    //查询所有货号和货物详情
    @Override
    public List<ProductMsg> findAll() {
        ProductMsgExample example = new ProductMsgExample();
        return productMsgDao.selectByExample(example);
    }
}
