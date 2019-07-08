package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	
	@Controller
	public static class HomeController {
		
		@RequestMapping("/")
		public @ResponseBody String home() {
			return "<h1>Welcome to "+Application.class.getName()+"</h1>";
		}
	}	
	
}
