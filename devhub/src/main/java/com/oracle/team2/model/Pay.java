package com.oracle.team2.model;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
//다대다 해소 테이블
public class Pay {	// 결제 테이블
	
	private Long pay_id;		//결제고유번호(seq) 	 PK		
	private String user_id;		//결제아이디(=유저아이디) FK-조인용 (user_tbl PK)   NULL
	private Long board_id;		//게시글번호(seq)		 FK-조인용 (board_tbl PK)	NULL
	@NotNull
	private Date pay_date;		// 시간		// sysdate not-null
	@NotNull
	private String pay_status;	// 상태		// not-null	기본값-0=미결제, 1=결제완료, 2=결제오류, 3=환불대기, 4=환불완료
	private String pay_log;		// 로그	null
	
	// 정보 전달용
	private String muid;	  // 결제 고유 아이디(결제대행사로 넘어갈 정보)
	private String pr_title; // 결제하는 프로젝트명
	private Long pr_price; 	// 결제하는 가격
	private String user_name;	// 결제하는사람 닉네임
	
}
