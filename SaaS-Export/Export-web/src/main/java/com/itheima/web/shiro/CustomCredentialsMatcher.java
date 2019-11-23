package com.itheima.web.shiro;

import com.itheima.commons.utils.Encrypt;
import com.itheima.domain.system.User;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.subject.PrincipalCollection;

public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //token是用户登录的时候输入的数据,先将token转化为UsernamePasswordToken类型的数据
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        //然后从upToken中取出来用户输入的密码
        String password = String.valueOf(upToken.getPassword());
        //在密码进行加密的时候是用密码和用户名一起然后加密成密码，加上的用户名叫做salt
        String email = upToken.getUsername();
        //利用common中的加密util进行加密
        String md5Password = Encrypt.md5(password, email);

        //从安全数据中找到该用户在数据库中的密码
        String dbPassword = String.valueOf(info.getCredentials());
        //然后进行两个密码的比较，成功返回true，失败false,然后就会转到授权的方法AuthRealm
        return md5Password.equals(dbPassword);
    }
}
