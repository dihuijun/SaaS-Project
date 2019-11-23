package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.cargo.ProductMessageDao;
import com.itheima.domain.cargo.*;
import com.itheima.service.cargo.ExtCProdctService;
import com.itheima.service.cargo.FactoryService;
import com.itheima.service.cargo.ProductMessageService;
import com.itheima.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtcPoductController extends BaseController {

    @Reference
    private ExtCProdctService extCProdctService;

    @Reference
    private FactoryService factoryService;

    @Reference
    private ProductMessageService productMessageService;

    @RequestMapping("/list")
    public String list(String contractId,String contractProductId,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "5") int size
                       ){
        FactoryExample example = new FactoryExample();
        FactoryExample.Criteria criteria = example.createCriteria();
        criteria.andCtypeEqualTo("附件");
        List<Factory> list = factoryService.findAll(example);
        request.setAttribute("factoryList",list);



        //查询所有货号并且封装到前台页面
        List<ProductMessage> productMessageList = productMessageService.findAll();
        request.setAttribute("productList",productMessageList);



        ExtCproductExample example1 = new ExtCproductExample();
        ExtCproductExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andContractProductIdEqualTo(contractProductId);

        PageInfo info = extCProdctService.findAll(example1, page, size);
        request.setAttribute("page",info);
        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);
        return "cargo/extc/extc-list";
    }

    //根据货号查询货物的详情封装到前台
    @RequestMapping("/findById")
    public @ResponseBody
    String findById(String productNo){
        ProductMessage productMessage = productMessageService.findByProductId(productNo);
        String productDesc = productMessage.getProductDesc();
        return productDesc;
    }



    @RequestMapping("/edit")
    public String edit(String contractId,String contractProductId, ExtCproduct extCproduct){

        extCproduct.setCompanyId(companyId);
        extCproduct.setCompanyName(companyName);

        if (StringUtils.isEmpty(extCproduct.getId())){
            extCProdctService.save(extCproduct);
        }else {
            extCProdctService.update(extCproduct);
        }

        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);
        return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;
    }


    @RequestMapping("/toUpdate")
    public String toUpdate(String id,String contractId,String contractProductId){
        //查询出来所有的厂家封装到修改页面
        FactoryExample example = new FactoryExample();
        FactoryExample.Criteria criteria = example.createCriteria();
        criteria.andCtypeEqualTo("附件");
        List<Factory> list = factoryService.findAll(example);
        request.setAttribute("factoryList",list);

        //查询当前要修改的附件回显到修改页面
        ExtCproduct extCproduct1 = extCProdctService.findById(id);
        request.setAttribute("extCproduct",extCproduct1);
        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);
        return "cargo/extc/extc-update";

    }

    @RequestMapping("/delete")
    public String delete(String id,String contractId,String contractProductId){
        extCProdctService.delete(id);
        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);

        return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;
    }

}
