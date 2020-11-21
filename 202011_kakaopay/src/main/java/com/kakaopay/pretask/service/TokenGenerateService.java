package com.kakaopay.pretask.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class TokenGenerateService {
	
	//SHA-256 해시문자열
	//만드는 현재시간 문자열과 대화방 아이디, salt 를 조합해서 생성
	public String generateToken(String roomId) throws NoSuchAlgorithmException {
		final String salt = "KAKAOPAY_SPEND";
		LocalDateTime nowTime = LocalDateTime.now();
		
		String convertMsg = roomId + nowTime;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(salt.getBytes());
		md.update(convertMsg.getBytes());
		
		return byteToHex(md.digest()).substring(0,3).toUpperCase();
	}
	
	public String byteToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		
		for (byte b: bytes) {
			builder.append(String.format("%02x", b));
		}
		
		return builder.toString();
	}
	
}
