package io.github.u2ware.sample.userAccounts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import io.github.u2ware.sample.core.UserAccount;
import io.github.u2ware.sample.core.UserAccount.UserAccountAuthority;
import io.github.u2ware.sample.core.AuditingEntity;


@Component
public class UserAccountTokenAuditing implements AuditorAware<AuditingEntity>{

	protected Log logger = LogFactory.getLog(getClass());

	public UserAccountToken getCurrentAuthentication() {
		UserAccountToken token = (UserAccountToken)SecurityContextHolder.getContext().getAuthentication();
		return token;
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
	
	@Override
	public Optional<AuditingEntity> getCurrentAuditor() {
		AuditingEntity auditing = new AuditingEntity();
		auditing.setCurrentUser(getCurrentPrincipalUuid());
		auditing.setCurrentAddress(getCurrentDetails().getRemoteAddress());
		return Optional.of(auditing);
	}
}
