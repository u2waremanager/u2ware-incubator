package com.skinfosec.dsp.anomaly.batch.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface DspBaseDocumentIndexRepository extends ElasticsearchRepository<DspBaseDocument, String>{

	
	Page<DspBaseDocument> findByTenantIdAndDomainId(String tenantId, String domainId, Pageable pageable);
	Page<DspBaseDocument> findByTenantIdAndDomainIdAndEventType(String tenantId, String domainId, Integer eventType, Pageable pageable);

	Page<DspBaseDocument> findByTenantIdAndDomainIdAndEventTypeAndEventStartTimeLessThan(String tenantId, String domainId, Integer eventType, Long eventStartTime, Pageable pageable);
	Page<DspBaseDocument> findByTenantIdAndDomainIdAndEventTypeAndEventStartTimeGreaterThan(String tenantId, String domainId, Integer eventType, Long eventStartTime, Pageable pageable);
	
	Page<DspBaseDocument> findByTenantIdAndDomainIdAndEventStartTimeLessThan(String tenantId, String domainId, Long eventStartTime, Pageable pageable);
	Page<DspBaseDocument> findByTenantIdAndDomainIdAndEventStartTimeGreaterThan(String tenantId, String domainId, Long eventStartTime, Pageable pageable);
	
}
