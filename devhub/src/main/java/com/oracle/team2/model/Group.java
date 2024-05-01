package com.oracle.team2.model;

import java.util.List;

import lombok.Data;

@Data
public class Group {
	//SK
	private List<Skill> skills; //그룹당 스킬이 여러개라서 여기서 꺼내줌
	private List<Grp_skill> grpSkills; //그룹당 스킬이 여러개라서 여기서 꺼내줌
	private String board_type;		// 게시판 유형		// 1=멘토주도, 2=멘토모집, 3=팀원모집
	
	private Long board_id; //게시글번호 fk -> board_tbl(게시판 pk)
	private Long group_id;  //그룹아이디(Max_seq) ,	PK
	
	private int group_num;	//인원수	 char
	
	private String ismentor; //멘토유무 char
	

	
	// SA
	private List<User> users;
	private int accept_num;
}
