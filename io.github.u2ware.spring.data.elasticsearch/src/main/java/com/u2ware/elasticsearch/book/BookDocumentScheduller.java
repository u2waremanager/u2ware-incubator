package com.u2ware.elasticsearch.book;

import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BookDocumentScheduller implements InitializingBean{

	@Autowired
	private BookDocument bookDocument;

	@Scheduled(cron="0 0 0 1/1 * ?")
	private void scheduled() {
		bookDocument.setIndex(new DateTime().toString("yyyyMMdd"));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		scheduled();
	}
}