package com.kakaopay.pretask.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kakaopay.pretask.service.MoneySplitService;
import com.kakaopay.pretask.service.TokenGenerateService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
		MoneySplitService.class,
		TokenGenerateService.class
	})
public class MoneyTokenTest {
	
	@Autowired
	MoneySplitService moneySplitService;
	
	@Autowired
	TokenGenerateService tokenGenerateService;
	
	@Test
	public void 토큰생성_테스트() {
		String roomId = "ABC";
		String generatedString = "";
		
		try {
			generatedString = tokenGenerateService.generateToken(roomId);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(generatedString);
		assertNotNull(generatedString);
	}
	
	@Test
	public void 뿌리기돈_분배_테스트() {
		//given
		long totalMoney = 6000;
		
		//when
		long[] moneys = moneySplitService.splitMoney(3, totalMoney);
		
		long calMoney = 0;
		for(long money : moneys) {
//			System.out.println(money);
			calMoney += money;
		}
		
		//then
		assertEquals(totalMoney, calMoney);
	}
}
