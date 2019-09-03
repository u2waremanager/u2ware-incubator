package io.github.u2ware.crawling.core;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import io.github.u2ware.crawling.core.Content.Type;

public class CrawlerWithCrawler4j extends Crawler<HtmlParseData> {
	
	public CrawlerWithCrawler4j(String seed, Parser<HtmlParseData> parser) {
		super(seed, parser);
	}
	
	@Override
	protected void crawling(Parser<HtmlParseData> parser, Collection<Content> contents) throws Exception {
		CrawlConfig crawlConfig = new CrawlConfig();
		crawlConfig.setThreadShutdownDelaySeconds(0);
		crawlConfig.setThreadMonitoringDelaySeconds(1);
		crawlConfig.setCleanupDelaySeconds(0);
		crawlConfig.setPolitenessDelay(0);
		crawlConfig.setCrawlStorageFolder("target/crawler4j/"+System.currentTimeMillis());
		crawlConfig.setMaxDepthOfCrawling(0);
		
		PageFetcher pageFetcher = new PageFetcher(crawlConfig);
		
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(false);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        


		
        log.info("[start] "+seed);
        CrawlController rootController =  new CrawlController(crawlConfig, pageFetcher, robotstxtServer);
        rootController.addSeed(seed);
        rootController.start(()->{
        	return new WebCrawler() {
        	    public void visit(Page page) {
        			if (page.getParseData() instanceof HtmlParseData) {
        				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
    					parser.onStart(contents, page.getWebURL().getURL().toString(), htmlParseData);
        			}
        	    }
        	};
        }, 1);
        

		AtomicInteger count = new AtomicInteger(0);
		for(Content content : contents) {

			Type type = content.getType();
			String anchor = content.getContent().toString();
			
			if(Type.SEED.equals(type) && ! seed.equals(anchor)) {
		        log.info("[visit #"+count.addAndGet(1)+"] "+anchor);
		        
		        CrawlController controller =  new CrawlController(crawlConfig, pageFetcher, robotstxtServer);
		        controller.addSeed(anchor);
		        controller.start(()->{
		        	return new WebCrawler() {
		        	    public void visit(Page page) {
		        			if (page.getParseData() instanceof HtmlParseData) {
		        				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
		    					parser.onVisit(contents, page.getWebURL().getURL(), htmlParseData);
		        			}
		        	    }
		        	};
		        }, 1);
			}
		}
		
        log.info("[finish] "+seed);
		parser.onFinish(contents, seed);
	}
}