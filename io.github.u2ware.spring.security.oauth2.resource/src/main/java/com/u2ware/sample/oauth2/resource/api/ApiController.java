package com.u2ware.sample.oauth2.resource.api;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

	
    @RequestMapping("/")
    public Principal resource(Principal principal) {
        return principal;
    }
//

}
