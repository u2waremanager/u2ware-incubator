package com.skinfosec.dsp.anomaly.batch.event;

public class Constants {

	public static final String AGG_ASSET = "aggAsset";
	public static final String AGG_ANOM = "aggAnom";
	public static final String AGG_UNIQ = "aggUniq";
	public static final String AGG_STATS = "aggStats";
	
	public final static Integer BASE_NORMAL_EVENT = 0;	// base-normal
	public final static Integer TIME_SERIES_EVENT = 9;	// time series
	
	public final static String ANOM_OPR_CODE_MIN = "min";	// 이상행위분석 목표필드연산자코드(min)
	public final static String ANOM_OPR_CODE_MAX = "max";	// 이상행위분석 목표필드연산자코드(max)
	public final static String ANOM_OPR_CODE_SUM = "sum";	// 이상행위분석 목표필드연산자코드(sum)
	public final static String ANOM_OPR_CODE_AVG = "mean";	// 이상행위분석 목표필드연산자코드(avg)
	public final static String ANOM_OPR_CODE_UNIQUE = "count";	// 이상행위분석 목표필드연산자코드(unique)
	
}
