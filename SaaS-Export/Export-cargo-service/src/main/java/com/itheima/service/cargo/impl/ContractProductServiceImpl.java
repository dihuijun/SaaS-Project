package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.cargo.ContractDao;
import com.itheima.dao.cargo.ContractProductDao;
import com.itheima.dao.cargo.ExtCproductDao;
import com.itheima.domain.cargo.*;
import com.itheima.domain.vo.ContractProductVo;
import com.itheima.service.cargo.ContractProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    private ContractProductDao contractProductDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ExtCproductDao extCproductDao;

    @Override
    public PageInfo findAll(ContractProductExample example, int page, int size) {

        PageHelper.startPage(page,size);
        List<ContractProduct> list = contractProductDao.selectByExample(example);
        PageInfo info = new PageInfo(list);
        return info;
    }

    @Override
    public void save(ContractProduct contractProduct) {
        System.out.println(contractProduct);
        //先设置一个总金额
        double amount = 0.0d;
        //如果点击保存传过来的货物实体类中单价和数量都不为空的话就计算出总金额
        if (contractProduct.getCnumber()!=null&&contractProduct.getPrice()!=null){
            amount = contractProduct.getCnumber()*contractProduct.getPrice();
        }
        //给传过来的货物实体类设置一个id
        contractProduct.setId(UUID.randomUUID().toString());
        //将金额设置进去
        contractProduct.setAmount(amount);

        //将新添加的货物保存到货物表中
        contractProductDao.insertSelective(contractProduct);

        //下面是更新合同表中的信息
        //先取出来当前添加的货物所属的合同的id
        String contractId = contractProduct.getContractId();
        //根据这个id去查找到这个货物所属的合同
        Contract contract = contractDao.selectByPrimaryKey(contractId);

        //计算合同的金额（合同以前的金额+本次货物的金额）
        if (contract.getTotalAmount()==null){
            contract.setTotalAmount(amount);
        }
        contract.setTotalAmount(contract.getTotalAmount()+amount);

        //计算货物的数量
        contract.setProNum(contract.getProNum()+contractProduct.getCnumber());

        //更新一下当前添加货物的合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    @Override
    public void update(ContractProduct contractProduct) {
        double amount = 0.0d;
        if (contractProduct.getCnumber()!=null&&contractProduct.getPrice()!=null){
            amount = contractProduct.getCnumber()*contractProduct.getPrice();
        }
        contractProduct.setAmount(amount);

        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());

        //通过id获取以前的货物
        ContractProduct oldCp = contractProductDao.selectByPrimaryKey(contractProduct.getId());

        //计算一下合同的金额（合同以前的金额-以前的货物金额+本次的货物金额）
        contract.setTotalAmount(contract.getTotalAmount()-oldCp.getAmount()+amount);
        contract.setProNum(contract.getProNum()-oldCp.getCnumber()+contractProduct.getCnumber());

        //保存货物
        contractProductDao.updateByPrimaryKeySelective(contractProduct);

        //更新合同
        contractDao.updateByPrimaryKeySelective(contract);

    }

    @Override
    public void delete(String id) {
        //获取当前货物下的所有附件
        ExtCproductExample example = new ExtCproductExample();
        ExtCproductExample.Criteria criteria = example.createCriteria();
        criteria.andContractIdEqualTo(id);
        List<ExtCproduct> extCproductList = extCproductDao.selectByExample(example);

        double amount = 0.0d;
        int extNum = 0;
        //循环这个货物下面的所有附件，依次删除
        for (ExtCproduct extCproduct : extCproductList) {
            amount += extCproduct.getAmount();
            extNum += extCproduct.getCnumber();
            extCproductDao.deleteByPrimaryKey(extCproduct.getId());
        }

        //通过id查询货物
        ContractProduct contractProduct = contractProductDao.selectByPrimaryKey(id);

        //通过实体类获取删除的货物金额
        Double proAmount = contractProduct.getAmount();

        //通过实体类获取删除的货物数量
        Integer cnumber = contractProduct.getCnumber();

        //通过这个货物查询到其属于哪个合同
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());

        //将合同中的金额更新，减掉删除的货物的金额
        contract.setTotalAmount(contract.getTotalAmount()-proAmount);

        //将合同中的货物数量更新，减掉删除货物的数量
        contract.setProNum(contract.getProNum()-cnumber);

        //对货物进行删除
        contractProductDao.deleteByPrimaryKey(id);

        //更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    //通过船期查询货物列表
    @Override
    public List<ContractProductVo> findByShipTime(String inputDate) {
        return contractProductDao.findByShipTime(inputDate);
    }
}
