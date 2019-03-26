package com.example.demo.event;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

@Document(indexName = "dsp_timeseries_*", type="logs")
public @Data class TimeSeriesEvent {

	@Id private Long eventId;
	
	@Field(type=FieldType.Date)    private Long eventStartTime;
	@Field(type=FieldType.Integer) private Integer eventType;
	@Field(type=FieldType.Long)    private Long customerId;
	@Field(type=FieldType.Long)    private Long providerId;
	@Field(type=FieldType.Long)    private Long tenantId;
	@Field(type=FieldType.Keyword) private String tenantName;
	@Field(type=FieldType.Long)    private Long domainId;
	@Field(type=FieldType.Keyword) private String domainName;
	@Field(type=FieldType.Keyword) private String operatedField; // 연산대상 필드명
	@Field(type=FieldType.Keyword) private String operator; // 연산자종류-count,min,max,mean
	@Field(type=FieldType.Long)   private Long operatedValue; // 연산된 값 - count,min,max,mean
	@Field(type=FieldType.Keyword) private String groupFields; // 그룹바이 필드들
	@Field(type=FieldType.Keyword) private String groupFieldValues; // 그룹바이 결과값들
	@Field(type=FieldType.Keyword) private String uniqueValue; // 연산대상 고유값
	@Field(type=FieldType.Keyword) private String profileId; // 데이터생성프로파일ID
	@Field(type=FieldType.Long)    private Long ruleId;
	@Field(type=FieldType.Integer) private Integer eventRiskLevel;
	@Field(type=FieldType.Integer) private Integer eventRiskScore;
	@Field(type=FieldType.Long)    private Long assetsId;
	
	@Field(type=FieldType.Keyword) private String baseEventIds;
	@Field(type=FieldType.Integer) private Integer baseEventCount;


	@Override
	public String toString() {
		return "TimeSeriesEvent [eventId=" + eventId + ", tenantId="
				+ tenantId + ", tenantName=" + tenantName + ", domainId=" + domainId + ", domainName=" + domainName
				+ ", uniqueValue=" + uniqueValue
				+ ", operatedValue=" + operatedValue
				+ ", baseEventCount=" + baseEventCount 
				+ ", eventStartTime=" + eventStartTime + "]";
	}

}
