package io.github.u2ware.sample;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.request.RequestPartDescriptor;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ClassUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponents.UriTemplateVariables;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import io.github.u2ware.sample.ApplicationTests.RestMockMvc.MockMvcUriTemplateVariables;
import io.github.u2ware.sample.userAccounts.UserAccountTokenBuilder;

public class ApplicationTests {

	public static class RestMockMvcNuilder {
		
		private WebApplicationContext context;
		private RestDocumentationContextProvider docs;
		private String uri;
		private boolean security = false;

		public static RestMockMvcNuilder of(WebApplicationContext context) {
			return new RestMockMvcNuilder(context);
		}

		private RestMockMvcNuilder(WebApplicationContext context) {
			this.context = context;
		}
		
		public RestMockMvcNuilder secure(boolean security) {
			this.security = security; return this;
		}
		public RestMockMvcNuilder docs(RestDocumentationContextProvider docs) {
			this.docs = docs; return this;
		}
		public RestMockMvcNuilder baseUri(String uri) {
			this.uri = uri; return this;
		}

		public RestMockMvc build() {
			DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(context);
			if(docs != null) {
				builder = builder.apply(MockMvcRestDocumentation.documentationConfiguration(docs)
//						.uris().withScheme("http").withHost("")//.withPort(80)
				);
			}
			if(security) {
				builder = builder.apply(SecurityMockMvcConfigurers.springSecurity());
			}
			
			String url = (uri != null) ? uri : context.getBean(RepositoryRestConfiguration.class).getBasePath().toString();
			return new RestMockMvc(builder.build(), url);
		}
	}
	
	
	
	public static class RestMockMvc {

		protected static void test(MockMvc mvc) throws Exception{
			RequestBuilder requestBuilder = MockMvcRequestBuilders.get(".....");
			ResultHandler handler = null;
			ResultMatcher matcher = null;
			
			ResultActions actions = mvc.perform(requestBuilder).andDo(handler).andExpect(matcher);
			MvcResult result = actions.andReturn();
			result.getResponse();
		}
		
//		protected static Log logger = LogFactory.getLog(RestMockMvc.class);

		private MockMvc mvc;
		private String baseUri;
		private MockMvcUriTemplateVariables variables;

		public RestMockMvc(MockMvc mvc, String baseUri) {
			this.mvc = mvc;
			this.baseUri = baseUri;
			this.variables = new MockMvcUriTemplateVariables();
		}
		
		public MockMvcUriTemplateVariables variables() {
			return variables;
		}

		
		//////////////////////////////////////////////////////////////////////////////////
		//
		/////////////////////////////////////////////////////////////////////////////////

