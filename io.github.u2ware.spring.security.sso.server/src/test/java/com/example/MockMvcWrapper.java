package com.example;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class MockMvcWrapper {

	private final MockMvc mvc;
	private final String baseUri;
	private final HashMap<String, MvcResultWrapper> results;
	
	
	public MockMvcWrapper(MockMvc mvc, String baseUri) {
		this.mvc = mvc;
		this.baseUri = baseUri;
		this.results = new HashMap<String, MvcResultWrapper>();
	}

	public <T> T content(String path) {
		if(path == null) return null;
		if (results.containsKey(path)) {
			return results.get(path).content("$");
		}else {
			int idx = path.indexOf('.');
			if(idx < 0) {
				return null;
			}else {
				String key = path.substring(0, idx);
				String jsonPath = "$"+path.substring(idx);
				if(results.containsKey(key)) {
					return results.get(key).content(jsonPath);
				}else {
					return null;
				}
			}
		}
	}
	
	public String link(String path) {
		if(path == null) return null;
		if (results.containsKey(path)) {
			return results.get(path).link();
		}else {
			int idx = path.indexOf('.');
			if(idx < 0) {
				return baseUri + path;
			}else {
				String key = path.substring(0, idx);
				String jsonPath = "$"+path.substring(idx);
				if(results.containsKey(key)) {
					return results.get(key).content(jsonPath);
				}else {
					return baseUri + path;
				}
			}
		}
	}
	
	
	public MockMvcRequestWrapper HEAD(String path) throws Exception {
		return head(link(path));
	}
	public MockMvcRequestWrapper OPTIONS(String path) throws Exception {
		return options(link(path));
	}
	public MockMvcRequestWrapper GET(String path) throws Exception {
		return get(link(path));
	}
	public MockMvcRequestWrapper POST(String path) throws Exception {
		return post(link(path));
	}
	public MockMvcRequestWrapper PUT(String path) throws Exception {
		return put(link(path));
	}
	public MockMvcRequestWrapper PATCH(String path) throws Exception {
		return patch(link(path));
	}
	public MockMvcRequestWrapper DELETE(String path) throws Exception {
		return delete(link(path));
	}
	public MockMvcRequestWrapper MULTIPART(String path) throws Exception {
		return multipart(link(path));
	}
	
	
	public MockMvcRequestWrapper head(String path) throws Exception {
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");
		return new MockMvcRequestWrapper(this, MockMvcRequestBuilders.head(path));
	}
	public MockMvcRequestWrapper options(String path) throws Exception {
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");
		return new MockMvcRequestWrapper(this, MockMvcRequestBuilders.options(path));
	}
	public MockMvcRequestWrapper get(String path) throws Exception {
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");
		return new MockMvcRequestWrapper(this, MockMvcRequestBuilders.get(path));
	}
	public MockMvcRequestWrapper post(String path) throws Exception {
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");
		return new MockMvcRequestWrapper(this, MockMvcRequestBuilders.post(path));
	}
	public MockMvcRequestWrapper put(String path) throws Exception {
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");
		return new MockMvcRequestWrapper(this, MockMvcRequestBuilders.put(path));
	}
	public MockMvcRequestWrapper patch(String path) throws Exception {
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");
		return new MockMvcRequestWrapper(this, MockMvcRequestBuilders.patch(path));
	}
	public MockMvcRequestWrapper delete(String path) throws Exception {
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");
		return new MockMvcRequestWrapper(this, MockMvcRequestBuilders.delete(path));
	}
	public MockMvcRequestWrapper multipart(String path) throws Exception {
		System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------");
		return new MockMvcRequestWrapper(this, MockMvcRequestBuilders.fileUpload(path));
	}
	
	public static class MockMvcRequestWrapper{
		
		private final MockMvcWrapper mvc;
		private final MockHttpServletRequestBuilder builder;
		private final MockMultipartHttpServletRequestBuilder multipart;

		private MockMvcRequestWrapper(MockMvcWrapper mvc, MockHttpServletRequestBuilder builder) {
			this.mvc = mvc;
			this.builder = builder;
			this.multipart = null;
		}
		private MockMvcRequestWrapper(MockMvcWrapper mvc, MockMultipartHttpServletRequestBuilder multipart) {
			this.mvc = mvc;
			this.builder = multipart;
			this.multipart = multipart;
		}
		
		public MockMvcRequestWrapper W(RequestPostProcessor postProcessor) {
			this.builder.with(postProcessor);
			return this;
		}

		public MockMvcRequestWrapper A(String... value) throws Exception {
			if(value.length == 0) {
				this.builder.accept(MediaType.APPLICATION_JSON_UTF8);
			}else {
				this.builder.accept(value);
			}
			return this;
		}
		public MockMvcRequestWrapper H(String key, String value) throws Exception {
			this.builder.header(key, value);
			return this;
		}
		public MockMvcRequestWrapper H(HttpHeaders headers) throws Exception {
			this.builder.headers(headers);
			return this;
		}
		public MockMvcRequestWrapper H(MultiValueMap<String,String> src) throws Exception {
			HttpHeaders headers = new HttpHeaders();
			headers.putAll(src);
			this.builder.headers(headers);
			return this;
		}
		public MockMvcRequestWrapper P(String key, Object value) throws Exception {
			this.builder.param(key, value.toString());
			return this;
		}
		public MockMvcRequestWrapper P(MultiValueMap<String,String> src) throws Exception {
			this.builder.params(src);
			return this;
		}
		public MockMvcRequestWrapper C(Object src) throws Exception {
			String requestContent = new ObjectMapper().writeValueAsString(src);
			this.builder.contentType(MediaType.APPLICATION_JSON_UTF8).content(requestContent);
			return this;
		}	
		public MockMvcRequestWrapper F(MockMultipartFile file) throws Exception {
			this.multipart.file(file);
			return this;
		}	
		
		public MockMvcResponseWrapper isXxx() throws Exception {
			return new MockMvcResponseWrapper(mvc.mvc.perform(builder).andDo(print()));
		}
		public MockMvcResponseWrapper is2xx() throws Exception {
			return new MockMvcResponseWrapper(mvc.mvc.perform(builder).andDo(print()).andExpect(status().is2xxSuccessful()));
		}
		public MockMvcResponseWrapper is3xx() throws Exception {
			return new MockMvcResponseWrapper(mvc.mvc.perform(builder).andDo(print()).andExpect(status().is3xxRedirection()));
		}
		public MockMvcResponseWrapper is4xx() throws Exception {
			return new MockMvcResponseWrapper(mvc.mvc.perform(builder).andDo(print()).andExpect(status().is4xxClientError()));
		}
		public MockMvcResponseWrapper is5xx() throws Exception {
			return new MockMvcResponseWrapper(mvc.mvc.perform(builder).andDo(print()).andExpect(status().is5xxServerError()));
		}

		public MockMvcResponseWrapper isXxx(String key) throws Exception {
			return isXxx().and(store(key));
		}
		public MockMvcResponseWrapper is2xx(String key) throws Exception {
			return is2xx().and(store(key));
		}
		public MockMvcResponseWrapper is3xx(String key) throws Exception {
			return is3xx().and(store(key));
		}
		public MockMvcResponseWrapper is4xx(String key) throws Exception {
			return is4xx().and(store(key));
		}
		public MockMvcResponseWrapper is5xx(String key) throws Exception {
			return is5xx().and(store(key));
		}
		
		private ResultHandler store(final String key) {
			return new ResultHandler() {
				public void handle(MvcResult result) throws Exception {
					mvc.results.put(key, new MvcResultWrapper(result));
				}
			};
		}
	}
	
	public static class MockMvcResponseWrapper  {
		
		private final ResultActions actions;

		private MockMvcResponseWrapper(ResultActions actions) {
			this.actions = actions;
		}
		
		public MockMvcResponseWrapper and(ResultMatcher matcher) throws Exception{
			this.actions.andExpect(matcher);
			return this;
		}

		public MockMvcResponseWrapper and(ResultHandler handler) throws Exception{
			this.actions.andDo(handler);
			return this;
		}

		//////////////////////////////////////////////////
		//
		//////////////////////////////////////////////////
		public MvcResult result() throws Exception{
			return this.actions.andReturn();
		}
		public MvcResultWrapper resultWrapper() throws Exception{
			return new MvcResultWrapper(result());
		}
		public <T> T resultContent(String path) throws Exception{
			return resultWrapper().content(path);
		}
		public String resultContent() throws Exception{
			return resultWrapper().content();
		}
		public String resultLink() throws Exception{
			return resultWrapper().link();
		}
	}
	
	public static class MvcResultWrapper  {
	
		private final MvcResult mvcResult;

		private MvcResultWrapper(MvcResult mvcResult) {
			this.mvcResult = mvcResult;
		}
		
		public MvcResult result() {
			return mvcResult;
		}
		
		public <T> T content(String path){
			try {
				String body = mvcResult.getResponse().getContentAsString();
				Object document = Configuration.defaultConfiguration().jsonProvider().parse(body);
				return JsonPath.read(document, path);
			} catch (Exception e) {
				return null;
			}
		}
		public String content(){
			try {
				return mvcResult.getResponse().getContentAsString();
			} catch (Exception e) {
				return null;
			}
		}
		
		public String link()  {
			String uri = null;

			uri = mvcResult.getResponse().getHeader("Location");
			if (uri != null) {
				return uri;
			}
			uri = (String) content("$._links.self.href");
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
	
	public void scan(ApplicationContext applicationContext) {

		String[] names = applicationContext.getBeanDefinitionNames();
		
		SortedMap<String, List<String>> map = new TreeMap<>();
		for(String name: names) {

			Object o = applicationContext.getBean(name);
			if(o != null) {
				String key = o.getClass().getName();
				
				if(map.containsKey(key)) {
					List<String> value = map.get(key);
					value.add(name);
					
				}else {
					List<String> value = new ArrayList<>();
					value.add(name);
					map.put(key, value);
				}
			}
		}
		
		System.out.println();
		for(Map.Entry<String, List<String>> e : map.entrySet()) {
			System.out.println("# \t"+e.getKey()+" = "+StringUtils.collectionToCommaDelimitedString(e.getValue()));
		}
		System.out.println();
	}
	public void scan(ApplicationContext applicationContext, Class<?> type) {
		System.out.println();
		System.out.println("# "+type.getName());
		String[] names = applicationContext.getBeanNamesForType(type);
		for(String name: names) {
			System.out.println("# \t"+name+" = "+applicationContext.getBean(name));
		}
	}
	
}
