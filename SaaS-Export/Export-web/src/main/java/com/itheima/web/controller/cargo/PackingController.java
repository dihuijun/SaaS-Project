package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.Packing;
import com.itheima.domain.cargo.PackingExample;
import com.itheima.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cargo/packing")
public class PackingController extends BaseController {

    //报运单点击装箱就会跳转到这里
    @RequestMapping("/toPacking")
    public String toPacking(String id){
        request.setAttribute("id",id);
        return "cargo/packing/pack-toPacking";
    }
    //装箱单点击保存之后的处理方式
    @RequestMapping("/edit")
    public String edit(Packing packing,String exportIds){

    }





    /*@RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1")int page,
            @RequestParam(defaultValue = "5")int size
    ){
        //查询所有的报运单并且分页显示
        PageInfo pageInfo = packingService.findAll(page,size);
        request.setAttribute("page",pageInfo);
        return "/cargo/packing/packing-list";
    }*/


}