		public MockMvcRequestSupport GET(String uri) throws Exception{
			return get(variables.resolveUri(uri, baseUri));
		}
		public MockMvcRequestSupport POST(String uri) throws Exception {
			return post(variables.resolveUri(uri, baseUri));
		}
		public MockMvcRequestSupport PUT(String uri) throws Exception{
			return put(variables.resolveUri(uri, baseUri));
		}
		public MockMvcRequestSupport PATCH(String uri) throws Exception{
			return patch(variables.resolveUri(uri, baseUri));
		}
		public MockMvcRequestSupport DELETE(String uri) throws Exception{
			return delete(variables.resolveUri(uri, baseUri));
		}
		public MockMvcRequestSupport OPTIONS(String uri) throws Exception {
			return options(variables.resolveUri(uri, baseUri));
		}
		public MockMvcRequestSupport HEAD(String uri) throws Exception {
			return head(variables.resolveUri(uri, baseUri));
		}
		public MockMvcRequestSupport MULTIPART(String uri) throws Exception {
			return multipart(variables.resolveUri(uri, baseUri));
		}
		
		
		public MockMvcRequestSupport get(String uri) throws Exception{
			System.out.println("\n-----------------------------------------------------------------------------------------------");
			return new MockMvcRequestSupport(MockMvcRequestBuilders.get(uri), variables, mvc);
		}
		public MockMvcRequestSupport post(String uri) throws Exception{
			System.out.println("\n-----------------------------------------------------------------------------------------------");
			return new MockMvcRequestSupport(MockMvcRequestBuilders.post(uri), variables, mvc);
		}
		public MockMvcRequestSupport put(String uri) throws Exception{
			System.out.println("\n-----------------------------------------------------------------------------------------------");
			return new MockMvcRequestSupport(MockMvcRequestBuilders.put(uri), variables, mvc);
		}
		public MockMvcRequestSupport patch(String uri) throws Exception{
			System.out.println("\n-----------------------------------------------------------------------------------------------");
			return new MockMvcRequestSupport(MockMvcRequestBuilders.patch(uri), variables, mvc);
		}
		public MockMvcRequestSupport delete(String uri) throws Exception{
			System.out.println("\n-----------------------------------------------------------------------------------------------");
			return new MockMvcRequestSupport(MockMvcRequestBuilders.delete(uri), variables, mvc);
		}
		public MockMvcRequestSupport options(String uri) throws Exception{
			System.out.println("\n-----------------------------------------------------------------------------------------------");
			return new MockMvcRequestSupport(MockMvcRequestBuilders.options(uri), variables, mvc);
		}
		public MockMvcRequestSupport head(String uri) throws Exception{
			System.out.println("\n-----------------------------------------------------------------------------------------------");
			return new MockMvcRequestSupport(MockMvcRequestBuilders.head(uri), variables, mvc);
		}
		public MockMvcRequestSupport multipart(String uri) throws Exception{
			System.out.println("\n-----------------------------------------------------------------------------------------------");
			return new MockMvcRequestSupport(MockMvcRequestBuilders.multipart(uri), variables, mvc);
		}
		
		
		public static class MockMvcRequestSupport{
			
			private MockHttpServletRequestBuilder builder;
			private MockMvcUriTemplateVariables variables;
			private MockMvc mvc;
			
			
			private ObjectMapper mapper = new ObjectMapper();
			private Map<String,Object> content = new HashMap<>();
			private Object contentValue;
			
			private MockMvcRequestSupport(MockHttpServletRequestBuilder builder, MockMvcUriTemplateVariables variables, MockMvc mvc) {
				this.builder = builder;
				this.variables = variables;
				this.mvc = mvc;
			}
			private MockMvcRequestSupport(MockMultipartHttpServletRequestBuilder builder, MockMvcUriTemplateVariables variables, MockMvc mvc) {
				this.builder = builder;
				this.variables = variables;
				this.mvc = mvc;
			}
			
			public MockMvcRequestSupport U(String name, String... roles) throws Exception{
				builder.with(user(name).roles(roles));
				return this;
			}
			public MockMvcRequestSupport U(Authentication authentication) throws Exception{
				builder.with(authentication(authentication));
				return this;
			}
			
			
			public MockMvcRequestSupport H(String key, Object value) throws Exception{
				builder.header(key,  variables.resolveUri(value.toString())); return this;
			}
			public MockMvcRequestSupport H(HttpHeaders headers) throws Exception{
				builder.headers(headers); return this;
			}
			public MockMvcRequestSupport P(String key, Object value) throws Exception{
				builder.param(key, variables.resolveUri(value.toString())); return this;
			}
			public MockMvcRequestSupport P(MultiValueMap<String,String> params) throws Exception{
				builder.params(params); return this;
			}
			
			public MockMvcRequestSupport C(String key, String value) throws Exception{
				content.put(key, value != null ? variables.resolveValue(value) : null); 
				return this;
			}
			public MockMvcRequestSupport C(String key, Object value) throws Exception{
				content.put(key, value); 
				return this;
			}
			
			public MockMvcRequestSupport C(Object contentValue) throws Exception{
				this.contentValue = contentValue; return this;
			}
			public MockMvcRequestSupport C() throws Exception{
				this.contentValue = mvc; return this;
			}
			
