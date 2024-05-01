package com.oracle.team2.service.SA_Service;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data

// 페이지 작업 자체를 모듈화해버리자
public class SA_Paging {

	private int currentPage = 1;
	
	private int rowPage = 9;
	private int pageBlock = 10;
	
	private int start;
	private int end;
	
	private int startPage;
	private int endPage;
	
	private int total;
	private int totalPage;
	
	// constructor
	public SA_Paging(int total, Integer currentPage1) {
		
		this.total = total;
		
		if (currentPage1 != null)	this.currentPage = currentPage1;
		
		start	= (currentPage - 1) * rowPage + 1;
		end	= start + rowPage - 1;
		
		totalPage = (int) Math.ceil( (double) total / rowPage);
		
		startPage	= currentPage - (currentPage - 1) % pageBlock;
		endPage		= startPage + pageBlock - 1;
		
		if (endPage > totalPage)	endPage = totalPage;
		
	}
	
}
