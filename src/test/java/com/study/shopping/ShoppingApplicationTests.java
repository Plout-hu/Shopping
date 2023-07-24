package com.study.shopping;

import com.study.shopping.utils.MD5Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class ShoppingApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	void testMd5(){
		String s="ynuadmin";
		System.out.println(MD5Utils.MD5Lower(s, "hxg"));
	}

}
