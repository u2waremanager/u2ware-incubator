package com.skinfosec.dsp.anomaly.batch.rule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="tb_dmn", schema="secuiot")
public @Data class Dmn { //도메인

	@Id
	@Column(name = "dmn_no")
	private Long dmnNo;//도메인번호
	
	@Column(name = "dmn_name")
	private String dmnName;//도메인이름
	
//	private String dmn_desc;//도메인설명
//	private String loc_name;//위치이름
//	private BigDecimal latitude_value;//위도값
//	private BigDecimal longitude_value;//경도값
//	private Long tenant_no;//테넌트번호
//	private String utc_code;//국제표준시코드
//	private Character del_able_yn;//삭제가능여부
//	private Character use_yn;//사용여부
//	private Long reg_usr_no;//등록자번호
//	private Timestamp reg_date;//등록일시
//	private Long mod_usr_no;//수정자번호
//	private Timestamp mod_date;//수정일시
	
	@Override
	public String toString() {
		return "[dmnNo=" + dmnNo + ", dmnName=" + dmnName + "]";
	}
}
