package com.skinfosec.dsp.report.es.batch.report;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="tb_oz_multi_src",  schema="secuiot")
public @Data class OzMultiSrc {

	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_oz_multi_src_seq_seq")
	@SequenceGenerator(name="tb_oz_multi_src_seq_seq", schema="secuiot", sequenceName = "tb_oz_multi_src_seq_seq", allocationSize=1)
	private Long seq;

	@Column(name="domain_id")
	private Long domainId;//도메인번호

	@Column(name="assets_id")
	private Long assetsId;//자산번호

	@Column(name="source_address")
	private String sourceAddress;

	@Column(name="source_country_code")
	private String sourceCountryCode;

	@Column(name="source_location_map")
	private String sourceLocationMap;

	@Column(name="source_location_map_sum")
	private Long sourceLocationMapSum; 

	@Column(name="report_date")
	private Timestamp reportDate;

	@Column(name="reg_date")
	private Timestamp regDate;//등록일시
}
