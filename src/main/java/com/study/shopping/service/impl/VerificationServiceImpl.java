package com.study.shopping.service.impl;

import com.study.shopping.mapper.CustomerMapper;
import com.study.shopping.mapper.VerificationCodeMapper;
import com.study.shopping.pojo.Customer;
import com.study.shopping.pojo.VerificationCode;
import com.study.shopping.service.MailSendService;
import com.study.shopping.service.VerificationCodeService;
import com.study.shopping.utils.Generator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class VerificationServiceImpl implements VerificationCodeService {
    @Autowired
    private VerificationCodeMapper verificationCodeMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private MailSendService mailSendService;
    @Override
    public VerificationCode getCode(Customer customer) {
        return verificationCodeMapper.getCode(customer.getUsername(),customer.getEmail());
    }
    @Override
    public boolean sendEmail(Customer customer) {
        String code= Generator.generateCode(4);
        String content="欢迎注册“易购之家”购物平台，您地验证码为"+code;
        boolean success= mailSendService.sendEmail(customer.getEmail(), content);
//        if(!success){
//            return false;
//        }
        log.info(content);
        customer.setCode(code);
        VerificationCode   verificationCode = new VerificationCode(customer.getUsername(), code, customer.getEmail(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(5));
        verificationCodeMapper.deleteByUsernameAndEmail(customer.getUsername(),customer.getEmail());
        verificationCodeMapper.insertCode(verificationCode);
        return true;
    }
}
