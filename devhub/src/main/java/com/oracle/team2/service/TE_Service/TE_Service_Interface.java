package com.oracle.team2.service.TE_Service;

import java.util.List;
import java.util.Map;

import com.oracle.team2.model.Board;
import com.oracle.team2.model.Kr_skill;
import com.oracle.team2.model.Skill_Gbl_lang;
import com.oracle.team2.model.Skill_Kr_skill;

public interface TE_Service_Interface {
	
	List<Skill_Kr_skill> getKrTechStackRankingList(int end, int techStackType, String keyword);

	Skill_Kr_skill getKrTechStackTotalList(int techStackType);
	
	List<Skill_Gbl_lang> getGblLangRankingList(int end);

	List<Skill_Gbl_lang> getGblLangTotalList();

	void setTiobeData(List<String[]> top20TableDataList);

	void setJobkoreaData(List<Skill_Kr_skill> jobKoreaDataList);

	void setSaramin(List<Skill_Kr_skill> saraminDataList);

	
}
