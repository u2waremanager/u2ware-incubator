package io.github.u2ware.crawling.htmlunit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.util.StringUtils;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import io.github.u2ware.crawling.ApplicationTests;

public class HtmlunitTests extends ApplicationTests{
	protected Log logger = LogFactory.getLog(getClass());

	@Test
	public void contextLoads() throws Exception {
		
		String url10 = "http://school.cbe.go.kr/seochon-e/M010302";
		String url20 =  "https://www.munjung.es.kr/new_big/board/bbs/board.php?bo_table=edu_01";
//		String url21 =  "https://www.munjung.es.kr/new_big/board/bbs/board.php?bo_table=edu_01&wr_id=";
		
		
		printPage("https://www.munjung.es.kr/new_big/board/bbs/board.php?bo_table=edu_01", (p, a) ->{
			return a.toString().contains("wr_id=");
		});		
		printPage("http://school.cbe.go.kr/seochon-e/M010302/", (p, a) ->{
			return a.toString().contains("view");
		});		
		printPage("http://school.gyo6.net/sanbuk/80119/board/44088", (p, a) ->{
			return a.hasAttribute("border");
		});
		printPage("http://www.dapsimni.es.kr/11844/subMenu.do", (p, a) ->{
			return a.hasAttribute("title");
		});		
	}

	private void printPage(String url, SeedFilter filter) throws Exception{

		List<String> urls = new ArrayList<>();
		
		
		WebClient client = new WebClient();
		client.getOptions().setThrowExceptionOnScriptError(false);
		client.getOptions().setUseInsecureSSL(true);
		client.getOptions().setCssEnabled(false);

		
		HtmlPage page = client.getPage(url);
		page.getAnchors().forEach(anchor->{
			if(filter.shouldVisit(page, anchor)) {
				urls.add(anchor.toString());
			}
		});
		
		urls.forEach(u->{
			logger.info("\n");
			logger.info(u);
			try {
				HtmlPage sub = client.getPage(url);
				sub.getAnchors().forEach(a->{
					if(a.toString().equals(u)) {
						try {
							HtmlPage pp = (HtmlPage)a.click();
							
							pp.getBody().asText();
							
							logger.info(StringUtils.trimAllWhitespace(pp.asText()));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}catch(Exception e) {
			}
		});
		client.close();
	}
	
	public interface SeedFilter{
		public boolean shouldVisit(HtmlPage page, HtmlAnchor anchor);
	}
	

}
