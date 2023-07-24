package com.study.shopping.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private int userId;
    private String username;
    private int userLevel;
    private LocalDateTime registerTime;
    private float consumptionAmount;
    private String tel;
    private String email;
    private String password;
    private int power;
}
