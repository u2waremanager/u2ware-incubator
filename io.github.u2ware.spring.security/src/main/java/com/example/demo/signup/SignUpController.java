package com.example.demo.signup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  
 * 
 * 
 * @author u2waremanager@gmail.com
 *
 */
@Controller
public class SignUpController {

	protected final Log logger = LogFactory.getLog(getClass());

	private @Autowired SignUpService signUpService;
//	private @Autowired SendMailService sendMailService;

	////////////////////////////////////////////////////////
	// exists
	////////////////////////////////////////////////////////
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> exists(
			@RequestParam(value = "username") String username) throws Exception {

		//signUpService.userExists(username);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	////////////////////////////////////////////////////////
	// register
	////////////////////////////////////////////////////////
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> register(
			@RequestParam(value = "username") String username,
			@RequestParam(value = "nickname") String nickname,
			@RequestParam(value = "password") String password) throws Exception {

		String token = null;//signUpService.createUser(username, nickname, password);
        
        return ResponseEntity.ok().header("authorization", token).build();
    }

	////////////////////////////////////////////////////////
	// get authorization token
	////////////////////////////////////////////////////////
	@RequestMapping(value = "/join", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<Object> authorization(
			@RequestHeader("authorization") String authorization) throws Exception {
        //1. authorization token exists ?
        //2. updated token ?
        String token = null;//signUpService.createToken(username);
        return ResponseEntity.ok().header("authorization", token.toString()).build();
	}

    
    ////////////////////////////////////////////////////////
	//
	////////////////////////////////////////////////////////
	@RequestMapping(value = "/join/verify/{token}", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> verify(
        @PathVariable("token") String token, 
        @RequestBody String body){
        return null;
    }
	@RequestMapping(value = "/join/verify/{token}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<Object> verify(
        @PathVariable("token") String token){
        return null;
    }



	

//	////////////////////////////////////////////////////////
//	// verification
//	////////////////////////////////////////////////////////
//	@RequestMapping(value = "/join/verify/{token}", method = RequestMethod.POST)
//	public @ResponseBody ResponseEntity<Object> verify(
//			@PathVariable("token") UUID token, 
//			SendMailRef sendmail) throws Exception {
//
//		UUID res = signUpService.verifyStarted(token, sendmail);
//
//		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
//		headers.add(RememberMeService.TOKEN_HEADER_NAME, res.toString());
//		return new ResponseEntity<Object>(headers, HttpStatus.OK);
//	}
//
//	@RequestMapping(value = "/join/verify/{token}", method = RequestMethod.PUT)
//	public @ResponseBody ResponseEntity<Object> verify(
//			@PathVariable("token") UUID token) throws Exception {
//
//		UUID res = signUpService.verifyFinished(token);
//
//		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
//		headers.add(RememberMeService.TOKEN_HEADER_NAME, res.toString());
//		return new ResponseEntity<Object>(headers, HttpStatus.OK);
//	}
//
//
//	////////////////////////////////////////////////////////
//	// password reset
//	////////////////////////////////////////////////////////
//	@RequestMapping(value = "/join/reset/{token}", method = RequestMethod.POST)
//	public @ResponseBody ResponseEntity<Object> reset(
//			@PathVariable("token") UUID token,
//			SendMailRef sendmail) throws Exception {
//
//		UUID res = signUpService.resetStarted(token, sendmail);
//
//		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
//		headers.add(RememberMeService.TOKEN_HEADER_NAME, res.toString());
//		return new ResponseEntity<Object>(headers, HttpStatus.OK);
//	}
//
//	@RequestMapping(value = "/join/reset/{token}", method = RequestMethod.PUT)
//	public @ResponseBody ResponseEntity<Object> reset(
//			@PathVariable("token") UUID token) throws Exception {
//
//		UUID res = signUpService.resetFinished(token);
//
//		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
//		headers.add(RememberMeService.TOKEN_HEADER_NAME, res.toString());
//		return new ResponseEntity<Object>(headers, HttpStatus.OK);
//	}
}