package com.example.demo;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2CallbackController{

    @RequestMapping(value = {"/", "/logon/*"}, method = RequestMethod.GET)
	public Map<String,Object> home(Principal principal) {

		Map<String,Object> r = new HashMap<>();
		r.put("current", System.currentTimeMillis());
		r.put("principal", principal);
		return r;
	}

}