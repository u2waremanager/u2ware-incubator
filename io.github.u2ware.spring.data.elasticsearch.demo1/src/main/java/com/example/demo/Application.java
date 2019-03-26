package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.Aggregations;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.demo.event.DspBase;
import com.example.demo.event.DspBaseQueryBuilder;
import com.example.demo.event.TimeSeriesEventQueryBuilder;
import com.example.demo.rule.AnomRuleDmn;
import com.example.demo.rule.AnomRuleDmnRepository;

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
	@Scheduled(cron="1 * * * * *")
	public void batch() {
		DateTime current = new DateTime();
		DateTime min = current.minusMinutes(1).withSecondOfMinute(0).withMillisOfSecond(0);
		DateTime max = current.minusMinutes(1).withSecondOfMinute(59).withMillisOfSecond(999);
		logger.info("=============================================================");
		logger.info("min="+min.toString("yyyy-MM-dd HH:mm:ss")+", max="+max.toString("yyyy-MM-dd HH:mm:ss")+" ");
		batch(min.getMillis(), max.getMillis());
	}

	/////////////////////////////////////////////////////
	// Batch Implements
	/////////////////////////////////////////////////////
	@Autowired
    private AnomRuleDmnRepository anomRuleDmnRepository;

	@Autowired
    private ElasticsearchOperations operations;

	public void batch(Long minTime, Long maxTime) {
		
		List<IndexQuery> quries = new ArrayList<>();

		logger.info("=========================================================");
		logger.info("minTime="+minTime+", maxTime="+maxTime);
		logger.info("\n");

		Iterable<AnomRuleDmn> rules = anomRuleDmnRepository.findAll();
		for(AnomRuleDmn rule : rules) {

			logger.info("\n");
			logger.info(rule);
			
			///////////////////////////////////////
			//
			///////////////////////////////////////
			Long tenantId = rule.getId().getTenant().getTenantNo();
			Long domainId = rule.getId().getDmn().getDmnNo();
			String targetFieldName = rule.getId().getRule().getTargetFieldName();
			String targetFieldOprtrCode = rule.getId().getRule().getTargetFieldOprtrCode();
			String anomRuleGroupFieldName = rule.getId().getRule().getFields().iterator().next().getAnomRuleGroupFieldName();
			
			SearchQuery query = new DspBaseQueryBuilder()
					.tenantId(tenantId)
					.domainId(domainId)
					.targetFieldName(targetFieldName)
					.targetFieldOprtrCode(targetFieldOprtrCode)
					.anomRuleGroupFieldName(anomRuleGroupFieldName)
					.minTime(minTime)
					.maxTime(maxTime)
					.build();
			long count = operations.count(query, DspBase.class);
			logger.info("Count DspBase: "+count+" row(s)");
			
			if(count > 0 ) {
				
				query.setPageable(PageRequest.of(0, 10));
				
				AggregatedPage<DspBase> queryResult =  (AggregatedPage<DspBase>)operations.queryForPage(query, DspBase.class);
				logger.info("Search DspBase: "+queryResult.getTotalElements()+" row(s)");

				
				///////////////////////////////////////
				//
				///////////////////////////////////////
				String tenantName = rule.getId().getTenant().getTenantName();
				String domainName = rule.getId().getDmn().getDmnName();
				List<Object> baseEventIds = queryResult.stream().map(s -> s.getEventId()).collect(Collectors.toList());
				Aggregations aggregations = queryResult.getAggregations();
				
				List<IndexQuery> indexQuries = new TimeSeriesEventQueryBuilder()
						.tenantId(tenantId)
						.tenantName(tenantName)
						.domainId(domainId)
						.domainName(domainName)
						.targetFieldName(targetFieldName)
						.targetFieldOprtrCode(targetFieldOprtrCode)
						.anomRuleGroupFieldName(anomRuleGroupFieldName)
						.minTime(minTime)
						.baseEventIds(baseEventIds)
						.baseEventCount(((Long)count).intValue())
						.aggregations(aggregations)
						.build();
				for(IndexQuery indexQuery : indexQuries) {
					logger.info("Create : "+indexQuery.getObject());
				}

				
				quries.addAll(indexQuries);
			}
			logger.info("\n");
		}

		if(! quries.isEmpty()) {
			operations.bulkIndex(quries);
			logger.info("Save : "+quries.get(0).getIndexName()+" "+quries.size()+" row(s)");
			logger.info("\n");
		}
	}
}

