package io.github.u2ware.sample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

@Configuration
@EnableJpaAuditing
public class ApplicationDataRestConfig {

	@Bean
	public CustomRepositoryRestExceptionHandler customRepositoryRestExceptionHandler() {
		return new CustomRepositoryRestExceptionHandler();
	}
	
	@ControllerAdvice
	public class CustomRepositoryRestExceptionHandler {

	    @ExceptionHandler(HttpServerErrorException.class)
	    ResponseEntity<?> handleNotFound(HttpServerErrorException o_O) {
	    	return ResponseEntity.status(o_O.getStatusCode()).build();
	    }
	}
	
}
