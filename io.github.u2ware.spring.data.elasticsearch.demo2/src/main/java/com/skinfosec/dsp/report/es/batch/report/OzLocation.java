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
@Table(name="tb_oz_location",  schema="secuiot")
public @Data class OzLocation {

	@Id 
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tb_oz_location_seq_seq")
	@SequenceGenerator(name="tb_oz_location_seq_seq", schema="secuiot", sequenceName = "tb_oz_location_seq_seq", allocationSize=1)
	private Long seq;

	@Column(name="domain_id")
	private Long domainId;//도메인번호
	
	@Column(name="assets_id")
	private Long assetsId;//자산번호

	@Column(name="device_location_map")
	private String deviceLocationMap;

	@Column(name="device_location_map_sum")
	private Long deviceLocationMapSum; 

	@Column(name="report_date")
	private Timestamp reportDate;

	@Column(name="reg_date")
	private Timestamp regDate;//등록일시
}
