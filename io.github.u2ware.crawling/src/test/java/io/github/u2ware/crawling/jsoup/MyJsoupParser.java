package io.github.u2ware.crawling.jsoup;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.github.u2ware.crawling.crawler4j.SimpleParser;

public class MyJsoupParser implements SimpleParser{

	protected Log logger = LogFactory.getLog(getClass());

	@Override
	public void onStarted(String url) {
		
	}

	@Override
	public void onCrawled(String url, String contents, int index) {
//		if(index == 0) return;
		Document doc = Jsoup.parse(contents);
		
		AtomicInteger numbering = new AtomicInteger(0);
		Map<Integer,String> text = new HashMap<>();
		
		doc.select("table tr td").forEach(e->{
			
			numbering.addAndGet(1);
			
			text.put(numbering.get(), e.text());
			logger.info("\t"+e.text());
			
			e.select("a").forEach(a->{
				if(text.get(numbering.get()).equals(a.text())){
					logger.info("\t\t"+a.getClass());
				}
			});
		});
	}
	
	@Override
	public void onFinished(String url) {
		
	}
}
