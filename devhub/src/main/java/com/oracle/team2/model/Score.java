package com.oracle.team2.model;

import lombok.Data;
//멘토아이디와 유저아이디는 같은데 이름이 같아서 오류가 나옴. 그래서 멘토아이디를넣은거
//다대다해소테이블(회원정보-User), 게시글-board), 복합키
@Data
public class Score { //평점테이블
	
	private String user_id; 	//유저아이디      복합키-PK  user_tbl의 PK와 이름이 겹침
	private String mentor_id;	//멘토아이디  <-  복합키-FK  회원정보(User테이블의 user_id)
	private Long board_id;   //게시글번호(seq) 복합키-FK  게시글(board_tbl의 PK)
	
	
	private int sc_score;	//평점 	0~10점 not null
	
	private String sc_cmt;		//평점 코멘트 null
	
	//조회용
	private double average_score;
}
