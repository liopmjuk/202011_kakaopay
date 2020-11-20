package com.kakaopay.pretask.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * API 호출자 정보
 * @author Jeon
 *
 */
@Getter
@AllArgsConstructor
@Builder
public class UserInfo {
	private int userId;
	private String roomId;
}
