package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.codecentric.boot.admin.config.EnableAdminServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
@EnableHystrixDashboard
@EnableTurbine
public class DashboardServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashboardServerApplication.class, args);
	}

	@Controller
	public static class BaseController{

		@RequestMapping(value="/")
		public String base() {
			return "index.html";
		}
	}

}
