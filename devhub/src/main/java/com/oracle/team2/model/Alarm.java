package com.oracle.team2.model;

import java.util.Date;

import lombok.Data;

@Data
public class Alarm {		// 알림 테이블

	private String user_id;		//아이디 FK 유저테이블(user_tbl PK)		// 유저아이디
	private Long board_id;		//게시글번호(seq) FK 게시글테이블(board_tbl PK)		
	private String al_type;		//종류 PK		
	
	private String al_content;	//알람내용
	
	private String al_read;		//읽음읽음			// 0=안읽음, 1=읽음
	
	private Date al_date;		//날짜// sysdate
	
	// 정보 전달 용
	private String pr_title;
	
	// page 정보
	private String pageNum;
	private int start; private int end;
	private String currentPage;
	
}
