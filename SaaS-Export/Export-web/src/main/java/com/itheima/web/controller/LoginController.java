package com.itheima.web.controller;


import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.service.system.ModuleService;
import com.itheima.service.system.UserService;
import com.itheima.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

	@RequestMapping("/login")
	public String login(String email,String password) {

        System.out.println(email);
	    try {

	    //登录方法的改造，加入shiro安全框架
        //创建subject
        Subject subject = SecurityUtils.getSubject();

        //通过email和password创建token
        UsernamePasswordToken upToken = new UsernamePasswordToken(email,password);

        //利用subject的login方法登录跳转到AuthRealm身份验证方法中
        subject.login(upToken);


        //通过subject取到安全数据
        User user  =(User) subject.getPrincipal();

            System.out.println(user);
        if (user!=null) {
            //查找当前用户对应的模块

            List<Module> moduleList = moduleService.findModuleByUserId(user);
            session.setAttribute("modules", moduleList);
            session.setAttribute("loginUser", user);
            return "home/main";
        }else {
            request.setAttribute("error", "用户不存在！");
            return "forward:login.jsp";
        }
        }catch (Exception e){
            request.setAttribute("error","用户名或者密码不正确！");
            return "forward:login.jsp";
        }

	}

    //退出
    @RequestMapping(value = "/logout",name="用户登出")
    public String logout(){
        //SecurityUtils.getSubject().logout();   //登出
        return "forward:login.jsp";
    }

    @RequestMapping("/home")
    public String home(){
	    return "home/home";
    }
}
