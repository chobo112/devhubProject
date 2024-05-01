

/* 데이터 출력 */
$(function() {
   $("#dropdownSearchButton").click(function() {
      $.ajax({
         type: "get",
         url: "/allskillJson", // 서버에서 데이터를 가져오는 URL로 수정해야 함
         success: function(allSkList2) {
            // 서버에서 받아온 데이터를 반복하여 checkbox와 label 요소를 생성하여 추가
            allSkList2.forEach(function(sk, index) {
               var checkboxId = "checkbox-item-" + (index + 11); // 고유한 ID 생성
               $("#formSkillList").append("<li><div class='flex items-center ps-2 rounded hover:bg-gray-100 dark:hover:bg-gray-600'>" + "<input id='" + checkboxId + "' type='checkbox' value='" + sk.skill_id + "' class='w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-700 dark:focus:ring-offset-gray-700 focus:ring-2 dark:bg-gray-600 dark:border-gray-500'>" + "<label for='" + checkboxId + "' class='w-full py-2 ms-2 text-sm font-medium text-gray-900 rounded dark:text-gray-300'>" + sk.skill_name + "</label></div></li>");
            });
         },
         error: function(xhr, status, error) {
            console.error("AJAX 요청 중 오류 발생:", status, error);
         }
      }); // ajax    
   }); // click           
}); // ready

/* 검색시 관련된 거 출력  */
function filterSkills(value) {
   var inputValue = value.toLowerCase();
   var skills = document.querySelectorAll('#formSkillList li');
   skills.forEach(function(skill) {
      var skillName = skill.textContent || skill.innerText;
      if (skillName.toLowerCase().indexOf(inputValue) > -1) {
         skill.style.display = "";
      } else {
         skill.style.display = "none";
      }
   });
}

// "추가" 버튼 클릭 이벤트 처리
$(".add-skill-button").click(function() {
   // 모달에서 선택한 값을 가져오는 로직을 추가해야 함
   var selectedSkillId = [];
   $("#formSkillList input:checked").toArray().forEach(dom=>{
      console.log(dom.value);
      selectedSkillId.push(dom.value);
   })
   console.log(selectedSkillId);

   
   // 선택한 값이 유효한지 확인
   if (selectedSkillId.length > 0) {
      // 선택한 값을 데이터베이스에 추가
      addSkillToDatabase(selectedSkillId);
   }
});

// 데이터베이스에 선택한 스킬 추가
function addSkillToDatabase(skillId) {
   alert("addSkillToDatabase ->" + skillId+" , " + (typeof skillId));
   console.log(skillId);
   // 선택한 스킬을 데이터베이스에 추가하는 서버 요청을 보냄
   $.ajax({
      type: "POST",
      url: "/addSkill",
      contentType: "application/json", // 요청의 Content-Type을 JSON으로 설정
      data: JSON.stringify(skillId),
      success: function(response) {
         // 데이터베이스에 스킬이 성공적으로 추가되었을 때의 동작을 정의
         // UI 업데이트 함수 호출
         addSkillToUI(skillId);
      },
      error: function(xhr, status, error) {
         // 에러 발생 시 사용자에게 메시지 표시
         alert("스킬 추가에 실패했습니다. 다시 시도해주세요.");
         console.error("에러 발생:", error);
      }
   });
}


// 화면에 선택한 스킬 추가
function addSkillToUI(skillId) {
   alert("Sending skillId to server:" + skillId);
   // 선택한 스킬을 화면에 추가하는 로직을 작성
   // 선택한 스킬의 정보를 서버에서 가져와야 한다면 AJAX 요청을 보내고,
   // 가져온 정보를 이용하여 새로운 스킬을 화면에 추가하는 로직을 작성할 수 있습니다.
   $.ajax({
      type: "GET",
      url: "/getSkillById?skillId=" + skillId, // 서버에서 스킬 정보를 가져오는 URL로 수정해야 함
      success: function(skill) {
         // 가져온 스킬 정보를 이용하여 새로운 아이템을 생성
         var newItem = '<li><p>' + skill.skill_name + '</p><img src="' + skill.skill_img + '" /></li>';

         // 생성한 아이템을 스킬 목록에 추가
         $("#skillsList").append(newItem);
      },
      error: function(xhr, status, error) {
         console.error("에러 발생:", error);
      }
   });
}

// 자기소개 저장 
function submitUserIntro() {
   var introData = document.getElementById("user_intro").value;

   // AJAX를 이용하여 데이터를 서버로 전송
   $.ajax({
      type: "POST",
      url: "/user_intro_update",
      contentType: "application/json",
      data: introData,
      success: function(response) {
         // 성공 시 처리할 내용
         console.log("자기소개가 성공적으로 수정되었습니다.");
      },
      error: function(xhr, status, error) {
         // 에러 발생 시 처리할 내용
         console.error("서버에러:", error);
      }
   });
}

// 깃허브 주소 수정
function submitGitHub() {
   var gitData = document.getElementById("github").value;
   alert("gitData=>" + (typeof gitData))

   // AJAX를 이용하여 데이터를 서버로 전송
   $.ajax({
      type: "POST",
      url: "/github_update",
      contentType: "application/json",
      data: gitData,
      success: function(response) {
         // 성공 시 처리할 내용
         console.log("깃허브가 성공적으로 수정되었습니다.");
      },
      error: function(xhr, status, error) {
         // 에러 발생 시 처리할 내용
         console.error("서버에러:", error);
      }
   });
}



function deleteSkill(skill_id) {
   alert("skill_id->" + (typeof skill_id) + "," + skill_id);
   // AJAX를 이용하여 서버에 스킬 삭제 요청을 보냅니다.
   $.ajax({
      type: "POST",
      url: "/delete_skill", // 스킬 삭제를 처리하는 서버의 엔드포인트 URL로 수정해야 합니다.
      data: skill_id, // 스킬 이름을 서버로 전달
      success: function(response) {
         // 서버에서 삭제 요청을 성공적으로 처리한 경우
         console.log("스킬 삭제 성공:", response);


         // UI에서 해당 스킬을 제거합니다.
         // 클릭된 버튼의 부모 li 요소를 선택하여 제거합니다.
         $("#skill_" + skill_id).remove();
      },
      error: function(xhr, status, error) {
         // 서버에서 삭제 요청을 처리하는 도중 에러가 발생한 경우
         console.error("스킬 삭제 에러:", error);
         // 필요한 에러 처리를 수행합니다.
      }
   });
}