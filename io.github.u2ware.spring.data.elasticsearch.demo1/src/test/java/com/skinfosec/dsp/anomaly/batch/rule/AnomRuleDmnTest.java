package com.skinfosec.dsp.anomaly.batch.rule;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.skinfosec.dsp.anomaly.batch.ApplicationTests;

public class AnomRuleDmnTest extends ApplicationTests{

	@Autowired
    private AnomRuleDmnRepository anomRuleDmnRepository;
	
	@Test
	public void contextLoads() throws Exception {
		Iterable<AnomRuleDmn> rules = anomRuleDmnRepository.findAll();
		for(AnomRuleDmn rule : rules) {
			logger.info(rule);
		}
	}
}