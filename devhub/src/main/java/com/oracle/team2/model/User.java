package com.oracle.team2.model;

import java.util.List;

import lombok.Data;

@Data
public class User {

	private String user_id;		//아이디 		PK
	private String local_code;	//행정동코드  	FK-조인용 (region PK) null
	
	private String user_name;	//닉네임 no-null
	
	private String user_pw;		//비밀번호  	not-null
	
	private String user_email;	//이메일  	not-null
	
	private int login_cnt;		//로그인시도횟수 not-null 기본값0
	
	private int point;			//포인트not-null 기본값0
	
	private String user_intro;	//자기소개	Null
	private String github;		//깃허브		Null
	
	private String del_status;	//삭제구분	not-null  0:정상-기본값, 1: 삭제, 2:사용중지 not-null
	
	private int user_score;	//평점 		not-null
	
	// 데이터 저장용
	private String change_pw;  //비밀번호 바뀐거 추가.
	private boolean resultYN;
	
	// 조회용
	private String[] skill_id;
	private String skill_name; 
	private String skill_img;
	// private String skill_id; // 민경님 변경 부탁
	
	// SA
	private List<Skill> skills;
	private String ap_accept;
}
