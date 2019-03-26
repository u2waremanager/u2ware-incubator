package com.example.demo.rule;

import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Fetch;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="tb_anom_rule", schema="secuiot")
public @Data class Rule {//이상_규칙

	public final static String ANOM_OPR_CODE_MIN = "min";	// 이상행위분석 목표필드연산자코드(min)
	public final static String ANOM_OPR_CODE_MAX = "max";	// 이상행위분석 목표필드연산자코드(max)
	public final static String ANOM_OPR_CODE_SUM = "sum";	// 이상행위분석 목표필드연산자코드(sum)
	public final static String ANOM_OPR_CODE_AVG = "mean";	// 이상행위분석 목표필드연산자코드(avg)
	public final static String ANOM_OPR_CODE_UNIQUE = "count";	// 이상행위분석 목표필드연산자코드(unique)

	@Id
	@Column(name="anom_rule_no")
	private Long anomRuleNo;//이상규칙번호
	private String anom_rule_clas_code;//이상규칙유형코드
	private String anom_rule_name;//이상규칙이름
	private String anom_rule_desc;//이상규칙설명
	private String rule_status_code;//규칙상태코드
	private Character rule_engine_trns_yn;//규칙엔진전송여부
	private String profile_status_code;//프로파일상태코드
	private Timestamp profile_commit_dt;//프로파일적용일시
	private Long evt_lrg_ctgr_no;//이벤트대범주번호
	private Long evt_mdm_ctgr_no;//이벤트중범주번호
	private Long evt_sml_ctgr_no;//이벤트소범주번호
	
	@Column(name="target_field_name")
	private String targetFieldName;//목표필드이름
	@Column(name="target_field_oprtr_code")
	private String targetFieldOprtrCode;//목표필드연산자코드
	
	private String thrsld_st_time_code;//임계기준시간코드
	private Integer thrsld_value;//임계값
	private String thrsld_oprtr_code;//임계연산자코드
	private Integer thrsld_range_str_value;//임계범위시작값
	private Integer thrsld_range_end_value;//임계범위종료값
	private String anom_st_time_code;//이상기준시간코드
	private String anom_target_time_code;//이상목표시간코드
	private String anom_value;//이상값
	private Character prf_yn;//검증여부
	private String op_dstrbt_status_code;//운영배포상태코드
	private Timestamp op_dstrbt_dt;//운영배포일시
	private Timestamp anals_run_dt;//분석실행일시
	private String risk_level_code;//위험레벨코드
	private Integer risk_score;//위험점수
	private Character risk_level_score_conv_yn;//위험레벨점수전환여부
	private Character dmn_choc_yn;//도메인선택여부
	private Long rule_group_no;//규칙그룹번호
	private String watch_field_name;//WATCH필드이름
	private Long watch_list_no;//WATCH목록번호
	private Long search_no;//검색번호
	private Character use_yn;//사용여부
	private Long reg_usr_no;//등록자번호
	private Timestamp reg_date;//등록일시
	private Long mod_usr_no;//수정자번호
	private Timestamp mod_date;//수정일시
	
	@OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@Fetch(FetchMode.SUBSELECT)
	protected Set<RuleField> fields = new LinkedHashSet<RuleField>();
	
	
	@Entity
	@Table(name="tb_anom_rule_group_field", schema="secuiot")
	public static class RuleField { //이상_규칙_그룹_필드

		@Id
		private @Getter @Setter Long anom_rule_group_field_no;//이상규칙그룹필드번호
		
		@ManyToOne(fetch = FetchType.LAZY) 
		@JoinColumn(name="anom_rule_no")
		private @JsonIgnore Rule parent;
		
		@Column(name="anom_rule_group_field_name")
		private @Getter @Setter String anomRuleGroupFieldName;//이상규칙그룹필드이름
		
//		private @Getter @Setter  String group_field_name;//그룹필드이름
		private @Getter @Setter Character use_yn;//사용여부
//		private @Getter @Setter Long reg_usr_no	;//등록자번호
//		private @Getter @Setter Timestamp reg_date;//등록일시
//		private @Getter @Setter Long mod_usr_no;//수정자번호
//		private @Getter @Setter Timestamp mod_date;//수정일시
		
		@Override
		public String toString() {
			return "[anomRuleGroupFieldName=" + anomRuleGroupFieldName + ", use_yn="+use_yn+"]";
		}
	}


	@Override
	public String toString() {
		return "[anomRuleNo=" + anomRuleNo + ", "+rule_status_code+", "+profile_status_code+", "+op_dstrbt_status_code +", fields="+fields+"]";
	}
}