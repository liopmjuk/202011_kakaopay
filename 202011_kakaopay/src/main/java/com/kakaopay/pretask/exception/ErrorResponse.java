package com.kakaopay.pretask.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 에러 응답 정보
 * @author Jeon
 *
 */
@Getter
@NoArgsConstructor
public class ErrorResponse {

	private String errorCode;
    private String errorMsg;

    private ErrorResponse(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
    private ErrorResponse(final SpendErrorCode code) {
    	this.errorCode = code.getErrorCode();
        this.errorMsg = code.getErrorMsg();
    }

    public static ErrorResponse of(final SpendErrorCode code) {
        return new ErrorResponse(code);
    }
    
    public static ErrorResponse of(final String errorCode, String errorMsg) {
        return new ErrorResponse(errorCode, errorMsg);
    }
}
