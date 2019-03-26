package com.example.demo.rule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="tb_tenant", schema="secuiot")
public @Data class Tenant { //테넌트

	@Id
	@Column(name = "tenant_no")
	private Long tenantNo;//테넌트번호
	
	@Column(name = "tenant_name")
	private String tenantName;//테넌트이름

//	private String tenant_desc;//테넌트설명
//	private Long custm_no;//고객사번호
//	private Character del_able_yn;//삭제가능여부
//	private Character use_yn;//사용여부
//	private Long reg_usr_no;//등록자번호
//	private Timestamp reg_date;//등록일시
//	private Long mod_usr_no;//수정자번호
//	private Timestamp mod_date;//수정일시
	
	@Override
	public String toString() {
		return "[tenantNo=" + tenantNo + ", tenantName=" + tenantName + "]";
	}
	
	
	
}
