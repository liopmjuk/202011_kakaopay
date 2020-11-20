package com.kakaopay.pretask.service;

import org.springframework.stereotype.Service;

@Service
public class MoneySplitService {
	
	public long[] splitMoney(int peopleNum, long seedMoney) {
		long[] moneys = new long[peopleNum];
		
		long halfSeed = (long) seedMoney / 2;
		
		//init
		for(int i=0; i < peopleNum; i++) {
			moneys[i] = 1;
		}
		
		seedMoney -= peopleNum;
		
		for(int i=0; i < peopleNum; i++) {
			
			//마지막 인원 수이거나 쪼갤 금액이 1이하면 쪼개기 멈춤
			if(i == peopleNum-1 || halfSeed <= 1) {
				moneys[i] += seedMoney;
				break;
			}
			
			//뿌리기 금액 세팅
			long money = halfSeed + (long) (halfSeed * Math.random() * 0.5);
			
			moneys[i] += money;
			
			seedMoney -= money;
			halfSeed = (long) seedMoney / 2;
		}
		
		return moneys;
	}
}