			public MockMvcRequestSupport F(String key, File file) throws Exception{
				MockMultipartHttpServletRequestBuilder r = (MockMultipartHttpServletRequestBuilder)builder;
				r.file(new MockMultipartFile(key, FileCopyUtils.copyToByteArray(file)));
				return this;
			}
			public MockMvcRequestSupport F(MockMultipartFile file) throws Exception{
				MockMultipartHttpServletRequestBuilder r = (MockMultipartHttpServletRequestBuilder)builder;
				r.file(file);
				return this;
			}

			private String characterEncoding = "utf-8";
			private String contentType = MediaType.APPLICATION_JSON_VALUE;
			
			public MockMvcRequestSupport characterEncoding(String characterEncoding) throws Exception{
				this.characterEncoding = characterEncoding;
				return this;
			}
			public MockMvcRequestSupport contentType(String contentType) throws Exception{
				this.contentType = contentType;
				return this;
			}

			
			private ResultActionsSupport perform() throws Exception {
				builder.header("Accept", "application/json");
				if(content.size() > 0) {
					builder.characterEncoding(characterEncoding);
					builder.contentType(contentType);
					builder.content(mapper.writeValueAsString(content));
				}
				if(contentValue != null) {
					builder.characterEncoding(characterEncoding);
					String x = "{}";
					if(ClassUtils.isAssignableValue(MockMvc.class, contentValue)) {
						builder.contentType(contentType);
					}else {
//						System.err.println(contentType);
						
						builder.contentType(contentType);
						
						if(ClassUtils.isAssignableValue(String.class, contentValue)) {
							x = contentValue.toString();
						}else {
							x = mapper.writeValueAsString(contentValue);
						}
//						System.err.println(x);
					}
					builder.content(x);
				}
				return new ResultActionsSupport(mvc.perform(builder).andDo(MockMvcResultHandlers.print()), variables);
			}
			
			public ResultActionsSupport is2xx() throws Exception {
				return perform().andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
			}
			public ResultActionsSupport is4xx() throws Exception {
				return perform().andExpect(MockMvcResultMatchers.status().is4xxClientError());
			}
			public ResultActionsSupport is5xx() throws Exception {
				return perform().andExpect(MockMvcResultMatchers.status().is5xxServerError());
			}

		}
		
		
		
		public static class ResultActionsSupport{
			
			private ResultActions actions;
			private MockMvcUriTemplateVariables variables;
			
			private ResultActionsSupport(ResultActions actions, MockMvcUriTemplateVariables variables) {
				this.actions = actions;
				this.variables = variables;
			}
			public ResultActionsSupport andDo(ResultHandler... resultHandlers) throws Exception{
				for(ResultHandler resultHandler : resultHandlers) {
					actions.andDo(resultHandler);
				}
				return this;
			}
			
			public ResultActionsSupport andExpect(ResultMatcher... resultMatchers) throws Exception{
				for(ResultMatcher resultMatcher : resultMatchers) {
					actions.andExpect(resultMatcher);
				}
				return this;
			}
			
			public ResultActionsSupport andExpect(String path, Object value) throws Exception {
				
				if(value == null) {
					actions.andExpect(MockMvcResultMatchers.jsonPath(path).doesNotExist());
				}else {
					actions.andExpect(MockMvcResultMatchers.jsonPath(path).value(value));
				}
				return this;
			}
//			public ResultActionsSupport jsonExpect(int value) throws Exception {
//				actions.andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements").value(value));
//				return this;
//			}
			public MvcResultSupport andReturn() throws Exception {
				return new MvcResultSupport(actions.andReturn());
			}
			public MvcResultSupport andReturn(String key) throws Exception {
				actions.andDo(variables.resultHandler(key));
				return new MvcResultSupport(actions.andReturn());
			}
		}
		
		
		
		public static class MvcResultSupport {
			
			private MvcResult mvcResult;

			private MvcResultSupport(MvcResult mvcResult) {
				this.mvcResult = mvcResult;
			}
			
