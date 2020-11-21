package com.kakaopay.pretask.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakaopay.pretask.entity.SpendInfo;
import com.kakaopay.pretask.exception.SpendErrorCode;
import com.kakaopay.pretask.exception.SpendException;
import com.kakaopay.pretask.repository.SpendRepository;

@Service
public class SpendInfoDao {
	@Autowired
	SpendRepository spendRepositoy;
	
	public SpendInfo findByToken(String token) {
		Optional<SpendInfo> spendInfo = spendRepositoy.findById(token);
		spendInfo.orElseThrow(() -> new SpendException(SpendErrorCode.NOT_ALLOWED_TOKEN));
		return spendInfo.get();
	}
	
	public void save(SpendInfo spendInfo) {
		spendRepositoy.save(spendInfo);
	}
}
