package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.cargo.ProductMessageDao;
import com.itheima.domain.cargo.ProductMessage;
import com.itheima.domain.cargo.ProductMessageExample;
import com.itheima.service.cargo.ProductMessageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ProductMessageServiceImpl implements ProductMessageService {

    @Autowired
    private ProductMessageDao productMessageDao;

    //查询所有的货号和货物详细信息，分页显示
    @Override
    public PageInfo findAllProduct(ProductMessageExample example,int page, int size) {
        PageHelper.startPage(page,size);
        List<ProductMessage> list = productMessageDao.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    //新增货号和详细信息
    @Override
    public void saveProductMessage(ProductMessage productMessage) {
        productMessageDao.insert(productMessage);
    }

    //根据id去查询货号和货物详细信息
    @Override
    public ProductMessage findByProductId(String productNo) {
        return productMessageDao.selectByPrimaryKey(productNo);
    }

    //修改
    @Override
    public void updateProductMessage(ProductMessage productMessage) {
        productMessageDao.updateByPrimaryKeySelective(productMessage);
    }
    //删除
    @Override
    public void delete(String productNo) {
        productMessageDao.deleteByPrimaryKey(productNo);
    }

    @Override
    public List<ProductMessage> findAll() {
        ProductMessageExample example = new ProductMessageExample();
        List<ProductMessage> productMessageList = productMessageDao.selectByExample(example);
        System.out.println("dihuijun" + productMessageList);
        return productMessageList;
    }
}
