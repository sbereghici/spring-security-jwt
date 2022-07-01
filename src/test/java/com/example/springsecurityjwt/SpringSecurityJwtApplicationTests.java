package com.example.springsecurityjwt;

import com.example.springsecurityjwt.jwt.JwtConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringSecurityJwtApplicationTests {
	@Autowired
	private JwtConfiguration jwtConfiguration;
	@Test
	void contextLoads() {
		System.out.println(jwtConfiguration.toString());
	}

}
