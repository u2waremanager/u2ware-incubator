package com.example.demo.event;

import static com.example.demo.event.Constants.AGG_ANOM;
import static com.example.demo.event.Constants.AGG_ASSET;
import static com.example.demo.event.Constants.AGG_STATS;
import static com.example.demo.event.Constants.AGG_UNIQ;
import static com.example.demo.event.Constants.ANOM_OPR_CODE_AVG;
import static com.example.demo.event.Constants.ANOM_OPR_CODE_MAX;
import static com.example.demo.event.Constants.ANOM_OPR_CODE_MIN;
import static com.example.demo.event.Constants.ANOM_OPR_CODE_SUM;
import static com.example.demo.event.Constants.ANOM_OPR_CODE_UNIQUE;
import static com.example.demo.event.Constants.TIME_SERIES_EVENT;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.joda.time.DateTime;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


@Component
public class TimeSeriesEventQueryBuilder {

	private Long tenantId;
	private String tenantName;
	private Long domainId;
	private String domainName;
	private String targetFieldName;
	private String targetFieldOprtrCode;
	private String anomRuleGroupFieldName;
	private Long minTime;
	//private Long maxTime;
	private List<Object> baseEventIds;
	private Integer baseEventCount;
	
	
	private Aggregations aggregations;
	
	public TimeSeriesEventQueryBuilder tenantId(Long tenantId) {
		this.tenantId = tenantId;
		return this;
	}
	public TimeSeriesEventQueryBuilder tenantName(String tenantName) {
		this.tenantName = tenantName;
		return this;
	}
	public TimeSeriesEventQueryBuilder domainId(Long domainId) {
		this.domainId = domainId;
		return this;
	}
	public TimeSeriesEventQueryBuilder domainName(String domainName) {
		this.domainName = domainName;
		return this;
	}
	public TimeSeriesEventQueryBuilder targetFieldName(String targetFieldName) {
		this.targetFieldName = targetFieldName;
		return this;
	}
	public TimeSeriesEventQueryBuilder targetFieldOprtrCode(String targetFieldOprtrCode) {
		this.targetFieldOprtrCode = targetFieldOprtrCode;
		return this;
	}
	public TimeSeriesEventQueryBuilder anomRuleGroupFieldName(String anomRuleGroupFieldName) {
		this.anomRuleGroupFieldName = anomRuleGroupFieldName;
		return this;
	}
	public TimeSeriesEventQueryBuilder minTime(Long minTime) {
		this.minTime = minTime;
		return this;
	}
//	public TimeSeriesEventQueryBuilder maxTime(Long maxTime) {
//		this.maxTime = maxTime;
//		return this;
//	}
	
	public TimeSeriesEventQueryBuilder baseEventIds(List<Object> baseEventIds) {
		this.baseEventIds = baseEventIds;
		return this;
	}
	
	public TimeSeriesEventQueryBuilder baseEventCount(Integer baseEventCount) {
		this.baseEventCount = baseEventCount;
		return this;
	}
	
	
	
	public TimeSeriesEventQueryBuilder aggregations(Aggregations aggregations) {
		this.aggregations = aggregations;
		return this;
	}

	
	public List<IndexQuery> build() {
		Assert.notNull(tenantId, "'tenantId' must not be null");
		Assert.notNull(tenantName, "'tenantName' must not be null");
		Assert.notNull(domainId, "'domainId' must not be null");
		Assert.notNull(domainName, "'domainName' must not be null");
		Assert.notNull(targetFieldName, "'targetFieldName' must not be null");
		Assert.notNull(targetFieldOprtrCode, "'targetFieldOprtrCode' must not be null");
		Assert.notNull(anomRuleGroupFieldName, "'anomRuleGroupFieldName' must not be null");
		Assert.notNull(baseEventIds, "'baseEventIds' must not be null");
		Assert.notNull(aggregations, "'aggregations' must not be null");

		List<IndexQuery> queries = new ArrayList<>();		
		
		////////////////////////////////////////////////////
		//
		////////////////////////////////////////////////////
		Terms assetTerms = (Terms)aggregations.getAsMap().get(AGG_ASSET);
		if(assetTerms == null) return queries;
		
		
		for (Bucket assetBucket : assetTerms.getBuckets()) {
			
			Terms anomTerms = assetBucket.getAggregations().get(AGG_ANOM);
			for (Bucket anomBucket : anomTerms.getBuckets()) {

				Terms uniqTerms = anomBucket.getAggregations().get(AGG_UNIQ);
				
				if(uniqTerms != null) {
					for (Bucket uniqBucket : uniqTerms.getBuckets()) {
						queries.add(extractQuery(createEvent(assetBucket, anomBucket, uniqBucket)));
					}
				}else {
					Stats stats = anomBucket.getAggregations().get(AGG_STATS);
					queries.add(extractQuery(createEvent(assetBucket, anomBucket, stats)));
				}
			}
		}
		return queries;
	}

