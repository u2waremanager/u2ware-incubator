package com.skinfosec.dsp.report.es.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.RestHighLevelClient;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.skinfosec.dsp.report.es.batch.schedule.DateTimeRangeEvent;

@SpringBootApplication
@EnableScheduling
@EnableElasticsearchRepositories(elasticsearchTemplateRef="elasticsearchOperations")
public class Application {

	protected Log logger = LogFactory.getLog(getClass());

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/////////////////////////////////////////////////////////////
	// With Transport Client
	//	 spring.data.elasticsearch.cluster-nodes=127.0.0.1:9300
	/////////////////////////////////////////////////////////////
//	@Autowired
//	private Client transportClient;
//
//	@Autowired
//	private ElasticsearchConverter converter;
//	
//	@Bean
//	public ElasticsearchTemplate elasticsearchOperations1() {
//		return new ElasticsearchTemplate1(transportClient, converter);
//	}
	
	/////////////////////////////////////////////////////////////
	// With REST Client
	//	 spring.elasticsearch.rest.urls=http://127.0.0.1:9200
	/////////////////////////////////////////////////////////////
	@Autowired
	private RestHighLevelClient restClient;
	
	@Autowired
	private ElasticsearchConverter converter;

	@Bean
	public ElasticsearchOperations elasticsearchOperations() {
		return new ElasticsearchRestTemplate(restClient, converter);
	}
	
	/////////////////////////////////////////////////////
	// Batch
	/////////////////////////////////////////////////////
	private @Autowired ApplicationEventPublisher publisher;

	@Scheduled(cron = "${com.skinfosec.dsp.report.es.batch.Application.cron}")
	public void batch() {
		DateTime current = new DateTime();
		DateTime min = current.minusDays(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
		DateTime max = current.minusDays(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
		logger.info("=========================================================");
		logger.info("minTime="+min+", maxTime="+max);
		logger.info("\n");
		
		publisher.publishEvent(new DateTimeRangeEvent(min,max));
	}
}