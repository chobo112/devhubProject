const imp = "imp13124555";
IMP.init(imp);
let paymentObject = {
		pg : 'html5_inicis.INIpayTest', //테스트 시 html5_inicis.INIpayTest 기재 
	    pay_method : 'card',
	    merchant_uid: "devhub_1", //상점에서 생성한 고유 주문번호
	    name : '프로젝트명 입력 부',
	    amount : 1240,
	    buyer_email : 'test@portone.io', // 구매자 메일
	    buyer_name : '구매자이름',	// 구매자 이름
	    buyer_tel : '010-1234-5678',   //필수 파라미터 입니다.
}

let pay_muid = $("#pay_muid");
let pay_pr_title = $("#pay_pr_title");
let pay_amount = $("#pay_amount");
let pay_method = $("#pay_method");
let pay_buyer_name = $("#pay_buyer_name");
let pay_tel = $("#pay_tel");
let pay_email = $("#pay_email");
let pay_phone = $("#pay_phone");
let pay_email_warn = $("#pay_email_warn");
let pay_phone_warn = $("#pay_phone_warn");

function requestPay() {
	pay_email_warn.text("");
	pay_phone_warn.text("");
	// 폼 정보 유효체크
	let emailRule = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;//이메일 정규식
	let phoneRule = /^\d{2,3}-\d{3,4}-\d{4}$/;  //전화번호 정규식
	
	if(!phoneRule.test(pay_tel.val())){
		pay_phone_warn.text("형식에 맞게 입력해주세요");
		pay_tel.focus();
		return;
	}
	if(!emailRule.test(pay_email.val())){
		pay_email_warn.text("이메일 형식에 맞게 작성해주세요");
		pay_email.focus();
		return;
	}
	
	// 폼정보 결제요청 객체에 넣는 부분
	paymentObject.buyer_email = pay_email.val();
	paymentObject.pay_method = pay_method.val();
	paymentObject.buyer_name = pay_buyer_name.val();
	paymentObject.buyer_tel = pay_tel.val();
	
	console.log(paymentObject);
	
	IMP.request_pay(paymentObject, function(rsp) { // callback 로직
		$.ajax({
			async: false,
			url: "conformPay",
			method: "POST",
			data: JSON.stringify({...rsp}),
			dataType: "json",
			contentType: "application/json",
			success: (response)=>{
				if(response.success){
					alert('결제 완료');
					location.href='';
				} else {
					alert('결제에 실패하였습니다.');
				}
			},
			error: () =>{alert('오류 관리자에게 문의하세요');}
		});
	});
}

const getPayInfo = (boardId) => {
	$.ajax({
		async: false,
		url: "payInfo",
		method: "GET",
		data: {board_id: boardId},
		dataType: "json",
		success: (response)=>{
			console.log(response);
			if(!response.isContinue){
				alert("다시 확인해주세요");
				location.href = "";
			}
			paymentObject.merchant_uid = response.muid;
			pay_muid.val(paymentObject.merchant_uid);
			paymentObject.name = response.name;
			pay_pr_title.val(paymentObject.name);
			paymentObject.amount = response.price;
			pay_amount.val(paymentObject.amount);
			paymentObject.buyer_name = response.buyer_name;
			pay_buyer_name.val(paymentObject.buyer_name);
			
			paymodal.show();
		},
		error: (error) => {
			alert("유효한 접근이 아닙니다.");
			console.log("AJAX 에러 "+error);
			location.href = "/";
		}
	})
}
