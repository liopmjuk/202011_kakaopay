package com.kakaopay.pretask.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 뿌리기 기능에서 발생되는 Exception 객체
 * @author Jeon
 *
 */
@Getter
@AllArgsConstructor
public class SpendException extends RuntimeException{
	private SpendErrorCode error;
}
