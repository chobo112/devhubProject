package com.oracle.team2.model;

import java.sql.Date;

import lombok.Data;

@Data
public class Point_log {//포인트로그 테이블 복합키
	//복합키 point_date, user_id
	private Long point_id;	//포인트 로그(MAX_seq) pk-복합키  기본값 0
	private String user_id; 	//아이디				FK-복합키  user_id -> user_tbl(회원정보테이블의PK)

	private Long board_id; 		//게시글번호(Seq) FK ->board_tbl(게시글 : board_id)
								//조인용도로 있는 fk, Null
	
	private Date point_date;	//날짜 not-null
	
	private int point; 			//포인트 not-null
	
	private String point_type;	//입출금종류 기본값-0:입금, 1:출금 not-null

	// 전송용
	private String pr_title; // 프로젝트명 
	private String date;
	private int round;			// 현재 회차
	private int tot_round;			// 전체 회차
	private int periodDays;				// 조회 기간 0: 전체기간, 1: 전일, 7: 7일전, 30: 30일전, 365: 1년전 
	private String search;
	// page 정보
	private String pageNum;
	private int start; private int end;
	private String currentPage;
}
