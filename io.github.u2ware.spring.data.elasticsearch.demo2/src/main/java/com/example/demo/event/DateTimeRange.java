package com.example.demo.event;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.search.aggregations.bucket.range.RangeAggregator.Range;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class DateTimeRange extends Range{

	public static final String PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

	public DateTimeRange(String key, DateTime from, DateTime to) {
		super(key, ((Long)from.getMillis()).doubleValue(), from.toString(PATTERN), ((Long)to.getMillis()).doubleValue(), to.toString(PATTERN));
	}

	public Long getFromMillis() {
		return new Double(getFrom()).longValue();
	}
	public Long getToMillis() {
		return new Double(getTo()).longValue();
	}
	public DateTime getFromAsDateTime() {
		return new DateTime(getFromMillis());
	}
	public DateTime getToAsDateTime() {
		return new DateTime(getToMillis());
	}
	
	

	public static DateTimeRange edgeHourOfDay(DateTime d) {
		DateTime f = minimumTimestamp(d.withHourOfDay(0));
		DateTime t = maximumTimestamp(d.withHourOfDay(23));
		return new DateTimeRange(null,f,t);
	}

	public static DateTimeRange[] everyHourOfDay(DateTime d) {
		List<DateTimeRange> ranges = everyHourOfDayList(d);
		return ranges.toArray(new DateTimeRange[ranges.size()]);
	}

	public static DateTimeRange[] continuousHourOfDay(DateTime d) {
		List<DateTimeRange> ranges = continuousHourOfDayList(d);
		return ranges.toArray(new DateTimeRange[ranges.size()]);
	}
	
	private static List<DateTimeRange> everyHourOfDayList(DateTime d) {
		ArrayList<DateTimeRange> ranges = new ArrayList<DateTimeRange>();
		for(int i = 0; i < 24; i++) {
			DateTime f = minimumTimestamp(d.withHourOfDay(i));
			DateTime t = maximumTimestamp(d.withHourOfDay(i));
			ranges.add(new DateTimeRange(String.format("%02d",  i), f, t));
		}
		//ranges.toArray(new DateTimeRange[ranges.size()]);
		return ranges;
	}

	private static List<DateTimeRange> continuousHourOfDayList(DateTime d) {
		List<DateTimeRange> ranges = everyHourOfDayList(d);
		
		DateTime t1 = DateTime.parse("1970-01-01", DateTimeFormat.forPattern("yyyy-MM-dd"));
		DateTime t2 = minimumTimestamp(d.withHourOfDay(0)).minusMillis(1);
		ranges.add(0, new DateTimeRange("before", t1, t2));
		
		DateTime t3 = maximumTimestamp(d.withHourOfDay(23)).plusMillis(1);
		DateTime t4 = DateTime.parse("9999-01-01", DateTimeFormat.forPattern("yyyy-MM-dd"));
		ranges.add(new DateTimeRange("after", t3, t4));
		
		return ranges;
	}
	

	////////////////////////////////////////////////////
	//
	////////////////////////////////////////////////////
	private static DateTime minimumTimestamp(DateTime d) {
		return d.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
	}
	private static DateTime maximumTimestamp(DateTime d) {
		return d.withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
	}
	
    ////////////////////////////////////////////////////////////
    //
    ////////////////////////////////////////////////////////////
//	public static DspBaseTimeRange weekRange(DateTime d) {
//		DateTime f = minimumTimestamp(d.dayOfWeek().withMinimumValue());
//		DateTime t = maximumTimestamp(d.dayOfWeek().withMaximumValue());
//		return new DspBaseTimeRange(f, t);
//	}
//	public static DspBaseTimeRange monthRange(DateTime d) {
//		DateTime f = minimumTimestamp(d.dayOfMonth().withMinimumValue());
//		DateTime t = maximumTimestamp(d.dayOfMonth().withMaximumValue());
//		return new DspBaseTimeRange(f, t);
//	}
//	public static DspBaseTimeRange yearRange(DateTime d) {
//		DateTime f = minimumTimestamp(d.dayOfYear().withMinimumValue());
//		DateTime t = maximumTimestamp(d.dayOfYear().withMaximumValue());
//		return new DspBaseTimeRange(f, t);
//	}
	
	public String toString() {
		return getFromAsDateTime()+" ~ "+getToAsDateTime();
	}

}
