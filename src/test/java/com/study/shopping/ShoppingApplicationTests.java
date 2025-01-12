package com.study.shopping;

import com.study.shopping.utils.Generator;
import com.study.shopping.utils.JwtUtils;
import com.study.shopping.utils.MD5Utils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ShoppingApplicationTests {


    /**
     * 测试MD5加密
     */
    @Test
    void testMd5() {
        String s = "ynuadmin";
        System.out.println(MD5Utils.MD5Lower(s, "hxg"));
    }

    /**
     * 测试生成随机验证码
     */
    @Test
    void randomCodeTest() {
        System.out.println(Generator.generateCode(4));
    }

    /**
     * 测试生成jwt
     */
    @Test
    void jwtGenerateTest(){
        Map<String,Object> claims=new HashMap<>();
        claims.put("username", "test1");
        claims.put("id", 1234);
        claims.put("power", 0);
        String jwt = JwtUtils.generateJwt(claims);
        System.out.println(jwt);
    }

    /**
     * 测试解析jwt
     */
    @Test
    void  jwtExplainTest(){
        Map<String,Object> claims=new HashMap<>();
        claims.put("username", "test1");
        claims.put("id", 1234);
        claims.put("power", 0);
        String jwt = JwtUtils.generateJwt(claims);
        Claims claims1 = JwtUtils.parseJWT(jwt);
        System.out.println(claims1);
    }
}
