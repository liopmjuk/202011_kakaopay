package com.kakaopay.pretask.exception;

import lombok.Getter;

/**
 * 뿌리기 기능에서 발생할 수 있는 에러 케이스 관리 Enum
 * @author Jeon
 *
 */
@Getter
public enum SpendErrorCode {
	
	//공통 에러
	ID_NOT_NUMBER("C001", "아이디는 숫자형이어야합니다."),
	NO_REQUEST_BODY("C002", "Request Body가 없습니다."),
	
	//Header 에러
	HEADER_NO_USERID("H001", "사용자를 조회할 수 없습니다."),
	HEADER_NO_ROOMID("H002", "대화방을 조회할 수 없습니다."),
	
	//토큰 생성 에러
	TOKEN_GEN_ERROR("G001","토큰 생성 시 오류가 발생했습니다."),
	
	//받기 에러
	//이미 받은 사용자
	ALREADY_RECEIVED_USER("R001","이미 받은 사용자입니다."),
	//뿌리기한 사용자
	RECV_NOT_ALLOWED_USER("R002","뿌린 사용자는 받을 수 없습니다."),
	//뿌리기에 속하지 않은 대화방
	RECV_NOT_ALLOWED_ROOM("R003","뿌리기한 방과 동일한 방에서만 받을 수 있습니다."),
	//10분 경과
	SPEND_EXPIRED("R004","받을 기간을 경과하였습니다."),
	//모두 받은 뿌리기 건
	SPEND_FINISHED("R005","이미 완료된 뿌리기건입니다."),
	
	//조회 에러
	//유효하지 않은 토큰
	NOT_ALLOWED_TOKEN("I001","유효하지 않은 토큰입니다."),
	//뿌리지 않은 사람이 조회
	INQUIRY_NOT_ALLOWED_USER("I002","조회할 수 없는 사용자입니다."),
	//7일 경과
	INQUIRY_EXPIRED("I003","조회 기간을 경과하였습니다.");
	
	private final String errorCode;
	private final String errorMsg;
	
	SpendErrorCode(String errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
}