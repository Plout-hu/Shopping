package com.study.shopping.service;

import com.study.shopping.pojo.Customer;
import com.study.shopping.pojo.VerificationCode;

public interface VerificationCodeService {
    VerificationCode getCode(Customer customer);

    boolean sendEmail(Customer customer);
}
