package com.oracle.team2.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Board {		// 게시글 테이블

	private Long board_id;		// PK			// 게시글번호
	
	private String user_id;		// FK (user_tbl PK)		// 작성자
	
	private String board_title;	// 제목
	
	private String pr_title;	// 프로젝트명
	
	private Date board_wdate;	// 작성일		// sysdate
	
	private Date board_edate;	// 마감일
	
	private String board_content;	// 내용
	
	private int board_view;			// 조회수
	private String local_code;		// FK (region_tbl PK)		// 행정동코드
	
	private String kakaolink;		// 카톡링크
	private String kakaopw;			// 카톡암호
	
	private String board_type;		// 게시판 유형		// 1=멘토주도, 2=멘토모집, 3=팀원모집
	
	private String board_status;	// 게시판 상태		// 0=종료, 1=모집중
	
	private Date pr_sdate;			// 프로젝트 시작일
	
	private Date pr_edate;		// 프로젝트 종료일
	
	private String pr_type;			// 진행방식		// 0=온라인, 1=오프라인, 2=온/오프라인 통합
	private int pr_price;			// 금액
	private int tot_round;			// 총회차
	private int cur_round;			// 진행된 회차
	
	private String del_status;		// 삭제구분		// 0=정상, 1=삭제, 2=게시 중지
	
	// SA 조회용
	private String user_name;	// 닉네임
	private int d_day;				// 디데이 연산값
	private String bookmarkYN;	// 북마크 Y/N
	
	// SA
	private Double user_score;
	private List<Skill> skills;
	
	// sa 20240316
	private int currentPage;
	private int start;
	private int end;
}