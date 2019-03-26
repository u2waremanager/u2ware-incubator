package com.example.demo.rule;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AnomRuleDmnRepository extends PagingAndSortingRepository<AnomRuleDmn, AnomRuleDmn.ID>{

	@Query("select distinct t from AnomRuleDmn t                 "
			+ " where t.id.detectAlarmDstnctCode    = 'ANODT02'  "
			+ " and t.use_yn           = 'Y'  "
			+ " and t.id.rule.use_yn   = 'Y'   "
			+ " and t.id.rule.rule_status_code      = 'active'   "
			+ " and t.id.rule.profile_status_code   = 'compiled' "
			+ " and t.id.rule.op_dstrbt_status_code = 'deployed' "
			+ "")
	Iterable<AnomRuleDmn> findAll();
	
}
