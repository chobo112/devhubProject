package com.oracle.team2.dao.TE_Dao;

import java.util.List;
import java.util.Map;

import com.oracle.team2.model.Skill_Gbl_lang;
import com.oracle.team2.model.Skill_Kr_skill;

public interface TE_Dao_Interface {

	List<Skill_Kr_skill> krTechStackRanking(int end, String techStackTypeStr, String keyword);

	Skill_Kr_skill getKrTechStackTotalList(int techStackType);

	List<Skill_Gbl_lang> getGblLangTotalList();

	List<Skill_Gbl_lang> getGblLangRankingList(int end);

	void setTiobeData(List<Skill_Gbl_lang> sendSkillGblLangList);

	void setJobkoreaData(List<Skill_Kr_skill> jobkoreaDataList);

	void setSaraminData(List<Skill_Kr_skill> saraminDataList);

	List<Skill_Kr_skill> getPrevTechStackTotalList(int techStackType);


	

}
