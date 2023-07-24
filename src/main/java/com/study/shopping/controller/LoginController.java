package com.study.shopping.controller;

import com.study.shopping.pojo.Customer;
import com.study.shopping.pojo.LoginHistory;
import com.study.shopping.pojo.Result;
import com.study.shopping.service.CustomerService;
import com.study.shopping.utils.JwtUtils;
import com.study.shopping.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    private CustomerService customerService;

    /**
     * 通过用户名和密码登录
     *
     * @param customer
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody Customer customer) {
        log.info("登录请求,{}", customer);
        boolean unseal = customerService.checkUnseal(customer);
        if (!unseal) {
            return Result.error("无法登录，该账号因为多次密码错误已被暂时限制登录");
        }
        Customer customer1 = customerService.login(customer);
        if (customer1 != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", customer1.getUsername());
            claims.put("id", customer1.getUserId());
            claims.put("power", customer1.getPower());
            String jwt = JwtUtils.generateJwt(claims);
            customerService.unseal(customer);
            return Result.success(jwt);
        }
        customerService.addFailLoginTimes(customer);
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/checkEmail")
    public Result checkCode(@RequestBody Customer customer) {
        log.info("验证邮箱：{}", customer);
        Customer customer1 = customerService.checkEmail(customer);
        if (customer1 != null) {
            return Result.success();
        }
        return Result.error("账户名与邮箱不匹配");
    }

    @PostMapping("/checkUsername")
    public Result checkUsername(@RequestBody Customer customer) {
        log.info("验证用户名：{}", customer);
        Customer customer1 = customerService.checkUsername(customer.getUsername());
        if (customer1 == null) {
            return Result.success();
        } else {
            return Result.error("该用户名已经存在");
        }
    }
}
