package com.study.shopping.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.study.shopping.mapper.CustomerMapper;
import com.study.shopping.pojo.Customer;
import com.study.shopping.pojo.LoginHistory;
import com.study.shopping.pojo.PageBean;
import com.study.shopping.service.CustomerService;
import com.study.shopping.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.study.shopping.utils.JwtUtils.parseJWT;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 用户登录操作
     *
     * @param customer
     * @return
     */
    @Override
    public Customer login(Customer customer) {

        return customerMapper.getByUsernameAndPassWord(customer);
    }

    /**
     * 查询用户是否被封禁
     *
     * @return
     */
    @Override
    public boolean checkUnseal(Customer customer) {
        LoginHistory loginHistory = customerMapper.getLoginHistory(customer);
        if (loginHistory != null && loginHistory.getConsecutiveFailures() == 5 && Duration.between(loginHistory.getLastFailTime(), LocalDateTime.now()).toHours() < 12) {
            log.info("账户未解封，不可以登录");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 解封用户
     *
     * @param customer
     */
    @Override
    public void unseal(Customer customer) {
        customerMapper.unseal(customer);
    }

    /**
     * 密码错误时。更新密码错误次数
     *
     * @param customer
     */
    @Override
    public void addFailLoginTimes(Customer customer) {
        LoginHistory loginHistory = customerMapper.getLoginHistory(customer);
        if (loginHistory == null) {
            loginHistory = new LoginHistory();
            loginHistory.setUsername(customer.getUsername());
            loginHistory.setConsecutiveFailures(loginHistory.getConsecutiveFailures() + 1);
            LocalDateTime now = LocalDateTime.now();
            loginHistory.setLastFailTime(now);
            customerMapper.addHistory(loginHistory);
        }else {
            loginHistory.setConsecutiveFailures(loginHistory.getConsecutiveFailures() + 1);
            LocalDateTime now = LocalDateTime.now();
            loginHistory.setLastFailTime(now);
            customerMapper.update(loginHistory);
        }
    }

    /**
     * 列出查询的员工信息
     *
     * @param page     第几页
     * @param pageSize 一页显示数据个数
     * @param start    筛选注册开始时间
     * @param end      筛选注册结束时间
     * @param id       用户id
     * @param name     用户username
     * @return 返回查询到的所有结果
     */
    @Override
    public PageBean listCustomer(Integer page, Integer pageSize, LocalDate start, LocalDate end, Integer id, String name) {
        PageHelper.startPage(page, pageSize);
        List<Customer> customerList = customerMapper.list(page, pageSize, start, end, id, name);
        Page<Customer> customerPage = (Page<Customer>) customerList;
        return new PageBean(customerPage.getTotal(), customerPage.getResult());
    }

    /**
     * 通过id删除客户
     *
     * @param ids 需要删除的客户的id集合
     */
    @Override
    public void delete(List<Integer> ids) {
        customerMapper.delete(ids);
    }

    /**
     * 添加客户
     *
     * @param customer 需要添加的客户信息
     */
    @Override
    public void save(Customer customer) {
        customer.setRegisterTime(LocalDateTime.now());
        if (customer.getPassword() == null) {
            customer.setPassword("123456");
        }
        customerMapper.save(customer);
    }

    /**
     * 解析token获取用户相关信息
     *
     * @param token 用户token
     * @return 返回用户相关信息
     */
    @Override
    public Map<String, Object> getInform(String token) {
        return JwtUtils.parseJWT(token);
    }

    @Override
    public Customer getById(Integer id) {
        return customerMapper.getById(id);
    }

    @Override
    public void resetPassword(Customer customer) {
        customerMapper.resetPassword(customer);
    }

    @Override
    public Customer checkEmail(Customer customer) {
        return customerMapper.checkEmail(customer);
    }

    @Override
    public void changePassWord(Customer customer) {
        customerMapper.changePassword(customer);
    }

    @Override
    public Customer checkUsername(String username) {
        return customerMapper.checkUsername(username);
    }

    @Override
    public void register(Customer customer) {
        customer.setRegisterTime(LocalDateTime.now());
        customer.setPower(0);
        customer.setUserLevel(0);
        customerMapper.save(customer);
    }
}
