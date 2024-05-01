package com.oracle.team2.model;

import lombok.Data;

@Data
public class BookMark { //북마크테이블
	private String user_id; //지원자아이디 FK	회원정보(user_tbl PK)
	private Long board_id;	//게시글번호(seq) FK /  board_tbl(게시글 -PK)

	// 20240318 sa
	int currentPage;
	private int start;
	private int end;
}
