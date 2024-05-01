package com.oracle.team2.model;

import java.util.Date;

import lombok.Data;

@Data
public class Comment {	// 댓글 테이블

	private Long cmt_id;		// PK		// 댓글번호
	private Long board_id;		// FK-조인용 (board_tbl PK)		// 게시글번호
	private String user_id;		// FK-조인용 (user_tbl PK)		// 유저아이디
	
	private String cmt_content;	// 댓글내용
	
	private Date cmt_date;		// 작성일시		// sysdate
	
	private Long cmt_ref;		// 참조댓글
	
	private int cmt_depth;		// 참조깊이
	
	private int cmt_numbering;		// 참조넘버링
	
	// page 정보 카드상세페이지 댓글용
	private String pageNum;
	private int start; private int end;
	private String currentPage;
	
	// 댓글에 닉네임 보이게 함 
	private String user_name;
	
	
}
