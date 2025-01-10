package com.study.shopping.controller;

import com.study.shopping.pojo.Customer;
import com.study.shopping.pojo.PageBean;
import com.study.shopping.pojo.Result;
import com.study.shopping.pojo.VerificationCode;
import com.study.shopping.service.CustomerService;
import com.study.shopping.service.VerificationCodeService;
import com.study.shopping.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private VerificationCodeService verificationCodeService;

    /**
     * 查询客户
     *
     * @param page     第几页
     * @param pageSize 一页数据个数
     * @param id       客户id
     * @param name     客户username
     * @param begin    客户注册开始时间
     * @param end      客户注册结束时间
     * @return 分页查询结果
     */
    @GetMapping
    public Result listCustomer(@RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "5") Integer pageSize,
                               Integer id, String name,
                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("分页查询,参数：{}，{}，{}，{},{},{}", page, pageSize, begin, end, id, name);
        PageBean pageBean = customerService.listCustomer(page, pageSize, begin, end, id, name);
        return Result.success(pageBean);
    }

    /**
     * 根据id删除客户信息
     *
     * @param ids 需要删除的客户的id集合
     * @return 返回删除成功信息
     */
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids) {
        log.info("删除客户，参数:{}", ids);
        customerService.delete(ids);
        return Result.success();
    }

    /**
     * 通过前端发出的json数据添加客户
     *
     * @param customer 客户信息
     * @return 返回添加成功信息
     */
    @PostMapping
    public Result save(@RequestBody Customer customer) {
        log.info("添加员工:{}", customer);
        customerService.save(customer);
        return Result.success();
    }

    /**
     * 通过token验证用户权限
     * @param token 前端携带的token值
     * @return 若该用户拥有管理员权限，返回true，否则，返回false
     */
    @GetMapping("inform")
    public Result inform(@RequestHeader("token")String token){
        log.info("token为：{}",token);
        Map<String,Object> right= customerService.getInform(token);
        return Result.success(right);
    }
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        log.info("根据id查询用户信息：{}",id);
        Customer customer=customerService.getById(id);
        return Result.success(customer);
    }
    @PutMapping("/resetPassword")
    public Result resetPassword(@RequestBody Customer customer){
        log.info("重置密码：{}",customer);
        customerService.resetPassword(customer);
        return Result.success();
    }
    @PutMapping("/forgetPassWord/{checkCode}")
    public Result forgetPassword(@RequestBody Customer customer,@PathVariable String checkCode){
        log.info("忘记密码：{}，111{}",customer,checkCode);
        VerificationCode code = verificationCodeService.getCode(customer);
        if (checkCode.toLowerCase().equals(code.getCode().toLowerCase())){
            if(code.getExpireTime().isBefore(LocalDateTime.now())){
                return Result.error("验证码已过期");
            }
            customerService.changePassWord(customer);
            return Result.success();
        }
        return Result.error("验证码错误");
    }
    @GetMapping("/getByUsername/{username}")
    public Result getByUserId(@PathVariable String username){
        log.info("根据用户名查询用户：{}",username);
        Customer customer=customerService.getByUsername(username);
        return Result.success(customer==null);
    }
    @PostMapping("/register/{checkCode}")
    public Result register(@RequestBody Customer customer,@PathVariable String checkCode){
        log.info("注册用户:{},{}",customer,checkCode);
        if(customer.getPower()==1&&!customer.getKey().equals(Constant.REGISTER_ADMIN_KEY)){
            return Result.error("管理员注册密钥错误，注册失败");
        }
        VerificationCode code = verificationCodeService.getCode(customer);
        if (checkCode.toLowerCase().equals(code.getCode().toLowerCase())){
            if(code.getExpireTime().isBefore(LocalDateTime.now())){
                return Result.error("验证码已过期");
            }
            customerService.register(customer);
            return Result.success();
        }
        return Result.error("验证码错误");
    }
}
