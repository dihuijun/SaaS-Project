package com.itheima.web.shiro;

import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.service.system.ModuleService;
import com.itheima.service.system.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;


    //用户授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //因为PrincipalCollection这个中存放的是一个collection集合，只有一个的时候也是集合，所以要先从集合中取出来User的安全数据
        User user = (User) principalCollection.getPrimaryPrincipal();

        //通过现在已经知道的user去查询其应该拥有的模块
        List<Module> modules = moduleService.findModuleByUserId(user);

        //构造集合存入当前用户拥有的菜单
        Set<String> set = new HashSet<String>();

        for (Module module : modules) {
            set.add(module.getName());
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(set);

        return info;
    }

    //身份认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //将authenticationToken 转化为Token
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;

        //获取传过来的用户名
        String email = upToken.getUsername();
        //根据传输过来的用户名（email） 去数据库查询对应的数据

        String password = String.valueOf(upToken.getPassword());


        User user = userService.findByEmail(email);

        //Object principal 安全数据  就是根据email从数据库查询出来的user
        //Object credentials 用户在数据库中的密码，因为使用加密方法，数据库中的密码是加密过的
        //String realmName 没啥用，一般用类名
        AuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());

        //返回AuthenticationInfo,下一步就会去进行密码比对，跳转到CustomCredentialsMatcher方法中
        return info;

    }
}
