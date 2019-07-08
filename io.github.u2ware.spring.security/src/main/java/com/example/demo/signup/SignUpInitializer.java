package com.example.demo.signup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.poscoict.rpa.controlroom.api.organizations.ref.OrganizationRef;
import com.poscoict.rpa.controlroom.api.organizations.ref.OrganizationRefRepository;
import com.poscoict.rpa.controlroom.api.signin.PrincipalAuthority;
import com.poscoict.rpa.controlroom.api.users.ref.UserRef;
import com.poscoict.rpa.controlroom.api.users.ref.UserRefRepository;

@Component
public class SignUpInitializer implements InitializingBean {

	protected Log logger = LogFactory.getLog(getClass());

	@Value("${com.poscoict.rpa.controlroom.api.signup.SignUpInitializer.organization:PoscoICT}") 
	private String organization;
	
	@Value("${com.poscoict.rpa.controlroom.api.signup.SignUpInitializer.username:poscoict@poscoict.com}") 
	private String username;
	
	@Value("${com.poscoict.rpa.controlroom.api.signup.SignUpInitializer.password:poscoict}") 
	private String password;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;

	@Autowired 
	private OrganizationRefRepository organizations;
	
	@Autowired 
	private UserRefRepository users;

	@Override
	public void afterPropertiesSet() throws Exception {

		UserRef u = users.findByUsername(username);
		if (u == null) {
			u = new UserRef();
		}
		OrganizationRef o = organizations.findByName(organization);
		if (o == null) {
			o = new OrganizationRef();
		}
		long currentTimeMillis = System.currentTimeMillis();

		o.setName(organization);
		o.setEnabled(true);
		o.setInsertedUser(u.getId());
		o.setInsertedDatetime(currentTimeMillis);
		o.setUpdatedUser(u.getId());
		o.setUpdatedDatetime(currentTimeMillis);
		o = organizations.save(o);

		u.setUsername(username);
		u.setNickname(username);
		u.setPassword(passwordEncoder.encode(password));
		u.setRoles(new String[] {PrincipalAuthority.ROLE_SUPER});
		u.setEnabled(true);
		u.setInsertedOrganization(o.getId());
		o.setInsertedUser(u.getId());
		o.setInsertedDatetime(currentTimeMillis);
		o.setUpdatedUser(u.getId());
		o.setUpdatedDatetime(currentTimeMillis);
		u = users.save(u);

		logger.debug("freepassUser: " + u);
	}
}