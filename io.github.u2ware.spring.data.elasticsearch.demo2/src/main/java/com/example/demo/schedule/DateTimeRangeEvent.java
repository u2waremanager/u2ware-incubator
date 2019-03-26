package com.example.demo.schedule;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationEvent;

import com.example.demo.event.DateTimeRange;

@SuppressWarnings("serial")
public class DateTimeRangeEvent extends ApplicationEvent{

	public DateTimeRangeEvent(DateTime date) {
		super(DateTimeRange.edgeHourOfDay(date));
	}
	public DateTimeRangeEvent(DateTime min, DateTime max) {
		super(new DateTimeRange(null, min, max));
	}

	public DateTimeRange getDateTimeRange() {
		return (DateTimeRange)getSource();
	}
	public DateTime getFromDateTime() {
		return getDateTimeRange().getFromAsDateTime();
	}
	public DateTime getToDateTime() {
		return getDateTimeRange().getToAsDateTime();
	}
	public Long getFromMillis() {
		return getDateTimeRange().getFromMillis();
	}
	public Long getToMillis() {
		return getDateTimeRange().getToMillis();
	}
	public String toString() {
		return getDateTimeRange().toString();
	}
}
