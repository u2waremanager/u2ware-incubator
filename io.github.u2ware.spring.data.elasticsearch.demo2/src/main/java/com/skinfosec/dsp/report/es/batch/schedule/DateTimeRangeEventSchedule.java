package com.skinfosec.dsp.report.es.batch.schedule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;

public abstract class DateTimeRangeEventSchedule implements ApplicationListener<DateTimeRangeEvent>{

	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void onApplicationEvent(DateTimeRangeEvent range) {
		batch(range);
	}

	public abstract int batch(DateTimeRangeEvent range);
	
}
