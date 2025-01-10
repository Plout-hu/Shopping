package com.study.shopping.mapper;

import com.study.shopping.pojo.VerificationCode;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VerificationCodeMapper {
    @Select("select * from shopping.verification_code where email=#{email} and username=#{username}")
    VerificationCode getCode(String username,String email);
    @Delete("delete from shopping.verification_code where email=#{email} and username=#{username}")
    void deleteByUsernameAndEmail(String username,String email);
    @Insert("insert into shopping.verification_code (username, code, email, create_time, expire_time) VALUES (#{username},#{code},#{email},#{createTime},#{expireTime})")
    void insertCode(VerificationCode verificationCode);
}
