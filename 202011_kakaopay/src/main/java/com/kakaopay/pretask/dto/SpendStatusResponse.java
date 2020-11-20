package com.kakaopay.pretask.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.kakaopay.pretask.vo.ReceivedInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 뿌리기 현재 상태 정보
 * @author Jeon
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpendStatusResponse {
	private LocalDateTime spendTime;				//뿌린 시각
	private long spendMoney;					//뿌린 금액
	private long receivedMoney;					//받기 완료된 금액
	private List<ReceivedInfo> receivedInfos;	//받기 완료된 정보
	
	public void calReceivedMoney() {
		for(ReceivedInfo receivedInfo : receivedInfos) {
			this.receivedMoney += receivedInfo.getMoney();
		}
	}
}
