package io.github.u2ware.sample;

import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserInfoController {

    private Log logger = LogFactory.getLog(getClass());


	@GetMapping("/user/info")
	public @ResponseBody Object userInfo( Principal principal) {

        logger.info("-----------------------------------");
        logger.info("principal: "+principal);
        logger.info("-----------------------------------");
		return principal;
	}

    @GetMapping("/")
    public @ResponseBody Object index() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info(authentication);
        logger.info(authentication.getClass());

        return authentication;
    }


    @RequestMapping("/hello")
    public @ResponseBody String hello() {
        return "hello: " + System.currentTimeMillis();
    }
    
}
