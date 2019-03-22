package com.skinfosec.dsp.report.es.batch.schedule;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.skinfosec.dsp.report.es.batch.ApplicationTests;
import com.skinfosec.dsp.report.es.batch.schedule.DateTimeRangeEvent;
import com.skinfosec.dsp.report.es.batch.schedule.TotalNumberOfEventsByCountrySchedule;
import com.skinfosec.dsp.report.es.batch.schedule.TotalNumberOfEventsByMultiDstSchedule;
import com.skinfosec.dsp.report.es.batch.schedule.TotalNumberOfEventsByLocationSchedule;
import com.skinfosec.dsp.report.es.batch.schedule.TotalNumberOfEventsByReasonSchedule;
import com.skinfosec.dsp.report.es.batch.schedule.TotalNumberOfEventsByMultiSrcSchedule;
import com.skinfosec.dsp.report.es.batch.schedule.TotalNumberOfEventsBaseSchedule;

public class DateTimeRangeEventScheduleTest extends ApplicationTests{

    protected @Autowired TotalNumberOfEventsBaseSchedule TotalNumberOfEventsBaseSchedule;
	protected @Autowired TotalNumberOfEventsByReasonSchedule TotalNumberOfEventsByReasonSchedule;
    protected @Autowired TotalNumberOfEventsByCountrySchedule TotalNumberOfEventsByCountrySchedule;
    protected @Autowired TotalNumberOfEventsByLocationSchedule TotalNumberOfEventsByLocationSchedule;

    
    protected @Autowired TotalNumberOfEventsByMultiDstSchedule TotalNumberOfEventsByMultiDstSchedule;
    protected @Autowired TotalNumberOfEventsByMultiSrcSchedule TotalNumberOfEventsByMultiSrcSchedule;
    protected @Autowired TotalNumberOfEventsByMultiSrcDstSchedule TotalNumberOfEventsByMultiSrcDstSchedule;

    @Test
	public void contextLoads() throws Exception {
//    	batch(TotalNumberOfEventsBaseSchedule);
//    	
//    	batch(TotalNumberOfEventsByReasonSchedule);
//    	batch(TotalNumberOfEventsByCountrySchedule);
//    	batch(TotalNumberOfEventsByLocationSchedule);
//
//
//    	batch(TotalNumberOfEventsByMultiDstSchedule);
//    	batch(TotalNumberOfEventsByMultiSrcSchedule);
//    	batch(TotalNumberOfEventsByMultiSrcDstSchedule);
    }

    protected void batch(DateTimeRangeEventSchedule schedule) {
    	int countPerDay = 24;
		int total = 0;
		int day = 0;
		int savedTotal = 0;
		DateTime date = parse("2019-01-01");
		while(date.isBeforeNow()) {
			int saved = schedule.batch(new DateTimeRangeEvent(date));
			total = total + saved;
			if(saved > 0) {
				savedTotal++;
			}
			day++;
			date = date.plusDays(1);
		}
		logger.info("################################################");
		logger.info("\texpectedsDay="+day);
		logger.info("\texpectedsRows="+(day * countPerDay));
		logger.info("\tactualsDay="+savedTotal);
		logger.info("\tactualsRows="+total);
		logger.info("\tactualsRows/countPerDay="+(total / countPerDay));
	}
	
	
	
	
	
	
	
	
}
