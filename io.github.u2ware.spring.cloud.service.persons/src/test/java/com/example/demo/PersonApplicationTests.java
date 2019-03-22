package com.example.demo;

import com.example.demo.persons.Person;
import com.example.demo.persons.PersonRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonApplicationTests {


	protected Log logger = LogFactory.getLog(getClass());


	private @Autowired PersonRepository personRepository;

	@Test
	public void contextLoads() {

		Person p1 = new Person();
		logger.info(p1);

		p1 = personRepository.save(p1);
		logger.info(p1);

		Person p2 = Person.builder().name("aaaa").build();
		logger.info(p2);

		p2 = personRepository.save(p2);
		logger.info(p1);
	}

}
