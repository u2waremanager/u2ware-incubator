package io.github.u2ware.crawling.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;


public abstract class Crawler<T> {
	
	protected Log log = LogFactory.getLog(getClass());

	protected String seed;
	protected Parser<T> parser;
	
	protected Crawler(String seed, Parser<T> parser) {
		Assert.notNull(seed, "rootSeed is null");
		Assert.notNull(parser, "parser is null");
		this.seed = seed;
		this.parser = parser;
	}
	
	
	public List<Content> start() {
		List<Content> contents = new ArrayList<>();
		try {
			crawling(parser, contents);
			log.info("["+contents.size() +" content(s)] "+seed);
		}catch(Exception e) {
			log.info("[error] "+seed, e);
		}
		return contents;
	}
	
	protected abstract void crawling(Parser<T> parser, Collection<Content> contents) throws Exception;
	
}
