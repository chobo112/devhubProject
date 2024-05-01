package com.oracle.team2.model;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class SK_board_write {
	
	List<Group> groups;	// 보드에서 그룹이 -> 스킬을 가지고 있는 구조. 그룹안에 리스트가 담겨있음

	
	private String board_title; //제목
	
	private String pr_title;	// 프로젝트명
	
	private Date board_wdate;	// 작성일		// sysdate
	
	private Date board_edate;	// 마감일
	
	private String board_content;	// 내용
	
	private int board_view;			// 조회수
	
	private String local_code;		// FK (region_tbl PK)		// 행정동코드
	
	private String kakaolink;		// 카톡링크 user_tbl에서 user_email 대신에 카카오링크, 카아오pw뿌려지자
	
	private String kakaopw;			// 카톡암호
	
	private String board_type;		// 게시판 유형		// 1=멘토주도, 2=멘토모집, 3=팀원모집
	
	private Date pr_sdate;			// 프로젝트 시작일

	private Date pr_edate;		// 프로젝트 종료일
	
	private String pr_type;			// 진행방식		// 0=온라인, 1=오프라인, 2=온/오프라인 통합

	private int pr_price;			// 금액

	private int tot_round;			// 총회차 - - > 프로젝트 시작일자 - 한달단위로 잘라서 총회차 계산해서 넣어주기

	private String mentor_id;	//멘토아이디 -> 게시글작성할때 보드타입이 1(멘토주도)인경우 mentor_id에도 user_id가 추가 
	
	private int min_people;		//최소인원 그룹인원들의 합 -> 최소인원을 넘어야지 플젝 진행가능

	private Long board_id;		// 보드의 아이디
	
	//user_tbl 
	
	
	private Integer[] groupIndexArray; //0이면 멘토.
	private Integer[] groupNumArray;
	private List<String[]> skillImgArray;
	
	
	//user가 없기때문에 임시로 user_id를 넣어주자
	private String user_id;

}
