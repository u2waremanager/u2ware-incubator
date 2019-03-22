package com.example;

import java.util.HashMap;
import java.util.Map;
import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController{


    @RequestMapping(value={"/", "/logon/github"})
    public Map<String,Object> welcome(Principal principal){
        Map<String,Object> r = new HashMap<>();
        r.put("current", System.currentTimeMillis());
		r.put("principal", principal);
        return r;
    }

    @RequestMapping("/info")
    public Principal resource(Principal principal) {
        return principal;
    }    



}