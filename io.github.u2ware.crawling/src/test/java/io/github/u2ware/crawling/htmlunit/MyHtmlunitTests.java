package io.github.u2ware.crawling.htmlunit;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyHtmlunitTests {

	protected Log logger = LogFactory.getLog(MyHtmlunitTests.class);
	
	@Test
	public void contextLoads() throws Exception {
		
		String itemList = "https://www.munjung.es.kr/new_big/board/bbs/board.php?bo_table=edu_01";
		String item =     "https://www.munjung.es.kr/new_big/board/bbs/board.php?bo_table=edu_01&wr_id=";
				
		
		WebClient webClient = new WebClient();
		webClient.getOptions().setUseInsecureSSL(true);
		
		HtmlPage page = webClient.getPage("https://naver.com");
		logger.info(page);
		
		page.getAnchors().forEach(a->{
			try {
				URL u = HtmlAnchor.getTargetUrl(a.getHrefAttribute(), page);
				
				if(u.toString().startsWith(item)) {
					logger.info(u);
					Page x = a.openLinkInNewWindow();
					logger.info(x.getClass());
				}
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		});
		
		page.querySelectorAll("table tr td").forEach(e->{
			
			logger.info(e);
		});
		
		
		
	}
}