package com.study.shopping.mapper;

import com.study.shopping.pojo.Customer;
import com.study.shopping.pojo.LoginHistory;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CustomerMapper {
    /**
     * 通过username和password来查询客户是否存在
     *
     * @param customer 存储用户的password和username信息用来查询
     * @return 返回查询到的用户信息
     */
    @Select("select * from shopping.customer where username=#{username} and password=MD5(#{password})")
    Customer getByUsernameAndPassWord(Customer customer);

    /**
     * 查询用户登录历史
     *
     * @return 返回历史记录结果
     */
    @Select("select * from shopping.loginhistory where username=#{username}")
    LoginHistory getLoginHistory(Customer customer);

    /**
     * 解封用户
     *
     * @param customer 需要解封的用户
     */
    @Delete("delete from shopping.loginhistory where username=#{username}")
    void unseal(Customer customer);

    /**
     * 新增用户的登录历史
     *
     * @param loginHistory 用户登录历史
     */
    @Insert("insert into shopping.loginhistory (username, consecutive_failures, last_fail_time) VALUES (#{username},#{consecutiveFailures}," +
            "#{lastFailTime}) on DUPLICATE KEY UPDATE consecutive_failures=#{consecutiveFailures},last_fail_time=#{lastFailTime}")
    void addHistory(LoginHistory loginHistory);


    List<Customer> list(Integer page, Integer pageSize, LocalDate start, LocalDate end, Integer id, String name);

    /**
     * 通过Id删除员工信息
     *
     * @param ids 需要删除的员工id集合
     */
    void delete(List<Integer> ids);

    /**
     * 添加客户
     *
     * @param customer 需要添加的客户信息
     */
    @Insert("insert into shopping.customer (username, user_level, register_time, consumption_amount, tel, email, password, power)" +
            " VALUES (#{username},#{userLevel},#{registerTime},#{consumptionAmount},#{tel},#{email},MD5(#{password}),#{power})")
    void save(Customer customer);

    @Select("select * from shopping.customer where user_id=#{id}")
    Customer getById(Integer id);

    @Update("update shopping.customer set password=MD5(#{password}) where user_id=#{userId}")
    void resetPassword(Customer customer);

    @Select("select * from shopping.customer where username=#{username} and email=#{email}")
    Customer checkEmail(Customer customer);
    @Update("update shopping.customer set password=MD5(#{password}) where username=#{username}")
    void changePassword(Customer customer);
    @Select("select * from shopping.customer where username=#{username}")
    Customer checkUsername(String username);
    @Update("update shopping.loginhistory set consecutive_failures=#{consecutiveFailures},last_fail_time=#{lastFailTime} where username=#{username}")
    void update(LoginHistory loginHistory);
    @Update("update shopping.customer set user_level=#{userLevel},consumption_amount=#{consumptionAmount} where user_id=#{userId}")
    void updateCustomer(Customer customer);
}
