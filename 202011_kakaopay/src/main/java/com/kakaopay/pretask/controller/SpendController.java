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
import com.kakaopay.pretask.dto.SpendStatusResponse;
import com.kakaopay.pretask.dto.TokenRequest;
import com.kakaopay.pretask.dto.TokenResponse;
import com.kakaopay.pretask.exception.SpendErrorCode;
import com.kakaopay.pretask.exception.SpendException;
import com.kakaopay.pretask.service.SpendService;
import com.kakaopay.pretask.vo.UserInfo;

//HTTP Header
//요청한 사용자의 식별값은 숫자 형태 ( X-USER-ID )
//요청한 사용자가 속한 대화방의 식별값은 문자 형태 ( X-ROOM-ID )
//단위테스트

@RestController
//뿌리기 기능 Rest API 컨트롤러
public class SpendController {
	
	@Autowired
	SpendService spendService;
	
	/**
	 * 뿌리기 API
	 * @param headers
	 * @param spendInfo
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping(value="/spend", method=RequestMethod.POST)
	public TokenResponse spendMoney(
			@RequestHeader Map<String, String> headers,
			@RequestBody SpendInfoRequest spendInfo) throws NoSuchAlgorithmException {
		
		UserInfo userInfo = extractHeaderInfo(headers);
		return spendService.spendMoney(spendInfo, userInfo);
	}
	
	/**
	 * 받기  API
	 * @param headers
	 * @param tokenRequest
	 * @return
	 */
	@RequestMapping(value="/receive", method=RequestMethod.POST)
	public MoneyResponse receiveMoney(
			@RequestHeader Map<String, String> headers,
			@RequestBody TokenRequest tokenRequest) {
		
		UserInfo userInfo = extractHeaderInfo(headers);
		return spendService.receiveMoney(tokenRequest.getToken(), userInfo);
	}
	
	/**
	 * 조회  API
	 * @param headers
	 * @param tokenRequest
	 */
	@RequestMapping(value="/inquiry", method=RequestMethod.POST)
	public SpendStatusResponse checkSpendingStatus(
			@RequestHeader Map<String, String> headers,
			@RequestBody TokenRequest tokenRequest) {
		
		UserInfo userInfo = extractHeaderInfo(headers);
		return spendService.inquirySpendingStatus(tokenRequest.getToken(), userInfo);
	}
	
	/**
	 * HTTP Header 정보 추출 (호출 사용자, 속한 대화방) 
	 * @param headers
	 * @return
	 */
	private UserInfo extractHeaderInfo(Map<String, String> headers) {
		
		String userId = headers.get("X-USER-ID");
		String roomId = headers.get("X-ROOM-ID");
		
		if("".equals(userId) || userId == null) { throw new SpendException(SpendErrorCode.HEADER_NO_USERID); }
		if("".equals(roomId) || roomId == null) { throw new SpendException(SpendErrorCode.HEADER_NO_ROOMID); }
		
		UserInfo userInfo = UserInfo.builder()
								.userId(Integer.parseInt(userId))
								.roomId(roomId)
								.build();
		return userInfo;
	}
}
