package com.example.demo.rule.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.ApplicationTests;
import com.example.demo.rule.Dmn;
import com.example.demo.rule.Rule;
import com.example.demo.rule.Tenant;

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