			public MvcResult get() {
				return mvcResult;
			}
			
			public <T> T contentAs(Class<T> type)  {
				try {
					return (T)new ObjectMapper().readValue(contentAsString(), type);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			public String contentAsString()  {
				try {
					return mvcResult.getResponse().getContentAsString();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return null;
				}
			}
			
			public <T> T path(String path) {
				try {
					String body = mvcResult.getResponse().getContentAsString();
					Object document = Configuration.defaultConfiguration().jsonProvider().parse(body);
					return JsonPath.read(document, path);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			public String link()  {
				String uri = null;
				uri = mvcResult.getResponse().getHeader("Location");
				if (uri != null) {
					return uri;
				}
				uri = (String) path("$._links.self.href");
				if (uri != null) {
					return uri;
				}
				uri = mvcResult.getRequest().getRequestURL().toString();
				if (uri != null) {
					return uri;
				}
				return null;
			}
		}
		
		
		@SuppressWarnings("serial")
		public static class MockMvcUriTemplateVariables extends HashMap<String, MvcResultSupport> implements UriTemplateVariables{

			private ResultHandler resultHandler(final String key) {
				return (mvcResult)->{
					put(key, new MvcResultSupport(mvcResult));
				};
			}
			
//			"{mtoLink1}" ==> link...
//			"{mtoLink1.$}" ==> body(map)
//			"{mtoLink1.$.prop}" ==> body path
			
			@Override
			public Object getValue(String name) {
				if (containsKey(name)) {
					return get(name).link();
				}else {
					int idx = name.indexOf('.');
					if(idx < 0) {
						return null;
					}else {
						String key = name.substring(0, idx);
						if(containsKey(key)) {
//							String jsonPath = "$"+name.substring(idx);
//							if("$.$".equals(jsonPath)) jsonPath = "$";
							String jsonPath = name.substring(idx+1);
							return get(key).path(jsonPath);
						}else {
							return null;
						}
					}
				}
			}
			
			public String resolveUri(String template) {
				return UriComponentsBuilder.fromUriString(template).build().expand(variables()).toUriString();
			}
			public String resolveUri(String template, String baseUri) throws Exception{
				String convert = resolveUri(template);
				return template.equals(convert) ? baseUri +template : convert;
			}
			
			public Object resolveValue(String template) {
				if(template.startsWith("{") && template.endsWith("}")) {
					String key = UriComponentsBuilder.fromUriString(template).build().expand( (name)->{ return name;}).toUriString();
					return getValue(key);
				}else {
					return resolveUri(template);
				}
			}

			private UriTemplateVariables variables() {
				return this;
			}
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	public static abstract class RestMockMvcDocs {
		
		private MockMvcUriTemplateVariables variables;
		
		public MockMvcUriTemplateVariables variables() {
			return variables;
		}
		public void variables(MockMvcUriTemplateVariables variables) {
			this.variables = variables;
		}
		
		protected abstract void randomTo(Map<String,Object> entity, int count);
		protected abstract void randomFrom(Map<String,Object> entity, int count);
		protected AtomicInteger count = new AtomicInteger(0);

		public final Map<String,Object> random() throws Exception{
			HashMap<String,Object> random = new HashMap<String,Object>();
			randomTo(random, count.addAndGet(1));
			return random;
		}
		
		public final Map<String,Object> random(String variable) throws Exception{
			@SuppressWarnings("unchecked")
			HashMap<String,Object> random = (HashMap<String, Object>) variables.resolveValue(variable);
			randomFrom(random, count.addAndGet(1));
			return random;
		}
		
		
		protected final ResultHandler document(String identifier, DescriptorsCallback callback){
			Descriptors descriptors = new Descriptors(identifier);
			callback.doWith(descriptors);

			List<Snippet> snippets = new ArrayList<>();
			if(descriptors.requestParameters.size() >  0){snippets.add(RequestDocumentation.requestParameters(descriptors.requestParameters));}
			if(descriptors.requestParts.size()      >  0){snippets.add(RequestDocumentation.requestParts(descriptors.requestParts));}
			if(descriptors.requestHeaders.size()    > -1){snippets.add(HeaderDocumentation.requestHeaders(descriptors.requestHeaders));}
			if(descriptors.requestFields.size()     >  0){snippets.add(PayloadDocumentation.requestFields(descriptors.requestFields));}
			if(descriptors.responseHeaders.size()   > -1){snippets.add(HeaderDocumentation.responseHeaders(descriptors.responseHeaders));}
			if(descriptors.responseFields.size()    >  0){snippets.add(PayloadDocumentation.responseFields(descriptors.responseFields));}
			
			return MockMvcRestDocumentation.document(identifier,
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					snippets.toArray(new Snippet[0])
			);
		}
		
		protected interface DescriptorsCallback {
			public void doWith(Descriptors descriptors);
		}
		
		public static class Descriptors{
			
			private final ParameterDescriptors requestParameters = new ParameterDescriptors();
			private final RequestPartDescriptors requestParts= new RequestPartDescriptors();
			private final HeaderDescriptors requestHeaders = new HeaderDescriptors();
			private final FieldDescriptors requestFields = new FieldDescriptors();
			private final HeaderDescriptors responseHeaders = new HeaderDescriptors();
			private final FieldDescriptors responseFields = new FieldDescriptors();
			private final String identifier;

			private Descriptors(String identifier) {
				this.identifier = identifier;
			}

			public ParameterDescriptors requestParameters() {return requestParameters;}
			public RequestPartDescriptors requestParts() {return requestParts;}
			public HeaderDescriptors requestHeaders() {return requestHeaders;}
			public FieldDescriptors requestFields() {return requestFields;}
			public HeaderDescriptors responseHeaders() {return responseHeaders;}
			public FieldDescriptors responseFields() {return responseFields;}
			public String identifier() {return identifier;}
			
			@SuppressWarnings("serial")
			public static class HeaderDescriptors extends ArrayList<HeaderDescriptor>{
				public HeaderDescriptor headerWithName(String name){
					HeaderDescriptor descriptor = HeaderDocumentation.headerWithName(name);
					this.add(descriptor);
					return descriptor;
				}
			}
			
			@SuppressWarnings("serial")
			public static class FieldDescriptors extends ArrayList<FieldDescriptor>{
				public FieldDescriptor fieldWithPath(String path){
					FieldDescriptor descriptor = PayloadDocumentation.fieldWithPath(path);
					this.add(descriptor);
					return descriptor;
				}

				public FieldDescriptor subsectionWithPath(String path){
					FieldDescriptor descriptor = PayloadDocumentation.subsectionWithPath(path);
					this.add(descriptor);
					return descriptor;
				}

			}
			
			@SuppressWarnings("serial")
			public static class MutableFieldDescriptors extends ArrayList<FieldDescriptor>{
				public FieldDescriptor fieldWithPath(String path){
					FieldDescriptor descriptor = PayloadDocumentation.fieldWithPath(path);
					this.add(descriptor);
					return descriptor;
				}
				public FieldDescriptor subsectionWithPath(String path){
					FieldDescriptor descriptor = PayloadDocumentation.subsectionWithPath(path);
					this.add(descriptor);
					return descriptor;
				}
			}

			
			
			
			
			
			
			@SuppressWarnings("serial")
			public static class ParameterDescriptors extends ArrayList<ParameterDescriptor>{
				public ParameterDescriptor parameterWithName(String name){
					ParameterDescriptor descriptor = RequestDocumentation.parameterWithName(name);
					this.add(descriptor);
					return descriptor;
				}
			}
			
			@SuppressWarnings("serial")
			public static class RequestPartDescriptors extends ArrayList<RequestPartDescriptor>{
				public RequestPartDescriptor partWithName(String name){
					RequestPartDescriptor descriptor = RequestDocumentation.partWithName(name);
					this.add(descriptor);
					return descriptor;
				}
			}
		}
	}
	
	
}
