package com.hpe.shiro;


import com.hpe.entity.User;
import com.hpe.service.IUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @todo 自定义 Realm,查询数据库并返回正确的数据
 * @author Hans
 * @time 2018下午6:11:20
 *
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private IUserService userSer;


    /**
     * @see , 授权,在配有缓存的情况下，只加载一次。
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        //当前登录用户，账号
        String userCode = principal.toString();
        System.out.println("当前登录用户:"+userCode);
        Set<String> roles = new HashSet<String>();


        SimpleAuthorizationInfo info = null;
        info = new SimpleAuthorizationInfo(roles);
        return info;
    }

    /**
     * @see , 认证登录，查询数据库，如果该用户名正确，得到正确的数据，并返回正确的数据
     * 		AuthenticationInfo的实现类SimpleAuthenticationInfo保存正确的用户信息
     *
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.将token转换为UsernamePasswordToken
        UsernamePasswordToken userToken = (UsernamePasswordToken)token;
        //2.获取token中的登录账户
        String userCode = userToken.getUsername();
        //3.查询数据库，是否存在指定的用户名和密码的用户(主键/账户/密码/账户状态/盐)
        User us = new User();
        us = userSer.findUserByUserCode(userCode);
        //4.1 如果没有查询到，抛出异常
        if( us == null ) {
            throw new UnknownAccountException("账户"+userCode+"不存在！");
        }

        //4.2 如果查询到了，封装查询结果，
        Object principal = us.getUserName();
        // 获取数据库中的密码
        Object credentials = us.getPasswd();
        // realmName：当前realm对象的name，调用父类的getName()方法即可
        String realmName = getName();
        String salt = us.getSalt();
        //获取盐，用于对密码在加密算法(MD5)的基础上二次加密ֵ 盐为注册用户时随机生成的盐
        ByteSource byteSalt = ByteSource.Util.bytes(salt);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, byteSalt, realmName);
        //5. 返回给调用login(token)方法
        return info;
    }

}
