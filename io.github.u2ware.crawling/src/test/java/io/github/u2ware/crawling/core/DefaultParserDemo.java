package io.github.u2ware.crawling.core;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import io.github.u2ware.crawling.ApplicationTests;
import io.github.u2ware.crawling.core.Content.Type;
import io.github.u2ware.crawling.htmlunit.MyHtmlunitTests;

public class DefaultParserDemo extends ApplicationTests{
	protected Log logger = LogFactory.getLog(MyHtmlunitTests.class);

	@Test
	public void contextLoads() throws Exception {

//		Collection<String> listOne = Arrays.asList("milan","iga","dingo","iga","elpha","iga","hafil","iga","meat","iga", "neeta.peeta","iga");
//		Collection<String> listTwo = Arrays.asList("hafil","iga","binga", "mike", "dingo","dingo","dingo");
//		Collection<String> similar = new HashSet<String>( listOne );
//		
//		
//		Collection<String> different = new HashSet<String>();
//		different.addAll( listOne );
//		different.addAll( listTwo );
//		
//		similar.retainAll( listTwo );
//		different.removeAll( similar );
//		
//		System.out.printf("One:%s%nTwo:%s%nSimilar:%s%nDifferent:%s%n", listOne, listTwo, similar, different);

		
//		try{ 
//			String text = "'<img src=\"cid:123456789\"/>'"; 
//			Pattern ptn = Pattern.compile("src=[\"']cid:([a-z-A-Z-\\s0-9]*)"); 
//			String text = "'123456789', 'ggg', 'aaaaaaaaaaaaaaaa'"; 
//			Pattern ptn = Pattern.compile("[']([a-z-A-Z-\\s0-9]*)"); 
//			Matcher matcher = ptn.matcher(text); 
//	
//			while(matcher.find()){ 
//				System.out.println(matcher.group(1)); 
//			} 
//		} catch(Exception e) { 
//			e.printStackTrace(); 
//		}
		String s00 = "hello world";
		String s01 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=949>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s02 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=907>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s03 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=906>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s04 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=905>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s05 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=952>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s06 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=951>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s07 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=950>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s08 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=949>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s09 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=948>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s10 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=947>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s11 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=946>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s12 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=945>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s13 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=944>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s14 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=943>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s15 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=942>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s16 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=941>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s17 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=940>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s18 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=939>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";
		String s19 = "HtmlAnchor[<a href=../bbs/board.php?bo_table=edu_01&amp;wr_id=938>] HtmlNoBreak[<nobr style=display:block; overflow:hidden;>]";

		
		String a = StringUtils.difference(s00, s01);
		
		logger.info(a);
		logger.info(a.length());
		logger.info(s01.length());
		logger.info(a.length() / s01.length());

		
		String b = StringUtils.difference(s01, s02);
		
		logger.info(b);
		logger.info(b.length());
		logger.info(s02.length());
		logger.info((100 * b.length() ) / s02.length());
		
		
		String a1 = "abcdefg";
		String a2 = "aceg";
		String a3 = "b";
		
		if(Math.abs(s01.length() - s02.length()) < 3) {
			logger.info(s01);
		}
		
		if(Math.abs(s00.length() - s02.length()) < 3) {
			logger.info("cccccccccccccccccccccccccc");
		}
		

		
		
		
		
		
		
//		logger.info("\n");
//		String url10 =  "https://www.munjung.es.kr/new_big/board/bbs/board.php?bo_table=edu_01";
//		String url11 =  "https://www.munjung.es.kr/new_big/board/bbs/board.php?bo_table=edu_01&wr_id=";
//		List<Content> contents10 = Crawling.of(url10, url11).start();
//		printContents(contents10);
//		
//		logger.info("\n");
//		String url20 = "http://school.cbe.go.kr/seochon-e/M010302";
//		String url21 = "http://school.cbe.go.kr/seochon-e/M010302/view/";
//		List<Content> contents20 = Crawling.of(url20,url21).start();
//		printContents(contents20);
//		
//		
//		String url30 =  "http://school.gyo6.net/sanbuk/80119/board/44088";
//		List<Content> contents30 = Crawling.of(url30).start();
//		printContents(contents30);
	}
	
	
	private void printContents(List<Content> contents) {
		
		contents.forEach(c->{ logger.info(c);});
		
		logger.info("\n");
		Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).stream().forEach(index->{
			logger.info("\n");
			contents.forEach(c->{
				if(index.equals(c.getIdentify()) && Type.TEXT.equals(c.getType())) {
					logger.info(c);
				}
			});
		});;
		
		logger.info("\n");
		contents.forEach(c->{
			if(Type.FILE.equals(c.getType())) {
				logger.info(c);
			}
		});
	}
	
	
}
