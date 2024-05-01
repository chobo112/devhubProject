package com.oracle.team2.webCrawler;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.oracle.team2.model.Skill_Kr_skill;
import com.oracle.team2.service.TE_Service.TE_Service_Interface;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TE_Crawler implements ApplicationRunner {
	private final TE_Service_Interface tes;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		 Runnable task = new Runnable() {
	            public void run() {
	            	try {
//		            	setTiobeData();
//		        		setJobkoreaData();
//		        		setSaraminData();
					} catch (Exception e) {
						System.out.println("run exception->" + e.getMessage());
					}

	            }
	        };
		
	        scheduler.schedule(task, 1, TimeUnit.SECONDS);
	        scheduler.shutdown();



	}// run

	private void setSaraminData() throws Exception{
//		List<Skill_Kr_skill> krTechStackList0 = tes.getKrTechStackTotalList(0);
//		List<Skill_Kr_skill> krTechStackList = krTechStackList0.subList(0, Math.min(krTechStackList0.size(), 10));
		List<Skill_Kr_skill> krTechStackList = tes.getKrTechStackRankingList(200, 0, "");
		List<Skill_Kr_skill> saraminDataList = new ArrayList<>();

		WebDriver driver = WebDriverUtil.getChromeDriver();

		try {
			if (!ObjectUtils.isEmpty(driver)) {
				System.out.println("TE_Crawler setSaraminData driver Start... ");

				for (Skill_Kr_skill sks : krTechStackList) {
					Skill_Kr_skill newSks = new Skill_Kr_skill();
					String skillName = sks.getSkill_name();
					String skillSearchName = sks.getSkill_name().replace("#", "%23").replace("+", "%2B");

					String saramin = "https://www.saramin.co.kr/zf_user/search?search_area=main&"
							+ "search_done=y&search_optional_item=n&searchType=search&searchword=" + skillSearchName;

					String saraminMajor = "https://www.saramin.co.kr/zf_user/search?searchType=" + "search&searchword="
							+ skillSearchName+ "&company_cd=0%2C1%2C2%2C3%2C4%2C5%2C6%2C7%2C9%2C10&"
							+ "company_type=scale001&panel_type=&search_optional_item=y&search_done=y&"
							+ "panel_count=y&preview=y";

					String query = "#recruit_info > div.header > span";
					
					
					String searchTotalCnt = getTextFromElement(driver, saramin, query);
					String searchMajorTotalCnt = getTextFromElement(driver, saraminMajor, query);

					System.out.println("searchTotalCnt      --> "+skillName+"  "+ searchTotalCnt);
					System.out.println("searchMajorTotalCnt --> "+skillName+"  "+ searchMajorTotalCnt);

					newSks.setSkill_name(skillName);
					newSks.setSkill_id(sks.getSkill_id());
					newSks.setIsmajor(0);
					newSks.setKr_ref("saramin");
					newSks.setRecruit_cnt(Integer.parseInt(searchTotalCnt));
					newSks.setMajor_recruit_cnt(Integer.parseInt(searchMajorTotalCnt));

					saraminDataList.add(newSks);
				}//for
			}//if
		} catch (Exception e) {
			System.out.println("setSaraminData exception->" + e.getMessage());
		}finally {
			System.out.println("finally setSaraminData saraminDataList.size() ... " + saraminDataList.size());
			driver.quit();
		}
		
		System.out.println("TE_Crawler setSaraminData saraminDataList.size() ... " + saraminDataList.size());
		tes.setSaramin(saraminDataList);

	}// setSaramin


	
	
	private void setJobkoreaData() throws Exception {
//		List<Skill_Kr_skill> krTechStackList0 = tes.getKrTechStackTotalList(0);
//		List<Skill_Kr_skill> krTechStackList = krTechStackList0.subList(0, Math.min(krTechStackList0.size(), 5));
		List<Skill_Kr_skill> krTechStackList = tes.getKrTechStackRankingList(200, 0, "");
		List<Skill_Kr_skill> jobkoreaDataList = new ArrayList<>();
		
		Date today = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = format.format(today);

		System.out.println("TE_Crawler setJobkoreaData krTechStackList --> " + krTechStackList);

		try {
			for (Skill_Kr_skill sks : krTechStackList) {

				Skill_Kr_skill newSks = new Skill_Kr_skill();

				String skillName = sks.getSkill_name();

				String jobkorea = "https://www.jobkorea.co.kr/Search/?stext=" + skillName;
				String jobKoreaMajor = "https://www.jobkorea.co.kr/Search/?stext=" + skillName + "&cotype=1";

				Document jobkoreaDoc = Jsoup.connect(jobkorea).get();
				Document jobKoreaMajorDoc = Jsoup.connect(jobKoreaMajor).get();

				Element content = jobkoreaDoc.getElementById("content");
				Element contentMajor = jobKoreaMajorDoc.getElementById("content");

				String searchTotalCnt = content.select(
						"#content > div > div > div.cnt-list-wrap > div > div.recruit-info > div.list-filter-wrap > p > strong")
						.text().replaceAll(",", "");
				String searchMajorTotalCnt = contentMajor.select(
						"#content > div > div > div.cnt-list-wrap > div > div.recruit-info > div.list-filter-wrap > p > strong")
						.text().replaceAll(",", "");

				System.out
						.println("TE_Crawler setJobkoreaData searchTotalCnt --> " + skillName + " : " + searchTotalCnt);
				System.out.println("TE_Crawler setJobkoreaData searchMajorTotalCnt --> " + skillName + " : "
						+ searchMajorTotalCnt);

				newSks.setSkill_name(skillName);
				newSks.setSkill_id(sks.getSkill_id());
				newSks.setKr_date(dateStr);
				newSks.setIsmajor(0);
				newSks.setKr_ref("jobkorea");
				newSks.setRecruit_cnt(Integer.parseInt(searchTotalCnt));
				newSks.setMajor_recruit_cnt(Integer.parseInt(searchMajorTotalCnt));
				jobkoreaDataList.add(newSks);

			} // for
		} catch (Exception e) {
			System.out.println("setJobkoreaData exception->" + e.getMessage());
		}
		System.out.println("TE_Crawler setJobkoreaData newKrTechStackList.size() --> " + jobkoreaDataList.size());

		tes.setJobkoreaData(jobkoreaDataList);
	}// method

	private void setTiobeData() throws Exception {
		List<String[]> tiobeTableDataList = new ArrayList<>();

		String tiobe = "https://www.tiobe.com/tiobe-index/";
		Document tiobeDoc = Jsoup.connect(tiobe).get();

		Element top20TableElement = tiobeDoc.getElementById("top20");
		Element otherPLTableElement = tiobeDoc.getElementById("otherPL");

		Elements top20TableRows = top20TableElement.select("tr:not(thead tr)");
		Elements otherPLTableRows = otherPLTableElement.select("tr:not(thead tr)");

		if (top20TableRows != null) {
			for (Element row : top20TableRows) {
				// 각 행의 모든 셀(td) 선택
				Elements tdCells = row.select("td");

				String imgSrc = null;
				String[] rowDataArray = new String[6];
				int columnIndex = 0;
				for (Element cell : tdCells) {
					Element imgElement = cell.selectFirst("img");
					if (imgElement != null) {
						imgSrc = "https://www.tiobe.com/" + imgElement.attr("src");
						rowDataArray[5] = imgSrc; // 이미지 소스를 마지막 인덱스에 할당

					} else {
						String cellData = cell.text().replaceAll("\\(|\\)", "").replaceAll("/", " ");
						if (!cellData.isEmpty()) {
							rowDataArray[columnIndex] = cellData;
							columnIndex++;
							System.out.print(cellData + " ");
							if (columnIndex == 5) {
								System.out.print(imgSrc + " ");
							}

						}
					}
				}
				System.out.println(); // 행 구분을 위한 개행
				tiobeTableDataList.add(rowDataArray);
			}
		}

		if (otherPLTableRows != null) {
			for (Element row : otherPLTableRows) {
				// 각 행의 모든 셀(td) 선택
				Elements tdCells = row.select("td");

				String[] rowDataArray = new String[6];
				int columnIndex = 0;
				for (Element cell : tdCells) {
					String cellData = cell.text().replaceAll("\\(|\\)", "").replaceAll("/", " ");
					if (!cellData.isEmpty()) {
						// columnIndex가 0일 때, 같은 값을 0번째와 1번째 인덱스에 할당
						if (columnIndex == 0) {
							rowDataArray[columnIndex] = cellData;
							rowDataArray[columnIndex + 1] = cellData; // 0의 값이 1에도 할당됨
							columnIndex += 2; // 다음 인덱스로 이동 (2번 인덱스로)
						}
						// columnIndex가 2일 때, 즉 실제로는 3번째 셀을 처리할 차례일 때
						else if (columnIndex == 2) {
							rowDataArray[columnIndex] = cellData;
							columnIndex++; // 다음 셀로 이동
						}
						// 이제 첫 번째 셀의 데이터를 3번째와 4번째 인덱스에도 할당해야 합니다.
						else if (columnIndex == 3) {
							rowDataArray[columnIndex] = cellData;
							rowDataArray[columnIndex + 1] = cellData; // 3의 값이 4에도 할당됨
							break; // 루프 종료
						}
					}
					rowDataArray[5] = "noImg";
				}

				// 수정된 부분: rowDataArray의 값을 출력합니다.
				for (String data : rowDataArray) {
					System.out.print(data + " ");
				}
				System.out.println(); // 행 구분을 위한 개행
				tiobeTableDataList.add(rowDataArray);
			}
		}
		tes.setTiobeData(tiobeTableDataList);
	}
	
	
	
	private String getTextFromElement(WebDriver driver, String url, String query) {
	    driver.get(url);
	    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
	    return driver.findElements(By.cssSelector(query))
	            .stream()
	            .findFirst()
	            .map(WebElement::getText)
	            .map(text -> text.replaceAll("[^0-9]", ""))
	            .orElse("0");
	}

}// class