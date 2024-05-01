package com.oracle.team2.model;

import lombok.Data;

@Data
public class Region {	// 지역 테이블

	private String local_code;	// 행정동코드 - pk 
	
	private String city;		// 시도 not-null
	
	private String local;		// 시군구 not-null
	
}
