package com.example.demo;

import com.example.demo.persons.Person;
import com.example.demo.persons.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class PersonApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(PersonApplication.class, args);
	}
	
	private @Autowired PersonRepository personRepository;

	@Override
	public void run(String... args) throws Exception {

		personRepository.save(Person.builder().name("lorem ipsum1").build());
		personRepository.save(Person.builder().name("aaaa").build());
		personRepository.save(Person.builder().name("bbbb").build());
	}

}
