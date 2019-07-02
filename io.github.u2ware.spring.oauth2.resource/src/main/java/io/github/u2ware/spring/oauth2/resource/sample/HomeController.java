package io.github.u2ware.spring.oauth2.resource.sample;

import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    private Log logger = LogFactory.getLog(getClass());

    @RequestMapping("/resource")
    public @ResponseBody String resource() {
        return "resource: " + System.currentTimeMillis();
    }
	@GetMapping("/resource/info")
	public Principal user(Principal user) {
		return user;
	}	

    @GetMapping("/index")
    public @ResponseBody Object index() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info(authentication);
        logger.info(authentication.getClass());

        if (authentication.getClass().isAssignableFrom(JwtAuthenticationToken.class)) {
        
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            logger.info("JwtAuthenticationToken: "+jwtToken);

            Jwt jwt = jwtToken.getToken();
            logger.info("OAuth2AuthenticationToken#jwt: "+jwt);
            logger.info("OAuth2AuthenticationToken#jwt: "+jwt.getClaims());
            // logger.info("OAuth2AuthenticationToken#principalName: "+principalName);
            return jwtToken;
        }
        return "";
    }


}
