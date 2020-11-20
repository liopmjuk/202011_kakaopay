package com.kakaopay.pretask.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 받기 완료된 정보
 * @author Jeon
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReceivedInfo {
	private int userId;			//받은 사용자 아이디
	private long money;			//받은 금액
}
