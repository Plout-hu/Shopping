package com.study.shopping.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode {
    private String username;
    private String code;
    private String email;
    private LocalDateTime createTime;
    private LocalDateTime expireTime;
}
