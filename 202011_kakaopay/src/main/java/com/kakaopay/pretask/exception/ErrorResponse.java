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

	private String code;
    private String message;

    private ErrorResponse(final SpendErrorCode code) {
        this.message = code.getErrorMsg();
        this.code = code.getErrorCode();
    }

    public static ErrorResponse of(final SpendErrorCode code) {
        return new ErrorResponse(code);
    }
}
