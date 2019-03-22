package com.skinfosec.dsp.report.es.batch.event;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.skinfosec.dsp.report.es.batch.ApplicationTests;

public class DspBaseQueryBuilderTest extends ApplicationTests{

	private @Autowired DspBaseQueryService dspBaseQueryOperations;
	
	@Test
	public void contextLoads() throws Exception {
		
		query("sourceAddress");
//		query("sourceCountrycode");
//		query("sourceDnsDomain");
//		query("sourceHostName");
//		query("sourceLocationArea");
//		query("sourceLocationBuilding");
//		query("sourceLocationFloor");
//		query("sourceLocationMap");
//		query("sourceLocationSite");
//		query("sourceMacAddress");
//		query("sourceNtDomain");
//		query("sourcePort");
//		query("sourceTranslatedAddress");
//		query("sourceTranslatedPort");
//		query("sourceUserId");
//		query("sourceUserName");
//		query("sourceUserPrivileges");
//		query("sourceZoneId");
//
//		query("destinationAddress");
//		query("destinationCountrycode");
//		query("destinationDnsDomain");
//		query("destinationHostName");
//		query("destinationLocationArea");
//		query("destinationLocationBuilding");
//		query("destinationLocationFloor");
//		query("destinationLocationMap");
//		query("destinationLocationSite");
//		query("destinationMacAddress");
//		query("destinationNtDomain");
//		query("destinationPort");
//		query("destinationTranslatedAddress");
//		query("destinationTranslatedPort");
//		query("destinationUserId");
//		query("destinationUserName");
//		query("destinationUserPrivileges");
//		query("destinationZoneId");
	
	}
	
	private void query(String field) {
		logger.info("##############################################");
		logger.info(field);
		dspBaseQueryOperations.query(new DspBaseQueryBuilder()
				.queryExists(field)
				.aggregationTerms(field)
				.aggregationTerms("domainId")
				.build());
	}
	
	
}
