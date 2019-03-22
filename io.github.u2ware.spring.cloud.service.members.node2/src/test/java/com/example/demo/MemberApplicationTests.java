package com.example.demo;

import com.example.demo.members.Member;
import com.example.demo.members.MemberRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberApplicationTests {

	protected Log logger = LogFactory.getLog(getClass());

	private @Autowired MemberRepository memberRepository;

	@Test
	public void contextLoads() {

		Member p1 = new Member();
		logger.info(p1);

		p1 = memberRepository.save(p1);
		logger.info(p1);

		Member p2 = Member.builder().count(3).build();
		logger.info(p2);

		p2 = memberRepository.save(p2);
		logger.info(p1);
		
	}

}
