package io.github.u2ware.crawling.jsoup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.u2ware.crawling.crawler4j.SimpleCrawling;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyJsoupParserTests {

	protected Log logger = LogFactory.getLog(MyJsoupParserTests.class);
	
	@Test
	public void contextLoads() throws Exception {
		
		logger.info("--------------------------------------------------------------------------");
        SimpleCrawling.of(
        	"https://www.munjung.es.kr/new_big/board/bbs/board.php?bo_table=edu_01",
        	"https://www.munjung.es.kr/new_big/board/bbs/board.php?bo_table=edu_01&wr_id="
        ).start(new MyJsoupParser());
	}
}