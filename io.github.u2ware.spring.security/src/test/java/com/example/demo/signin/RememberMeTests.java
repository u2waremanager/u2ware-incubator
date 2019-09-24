 package com.poscoict.rpa.controlroom.api.signin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import com.poscoict.rpa.controlroom.api.ApplicationTests;

/**
 * 
 * @author u2waremanager@gmail.com
 *
 */
public class RememberMeTests extends ApplicationTests{


	@Test
	public void contextLoads() throws Exception {

		/////////////////////////////
		//
		/////////////////////////////
		MvcResult r0 = $.mvc.perform(
				post("/login")
				.param("username", userProperties.getUsername())
				.param("password", userProperties.getPassword())
			).andDo(
				print()
			).andExpect(
				status().is2xxSuccessful()
			).andReturn();


		/////////////////////////////
		//
		/////////////////////////////
		$.mvc.perform(
				get($.uri("/account"))
			).andDo(
				print()
			).andExpect(
				status().is4xxClientError()
			);

		MvcResult r1 = $.mvc.perform(
				get($.uri("/profile"))
				.header("Authorization", r0.getResponse().getHeader("Authorization"))
				.header("Access-Control-Request-Method", "GET")
				.header("Origin", "http://www.someurl.com")
			).andDo(
				print()
			).andExpect(
				status().is2xxSuccessful()
			).andReturn();
		
		
		MvcResult r2 = $.mvc.perform(
				get($.uri("/account"))
				.header("Authorization", r1.getResponse().getHeader("Authorization"))
			).andDo(
				print()
			).andExpect(
				status().is2xxSuccessful()
			).andReturn();


		try {
			$.mvc.perform(
					get($.uri("/account"))
					.header("Authorization", r1.getResponse().getHeader("Authorization"))
				).andDo(
					print()
				).andExpect(
					status().is4xxClientError()
				);
		}catch(Exception e) {
			e.printStackTrace();
		}

	
		$.mvc.perform(
			get($.uri("/account"))
			.header("Authorization", r2.getResponse().getHeader("Authorization"))
		).andDo(
			print()
		).andExpect(
			status().is4xxClientError()
		);
	
	
	}

}
