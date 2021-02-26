package io.github.u2ware.sample.userAccounts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import io.github.u2ware.sample.core.AuditingEntity;
import io.github.u2ware.sample.core.UserAccount;
import io.github.u2ware.sample.core.UserAccount.UserAccountAuthority;


@Component
public class UserAccountTokenAuditing implements AuditorAware<AuditingEntity>{

	protected Log logger = LogFactory.getLog(getClass());

	public UserAccountToken getCurrentAuthentication() {
		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(ClassUtils.isAssignableValue(UsernamePasswordAuthenticationToken.class, auth)) {
			UserAccountToken token = (UserAccountToken)auth;
			return token;
			
		}else if(ClassUtils.isAssignableValue(UserAccountToken.class, auth)) {
			UserAccountToken token = (UserAccountToken)auth;
			return token;
		}
		throw new RuntimeException();
	}

	
	public WebAuthenticationDetails getCurrentDetails() {
		return (WebAuthenticationDetails)getCurrentAuthentication().getDetails();
	}
	public Jwt getCurrentPrincipal(){
		return getCurrentAuthentication().getToken();
	}
	public String getCurrentPrincipalName(){
		return getCurrentAuthentication().getName();
	}
	public UUID getCurrentPrincipalUuid() {
		return UUID.fromString(getCurrentPrincipal().getId());
	}
	
	public UserAccount getCurrentUserAccount(){
		return (UserAccount)getCurrentAuthentication().getTokenAttributes().get(UserAccountToken.USER_ACCOUNT);
	}

	public Collection<UserAccountAuthority> getCurrentUserAccountAuthorities() {
		List<UserAccountAuthority> reponse = new ArrayList<>();
		getCurrentAuthentication().getAuthorities().forEach(a->{reponse.add(new UserAccountAuthority(a.getAuthority()));});
		return reponse;
	}

    public String[] getRoles() {
		Collection<GrantedAuthority> authorities = getCurrentAuthentication().getAuthorities();
        String[] roles = new String[authorities.size()];
        int i = 0;
		for(GrantedAuthority authority : authorities) {
            roles[i] = authority.getAuthority();
            i++;
        }
        return roles;
    }
	
	public boolean hasRole(String role) {
		Collection<GrantedAuthority> authorities = getCurrentAuthentication().getAuthorities();
		for(GrantedAuthority authority : authorities) {
			if(authority.getAuthority().equals(role)) {
				return true;
			}
		}
		return false;
	}
	
	
	
	@Override
	public Optional<AuditingEntity> getCurrentAuditor() {
		AuditingEntity auditing = new AuditingEntity();
		auditing.setCurrentUser(getCurrentPrincipalUuid());
		if(getCurrentDetails() != null)
		auditing.setCurrentAddress(getCurrentDetails().getRemoteAddress());
		return Optional.of(auditing);
	}
}
