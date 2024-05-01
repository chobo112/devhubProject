package com.oracle.team2.model;

import lombok.Data;

@Data
//다대다 해소테이블 +복합키 (User, Skill테이블과)
public class User_skill {  //유저스킬테이블
	private String user_id;  //복합키-FK user_id -> user_tbl 회원정보테이블(user_tbl)
	private String skill_id; //복합키-FK skill_id -> skill_tbl 기술스택테이블(PK)
}
