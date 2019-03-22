package com.skinfosec.dsp.anomaly.batch.rule.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.skinfosec.dsp.anomaly.batch.ApplicationTests;
import com.skinfosec.dsp.anomaly.batch.rule.Dmn;
import com.skinfosec.dsp.anomaly.batch.rule.Rule;
import com.skinfosec.dsp.anomaly.batch.rule.Tenant;

public class RepositoryTest extends ApplicationTests{

	@Autowired(required=false)
    private DmnRepository repository;
	
	@Autowired(required=false)
	private TenantRepository tenantRepository;

	@Autowired(required=false)
	private RuleRepository ruleRepository;
	
	@Test
	public void contextLoads() throws Exception {

		Iterable<Dmn> rr1 = repository.findAll();
		for(Dmn r : rr1) {
			logger.info("Dmn : "+r);
		}

		Iterable<Tenant> rr2 = tenantRepository.findAll();
		for(Tenant r : rr2) {
			logger.info("Tenant : "+r);
		}

		Iterable<Rule> rr3 = ruleRepository.findAll();
		for(Rule r : rr3) {
			logger.info("Rule : "+r);
		}
	
	}
	
}
