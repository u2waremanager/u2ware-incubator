package com.example.demo.event.repository;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.demo.ApplicationTests;
import com.example.demo.rule.AnomRuleDmn;
import com.example.demo.rule.AnomRuleDmnRepository;

public class DspBaseDocumentIndexRepositoryTest extends ApplicationTests{
	
	@Autowired
    private AnomRuleDmnRepository anomRuleDmnRepository;

	@Autowired
	private DspBaseDocumentIndex dspBaseIndex;
	
	@Autowired
	private DspBaseDocumentIndexRepository dspBaseDocumentIndexRepository;

	
	@Test
	public void contextLoads() throws Exception {


		Iterable<AnomRuleDmn> rules = anomRuleDmnRepository.findAll();
		for(AnomRuleDmn rule : rules) {

			logger.info(rule);

			String tenantId = rule.getId().getTenant().getTenantNo().toString();
			String domainId = rule.getId().getDmn().getDmnNo().toString();
			

			DateTime day = DateTimeFormat.forPattern("yyyyMMdd").parseDateTime("20190130");
			long min = day.withMinuteOfHour(0).withMinuteOfHour(0).withMillisOfSecond(0).getMillis();
			//long max = day.withMinuteOfHour(23).withMinuteOfHour(59).withMillisOfSecond(999).getMillis();
			
			////////////////////////////////////////////
			//
			//////////////////////////////////////////
			scan("dsp_base_2_1_20190130", tenantId, domainId, min);
			scan("dsp_base_2_1_20190131", tenantId, domainId, min);
			scan("dsp_base_2_1_20190208", tenantId, domainId, min);
		}
		
	}
	
	private void scan(String indexName, String tenantId, String domainId, Long min) {
		
		logger.info(indexName);
		dspBaseIndex.setIndexName(indexName);
		Page<DspBaseDocument> toatal = dspBaseDocumentIndexRepository.findByTenantIdAndDomainId(tenantId, domainId, PageRequest.of(0, 10));
		logger.info("total: "+ toatal.getTotalElements());

		Page<DspBaseDocument> rr = null;
		if(toatal.getTotalElements() > 0) {

			rr = dspBaseDocumentIndexRepository.findByTenantIdAndDomainIdAndEventStartTimeLessThan(tenantId, domainId, min, PageRequest.of(0, 10));
			logger.info("\tLessThan: "+ rr.getTotalElements());
			for(DspBaseDocument r : rr) {
				logger.info("\t"+r);
			}

			if(rr.getTotalElements() > 0) {
				for(int i=0 ; i < 10; i++) {
					rr = dspBaseDocumentIndexRepository.findByTenantIdAndDomainIdAndEventTypeAndEventStartTimeLessThan(tenantId, domainId, i, min, PageRequest.of(0, 10));
					logger.info("\t\tEvent "+i+": "+ rr.getTotalElements());
					if(rr.getTotalElements() > 0) {
						for(DspBaseDocument r : rr) {
							logger.info("\t\t"+r);
						}
					}
				}
			}
			
			
			rr = dspBaseDocumentIndexRepository.findByTenantIdAndDomainIdAndEventStartTimeGreaterThan(tenantId, domainId, min, PageRequest.of(0, 10));
			logger.info("\tGreaterThan: "+ rr.getTotalElements());
			for(DspBaseDocument r : rr) {
				logger.info(r);
			}
			if(rr.getTotalElements() > 0) {
				for(int i=0 ; i < 10; i++) {
					rr = dspBaseDocumentIndexRepository.findByTenantIdAndDomainIdAndEventTypeAndEventStartTimeGreaterThan(tenantId, domainId, i, min, PageRequest.of(0, 10));
					logger.info("\t\tEvent "+i+": "+ rr.getTotalElements());
					if(rr.getTotalElements() > 0) {
						for(DspBaseDocument r : rr) {
							logger.info("\t\t"+r);
						}
					}
				}
			}
		}
	}
	
}
