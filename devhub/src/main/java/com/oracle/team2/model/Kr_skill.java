package com.oracle.team2.model;

import lombok.Data;

@Data
public class Kr_skill { //국내기술통계 - 복합키(날짜, 대기업 공고수)
	private String skill_id; //기술코드 - 복합키-fK -> 기술스택(skill_tbl)에서 옴
	private String kr_date;	//날짜 		복합키-pk
	private Long ismajor;	//대기업여부 	복합키-pk
	private String kr_ref;	//출처 		복합키-pk
	
	private int recruit_cnt; //공고수
	
	
}
