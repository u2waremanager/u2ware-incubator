package io.github.u2ware.crawling.crawler4j;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleTests {

	protected Log logger = LogFactory.getLog(SimpleTests.class);
	
	@Test
	public void contextLoads() throws Exception {
		
        logger.info("--------------------------------------------------------------------------");
        CrawlConfig config = new CrawlConfig();
        config.setThreadShutdownDelaySeconds(0);
        config.setCleanupDelaySeconds(0);
        config.setCrawlStorageFolder("target/crawler");
        config.setMaxDepthOfCrawling(0); //Depth....
        
        PageFetcher pageFetcher = new PageFetcher(config);
        
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(false);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        
        
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        controller.addSeed("http://school.cbe.go.kr/seochon-e/M010302");
        controller.start(SimpleCrawler.class, 10);
        
        logger.info("--------------------------------------------------------------------------");
        SimpleCrawling.of("http://school.cbe.go.kr/seochon-e/M010302").start();
        SimpleCrawling.of("http://school.cbe.go.kr/seochon-e/M010302").start();
        SimpleCrawling.waitUntilFinish();
        

		
	
	}
}