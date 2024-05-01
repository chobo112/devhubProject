
package com.oracle.team2.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Apply { //지원관리 테이블 다대다해소 테이블
	private String user_id; //지원자아이디 FK	회원정보(user_tbl PK)
	private Long group_id;//그룹아이디(MAX_seq) FK / group_tbl(지원자그룹 pk)
	private Long board_id;//게시글번호(seq) FK /  board_tbl(게시글 -PK)
	
	@NotNull
	private String ap_accept; //0대기, 1수락, 2거절
	@NotNull
	private String ap_result; //0미진행, 1최종진행(팀원모집 : 최종진행, 멘토모집&멘토주도 : 결제까지 끝)
}
