package com.oracle.team2.model;

import lombok.Data;

@Data
public class Gbl_lang { //세계언어 통계
	private String skill_id;	//기술코드	 skill_id FK => skill_tbl(기술스택) pk
	private String gbl_date;	//날짜 PK
	
	
	private float rate;	//지분율
	
	private float change;	//변화율
	
	private Integer last_rank;	//전년순위 null
}
