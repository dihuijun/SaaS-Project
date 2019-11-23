package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.cargo.*;
import com.itheima.domain.cargo.*;
import com.itheima.domain.vo.ExportProductResult;
import com.itheima.domain.vo.ExportResult;
import com.itheima.service.cargo.ExportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private ExportDao exportDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private ContractProductDao contractProductDao;

    @Autowired
    private ExportProductDao exportProductDao;

    @Autowired
    private ExtCproductDao extCproductDao;

    @Autowired
    private ExtEproductDao extEproductDao;


    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Export export) {
        //传过来所有的货运单以后开始填写海关需要的信息
        //先设置报运单的id
        export.setId(UUID.randomUUID().toString());

        //设置export的状态为0,因为这只是第一轮提交，还需要企业内部的审核
        export.setState(0);


        //通过报运单中的contractIDS获取合同的id集合
        String[] contractIds = export.getContractIds().split(",");

        //操作合同

        int proNum = 0;
        int extNUm = 0;

        //循环合同的id集合
        for (String contractId : contractIds) {
            //通过id去查询合同
            Contract contract = contractDao.selectByPrimaryKey(contractId);
            contract.setState(2);
            //合同更新,因为你要报运，所以更改合同那里显示状态为已经报运
            contractDao.updateByPrimaryKeySelective(contract);

            //操作货物
            //每个合同下面都有货物，货物需要在报运表的数据库中存在
            //通过合同的id去查询货物
            ContractProductExample example = new ContractProductExample();
            ContractProductExample.Criteria criteria = example.createCriteria();
            criteria.andContractIdEqualTo(contractId);

            //这里查询的是当前合同下面的所有货物,然后形成的一个集合
            List<ContractProduct> contractProductList = contractProductDao.selectByExample(example);

            //每个货物在报运表中就是一个报运的商品，所以我们建立一个map集合，将货物的id转变为报运商品的id
            Map<String, String> map = new HashMap<>();
            //循环货物的集合取出来货物
            for (ContractProduct contractProduct : contractProductList) {
                //将货物写入到报运的实体类中
                ExportProduct exportProduct = new ExportProduct();
                //这里我们使用beanUtils去赋值所有的值到实体类
                BeanUtils.copyProperties(exportProduct,contractProduct);
                //报运商品单中存在报运单的id
                exportProduct.setExportId(export.getId());
                //设置报运商品的id
                exportProduct.setId(UUID.randomUUID().toString());
                //将报运商品保存进去
                exportProductDao.insertSelective(exportProduct);
                //将货物的id和报运商品的id放到map集合中
                map.put(contractProduct.getId(),exportProduct.getId());
                //计算报运货物的数量
                proNum = proNum+contractProduct.getCnumber();
            }

            //操作附件
            //附件表中存在合同的id，根据合同的id去查询附件
            ExtCproductExample extCproductExample = new ExtCproductExample();
            ExtCproductExample.Criteria criteria1 = extCproductExample.createCriteria();
            criteria1.andContractIdEqualTo(contractId);
            //查询出来的所有的附件
            List<ExtCproduct> extCproductList = extCproductDao.selectByExample(extCproductExample);

            //循环所有的附件集合
            for (ExtCproduct extCproduct : extCproductList) {
                //构造报运附件实体类
                ExtEproduct extEproduct = new ExtEproduct();
                //将所有的附件复制到报运附件中
                BeanUtils.copyProperties(extCproduct,extEproduct);
                //设置附件表中的报运单id
                extEproduct.setExportId(export.getId());
                //设置附件自己的id
                extEproduct.setId(UUID.randomUUID().toString());
                //设置附件对应的报运商品id
                String exportProductId = map.get(extCproduct.getContractProductId());
                extEproduct.setExportProductId(exportProductId);

                //保存报运单的附件
                extEproductDao.insertSelective(extEproduct);
                //计算附件的数量
                extNUm = extNUm+extCproduct.getCnumber();
            }
        }

        //设置报运单的商品数量
        export.setProNum(proNum);
        //设置报运单的附件数量
        export.setExtNum(extNUm);
        //保存报运单
        exportDao.insertSelective(export);
    }

    @Override
    public void update(Export export) {
        exportDao.updateByPrimaryKeySelective(export);
        List<ExportProduct> exportProducts = export.getExportProducts();

        if (exportProducts != null) {
            for (ExportProduct exportProduct : exportProducts) {
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public PageInfo findAll(ExportExample example, int page, int size) {
        PageHelper.startPage(page, size);
        List<Export> list = exportDao.selectByExample(example);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public void updateE(ExportResult result) {
        Export export = new Export();
        export.setId(result.getExportId());
        export.setState(result.getState());
        export.setRemark(result.getRemark());
        exportDao.updateByPrimaryKeySelective(export);

        Set<ExportProductResult> products = result.getProducts();

        for (ExportProductResult product : products) {
            ExportProduct exportProduct = new ExportProduct();
            exportProduct.setId(product.getExportProductId());
            exportProduct.setTax(product.getTax());
            exportProductDao.updateByPrimaryKeySelective(exportProduct);
        }
    }
}
