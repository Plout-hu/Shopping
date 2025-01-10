package com.study.shopping.service.impl;

import com.study.shopping.service.MailSendService;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailSendServiceImpl implements MailSendService {
    @Autowired
    private  JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private  String from;
    public boolean sendEmail(String email, String content) {
        log.info("发邮件:{}",from);
        //获取MimeMessage对象
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true);
            //邮件发送人
            messageHelper.setFrom(from);
            //邮件接收人,设置多个收件人地址
            InternetAddress[] internetAddressTo = InternetAddress.parse(email);
            messageHelper.setTo(internetAddressTo);
            //messageHelper.setTo(to);
            //邮件主题
            message.setSubject("注册验证码");
            //邮件内容，html格式
            messageHelper.setText(content, true);
            //发送
            mailSender.send(message);
            //日志信息
            log.info("发送邮件成功:{}",message);
            return true;
        } catch (Exception e) {
            log.info("发送邮件出错:{}",e.getMessage());
            return  false;
        }
    }
}
