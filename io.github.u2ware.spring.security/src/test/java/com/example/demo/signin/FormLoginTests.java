package com.poscoict.rpa.controlroom.api.signin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.poscoict.rpa.controlroom.api.ApplicationTests;

/**
 * 
 * @author u2waremanager@gmail.com
 *
 */
public class FormLoginTests extends ApplicationTests{


	public void contextLoads() throws Exception {

		$.mvc.perform(
				post("/login")
				.param("username", userProperties.getUsername()+"sss")
				.param("password", userProperties.getPassword())
			).andDo(
				print()
			).andExpect(
				status().is4xxClientError()
			);

		$.mvc.perform(
				post("/login")
				.param("username", userProperties.getUsername())
				.param("password", userProperties.getPassword())
			).andDo(
				print()
			).andExpect(
				status().is2xxSuccessful()
			);
	}	

}

