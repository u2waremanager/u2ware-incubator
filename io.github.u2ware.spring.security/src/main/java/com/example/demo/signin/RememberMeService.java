package com.example.demo.signin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

//@Component
public class RememberMeService implements RememberMeServices, LogoutHandler{

    protected Log logger = LogFactory.getLog(getClass()); 

    @Override
	public Authentication autoLogin(HttpServletRequest request, HttpServletResponse response) {
        logger.info("autoLogin");
        //PersistentRememberMeToken token = tokenRepository.getTokenForSeries(presentedSeries);
        //tokenRepository.removeUserTokens;
        //tokenRepository.updateToken(
		return null;
	}

	@Override
	public void loginFail(HttpServletRequest request, HttpServletResponse response) {
        logger.info("loginFail");
	}

	@Override
	public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        logger.info("loginSuccess");
        //tokenRepository.createNewToken(persistentToken);
    }
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logger.info("logout");
		if (authentication != null) {
			//tokenRepository.removeUserTokens(authentication.getName());
		}
    }

	public static final String DEFAULT_PARAMETER = "remember-me";
    
    private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    
	protected Authentication createAuthentication(HttpServletRequest request, String key, UserDetails user) {
		userDetailsChecker.check(user);
		RememberMeAuthenticationToken auth = new RememberMeAuthenticationToken(key, user, authoritiesMapper.mapAuthorities(user.getAuthorities()));
		auth.setDetails(authenticationDetailsSource.buildDetails(request));
		return auth;
    }

}
