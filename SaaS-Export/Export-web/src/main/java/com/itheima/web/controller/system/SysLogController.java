package com.itheima.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.itheima.service.system.SysLogService;
import com.itheima.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/system/log")
public class SysLogController extends BaseController {

    @Autowired
    private SysLogService sysLogService;

    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        PageInfo pageInfo = sysLogService.findAll(companyId, page, size);
        request.setAttribute("page",pageInfo);
        return "system/log/log-list";
    }



}
