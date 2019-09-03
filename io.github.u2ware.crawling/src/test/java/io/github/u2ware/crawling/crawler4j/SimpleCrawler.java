package io.github.u2ware.crawling.crawler4j;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;


public class SimpleCrawler extends WebCrawler {

    protected Log logger = LogFactory.getLog(getClass());

     @Override
     public boolean shouldVisit(Page referringPage, WebURL url) {
    	 String href = url.getURL();//.toLowerCase();
         logger.info("shouldVisit: " + href);
    	 return true;
     }
     
     @Override
     public void visit(Page page) {

         int docid = page.getWebURL().getDocid();
         String url = page.getWebURL().getURL();
         String domain = page.getWebURL().getDomain();
         String path = page.getWebURL().getPath();
         String subDomain = page.getWebURL().getSubDomain();
         String parentUrl = page.getWebURL().getParentUrl();
         String anchor = page.getWebURL().getAnchor();

         logger.info("Docid: {}"+ docid);
         logger.info("URL: {}"+ url);
         logger.info("Domain: '{}'"+ domain);
         logger.info("Sub-domain: '{}'"+ subDomain);
         logger.info("Path: '{}'"+ path);
         logger.info("Parent page: {}"+ parentUrl);
         logger.info("Anchor text: {}"+ anchor);

         if (page.getParseData() instanceof HtmlParseData) {
             HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
             String text = htmlParseData.getText();
             String html = htmlParseData.getHtml();
             Set<WebURL> links = htmlParseData.getOutgoingUrls();

             logger.info("Text length: {} "+ text.length());
             logger.info("Html length: {} "+ html.length());
             logger.info("Number of outgoing links: {}"+ links.size());
         }

         Header[] responseHeaders = page.getFetchResponseHeaders();
         if (responseHeaders != null) {
             logger.debug("Response headers:");
             for (Header header : responseHeaders) {
                 logger.info("\t{}: {}"+ header.getName()+ header.getValue());
             }
         }

         logger.debug("=============");
     }
}

