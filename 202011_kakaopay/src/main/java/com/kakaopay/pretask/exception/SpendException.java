package com.kakaopay.pretask.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SpendException extends RuntimeException{
	private SpendErrorCode error;
}
