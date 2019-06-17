package io.github.u2ware.spring.data.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @EnableMapRepositories("io.github.u2ware.spring.data.rest.keyvalue")
// @EnableJpaRepositories("io.github.u2ware.spring.data.rest.jpa")
//@EnableLdapRepositories("io.github.u2ware.spring.data.rest.ldap")
//@EnableWhatRepositories("io.github.u2ware.spring.data.rest.what")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


}
