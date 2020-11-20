package com.kakaopay.pretask;

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
public class PretaskUnitTest {
	
	@Autowired
	MoneySplitService moneySplitService;
	
	@Autowired
	TokenGenerateService tokenGenerateService;
	
	@Test
	public void generateTokenTest() {
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
	public void moneySplitTest() {
		//given
		long totalMoney = 5000;
		
		//when
		long[] moneys = moneySplitService.splitMoney(1, totalMoney);
		
		long calMoney = 0;
		for(long money : moneys) {
			calMoney += money;
		}
		
		//then
		assertEquals(totalMoney, calMoney);
	}
}
