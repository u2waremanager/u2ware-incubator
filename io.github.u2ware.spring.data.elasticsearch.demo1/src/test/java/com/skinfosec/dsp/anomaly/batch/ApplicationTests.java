package com.skinfosec.dsp.anomaly.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	protected Log logger = LogFactory.getLog(getClass());
	
	protected @Autowired Application application;

	@Test
	public void contextLoads() throws Exception {
		
		//application.batch(null, null);
		
		//application.batch(1548700000000l, 1548700000000l);
		
		Thread.sleep(60*1000*4);
	}
}

