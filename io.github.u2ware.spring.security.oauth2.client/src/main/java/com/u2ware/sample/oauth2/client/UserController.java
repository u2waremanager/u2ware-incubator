package com.u2ware.sample.oauth2.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UserController {

    @Autowired
    private OAuth2RestOperations restTemplate;

	@Value("${config.oauth2.resourceURI}")
    private String resourceURI;

    @RequestMapping("/")
    public Object home() {
        return restTemplate.getForObject(resourceURI, Map.class);
    }
    
}