	//////////////////////////////////////////////////////////////////
	//
	///////////////////////////////////////////////////////////////////
	private <T> IndexQuery extractQuery(TimeSeriesEvent event) {
		IndexQuery indexQuery = new IndexQuery();
		indexQuery.setObject(event);
		indexQuery.setId(event.getEventId().toString());
		indexQuery.setIndexName(extractIndex(event));
		indexQuery.setType(extractType(event));
		return indexQuery;
	}
	
	private String extractIndex(TimeSeriesEvent event) {
		
		String index = "dsp_timeseries_"+event.getTenantId()+"_"+event.getDomainId()+"_";
		if(event.getEventStartTime() != null) {
			index = index + new DateTime(event.getEventStartTime()).toString("yyyyMMdd");
		}else {
			index = index +"test";
		}
		return index;
	}

	private String extractType(TimeSeriesEvent event) {
		return "logs";
	}
	
	
	
	/////////////////////////////////////////////////////////////////////////////
	//
	/////////////////////////////////////////////////////////////////////////////
	private <T> TimeSeriesEvent createEvent(Bucket asset, Bucket anom, T uniqOrStats) {
		
		Long eventId = createEventId();
		String groupFieldValues = createEventAnomValue(anom);
		Long assetsId = createEventAssetValue(asset);
		
		TimeSeriesEvent e = new TimeSeriesEvent();
		e.setEventId(eventId);
		e.setEventStartTime(minTime);
		e.setEventType(TIME_SERIES_EVENT);
		e.setTenantId(tenantId);
		e.setTenantName(tenantName);
		e.setDomainId(domainId);
		e.setDomainName(domainName);
		e.setOperatedField(targetFieldName);
		e.setOperator(targetFieldOprtrCode);
		e.setGroupFields(anomRuleGroupFieldName);
		e.setGroupFieldValues(groupFieldValues);
		e.setAssetsId(assetsId);

		if(uniqOrStats instanceof Stats) {
			Long operatedValue = createEventStatsValue((Stats)uniqOrStats, targetFieldOprtrCode);
			e.setOperatedValue(operatedValue);
		}else{
			String uniqueValue = createEventUniqValue((Bucket)uniqOrStats);
			e.setUniqueValue(uniqueValue);
		}

		e.setBaseEventIds(baseEventIds.toString());;
		e.setBaseEventCount(baseEventCount);
		return e;
	}
	
	private Long createEventId() {
		long currlong = System.currentTimeMillis();//milliseconds < 2^44
		long randlong = ThreadLocalRandom.current().nextLong(BIT19) << 44;
		return randlong+currlong;
	}

	private Long createEventAssetValue(Bucket asset) {
		return asset.getKeyAsNumber().longValue();
	}
	
	private String createEventAnomValue(Bucket anom) {
		return anom.getKeyAsString();
	}
	
	private String createEventUniqValue(Bucket uniq) {
		return uniq.getKeyAsString();
	}
	
	private Long createEventStatsValue(Stats stats, String code) {
		switch(code) {
			case ANOM_OPR_CODE_MIN:
				return (long) stats.getMin();
			case ANOM_OPR_CODE_MAX:
				return (long) stats.getMax();
			case ANOM_OPR_CODE_SUM:
				return (long) stats.getSum();
			case ANOM_OPR_CODE_AVG:
				return (long) stats.getAvg();
			case ANOM_OPR_CODE_UNIQUE:
				return (long) stats.getCount();
		}
		return null;
	}

	private static final long BIT19 = (long)Math.pow(2, 19);
}
