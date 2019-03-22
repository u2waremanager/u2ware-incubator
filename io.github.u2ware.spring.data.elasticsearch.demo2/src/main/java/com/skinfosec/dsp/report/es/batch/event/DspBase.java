package com.skinfosec.dsp.report.es.batch.event;


import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

@Document(indexName = "dsp_base_*", type="logs")
public @Data class DspBase {

	@Id
	private String id;
	
	@Field(type=FieldType.Keyword)	private String applicationProtocol;
	@Field(type=FieldType.Keyword)	private String assetsId;
	@Field(type=FieldType.Integer)	private Integer baseEventLength;
	@Field(type=FieldType.Integer)	private Integer bytesIn;
	@Field(type=FieldType.Integer)	private Integer bytesOut;
	@Field(type=FieldType.Ip)		private String collectorAddress;
	@Field(type=FieldType.Keyword)	private String collectorId;
	@Field(type=FieldType.Date)		private Long collectorReceiptTime;
	@Field(type=FieldType.Integer)	private Integer collectorSeverity;
	@Field(type=FieldType.Keyword)	private String collectorTimeZone;
	@Field(type=FieldType.Integer)	private Integer collectorTimeZoneOffset;
	@Field(type=FieldType.Keyword)	private String collectorType;
	@Field(type=FieldType.Keyword)	private String collectorVersion;
	@Field(type=FieldType.Keyword)	private String customerId;
	@Field(type=FieldType.Ip)		private String destinationAddress;
	@Field(type=FieldType.Keyword)	private String destinationCountrycode;
	@Field(type=FieldType.Keyword)	private String destinationDnsDomain;
	@Field(type=FieldType.Keyword)	private String destinationHostName;
	@Field(type=FieldType.Keyword)	private String destinationLocationArea;
	@Field(type=FieldType.Keyword)	private String destinationLocationBuilding;
	@Field(type=FieldType.Keyword)	private String destinationLocationFloor;
	@Field(type=FieldType.Keyword)	private String destinationLocationMap;
	@Field(type=FieldType.Keyword)	private String destinationLocationSite;
	@Field(type=FieldType.Keyword)	private String destinationMacAddress;
	@Field(type=FieldType.Keyword)	private String destinationNtDomain;
	@Field(type=FieldType.Integer)	private Integer destinationPort;
	@Field(type=FieldType.Keyword)	private String destinationProcessId;
	@Field(type=FieldType.Keyword)	private String destinationProcessName;
	@Field(type=FieldType.Keyword)	private String destinationServiceName;
	@Field(type=FieldType.Ip)		private String destinationTranslatedAddress;
	@Field(type=FieldType.Integer)	private Integer destinationTranslatedPort;
	@Field(type=FieldType.Keyword)	private String destinationUserId;
	@Field(type=FieldType.Keyword)	private String destinationUserName;
	@Field(type=FieldType.Keyword)	private String destinationUserPrivileges;
	@Field(type=FieldType.Keyword)	private String destinationZoneId;
	@Field(type=FieldType.Keyword)	private String deviceAction;
	@Field(type=FieldType.Ip)		private String deviceAddress;
	@Field(type=FieldType.Keyword)	private String deviceChannelId;
	@Field(type=FieldType.Keyword)	private String deviceChannelName;
	@Field(type=FieldType.Date)		private Long deviceCustomDate1;
	@Field(type=FieldType.Keyword)	private String deviceCustomDate1Label;
	@Field(type=FieldType.Date)		private Long deviceCustomDate2;
	@Field(type=FieldType.Keyword)	private String deviceCustomDate2Label;
	@Field(type=FieldType.Long)		private Long deviceCustomNumber1;
	@Field(type=FieldType.Keyword)	private String deviceCustomNumber1Label;
	@Field(type=FieldType.Long)		private Long deviceCustomNumber2;
	@Field(type=FieldType.Keyword)	private String deviceCustomNumber2Label;
	@Field(type=FieldType.Long)		private Long deviceCustomNumber3;
	@Field(type=FieldType.Keyword)	private String deviceCustomNumber3Label;
	@Field(type=FieldType.Keyword)	private String deviceCustomString1;
	@Field(type=FieldType.Keyword)	private String deviceCustomString1Label;
	@Field(type=FieldType.Keyword)	private String deviceCustomString2;
	@Field(type=FieldType.Keyword)	private String deviceCustomString2Label;
	@Field(type=FieldType.Keyword)	private String deviceCustomString3;
	@Field(type=FieldType.Keyword)	private String deviceCustomString3Label;
	@Field(type=FieldType.Keyword)	private String deviceCustomString4;
	@Field(type=FieldType.Keyword)	private String deviceCustomString4Label;
	@Field(type=FieldType.Keyword)	private String deviceCustomString5;
	@Field(type=FieldType.Keyword)	private String deviceCustomString5Label;
	@Field(type=FieldType.Keyword)	private String deviceCustomString6;
	@Field(type=FieldType.Keyword)	private String deviceCustomString6Label;
	@Field(type=FieldType.Integer)	private Integer deviceDirection;
	@Field(type=FieldType.Keyword)	private String deviceDnsDomain;
	@Field(type=FieldType.Keyword)	private String deviceFacility;
	@Field(type=FieldType.Keyword)	private String deviceHostName;
	@Field(type=FieldType.Keyword)	private String deviceInboundInterface;
	@Field(type=FieldType.Keyword)	private String deviceLocationArea;
	@Field(type=FieldType.Keyword)	private String deviceLocationBuilding;
	@Field(type=FieldType.Keyword)	private String deviceLocationFloor;
	@Field(type=FieldType.Keyword)	private String deviceLocationMap;
	@Field(type=FieldType.Keyword)	private String deviceLocationSite;
	@Field(type=FieldType.Keyword)	private String deviceMacAddress;
	@Field(type=FieldType.Keyword)	private String deviceNtDomain;
	@Field(type=FieldType.Keyword)	private String deviceOutboundInterface;
	@Field(type=FieldType.Keyword)	private String devicePayloadId;
	@Field(type=FieldType.Keyword)	private String deviceProcessId;
	@Field(type=FieldType.Keyword)	private String deviceProcessName;
	@Field(type=FieldType.Keyword)	private String deviceProduct;
	@Field(type=FieldType.Date)		private Long deviceReceiptTime;
	@Field(type=FieldType.Long)		private Long deviceSerial;
	@Field(type=FieldType.Long)		private Long deviceServerCode;
	@Field(type=FieldType.Integer)	private Integer deviceSeverity;
	@Field(type=FieldType.Keyword)	private String deviceTimeFormat;
	@Field(type=FieldType.Keyword)	private String deviceTimeZone;
	@Field(type=FieldType.Ip)		private String deviceTranslatedAddress;
	@Field(type=FieldType.Keyword)	private String deviceVendor;
	@Field(type=FieldType.Keyword)	private String deviceVersion;
	@Field(type=FieldType.Keyword)	private String deviceZoneId;
	@Field(type=FieldType.Keyword)	private String domainId;
	@Field(type=FieldType.Keyword)	private String domainName;
	@Field(type=FieldType.Date)		private Long enrichmentTime;
	@Field(type=FieldType.Keyword)	private String eventCategory;
	@Field(type=FieldType.Keyword)	private String eventClassId;
	@Field(type=FieldType.Integer)	private Integer eventCount;
	@Field(type=FieldType.Date)		private Long eventEndTime;
	@Field(type=FieldType.Text)		private String eventId;
	@Field(type=FieldType.Keyword)	private String eventName;
	@Field(type=FieldType.Keyword)	private String eventOutcome;
	@Field(type=FieldType.Keyword)	private String eventReason;
	@Field(type=FieldType.Integer)	private Integer eventRiskLevel;
	@Field(type=FieldType.Integer)	private Integer eventRiskScore;
	@Field(type=FieldType.Date)		private Long eventStartTime;
	@Field(type=FieldType.Integer)	private Integer eventType;
	@Field(type=FieldType.Date)		private Long fileCreateTime;
	@Field(type=FieldType.Keyword)	private String fileHash;
	@Field(type=FieldType.Keyword)	private String fileId;
	@Field(type=FieldType.Date)		private Long fileModificationTime;
	@Field(type=FieldType.Keyword)	private String fileName;
	@Field(type=FieldType.Keyword)	private String filePath;
	@Field(type=FieldType.Keyword)	private String filePermission;
	@Field(type=FieldType.Integer)	private Integer fileSize;
	@Field(type=FieldType.Keyword)	private String fileType;
	@Field(type=FieldType.Date)		private Long flexDate1;
	@Field(type=FieldType.Keyword)	private String flexDate1Label;
	@Field(type=FieldType.Date)		private Long flexDate2;
	@Field(type=FieldType.Keyword)	private String flexDate2Label;
	@Field(type=FieldType.Date)		private Long flexDate3;
	@Field(type=FieldType.Keyword)	private String flexDate3Label;
	@Field(type=FieldType.Date)		private Long flexDate4;
	@Field(type=FieldType.Keyword)	private String flexDate4Label;
	@Field(type=FieldType.Long)		private Long flexNumber1;
	@Field(type=FieldType.Keyword)	private String flexNumber1Label;
	@Field(type=FieldType.Long)		private Long flexNumber2;
	@Field(type=FieldType.Keyword)	private String flexNumber2Label;
	@Field(type=FieldType.Keyword)	private String flexString1;
	@Field(type=FieldType.Keyword)	private String flexString1Label;
	@Field(type=FieldType.Keyword)	private String flexString2;
	@Field(type=FieldType.Keyword)	private String flexString2Label;
	@Field(type=FieldType.Keyword)	private String hrDepartment;
	@Field(type=FieldType.Keyword)	private String hrEmployeeName;
	@Field(type=FieldType.Keyword)	private String hrEmployeeNumber;
	@Field(type=FieldType.Keyword)	private String hrIsInOffice;
	@Field(type=FieldType.Keyword)	private String hrJobGrade;
	@Field(type=FieldType.Keyword)	private String motionId;
	@Field(type=FieldType.Keyword)	private String motionName;
	@Field(type=FieldType.Date)		private Long oldFileCreateTime;
	@Field(type=FieldType.Keyword)	private String oldFileHash;
	@Field(type=FieldType.Keyword)	private String oldFileId;
	@Field(type=FieldType.Date)		private Long oldFileModificationTime;
	@Field(type=FieldType.Keyword)	private String oldFileName;
	@Field(type=FieldType.Keyword)	private String oldFilePath;
	@Field(type=FieldType.Keyword)	private String oldFilePermission;
	@Field(type=FieldType.Integer)	private Integer oldFileSize;
	@Field(type=FieldType.Keyword)	private String oldFileType;
	@Field(type=FieldType.Keyword)	private String providerId;
	@Field(type=FieldType.Keyword)	private String rawEvent;
	@Field(type=FieldType.Integer)	private Integer rawEventLength;
	@Field(type=FieldType.Keyword)	private String requestClientApplication;
	@Field(type=FieldType.Keyword)	private String requestContext;
	@Field(type=FieldType.Keyword)	private String requestCookies;
	@Field(type=FieldType.Keyword)	private String requestMethod;
	@Field(type=FieldType.Keyword)	private String requestUrl;
	@Field(type=FieldType.Keyword)	private String requestUrlFileName;
	@Field(type=FieldType.Keyword)	private String requestUrlQuery;
	@Field(type=FieldType.Ip)		private String ruleEngineAddress;
	@Field(type=FieldType.Keyword)	private String ruleEngineId;
	@Field(type=FieldType.Keyword)	private String ruleEngineVersion;
	@Field(type=FieldType.Long)		private Long saveEsTime;
	@Field(type=FieldType.Ip)		private String sourceAddress;
	@Field(type=FieldType.Keyword)	private String sourceCountrycode;
	@Field(type=FieldType.Keyword)	private String sourceDnsDomain;
	@Field(type=FieldType.Keyword)	private String sourceHostName;
	@Field(type=FieldType.Keyword)	private String sourceLocationArea;
	@Field(type=FieldType.Keyword)	private String sourceLocationBuilding;
	@Field(type=FieldType.Keyword)	private String sourceLocationFloor;
	@Field(type=FieldType.Keyword)	private String sourceLocationMap;
	@Field(type=FieldType.Keyword)	private String sourceLocationSite;
	@Field(type=FieldType.Keyword)	private String sourceMacAddress;
	@Field(type=FieldType.Keyword)	private String sourceNtDomain;
	@Field(type=FieldType.Integer)	private Integer sourcePort;
	@Field(type=FieldType.Ip)		private String sourceTranslatedAddress;
	@Field(type=FieldType.Integer)	private Integer sourceTranslatedPort;
	@Field(type=FieldType.Keyword)	private String sourceUserId;
	@Field(type=FieldType.Keyword)	private String sourceUserName;
	@Field(type=FieldType.Keyword)	private String sourceUserPrivileges;
	@Field(type=FieldType.Keyword)	private String sourceZoneId;
	@Field(type=FieldType.Text)		private String tanantId;
	@Field(type=FieldType.Keyword)	private String tenantId;
	@Field(type=FieldType.Keyword)	private String tenantName;
	@Field(type=FieldType.Integer)	private Integer vasAlgorithm;
	@Field(type=FieldType.Integer)	private Integer vasEventType;
	@Field(type=FieldType.Keyword)	private String vasName;
	@Field(type=FieldType.Keyword)	private String vasObject;
	@Field(type=FieldType.Keyword)	private String vasUuid;
	
	
	@Transient
	public String getFormattedEventStartTime() {
		return (eventStartTime == null ? "" : new DateTime(eventStartTime).toString("yyyy-MM-dd HH:mm:ss.SSS"));
	}

	@Transient
	public String getFormattedEventEndTime() {
		return (eventEndTime == null ? "" : new DateTime(eventEndTime).toString("yyyy-MM-dd HH:mm:ss.SSS"));
	}
	
	@Override
	public String toString() {
		return "DspBase [domainId=" + domainId+", assetsId=" + assetsId+", tanantId=" + tanantId+", tenantId=" + tenantId
				+", eventStartTime=" + eventStartTime + "/"+getFormattedEventStartTime()  
				+", eventEndTime=" + eventEndTime + "/"+getFormattedEventEndTime()
				+"]";
	}
	
	
	
}
