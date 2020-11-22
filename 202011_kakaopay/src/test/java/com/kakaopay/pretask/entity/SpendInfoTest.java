package com.kakaopay.pretask.entity;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SpendInfoTest {
	
	final LocalDateTime now = LocalDateTime.now();
	
	SpendInfo spendInfo = SpendInfo.builder()
			.token("AAA")
			.spendUserId(10)
			.roomId("ABC")
			.totalMoney(5000L)
			.build();
			
	@Test
	public void 조회유효기간경과_테스트() {
		spendInfo = spendInfo.toBuilder()
				.spendTime(now.minusDays(8))
				.build();
		
		assertTrue(spendInfo.isInquiryExpired(now));
	}
	
	@Test
	public void 받기유효기간경과_테스트() {
		spendInfo = spendInfo.toBuilder()
				.spendTime(now.minusMinutes(11))
				.build();
		
		assertTrue(spendInfo.isSpendExpired(now));
	}
}
