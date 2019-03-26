package com.example.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.schedule.DateTimeRangeEvent;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	protected Log logger = LogFactory.getLog(getClass());
	
	private @Autowired ApplicationEventPublisher publisher;

	public DateTime parse(String date) {
		return DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd"));
	}

	@Test
	public void contextLoads() throws Exception {
		
		publisher.publishEvent(new DateTimeRangeEvent(parse("2019-02-21")));
	}
}

