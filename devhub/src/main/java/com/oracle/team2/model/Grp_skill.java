package com.oracle.team2.model;

import lombok.Data;

@Data
public class Grp_skill { //그룹스킬테이블 -> 다대다 해소 테이블
	private String skill_id; // FK	기술코드 -> skill_tbl(기술스택테이블 pk)
	private Long group_id;	//  FK	그룹아이디(MAX_seq) -> group_tbl(지원자그룹 pk)
	private Long board_id;	//	FK  게시글번호- seq -> board_tbl(게시글테이블 PK)
							

}
