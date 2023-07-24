package com.study.shopping.service;

import com.study.shopping.pojo.Customer;
import com.study.shopping.pojo.PageBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface CustomerService {

    Customer login( Customer customer);

    boolean checkUnseal( Customer customer);

    void unseal(Customer customer);

    void addFailLoginTimes(Customer customer);

    PageBean listCustomer(Integer page, Integer pageSize, LocalDate start, LocalDate end, Integer id, String name);

    void delete(List<Integer> ids);

    void save(Customer customer);


    Map<String, Object> getInform(String token);

    Customer getById(Integer id);

    void resetPassword(Customer customer);

    Customer checkEmail(Customer customer);

    void changePassWord(Customer customer);

    Customer checkUsername(String username);

    void register(Customer customer);
}
