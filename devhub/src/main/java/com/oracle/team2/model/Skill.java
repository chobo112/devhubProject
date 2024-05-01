package com.oracle.team2.model;

import lombok.Data;

@Data
public class Skill { //기술스택테이블
	
	private String skill_id; 	//기술코드 pk
	
	private String skill_name; 	//기술명칭 not-null
	
	private String skill_img; 	// 이미지 not-null
	
	private String skill_type; 	//종류 char not-null
	//0:기타, 1:협업툴, 2:데이터, 3:모바일, 
	//4:프론트엔드, 5:데브옵스, 6:백엔드, 
	//7: 테스팅툴, 8:데이터베이스, 9:언어
	
	/* char타입 적용 예시
	 * String skill_type;
		switch (dbSskill_type) {
		    case '0':
		        skill_type = "정상";
		        break;
		    case '1':
		        skill_type = "비정상";
		        break;
		    case '2':
		        skill_type = "판별중";
		        break;
		    default:
		        skill_type = "알 수 없음";
		        break;
}
	 * */
	
}
