package io.github.u2ware.crawling.core;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import io.github.u2ware.crawling.core.Content.Type;

public class CrawlerWithHtmlunit extends Crawler<HtmlPage>{

	
	public CrawlerWithHtmlunit(String seed, Parser<HtmlPage> parser) {
		super(seed, parser);
	}

	@Override
	protected void crawling(Parser<HtmlPage> parser, Collection<Content> contents) throws Exception {
		

		WebClient webClient = new WebClient();
		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		
		
        log.info("[start] "+seed);
		HtmlPage source = webClient.getPage(seed);
		parser.onStart(contents, seed, source);
        
		AtomicInteger count = new AtomicInteger(0);
		for(Content content : contents) {

			Type type = content.getType();
			String anchor = content.getContent().toString();
			
			if(Type.SEED.equals(type) && ! seed.equals(anchor)) {
		        log.info("[visit #"+count.addAndGet(1)+"] "+anchor);

				HtmlPage anchorSource = webClient.getPage(anchor);
				parser.onVisit(contents, anchor, anchorSource);
			}
		}
		webClient.close();
		
        log.info("[finish] "+seed);
		parser.onFinish(contents, seed);
	}
	
}
