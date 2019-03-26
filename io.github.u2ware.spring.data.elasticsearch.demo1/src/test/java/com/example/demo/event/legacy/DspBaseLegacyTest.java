package com.example.demo.event.legacy;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.stats;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.stats.StatsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.example.demo.ApplicationTests;
import com.example.demo.event.Constants;
import com.example.demo.rule.AnomRuleDmn;
import com.example.demo.rule.AnomRuleDmnRepository;
import com.example.demo.rule.Rule;

public class DspBaseLegacyTest extends ApplicationTests{

	@Autowired
    private AnomRuleDmnRepository anomRuleDmnRepository;

	@Autowired
    private RestHighLevelClient client;

	@Test
	public void contextLoads() throws Exception {
		Iterable<AnomRuleDmn> rules = anomRuleDmnRepository.findAll();
		for(AnomRuleDmn rule : rules) {
			logger.info("\n");
			logger.info(rule);
			executeByClient(rule);
		}
	}
	
	private final static String index = "dsp_base_*";
	private final static String type = "logs";

	private void executeByClient(AnomRuleDmn rule) throws Exception {
		
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(extractQuery(rule));
		sourceBuilder.aggregation(extractAggregation(rule));
		sourceBuilder.from(0);
		//sourceBuilder.size(0); //size(0)이면 Reponse에서 원천데이터 제외

		SearchRequest searchRequest = new SearchRequest(index);
		searchRequest.types(type);
		searchRequest.source(sourceBuilder);
		
		logger.info("executeByClient Reqest: "+searchRequest); // Debugging
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		logger.info("executeByClient Response: "+response.getClass());

		////////////////////////////////
		// SearchHit
		////////////////////////////////
		resolveSearchHits(response.getHits());		

		////////////////////////////////
		// Aggregations
		////////////////////////////////
		resolveAggregations(response.getAggregations());
	}
	
	
	private BoolQueryBuilder extractQuery(AnomRuleDmn rule) {
		return boolQuery()
			//.must(rangeQuery("eventStartTime").gte(_eventStartTime).lte(_eventEndTime))
			.must(termQuery("eventType", Constants.BASE_NORMAL_EVENT))
			.must(termQuery("tenantId", rule.getId().getTenant().getTenantNo()))
			.must(termQuery("domainId", rule.getId().getDmn().getDmnNo()));
	}

	private AbstractAggregationBuilder<?> extractAggregation(AnomRuleDmn rule) {
		String targetField = rule.getId().getRule().getTargetFieldName();
		String targetFieldOprtr = rule.getId().getRule().getTargetFieldOprtrCode();
		String groupFieldsName = rule.getId().getRule().getFields().iterator().next().getAnomRuleGroupFieldName();

		String[] fields = StringUtils.commaDelimitedListToStringArray(groupFieldsName);
		StringBuilder groupFields = new StringBuilder();
		groupFields.append("[");
		for(String field : fields) {
			if(groupFields.length() > 1) {
				groupFields.append(",");
			}
			groupFields.append("doc."+field+".value");
		}
		groupFields.append("].join(\",\")");
		

		Script script = new Script(groupFields.toString());
		TermsAggregationBuilder subAggregation = terms(Constants.AGG_ANOM).script(script).size(10000);

		// Target Field Aggregation 설정
		if(Rule.ANOM_OPR_CODE_UNIQUE.equals(targetFieldOprtr) ){
			// Unique Count : UniqueValue Field Accumulation 분석
			AggregationBuilder aggUniqValue = terms(Constants.AGG_UNIQ).field(targetField).size(10000);
			subAggregation = subAggregation.subAggregation(aggUniqValue);
		} else {
			// Matrix : operatedValue Field Accumulation Matrix 분석
			StatsAggregationBuilder aggStatsValue = stats(Constants.AGG_STATS).field(targetField);
			subAggregation = subAggregation.subAggregation(aggStatsValue);
		}
		
		TermsAggregationBuilder aggregation = terms(Constants.AGG_ASSET).field("assetsId").size(10000);
		aggregation.subAggregation(subAggregation);
		
		return aggregation;
	}
	
	
	private void resolveSearchHits(SearchHits searchHits) {

		long totalHits = searchHits.getTotalHits();
		float maxScore = searchHits.getMaxScore();
		int count = 0;
		for (SearchHit hit : searchHits) {
			if (hit != null) {
				Map<String,Object> source = hit.getSourceAsMap();
				String tenantId = (String)source.get("tenantId");
				String domainId = (String)source.get("domainId");
				Long eventStartTime = (Long)source.get("eventStartTime");
				Integer eventType = (Integer)source.get("eventType");
				
				
				
				
				String formattedEventStartTime = new DateTime(eventStartTime).toString("yyyyMMdd");
				
				logger.info("executeByClient : "+hit.getIndex()+ " "+hit.getId()+" "+eventType+" "+tenantId+" "+domainId+" "+eventStartTime+" "+formattedEventStartTime);
				count++;
			}
		}
		logger.info("executeByClient totalHits: "+totalHits);
		logger.info("executeByClient maxScore: "+maxScore);
		logger.info("executeByClient count: "+count);
		
	}

	private void resolveAggregations(Aggregations aggregations) {
		
		int count = 0;
		
		Terms assetTerms = aggregations.get(Constants.AGG_ASSET);
		for (Terms.Bucket assetBucket : assetTerms.getBuckets()) {
			
			Terms anomTerms = assetBucket.getAggregations().get(Constants.AGG_ANOM);
			for (Terms.Bucket anomBucket : anomTerms.getBuckets()) {

				Terms uniqTerms = anomBucket.getAggregations().get(Constants.AGG_UNIQ);

				if(uniqTerms != null) {
					for (Terms.Bucket uniqBucket : uniqTerms.getBuckets()) {
						logger.info((count++)+ " XXX "+uniqBucket.getKeyAsString());
					}
				}else {
					Stats stats = anomBucket.getAggregations().get(Constants.AGG_STATS);
					logger.info((count++)+ " YYY "+stats.getMin()+" "+stats.getMax()+" "+stats.getSum()+" "+stats.getAvg()+" "+stats.getCount());
				}
			}
		}
	}
}

