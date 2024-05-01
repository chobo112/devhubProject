package com.oracle.team2.handler;

import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {
	// 웹소켓 세션을 담아둘 맵
	HashMap<String, WebSocketSession> sessionMap = new HashMap<>();
	//  세션 ID -->65693081-b6d0-5973-d45d-f406cb170ffe , Instance session1(김준수 )
    //  세션 ID -->39612536-fb45-5a84-613e-253099b79b1d , Instance  session2(조정은 )
	//            4daa55bd-30af-5db9-5e10-794b57ac7016,  Instance  session3(번헬싱)

	// 웹소켓 세션 ID과 User를 담아둘 맵
	HashMap<String, String> sessionUserMap = new HashMap<>();
	
	// 웹소켓 세션 ID과 User를 담아둘 JSONObject
	JSONObject jsonUser = null;
	
	
	// 2. handleTextMessage 메소드는 메시지를 수신하면 실행
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// Message 수신
		String msg = message.getPayload();
		System.out.println("SocketHandler handleTextMessage msg->"+msg);
		
		JSONObject jsonObj = jsonToObjectParser(msg);
		// type을 Get하여 분기 
		String msgType = (String) jsonObj.get("type");
		System.out.println("SocketHandler handleTextMessage  msgType->"+msgType);
		switch (msgType) {
		     // 전체 Message 
	       case "message":  
	       	jsonUser =  new JSONObject(sessionUserMap);
	        System.out.printf( "JSONUser: %s", jsonUser);
	   		// 전송대상을 기준으로  분기 
	   		String yourName = (String) jsonObj.get("yourName");
	   		System.out.println("SocketHandler handleTextMessage  yourName->"+yourName);
	   		// 전체
	   		if (yourName.equals("ALL")) {
			     	for(String key : sessionMap.keySet()) {
							WebSocketSession wss = sessionMap.get(key);
							try {
								System.out.println("message key->"+key);
								System.out.println("message wss->"+wss);
								wss.sendMessage(new TextMessage(jsonObj.toJSONString()));
							}catch(Exception e) {
								e.printStackTrace();
							}
					}
	   			
	   		} else {  // 개인 전송 대상자(yourName은 개인 sessionID)
	   			// 상대방
		    		System.out.println("개인 전송 대상자 상대방 sessionID-->"+yourName);
					WebSocketSession wss1 = sessionMap.get(yourName);
					try {
						wss1.sendMessage(new TextMessage(jsonObj.toJSONString()));
					}catch(Exception e) {
						e.printStackTrace();
					}
		    		// 나에게도 보내줘
		    		String meName = (String) jsonObj.get("sessionId");
		    		WebSocketSession wss2 = sessionMap.get(meName);
		    		System.out.println("개인 전송 대상자 나-->"+meName);
					try {
						wss2.sendMessage(new TextMessage(jsonObj.toJSONString()));
					}catch(Exception e) {
						e.printStackTrace();
					}
	   			
	   		}
	        break;	
	        // sessionUserMap에 User등록 
	     case "userSave":
	            // sessionUserMap에 sessionId와  userName 등록  
	     	    String sessionId   = (String) jsonObj.get("sessionId");
	     	    String userName    = (String) jsonObj.get("userName");
	     	    String saveStatus  = (String) jsonObj.get("saveStatus");
	     	    //    신규   등록 
	     	    if   (saveStatus.equals("Create") ) {
		     	    sessionUserMap.put(sessionId, userName);
		     	    System.out.println("================================================");
		     	    System.out.println("== sessionUserMap 저장내용 조회하여 arrayJsonUser에  ==");
		     	    System.out.println("==  각각의 JSONObject jsonUser로  변환             ==");
		     	    System.out.println("== 1. type : userSave                          ==");
		     	    System.out.println("== 2. sessionId : sessionUserMap.sessionId     ==");
		     	    System.out.println("== 3. userName  : sessionUserMap.userName      ==");
		     	    System.out.println("=================================================");
	     	    	
	     	    } else {   // Delete
		        	System.out.println("handleTextMessage userDelete start.. ");
		        	System.out.println("handleTextMessage userDelete session.getId()->"+session.getId());
		     		// 웹소켓 종료
		     		sessionMap.remove(session.getId());
		     		// sessionUserMap 종료
		     		sessionUserMap.remove(session.getId());
		           // break;
	     	    }
	     	    
	     	    JSONArray arrayJsonUser = new JSONArray();
	     	    System.out.println("== 1. type : userSave                         ==");
	     	    Iterator<String> mapIter = sessionUserMap.keySet().iterator();
	     	    System.out.println("== 2. sessionId : sessionUserMap.sessionId    ==");
	     	    System.out.println("== 3. userName  : sessionUserMap.userName     ==");
		     	while(mapIter.hasNext()){
	     	       String key = mapIter.next();
	     	       String value = sessionUserMap.get( key );
	     	       System.out.println("Key : Value -->"+key+" : "+value);
		     	   jsonUser =  new JSONObject();
		     	   jsonUser.put("type", "userSave");
		     	   jsonUser.put("sessionId",key);
		     	   jsonUser.put("userName",value);
		     	   arrayJsonUser.add(jsonUser);
	     	    }
	     	    System.out.println("====== session을 Get하여 User 내용 전송 ========");
	            System.out.printf( "arrayJsonUser: %s", arrayJsonUser);
	           
	            // 전체에 User등록을 하게 함
		     	for(String key : sessionMap.keySet()) {
						WebSocketSession wss = sessionMap.get(key);
						try {
							wss.sendMessage(new TextMessage(arrayJsonUser.toJSONString()));
						}catch(Exception e) {
							e.printStackTrace();
						}
				}
		        break;
		        
		    case "userDelete":  
	        	System.out.println("handleTextMessage userDelete start.. ");
	        	System.out.println("handleTextMessage userDelete session.getId()->"+session.getId());
	     		// 웹소켓 종료
	     		sessionMap.remove(session.getId());
	     		// sessionUserMap 종료
	     		sessionUserMap.remove(session.getId());
	            break;
		}
	        
	        
	        
	        
	        
	}
	
	//  @SuppressWarning
	//  이건 컴파일러가 일반적으로 경고하는 내용 중	"이건 하지마" 하고 제외시킬 때 쓰임
	//	따라서 어떤 경고를 제외시킬지 옵션
	//	1. all : 모든 경고를 억제
	//	2. cast : 캐스트 연산자 관련 경고 억제
	//	3. dep-ann : 사용하지 말아야 할 주석 관련 경고 억제
	//	4. deprecation : 사용하지 말아야 할 메소드 관련 경고 억제
	//	5. fallthrough : switch문에서의 break 누락 관련 경고 억제
	//	6. finally : 반환하지 않는 finally 블럭 관련 경고 억제
	//	7. null : null 분석 관련 경고 억제
	//	8. rawtypes : 제네릭을 사용하는 클래스 매개 변수가 불특정일 때의 경고 억제
	//	9. unchecked : 검증되지 않은 연산자 관련 경고 억제
	//	10. unused : 사용하지 않는 코드 관련 경고 억제
      

	@SuppressWarnings("unchecked")
	// 1. 웹소켓 연결이 되면 동작
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("SocketHandler afterConnectionEstablished start...");
		// 웹소켓  연결이 되면 동작
		super.afterConnectionEstablished(session);
		// 연결 소겟을 MAP에 등록 
		sessionMap.put(session.getId(), session);
		
		JSONObject jsonObject = new JSONObject();
		// 대상 : Client
		jsonObject.put("type", "getId");
		jsonObject.put("sessionId", session.getId());
		//  session Server 등록 -->  Socket Server가 Client에게 전송 
		session.sendMessage(new TextMessage(jsonObject.toJSONString()));
	
	}
	
	// 3.웹소켓이 종료되면 동작
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("SocketHandler afterConnectionClosed start...");
		// 웹소켓 종료
		sessionMap.remove(session.getId());
		super.afterConnectionClosed(session, status);
	}
	
	// handleTextMessage 메소드 에 JSON파일이 들어오면 파싱해주는 함수를 추가
	private static JSONObject jsonToObjectParser(String jsonStr) {
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = null;
		try {
			jsonObj = (JSONObject) parser.parse(jsonStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}	
	
}
