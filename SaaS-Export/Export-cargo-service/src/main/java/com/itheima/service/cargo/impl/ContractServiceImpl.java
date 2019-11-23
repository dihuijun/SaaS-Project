package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.cargo.ContractDao;
import com.itheima.dao.cargo.ContractProductDao;
import com.itheima.dao.cargo.ExtCproductDao;
import com.itheima.domain.cargo.Contract;
import com.itheima.domain.cargo.ContractExample;
import com.itheima.service.cargo.ContractService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ExtCproductDao extCproductDao;

    @Autowired
    private ContractProductDao contractProductDao;

    @Override
    public PageInfo findAll(ContractExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<Contract> list = contractDao.selectByExample(example);
        PageInfo info = new PageInfo(list);
        return info;
    }

    @Override
    public void save(Contract contract) {
        contract.setId(UUID.randomUUID().toString());
        Date date = new Date();
        contract.setShipTime(date);
        contract.setDeliveryPeriod(date);
        contract.setCreateTime(date);
        contract.setUpdateTime(date);
        contractDao.insertSelective(contract);
    }

    @Override
    public Contract findById(String id) {
        Contract contract = contractDao.selectByPrimaryKey(id);
        return contract;
    }

    @Override
    public void delete(String id) {
        //因为附件和货物中都有合同的ID,所以直接删除当前合同所属的附件和货物即可
        extCproductDao.deleteByPrimaryKey(id);
        //删除货物
        contractProductDao.deleteByPrimaryKey(id);
        //删除合同
        contractDao.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Contract contract) {
        contractDao.updateByPrimaryKeySelective(contract);
    }


}
