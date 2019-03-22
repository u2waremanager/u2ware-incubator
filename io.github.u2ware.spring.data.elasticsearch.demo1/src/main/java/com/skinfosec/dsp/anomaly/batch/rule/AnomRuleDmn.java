package com.skinfosec.dsp.anomaly.batch.rule;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="tb_anom_rule_dmn", schema="secuiot")
public @Data class AnomRuleDmn { //이상_규칙_도메인

	
	@EmbeddedId
	private ID id; 
	
	@Embeddable
	@SuppressWarnings("serial")
	public @Data static class ID implements Serializable{

		@ManyToOne
		@JoinColumn(name="anom_rule_no")
		private Rule rule;

		@ManyToOne
		@JoinColumn(name="tenant_no")
		private Tenant tenant;

		@ManyToOne
		@JoinColumn(name="dmn_no")
		private Dmn dmn;//도메인번호
		
		@Column(name="detect_alarm_dstnct_code")
		private String detectAlarmDstnctCode;//탐지알람구분코드

		@Override
		public String toString() {
			return "[rule=" + rule + ", tenant=" + tenant + ", dmn=" + dmn + "]";
		}
	}
	
	private Character use_yn;//사용여부
	private Long reg_usr_no;//등록자번호
	private Timestamp reg_date;//등록일시
	private Long mod_usr_no;//수정자번호
	private Timestamp mod_date;//수정일시
	
	
	@Override
	public String toString() {
		return "AnomRuleDmn " + id;
	}
	
}
