package com.poscoict.rpa.controlroom.api.signin;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;

import java.util.Map;

import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.poscoict.rpa.controlroom.api.ApplicationDocs;
import com.poscoict.rpa.controlroom.api.ApplicationMockMvc;
import com.poscoict.rpa.controlroom.api.signup.SignUpInitializerProperties;

/**
 * 
 * @author u2waremanager@gmail.com
 *
 */
public class SignInDocs extends ApplicationDocs{

	public SignInDocs(ApplicationMockMvc mvc) {
		super(mvc, SignInDocs.class);
	}

	public MultiValueMap<String, String> loginForm(SignUpInitializerProperties userProperties) {
		MultiValueMap<String, String> m = new LinkedMultiValueMap<>();
		m.add("username", userProperties.getUsername());
		m.add("password", userProperties.getPassword());
		return m;
	}

	public MultiValueMap<String, String> loginForm(String username, String passeord) {
		MultiValueMap<String, String> m = new LinkedMultiValueMap<>();
		m.add("username", username);
		m.add("password", passeord);
		return m;
	}
	public MultiValueMap<String, String> loginForm(Map<String, Object> arg) {
		MultiValueMap<String, String> m = new LinkedMultiValueMap<>();
		m.add("username", arg.get("username").toString());
		m.add("password", arg.get("username").toString());
		return m;
	}
	
	public ResultHandler loginDocs(){
		return document(getPath()+"-login",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint()),
			requestParameters(
				parameterWithName("username").description(""),
				parameterWithName("password").description("")
			),
			responseHeaders(headerWithName("Authorization").description("인증토큰")),
			responseFields(
				fieldWithPath("nickname").description("사용자 이름"),
				fieldWithPath("username").description("사용자 계정"),
				fieldWithPath("roles").description("사용자 권한")
				//fieldWithPath("organization").description("그룹링크")
				//fieldWithPath("user").description("사용자링크")
			)
		);
	}
	public ResultHandler logoutDocs(){
		return document(getPath()+"-logout",
			preprocessRequest(prettyPrint()),
			preprocessResponse(prettyPrint())
		);
	}

	
}
