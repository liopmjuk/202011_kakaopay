package com.kakaopay.pretask.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

/**
 * 뿌리기에 필요한 정보
 * @author Jeon
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpendInfoRequest {
	private long spendMoney;			//뿌릴 금액
	private int peopleNum;				//뿌릴 인원
}
