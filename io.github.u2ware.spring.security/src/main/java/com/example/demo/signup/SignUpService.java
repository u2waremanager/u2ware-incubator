package com.example.demo.signup;

import java.util.UUID;

import javax.transaction.Transactional;

import com.poscoict.rpa.controlroom.api.sendMails.ref.SendMailRef;
import com.poscoict.rpa.controlroom.api.sendMails.ref.SendMailRefRepository;
import com.poscoict.rpa.controlroom.api.signin.PrincipalAuthority;
import com.poscoict.rpa.controlroom.api.users.ref.UserRef;
import com.poscoict.rpa.controlroom.api.users.ref.UserRefRepository;
import com.poscoict.rpa.controlroom.api.usersLogs.ref.UsersLogRef;
import com.poscoict.rpa.controlroom.api.usersLogs.ref.UsersLogRefRepository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 
 * 
 * 
 * @author u2waremanager@gmail.com
 *
 */
@Service
@Transactional
public class SignUpService {

	protected final Log logger = LogFactory.getLog(getClass());

	private @Autowired PasswordEncoder encoder;

	private @Autowired UserRefRepository users;
	private @Autowired UsersLogRefRepository usersLogs;

	private @Autowired SendMailRefRepository mails;

	//////////////////////////////////////////////////////
	//
	//////////////////////////////////////////////////////
	public boolean userExists(String username) throws Exception {
		boolean exist = users.existsByUsername(username);
		if (exist)
			throw new Exception("Username Already Exists");
		return exist;
	}

	public UUID createUser(String username, String nickname, String password) throws Exception {

		boolean exist = users.existsByUsername(username);
		if (exist)
			throw new Exception("Username Already Exists");

		long currentTimeMillis = System.currentTimeMillis();

		UserRef u = new UserRef();
		u.setUsername(username);
		u.setPassword(encoder.encode(password));
		u.setNickname(nickname);
		u.setRoles(new String[] { PrincipalAuthority.ROLE_USER });
		u.setEnabled(false);
		u.setInsertedOrganization(u.getId());
		u.setInsertedUser(u.getId());
		u.setInsertedDatetime(currentTimeMillis);
		u.setUpdatedUser(u.getId());
		u.setUpdatedDatetime(currentTimeMillis);
		u = users.save(u);

		
		UsersLogRef l = new UsersLogRef();
		l.setName("signup");
		l.setType("signup");
		l.setInsertedOrganization(u.getInsertedOrganization());
		l.setInsertedUser(u.getId());
		l.setInsertedDatetime(currentTimeMillis);
		usersLogs.save(l);

		return u.getId();
	}

	public UUID createToken(String username) throws Exception {

		UserRef u = users.findByUsername(username);
		if (u == null)
			throw new UsernameNotFoundException("");

		u.setToken(UUID.randomUUID());
		u = users.save(u);

		return u.getToken();
	}

	//////////////////////////////////////////////////////
	//
	//////////////////////////////////////////////////////
	public UUID verifyStarted(UUID token, SendMailRef mail) throws Exception {
		UserRef u = users.findByToken(token);
		if (u == null)
			throw new UsernameNotFoundException("");

		mail.setTo(new String[] {u.getUsername()});
		mail.setInsertedDatetime(System.currentTimeMillis());
		mails.save(mail);

		return token;
	}
	public UUID verifyFinished(UUID token) throws Exception {
		UserRef u = users.findByToken(token);
		if (u == null)
			throw new UsernameNotFoundException("");

		///////////////////////////////////
		u.setEnabled(true);
		u.setToken(null);
		u = users.save(u);

		///////////////////////////////////
		UsersLogRef l = new UsersLogRef();
		l.setName("verify");
		l.setType("verify");
		l.setInsertedOrganization(u.getInsertedOrganization());
		l.setInsertedUser(u.getId());
		l.setInsertedDatetime(System.currentTimeMillis());
		usersLogs.save(l);

		return token;
	}

	//////////////////////////////////////////////////////
	//
	//////////////////////////////////////////////////////
	public UUID resetStarted(UUID token, SendMailRef mail) throws Exception {
		UserRef u = users.findByToken(token);
		if (u == null)
			throw new UsernameNotFoundException("");

		mail.setTo(new String[] {u.getUsername()});
		mail.setInsertedDatetime(System.currentTimeMillis());
		mails.save(mail);
		return token;
	}

	public UUID resetFinished(UUID token) throws Exception {
		UserRef u = users.findByToken(token);
		if (u == null)
			throw new UsernameNotFoundException("");
		if (! u.getEnabled())
			throw new UsernameNotFoundException("");

		String[] array = StringUtils.delimitedListToStringArray(token.toString(), "-");
		String newpassword = array[array.length-1];
		
		///////////////////////////////////
		u.setPassword(encoder.encode(newpassword));
		u.setToken(null);
		u = users.save(u);

		///////////////////////////////////
		UsersLogRef l = new UsersLogRef();
		l.setName("reset");
		l.setType("reset");
		l.setInsertedOrganization(u.getInsertedOrganization());
		l.setInsertedUser(u.getId());
		l.setInsertedDatetime(System.currentTimeMillis());
		usersLogs.save(l);

		return token;
	}
}