package com.kakaopay.pretask.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kakaopay.pretask.exception.SpendErrorCode;
import com.kakaopay.pretask.exception.SpendException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Table(name="SPEND_INFO")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class SpendInfo {
	@Id
	@Column(name="TOKEN")
	private String token;
	
	@Column(name="SPEND_USER_ID")
	private int spendUserId;
	
	@Column(name="ROOM_ID")
	private String roomId;
	
	@Column(name="SPEND_TIME")
	private LocalDateTime spendTime;
	
	@Column(name="TOTAL_MONEY")
	private long totalMoney;
	
	@Column(name="ALL_RECEIVED_YN")
	private String allReceivedYn;

	//뿌린 사용자와 같은 사용자인지 조회
	public void checkSameUserId(int userId) {
		if(this.spendUserId == userId) {
			throw new SpendException(SpendErrorCode.RECV_NOT_ALLOWED_USER);
		}
	}
	
	//뿌린 사용자와 다른 사용자인지 조회
	public void checkNotSameUserId(int userId) {
		if(this.spendUserId != userId) {
			throw new SpendException(SpendErrorCode.INQUIRY_NOT_ALLOWED_USER);
		}
	}
	
	public void checkRoomId(String roomId) {
		if(!this.roomId.equals(roomId)) {
			throw new SpendException(SpendErrorCode.RECV_NOT_ALLOWED_ROOM);
		}
	}

	public boolean isSpendExpired(LocalDateTime nowTime) {
		LocalDateTime expireTime = this.spendTime.plusMinutes(10);
		return expireTime.isBefore(nowTime);
	}
	
	public boolean isInquiryExpired(LocalDateTime nowDate) {
		LocalDateTime expireDate = this.spendTime.plusDays(7);
		return expireDate.isBefore(nowDate);
	}
}
