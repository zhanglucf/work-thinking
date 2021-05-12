package com.github.zhangkaitao.shiro.chapter3;

import com.alibaba.druid.sql.visitor.functions.If;
import junit.framework.Assert;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.junit.Test;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-26
 * <p>Version: 1.0
 */
public class AuthorizerTest extends BaseTest {

    @Test
    public void testIsPermitted() {
        login("classpath:shiro-authorizer.ini", "zhang", "123");
        //判断拥有权限：user:create
//        subject().checkPermission("user1:update");
//        subject().checkPermission("user1:update");
//        subject().checkPermission("user1:delete");
//        subject().checkPermission("user2:update");
//        subject().checkPermission("user3:update");
//        //通过二进制位的方式表示权限
//        Assert.assertTrue(subject().isPermitted("+user1+2"));//新增权限
//        Assert.assertTrue(subject().isPermitted("+user1+8"));//查看权限
//        Assert.assertTrue(subject().isPermitted("+user2+10"));//新增及查看
//
//        Assert.assertFalse(subject().isPermitted("+user1+4"));//没有删除权限
        subject().checkPermission("menu:view");
        Assert.assertTrue(subject().isPermitted("menu:view"));//通过MyRolePermissionResolver解析得到的权限
    }

    @Test
    public void testIsPermitted2() {
        login("classpath:shiro-jdbc-authorizer.ini", "zhang", "123");
        //判断拥有权限：user:create
//        subject().checkPermission("user1:update");
//
//        subject().checkPermission("user2:update");
//        //通过二进制位的方式表示权限
//        subject().checkPermission("+user1+2");//新增权限
//        subject().checkPermission("+user1+8");//查看权限
//        subject().checkPermission("+user2+10");//新增及查看

        subject().checkPermission("+user1+4");//没有删除权限

        //通过MyRolePermissionResolver解析得到的权限
        subject().checkPermission("menu:view");

    }



}
