package io.github.u2ware.crawling.core;

import java.net.URI;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import io.github.u2ware.crawling.core.Content.Type;

public class CrawlerWithTemplate extends Crawler<String>{

	public CrawlerWithTemplate(String seed, Parser<String> parser) {
		super(seed, parser);
	}

	@Override
	protected void crawling(Parser<String> parser, Collection<Content> contents) throws Exception {
		
		RestTemplate restTemplate = new RestTemplate();

        log.info("[start] "+seed);
        RequestEntity<?> request = RequestEntity.get(new URI(seed)).build();
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		String source = response.getBody();
		parser.onStart(contents, source, seed);
		
		
		AtomicInteger count = new AtomicInteger(0);
		for(Content content : contents) {

			Type type = content.getType();
			String anchor = content.getContent().toString();
			
			if(Type.SEED.equals(type) && ! seed.equals(anchor)) {
		        log.info("[visit #"+count.addAndGet(1)+"] "+anchor);
		        
				RequestEntity<?> req = RequestEntity.get(new URI(anchor)).build();
				ResponseEntity<String> res = restTemplate.exchange(req, String.class);
				String anchorSource = res.getBody();
				parser.onVisit(contents, anchorSource, anchor);
			}
		}
		
        log.info("[finish] "+seed);
		parser.onFinish(contents, seed);
	}

}
