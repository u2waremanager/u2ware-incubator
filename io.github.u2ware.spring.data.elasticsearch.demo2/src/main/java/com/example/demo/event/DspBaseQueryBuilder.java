package com.example.demo.event;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregator.Range;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

public class DspBaseQueryBuilder {
	
	protected Log logger = LogFactory.getLog(getClass());

	private BoolQueryBuilder query = boolQuery();
	private AbstractAggregationBuilder<?> root;
	private AbstractAggregationBuilder<?> parent;
	private ArrayList<Order> orders = new ArrayList<>();
	
	public DspBaseQueryBuilder queryExists(String field) {
		query = query.must(existsQuery(field));
		return this;
	}
	public DspBaseQueryBuilder queryTerms(String field, Object value) {
		query = query.must(termQuery(field, value));
		return this;
	}

	public DspBaseQueryBuilder queryFormattedRange(String field, DateTimeRange range) {
		query = query.must(rangeQuery(field).format(DateTimeRange.PATTERN).gte(range.getFromAsString()).lte(range.getToAsString()));
		return this;
	}

	public DspBaseQueryBuilder queryRange(String field, DateTimeRange range) {
		query = query.must(rangeQuery(field).gte(range.getFrom()).lte(range.getTo()));
		return this;
	}
	
	public DspBaseQueryBuilder aggregationTerms(String field) {
		TermsAggregationBuilder aggregation = AggregationBuilders.terms(field).field(field).size(Integer.MAX_VALUE);
		return aggregation(aggregation);
	}
	
	public DspBaseQueryBuilder aggregationTerms(String field, int size) {
		TermsAggregationBuilder aggregation = AggregationBuilders.terms(field).field(field).size(size);
		return aggregation(aggregation);
	}

	
	public DspBaseQueryBuilder aggregationRange(String field, DateTimeRange... ranges) {
		RangeAggregationBuilder aggregation = AggregationBuilders.range(field).field(field);
		for(DateTimeRange range : ranges) {
			aggregation = aggregation.addRange(new Range(range.getKey(), range.getFrom(), range.getTo()));
		}
		return aggregation(aggregation);
	}
	
	public DspBaseQueryBuilder aggregationFormattedRange(String field, DateTimeRange... ranges) {
		RangeAggregationBuilder aggregation = AggregationBuilders.range(field).field(field);
		aggregation = aggregation.format(DateTimeRange.PATTERN);
		for(DateTimeRange range : ranges) {
			aggregation = aggregation.addRange(new Range(range.getKey(), range.getFromAsString(), range.getToAsString()));
		}
		return aggregation(aggregation);
	}
	
	public DspBaseQueryBuilder aggregation(AbstractAggregationBuilder<?> aggregation) {
		if(this.root == null) {
			this.root = aggregation;

		}else {
			if(parent == null) {
				this.parent = root;
			}
			this.parent.subAggregation(aggregation);
			this.parent = aggregation;
		}
		return this;
	}

	public DspBaseQueryBuilder orderAsc(String field) {
		orders.add(new Order(Direction.ASC, field));
		return this;
	}
	public DspBaseQueryBuilder orderDesc(String field) {
		orders.add(new Order(Direction.DESC, field));
		return this;
	}
	
	
	
	/////////////////////////////////////////////////////////////////////
	//
	//////////////////////////////////////////////////////////////////////
	public SearchQuery build() {

		return new NativeSearchQueryBuilder()
			    .withIndices(extractIndex())
			    .withTypes(extractType())
			    .withQuery(extractQuery())
			    .withPageable(extractPageable())
			    .addAggregation(extractAggregation())
			    .build();
	}

	/////////////////////////////////////////////////////////////////////
	//
	//////////////////////////////////////////////////////////////////////
	private String extractIndex() {
		return "dsp_base_*";
	}

	private String extractType() {
		return "logs";
	}

	private BoolQueryBuilder extractQuery() {
		return query;
	}
	
	private AbstractAggregationBuilder<?> extractAggregation() {
		return root;
	}
	
	private Pageable extractPageable() {
		return PageRequest.of(0, 10, Sort.by(orders));
	}


}