package com.oracle.team2.model;

import lombok.Data;

@Data
public class Refund { //환급 테이블 복합키
	
	private String user_id; 		//아이디 - user의 pk
	private Long point_id; 			//포인트로그ID(max seq) -point_log테이블의 pk
	
	private String refund_status; 	//환급상태 not-null  0:환급대기, 1:환급완료
	
	private String account; 		//계좌번호 not-null
	
	private String account_owner; //계좌번호 예금주
	
	private String bank;			// 은행

	private Long point;
	
}
