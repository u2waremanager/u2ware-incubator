package com.example.demo.rule;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.ApplicationTests;
import com.example.demo.rule.AnomRuleDmn;
import com.example.demo.rule.AnomRuleDmnRepository;

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