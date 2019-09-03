package io.github.u2ware.crawling.core;

import java.net.URL;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.github.u2ware.crawling.core.Content.Type;

public class CrawlerWithJsoup extends Crawler<Document>{

	public CrawlerWithJsoup(String seed, Parser<Document> parser) {
		super(seed, parser);
	}

	@Override
	protected void crawling(Parser<Document> parser, Collection<Content> contents) throws Exception {
		
        log.info("[start] "+seed);
		Document source = Jsoup.parse(new URL(seed), 3000);
		parser.onStart(contents, seed, source);

		
		AtomicInteger count = new AtomicInteger(0);
		for(Content content : contents) {

			Type type = content.getType();
			String anchor = content.getContent().toString();
			
			if(Type.SEED.equals(type) && ! seed.equals(anchor)) {
		        log.info("[visit #"+count.addAndGet(1)+"] "+anchor);
		        
				Document anchorSource = Jsoup.parse(new URL(anchor), 3000);
				parser.onVisit(contents, anchor, anchorSource);
			}
		}
	
        log.info("[finish] "+seed);
		parser.onFinish(contents, seed);
	}
}
