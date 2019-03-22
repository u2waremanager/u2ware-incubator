package com.example.demo;

import com.example.demo.members.Member;
import com.example.demo.members.MemberRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class MemberApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(MemberApplication.class, args);
	}
	
	private @Autowired MemberRepository memberRepository;

	@Override
	public void run(String... args) throws Exception {
		memberRepository.save(Member.builder().count(1).build());
		memberRepository.save(Member.builder().count(2).build());
		memberRepository.save(Member.builder().count(3).build());
	}

	@RestController
	public static class BaseController{
		@HystrixCommand
		@RequestMapping(value="/base")
		public String base() {
			return getClass().getName()+" Node 1";
		}
	}

}
