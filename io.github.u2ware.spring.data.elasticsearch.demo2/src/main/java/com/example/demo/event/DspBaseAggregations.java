package com.example.demo.event;

import java.sql.Timestamp;
import java.util.HashMap;

import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation.Bucket;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.joda.time.DateTime;

@SuppressWarnings("serial")
public class DspBaseAggregations extends HashMap<String, Bucket>{

	public Long getDocCount(String key) {
		return get(key).getDocCount();
	}

	public Object getKey(String key) {
		return get(key).getKey();
	}
	public String getKeyAsString(String key) {
		return get(key).getKeyAsString();
	}
	public Long getKeyAsLong(String key) {
		return Long.parseLong(getKeyAsString(key));
	}

	public Range.Bucket getRangeBucket(String key) {
		return (Range.Bucket)get(key);
	}
	public Terms.Bucket getTermsBucket(String key) {
		return (Terms.Bucket)get(key);
	}

	public Double getFrom(String key) {
		return (Double)(getRangeBucket(key).getFrom());
	}
	public Long getFromAsMillis(String key) {
		return getFrom(key).longValue();
	}
	public DateTime getFromAsDateTime(String key) {
		return new DateTime(getFromAsMillis(key));
	}
	public Timestamp getFromAsTimestamp(String key) {
		return new Timestamp(getFromAsMillis(key));
	}
	public Double getTo(String key) {
		return (Double)(getRangeBucket(key).getTo());
	}
	public Long getToAsMillis(String key) {
		return getFrom(key).longValue();
	}
	public DateTime getToAsDateTime(String key) {
		return new DateTime(getToAsMillis(key));
	}
	public Timestamp getToAsTimestamp(String key) {
		return new Timestamp(getToAsMillis(key));
	}

}
