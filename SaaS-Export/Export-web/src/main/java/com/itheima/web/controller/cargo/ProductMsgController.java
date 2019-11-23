package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.ProductMessage;
import com.itheima.domain.cargo.ProductMessageExample;
import com.itheima.domain.cargo.ProductMsg;
import com.itheima.domain.cargo.ProductMsgExample;
import com.itheima.service.cargo.ProductMessageService;
import com.itheima.service.cargo.ProductMsgService;
import com.itheima.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cargo/productMsg")
public class ProductMsgController extends BaseController {

    @Reference
    private ProductMsgService productMsgService;

    //查询所有货物的货号和详细信息并且分页显示
    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1")int page,
            @RequestParam(defaultValue = "5")int size
    ){
        ProductMsgExample example = new ProductMsgExample();
        PageInfo pageInfo = productMsgService.findAllProduct(example,page,size);
        request.setAttribute("page",pageInfo);
        return "cargo/productMsg/productMsg-list";
    }

    //新增货号和详细信息跳转
    @RequestMapping("/toAdd")
    public String toAdd(){
        return "cargo/productMsg/productMsg-add";
    }

    //保存货号和货物详细信息
    @RequestMapping("/save")
    public String save(ProductMsg productMsg){
        productMsgService.saveProductMsg(productMsg);
        return "redirect:/cargo/productMsg/list.do";
    }

    //查询要修改的货号的货物详细信息
    @RequestMapping("/toUpdate")
    public String toUpdate(String productNo){
        ProductMsg productMsg = productMsgService.findByProductId(productNo);
        request.setAttribute("page",productMsg);
        return "cargo/productMsg/productMsg-update";
    }

    //修改
    @RequestMapping("edit")
    public String edit(ProductMsg productMsg){
        productMsgService.updateProductMsg(productMsg);
        return "redirect:/cargo/productMsg/list.do";
    }

    //删除
    @RequestMapping("/delete")
    public String delete(String productNo){
        productMsgService.delete(productNo);
        return "redirect:/cargo/productMsg/list.do";
    }


}
