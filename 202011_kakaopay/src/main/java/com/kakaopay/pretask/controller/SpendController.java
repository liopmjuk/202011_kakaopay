package com.kakaopay.pretask.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kakaopay.pretask.dto.MoneyResponse;
import com.kakaopay.pretask.dto.SpendInfoRequest;
import com.kakaopay.pretask.dto.TokenRequest;
import com.kakaopay.pretask.dto.TokenResponse;
import com.kakaopay.pretask.exception.SpendErrorCode;
import com.kakaopay.pretask.exception.SpendException;
import com.kakaopay.pretask.service.SpendService;
import com.kakaopay.pretask.vo.UserInfo;

//HTTP Header
//요청한 사용자의 식별값은 숫자 형태 ( X-USER-ID )
//요청한 사용자가 속한 대화방의 식별값은 문자 형태 ( X-ROOM-ID )
//잔액 체크 안함

//단위테스트

@RestController
//뿌리기 기능 Rest API 컨트롤러
public class SpendController {
	
	@Autowired
	SpendService spendService;
	
	@RequestMapping(value="/spend", method=RequestMethod.POST)
	public TokenResponse spendMoney(
			@RequestHeader Map<String, String> headers,
			@RequestBody SpendInfoRequest spendInfo) throws NoSuchAlgorithmException {
		//고유 token 발급 - 응답
		//token 3자리 문자열로 예측이 불가능해야 함.
		//뿌릴 금액을 인원수에 맞게 분배하여 저장 (Rand 함수 사용)
			
		UserInfo userInfo = extractHeaderInfo(headers);
		return spendService.spendMoney(spendInfo, userInfo);
	}
	
	@RequestMapping(value="/receive", method=RequestMethod.POST)
	public MoneyResponse receiveMoney(
			@RequestHeader Map<String, String> headers,
			@RequestBody TokenRequest tokenRequest) {
		//뿌리기 건 중 누구에게도 할당되지 않은 분배건 하나를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줌
		//뿌리기 당 한 사용자는 한번만 받음
		//자신이 뿌리기한 건은 자신이 받을 수 없음
		//뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있음
		//뿌린 건은 10분 간만 유효
		
		UserInfo userInfo = extractHeaderInfo(headers);
		return spendService.receiveMoney(tokenRequest.getToken(), userInfo);
	}
	
	@RequestMapping(value="/inquiry", method=RequestMethod.POST)
	public void checkSpendingStatus(
			@RequestHeader Map<String, String> headers,
			@RequestBody TokenRequest tokenRequest) {
		//뿌리기 건의 현재 상태를 응답값
		//현재 상태 : 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보 ([받은 금액, 받은 사용자 아이디] 리스트)
		//뿌린 사람 자신만 조회 가능
		//다른 사람의 뿌리기 건이나 유효하지 않은 token 에 대해서는 조회 실패 응답
		//7일 동안 조회 가능
		
		UserInfo userInfo = extractHeaderInfo(headers);
		spendService.inquirySpendingStatus(tokenRequest.getToken(), userInfo);
	}
	
	//HTTP Header 정보 추출 (호출 사용자, 속한 대화방) 
	private UserInfo extractHeaderInfo(Map<String, String> headers) {
		
		String userId = headers.get("X-USER-ID");
		String roomId = headers.get("X-ROOM-ID");
		
		if("".equals(userId)) { throw new SpendException(SpendErrorCode.HEADER_NO_USERID); }
		if("".equals(roomId)) { throw new SpendException(SpendErrorCode.HEADER_NO_ROOMID); }
		
		UserInfo userInfo = UserInfo.builder()
								.userId(Integer.parseInt(userId))
								.roomId(roomId)
								.build();
		return userInfo;
	}
}
