package com.example.demo.signon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * 
 * @author u2waremanager@gmail.com
 *
 */
@BasePathAwareController
public class SignOnController {

	protected final Log logger = LogFactory.getLog(getClass());

	private @Autowired SignOnService signedService;

	@RequestMapping(value = "/account", method = RequestMethod.GET)
	//@PreAuthorize("")
	public @ResponseBody ResponseEntity<Object> currentUser() throws Exception {

		Object source = signedService.currentUser();
		return new ResponseEntity<Object>(source, HttpStatus.OK);
	}

	@RequestMapping(value = "/account", method = RequestMethod.POST)
	//@PreAuthorize("")
	public @ResponseBody ResponseEntity<Object> changeNickname(
			@RequestParam("nickname") String nickname) throws Exception {

		signedService.changeNickname(nickname);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping(value = "/account", method = RequestMethod.PUT)
	//@PreAuthorize("")
	public @ResponseBody ResponseEntity<Object> changePassword(
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword) throws Exception {

		signedService.changePassword(oldPassword, newPassword);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping(value = "/account", method = RequestMethod.DELETE)
	//@PreAuthorize("")
	public @ResponseBody ResponseEntity<Object> deleteUser(
			@RequestParam MultiValueMap<String, Object> params)throws Exception {

		signedService.deleteUser(params);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

//	@RequestMapping(value = "/account", method = RequestMethod.POST)
//	public @ResponseBody ResponseEntity<Object> account(
//			@RequestParam MultiValueMap<String, Object> params) throws Exception {
//
//		Object source = signedService.updateUser(params);
//		return new ResponseEntity<Object>(source, HttpStatus.OK);
//	}

}