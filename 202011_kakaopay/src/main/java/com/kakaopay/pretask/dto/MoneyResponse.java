package com.kakaopay.pretask.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * 받기  API 응답정보
 * @author Jeon
 *
 */
public class MoneyResponse {
	long money;
}
