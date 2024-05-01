window.onload = function(){
	$.ajax({
		type: "GET",
		url: "recentAlarms",
		success: getAlarms
	})
}

const getAlarms = (response) =>{
			//console.log(response);
			const hasUnread = response.indexOf('data-readstatus="0"');
			console.log('안읽은 알람', hasUnread);
			if(hasUnread >=0){
				$('#notificationUnreadPoint').removeClass('hidden');
			} else {
				$('#notificationUnreadPoint').addClass('hidden');
			}
			$('#alarmList').replaceWith(response);
		}
	
const deleteAlarm = function(userId,boardId,alType){
	console.log(userId,boardId,alType);
	$.ajax({
		async: false,
		type: "DELETE",
		url: "deleteAlarm",
		data: {user_id:userId, board_id:boardId, al_type:alType},
		success: response=>{
			console.log("delete응답",response);
			if(response=='success'){
				$.ajax({
					type: "GET",
					url: "recentAlarms",
					success: getAlarms
				});
			} else {
				alert("삭제 실패");
			}
		}
	})
}

const readAlarm = function(userId,boardId,alType){
	$.ajax({
		type:"PATCH",
		url: "readAlarm",
		data: {user_id:userId, board_id:boardId, al_type:alType},
		success: (response) => {
			console.log(response);
			$.ajax({
					type: "GET",
					url: "recentAlarms",
					success: getAlarms
			});
		}
	})
	
	// 타입에 따른 page이동
	const url = getUrlByAlType(boardId,alType);
	location.href = url;
}


const getUrlByAlType = (boardId,alType)=>{
	switch(alType){
		case '1': // 관심글 D-1
			return `bookmark`;
		case '2': // 관심글 종료
			return `bookmark`;
		case '3': // 참여글 D-1
			return `/MyProjectMain`;
		case '4': // 참여글 종료
			return `/MyProjectMain`;
		case '5': // 등록글 D-1
			return `applyDetail?board_id=${boardId}`;
		case '6': // 등록글 종료
			return `applyDetail?board_id=${boardId}`;
		case '7': // 멘토에게 지원자 알림
			return `applyDetail?board_id=${boardId}`;
		case '8': // 모집장에게 지원자 알림
			return `applyDetail?board_id=${boardId}`;
		case '9': // 모집장에게 멘토지원 알림
			return `applyDetail?board_id=${boardId}`;
		case '10': // 지원 수락 알림
			return `/MyProjectMain`;
		case '11': // 지원 거절 알림
			return `/MyProjectMain`;
		case '12': // 결제요청
			return `payView?board_id=${boardId}`;
		case '13': // 포인트입금
			return `pointView?periodDays=30`;
		case '14': // 댓글
			return `cardDetailForm?board_id=${boardId}`
		default :
			return "/";
	}
}