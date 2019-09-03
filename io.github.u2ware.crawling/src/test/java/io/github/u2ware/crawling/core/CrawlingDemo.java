package io.github.u2ware.crawling.core;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import io.github.u2ware.crawling.ApplicationTests;
import io.github.u2ware.crawling.htmlunit.MyHtmlunitTests;

public class CrawlingDemo extends ApplicationTests{
	protected Log logger = LogFactory.getLog(MyHtmlunitTests.class);

	@Test
	public void contextLoads() throws Exception {
		
		String url1 = "http://school.cbe.go.kr/seochon-e/M010302";
		String url20 =  "https://www.munjung.es.kr/new_big/board/bbs/board.php?bo_table=edu_01";
		String url21 =  "https://www.munjung.es.kr/new_big/board/bbs/board.php?bo_table=edu_01&wr_id=";
		
		logger.info("----------------------------------------------------------------");
		Crawling.of(url1).start(new Parser1());
        
		logger.info("----------------------------------------------------------------");
		Crawling.of(url1).start(new Parser2());

		logger.info("----------------------------------------------------------------");
		Crawling.of(url1).start(new Parser3());

		logger.info("----------------------------------------------------------------");
		Crawling.of(url1).start(new Parser4());
        
		logger.info("----------------------------------------------------------------");
		Crawling.of(url20).start(new Parser1());
        
		logger.info("----------------------------------------------------------------");
		Crawling.of(url20).start(new Parser2());

		logger.info("----------------------------------------------------------------");
		Crawling.of(url20).start(new Parser3());

		logger.info("----------------------------------------------------------------");
		Crawling.of(url20).start(new Parser4());
	}
	
	private static class Parser1 implements Parser<String>{

		protected Log logger = LogFactory.getLog(getClass());

		@Override
		public void onInit(String rootSeed, String subSeed) {
			logger.info(rootSeed);
		}
		@Override
		public void onStart(Collection<Content> contents, String seed, String source) {
			logger.info(seed);
		}
		@Override
		public void onVisit(Collection<Content> contents, String anchor, String source) {
			logger.info(anchor);
		}
		@Override
		public void onFinish(Collection<Content> contents, String seed) {
			logger.info(seed);
		}
		@Override
		public void onDestroy(String rootSeed, String subSeed) {
			logger.info(rootSeed);
		}
	}
	private static class Parser2 implements Parser<Document>{
		protected Log logger = LogFactory.getLog(getClass());

		@Override
		public void onInit(String rootSeed, String subSeed) {
			logger.info(rootSeed);
		}
		@Override
		public void onStart(Collection<Content> contents, String seed, Document source) {
			logger.info(seed);
		}
		@Override
		public void onVisit(Collection<Content> contents, String anchor, Document source) {
			logger.info(anchor);
		}
		@Override
		public void onFinish(Collection<Content> contents, String seed) {
			logger.info(seed);
		}
		@Override
		public void onDestroy(String rootSeed, String subSeed) {
			logger.info(rootSeed);
		}
	}
	private static class Parser3 implements Parser<HtmlParseData>{
		protected Log logger = LogFactory.getLog(getClass());

		@Override
		public void onInit(String rootSeed, String subSeed) {
			logger.info(rootSeed);
		}
		@Override
		public void onStart(Collection<Content> contents, String seed, HtmlParseData source) {
			logger.info(seed);
		}
		@Override
		public void onVisit(Collection<Content> contents, String anchor, HtmlParseData source) {
			logger.info(anchor);
		}
		@Override
		public void onFinish(Collection<Content> contents, String seed) {
			logger.info(seed);
		}
		@Override
		public void onDestroy(String rootSeed, String subSeed) {
			logger.info(rootSeed);
		}
	}
	private static class Parser4 implements Parser<HtmlPage>{
		protected Log logger = LogFactory.getLog(getClass());

		@Override
		public void onInit(String rootSeed, String subSeed) {
			logger.info(rootSeed);
		}
		@Override
		public void onStart(Collection<Content> contents, String seed, HtmlPage source) {
			logger.info(seed);
		}
		@Override
		public void onVisit(Collection<Content> contents, String anchor, HtmlPage source) {
			logger.info(anchor);
		}
		@Override
		public void onFinish(Collection<Content> contents, String seed) {
			logger.info(seed);
		}
		@Override
		public void onDestroy(String rootSeed, String subSeed) {
			logger.info(rootSeed);
		}
	}
}