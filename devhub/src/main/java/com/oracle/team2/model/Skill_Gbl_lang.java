package com.oracle.team2.model;

import lombok.Data;

@Data
public class Skill_Gbl_lang { //세계언어 통계
	
	private String skill_id; 	//기술코드 pk
	
	private String gbl_date;	//날짜 PK
	
	private String skill_name; 	//기술명칭 not-null
	
	private String skill_img; 	// 이미지 not-null
	
	private String skill_type; 	//종류 char not-null
	
	private Integer last_rank;	//전년순위 null
	
	private String rate;	//지분율
	
	private String change;	//변화율
	
	
	//조회용
	private Integer rowGblRanking;
	
	

	

	
	
}
