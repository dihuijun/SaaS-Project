package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.cargo.ContractDao;
import com.itheima.dao.cargo.ContractProductDao;
import com.itheima.dao.cargo.ExtCproductDao;
import com.itheima.domain.cargo.Contract;
import com.itheima.domain.cargo.ContractProduct;
import com.itheima.domain.cargo.ExtCproduct;
import com.itheima.domain.cargo.ExtCproductExample;
import com.itheima.service.cargo.ExtCProdctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class ExtCProdctServiceImpl implements ExtCProdctService {

    @Autowired
    private ExtCproductDao extCproductDao;

    @Autowired
    private ContractDao contractDao;




    @Override
    public PageInfo findAll(ExtCproductExample example, int page, int size) {
        PageHelper.startPage(page,size);
        List<ExtCproduct> extCproductList = extCproductDao.selectByExample(example);
        PageInfo info = new PageInfo(extCproductList);
        return info;
    }

    @Override
    public void save(ExtCproduct extCproduct) {
        double amount = 0.0d;
        //计算附件的金额
        if (StringUtils.isEmpty(extCproduct)){
           amount = (extCproduct.getPrice()*extCproduct.getCnumber());
        }
        //2.设置附件的金额
        extCproduct.setAmount(amount);
        //设置附件的Id
        extCproduct.setId(UUID.randomUUID().toString());
        //保存附件
        extCproductDao.insertSelective(extCproduct);
        //通过传过来的附件中合同的id去查询附件
        String contractId = extCproduct.getContractId();
        Contract contract = contractDao.selectByPrimaryKey(contractId);
        //计算合同中的附件数量
        contract.setExtNum(contract.getExtNum()+extCproduct.getCnumber());
        //计算合同中的金额
        contract.setTotalAmount(contract.getTotalAmount()+amount);
        //更新一下数据库中的当前附件所属合同的数据
        contractDao.updateByPrimaryKeySelective(contract);

    }

    @Override
    public void update(ExtCproduct extCproduct) {
        double amount = 0.0d;
        //计算修改之后传过来的附件的金额
        if (StringUtils.isEmpty(extCproduct)){
            amount = (extCproduct.getPrice()*extCproduct.getCnumber());
        }
        //设置附件的金额
        extCproduct.setAmount(amount);
        //通过id查找合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //获取还没修改过的附件
        ExtCproduct oldExt = extCproductDao.selectByPrimaryKey(extCproduct.getId());
        //更新合同中的金额，因为改变附件金额会变
        contract.setTotalAmount(contract.getTotalAmount()-oldExt.getAmount()+amount);
        //更新合同中的附件数量，因为改变附件，合同中的附件也要响应的作出改变
        contract.setExtNum(contract.getExtNum() - oldExt.getCnumber() + extCproduct.getCnumber());
        //保存附件
        extCproductDao.updateByPrimaryKeySelective(extCproduct);
        //更新合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public ExtCproduct findById(String id) {
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);
        return extCproduct;
    }

    @Override
    public void delete(String id) {
        //先通过当前穿过来要删除的附件的id去附件表中查出来附件
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);
        //获取这个要删除的附件的金额
        Double amount = extCproduct.getAmount();
        //获取这个要删除的附件的数量
        Integer cnumber = extCproduct.getCnumber();
        //通过这个附件中的货物id查询出来属于哪个合同
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        //计算删除之后合同的金额
        contract.setTotalAmount(contract.getTotalAmount()-amount);
        //计算删除之后合同的数量
        contract.setExtNum(contract.getExtNum()-cnumber);
        //删除附件
        extCproductDao.deleteByPrimaryKey(id);
        //更新合同
        contractDao.updateByPrimaryKeySelective(contract);

    }
}
