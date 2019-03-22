package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;



@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableZuulProxy
public class EdgeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdgeServerApplication.class, args);
	}

	// @Bean
	// public WebMvcConfigurer webMvcConfigurer() {
	// 	return new WebMvcConfigurerAdapter() {
	// 		@Override
	// 		public void addCorsMappings(CorsRegistry registry) {
	// 			registry
	// 				.addMapping("/**")
	// 				.allowedOrigins("*")
	// 				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
	// 				.allowCredentials(true)
	// 				.maxAge(3600);
	// 		}
	// 	};
	// }

	// @Bean
	// public CorsConfigurationSource corsConfigurationSource() {
	// 	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

	// 	CorsConfiguration configuration = new CorsConfiguration();
	// 	configuration.setMaxAge(86400l);
	// 	configuration.setAllowedOrigins(Arrays.asList("*"));
	// 	configuration.setAllowCredentials(true);
	// 	configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
	// 	configuration.setAllowedHeaders(Arrays.asList("*"));
	// 	configuration.setExposedHeaders(Arrays.asList("Authorization", "xsrf-token", "content-type", "content-Disposition", "content-transfer-encoding"));
	// 	source.registerCorsConfiguration("/**", configuration);
	// 	return source;
	// }
    	
    // @Bean
    // public FilterRegistrationBean corsFilter() {
    //     CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        

    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", config);

    //     FilterRegistrationBean bean = new FilterRegistrationBean();
    //     bean.setOrder(0);
    //     return bean;
    // } 

	@Bean
	public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

}
