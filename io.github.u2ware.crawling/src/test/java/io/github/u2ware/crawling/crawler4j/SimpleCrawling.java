package io.github.u2ware.crawling.crawler4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.CrawlController.WebCrawlerFactory;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class SimpleCrawling {
	
	public static SimpleCrawling of(String... pageUrls) {
		return new SimpleCrawling(pageUrls);
	}
	
	protected Log logger = LogFactory.getLog(getClass());

	private String defaultCrawlStorageFolder = "data/"+System.currentTimeMillis();
	private int defaultNumberOfCrawlers = 1;
	
	private String[] pageUrls;
	private CrawlConfig crawlConfig;
	private PageFetcher pageFetcher;
	private RobotstxtServer robotstxtServer;

	private SimpleCrawling(String... pageUrls) {
		Assert.notEmpty(pageUrls, "pageUrls is empty");
		
		this.pageUrls = pageUrls;
		
		this.crawlConfig = new CrawlConfig();
		crawlConfig.setThreadShutdownDelaySeconds(0);
		crawlConfig.setThreadMonitoringDelaySeconds(1);
		crawlConfig.setCleanupDelaySeconds(0);
		crawlConfig.setPolitenessDelay(0);
		crawlConfig.setCrawlStorageFolder(defaultCrawlStorageFolder);
		crawlConfig.setMaxDepthOfCrawling(pageUrls.length - 1);
		
		this.pageFetcher = new PageFetcher(crawlConfig);
		
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(false);
        this.robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
	}
	
	
	public SimpleCrawling crawlConfig(CrawlConfig crawlConfig) {
		Assert.notNull(crawlConfig, "crawlConfig is null");
		this.crawlConfig = crawlConfig;
		return this;
	}
	public SimpleCrawling pageFetcher(PageFetcher pageFetcher) {
		Assert.notNull(pageFetcher, "pageFetcher is null");
		this.pageFetcher = pageFetcher;
		return this;
	}
	public SimpleCrawling robotstxtServer(RobotstxtServer robotstxtServer) {
		Assert.notNull(robotstxtServer, "robotstxtServer is null");
		this.robotstxtServer = robotstxtServer;
		return this;
	}
	

	private CrawlController crawlController() {
		try {
			CrawlController crawlController =  new CrawlController(crawlConfig, pageFetcher, robotstxtServer);
			crawlController.addSeed(pageUrls[0]);
			return crawlController;
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	public void start() {
		start(null, defaultNumberOfCrawlers);
	}
	
	public void start(int numberOfCrawlers) {
		start(null, numberOfCrawlers);
	}
	
	public void start(SimpleParser parser) {
		start(parser, defaultNumberOfCrawlers);
	}
	
	public void start(SimpleParser parser, int numberOfCrawlers) {
		crawlController().start(new DefaultWebCrawler(parser, pageUrls), numberOfCrawlers);
	}
	

	public void startNonBlocking() {
		startNonBlocking(null, defaultNumberOfCrawlers);
	}
	
	public void startNonBlocking(int numberOfCrawlers) {
		startNonBlocking(null, numberOfCrawlers);
	}
	
	public void startNonBlocking(SimpleParser parser) {
		startNonBlocking(parser, defaultNumberOfCrawlers);
	}
	
	public void startNonBlocking(SimpleParser parser, int numberOfCrawlers) {
		CrawlController controller = crawlController();
		controller.startNonBlocking(new DefaultWebCrawler(parser, pageUrls), numberOfCrawlers);
		SimpleCrawling.appendController(controller);
	}

	
	private static List<CrawlController> controllers = new ArrayList<>();

	public static void appendController(CrawlController controller) {
		controllers.add(controller);
	}
	
	public static void waitUntilFinish() {
		controllers.forEach(controller->{
			controller.waitUntilFinish();
		});
		controllers.clear();
	}
	
	
	private static class DefaultWebCrawler extends WebCrawler implements WebCrawlerFactory<WebCrawler>{
		
		protected Log logger = LogFactory.getLog(getClass());
		
	    private AtomicInteger count;
		private String[] pageUrls;
		private SimpleParser parser;
		
		private DefaultWebCrawler(SimpleParser parser, String... pageUrls) {
			this.parser = parser;
			this.pageUrls = pageUrls;
			this.count = new AtomicInteger(0);
		}

		private int getIndex(String pageUrl) {
			
			int i = -1;
			for(String p : pageUrls) {
				i++;
				if(p.equals(pageUrl)) {
					return i;
				}
			}
			return -1;
		}
		
		public WebCrawler newInstance() throws Exception {
			return new DefaultWebCrawler(parser, pageUrls);
		}
		
		
	    @Override
	    public boolean shouldVisit(Page referringPage, WebURL url) {
	    	int parentIndex = getIndex(referringPage.getWebURL().getURL());
	    	if(parentIndex < 0) {
	    		return false;
	    	}
	    	String target = pageUrls[parentIndex+1];
	        return StringUtils.startsWithIgnoreCase(url.getURL(), target);
	    }
	    
	    @Override
	    public void visit(Page page) {
	    	
	        String url = page.getWebURL().getURL();
	        logger.info("[onCrawled #"+this.count.get()+"] "+url);

			if (page.getParseData() instanceof HtmlParseData) {
				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
				if(parser != null) 
					parser.onCrawled(url, htmlParseData.getHtml(), this.count.get());
			}
	        this.count.set(this.count.get()+1);
	    }
	    @Override
	    public void onStart() {
	        logger.info("[onStarted] "+ pageUrls[0]);
			if(parser != null) 
				parser.onStarted(pageUrls[0]);
	    }
	    
	    @Override
	    public void onBeforeExit() {
	        logger.info("[onFinished] "+ pageUrls[0]);
			if(parser != null) 
				parser.onFinished(pageUrls[0]);
	    }
	}
}