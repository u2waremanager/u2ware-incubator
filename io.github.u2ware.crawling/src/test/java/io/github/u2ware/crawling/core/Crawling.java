package io.github.u2ware.crawling.core;

import java.util.List;

import org.jsoup.nodes.Document;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.ClassUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import edu.uci.ics.crawler4j.parser.HtmlParseData;


public class Crawling {

	public static Crawling of(String rootSeed){
		return new Crawling(rootSeed, rootSeed);
	}
	public static Crawling of(String rootSeed, String subSeed){
		return new Crawling(rootSeed, subSeed);
	}
	
	private String rootSeed;
	private String subSeed;
	
	private Crawling(String rootSeed, String subSeed) {
		this.rootSeed = rootSeed;
		this.subSeed = subSeed;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<Content> start(Parser<T> parser){
		
		Class<?> sourceType = GenericTypeResolver.resolveTypeArgument(parser.getClass(), Parser.class);
		
		if(ClassUtils.isAssignable(HtmlParseData.class, sourceType)) {
			parser.onInit(rootSeed, subSeed);
			List<Content> contents =  new CrawlerWithCrawler4j(rootSeed, (Parser<HtmlParseData>)parser).start();
			parser.onDestroy(rootSeed, subSeed);
			return contents;
			
		} else if(ClassUtils.isAssignable(HtmlPage.class, sourceType)) {
			parser.onInit(rootSeed, subSeed);
			List<Content> contents = new CrawlerWithHtmlunit(rootSeed, (Parser<HtmlPage>)parser).start();
			parser.onDestroy(rootSeed, subSeed);
			return contents;
			
		} else if(ClassUtils.isAssignable(Document.class, sourceType)) {
			parser.onInit(rootSeed, subSeed);
			List<Content> contents = new CrawlerWithJsoup(rootSeed, (Parser<Document>)parser).start();
			parser.onDestroy(rootSeed, subSeed);
			return contents;

		} else if(ClassUtils.isAssignable(String.class, sourceType)) {
			parser.onInit(rootSeed, subSeed);
			List<Content> contents = new CrawlerWithTemplate(rootSeed, (Parser<String>)parser).start();
			parser.onDestroy(rootSeed, subSeed);
			return contents;
			
		} else {
			throw new RuntimeException(sourceType+" is not support.");
		}
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Content> start(){
		
		UriComponents uri = UriComponentsBuilder.fromUriString(rootSeed).build();
		String className = uri.getHost().replace(".", "_").replace("-", "_");
		String parserName = "com.iscreammedia.hiclass.crawler.ref."+className;
		
		Parser parser = new DefaultParser();
		if(ClassUtils.isPresent(parserName, ClassLoader.getSystemClassLoader())) {
			try {
				Class<?> clazz = ClassUtils.forName(parserName, ClassLoader.getSystemClassLoader());
				
				if(ClassUtils.isAssignable(Parser.class, clazz)) {
					parser = (Parser)clazz.newInstance();
				}
			} catch (Exception e) {
			}
		}
		
		parser.onInit(rootSeed, subSeed);
		List<Content> contents = start(parser);
		parser.onDestroy(rootSeed, subSeed);
		
		return contents;
	}
	
}
