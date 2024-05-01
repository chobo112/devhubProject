package com.oracle.team2.model;

import lombok.Data;

@Data
public class Skill_Kr_skill { //기술스택테이블
	
	private String skill_id; 	//기술코드 pk
	
	private String skill_name; 	//기술명칭 not-null
	
	private String skill_img; 	//이미지 not-null
	
	private String skill_type; 	//종류 char not-null
	
	
	private String kr_date;		//날짜 		복합키-pk
	private Integer ismajor;		//대기업여부 	복합키-pk
	private String kr_ref;		//출처 		복합키-pk
	
	private int recruit_cnt; 	//공고수 not-null
	
	
//	조회용
//	TE
	private String alternativeImagePath;
	private int major_recruit_cnt; 
	private int total_recruit_cnt;
	private int total_recruit_cnt_ismajor;
	private int rowKrRanking;
	private Double rate;

	private int prev_total_recruit_cnt;
	private int prevRowKrRanking;
	private Double prevRate;
	
	private int sum_total_recruit_cnt;
	private int sum_total_recruit_cnt_ismajor;
	private int row_count;
	
	
	

	
}
