package com.kakaopay.pretask.service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakaopay.pretask.dto.MoneyResponse;
import com.kakaopay.pretask.dto.SpendInfoRequest;
import com.kakaopay.pretask.dto.SpendStatusResponse;
import com.kakaopay.pretask.dto.TokenRequest;
import com.kakaopay.pretask.dto.TokenResponse;
import com.kakaopay.pretask.entity.SpendInfo;
import com.kakaopay.pretask.entity.SpendMoneyDetail;
import com.kakaopay.pretask.exception.SpendErrorCode;
import com.kakaopay.pretask.exception.SpendException;
import com.kakaopay.pretask.repository.SpendDetailRepository;
import com.kakaopay.pretask.repository.SpendRepository;
import com.kakaopay.pretask.vo.ReceivedInfo;
import com.kakaopay.pretask.vo.UserInfo;

@Service
public class SpendService {

	@Autowired
	MoneySplitService moneySplitService;
	
	@Autowired
	TokenGenerateService tokenGenerateService;
	
	@Autowired
	SpendRepository spendRepository;
	
	@Autowired
	SpendDetailRepository spendDetailRepository;
	
	private static Random random = new Random();
	
	public TokenResponse spendMoney(SpendInfoRequest spendInfoRequest, UserInfo userInfo) throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		int peopleNum = spendInfoRequest.getPeopleNum();
		long spendMoney = spendInfoRequest.getSpendMoney();
		
		//분배
		long splitedMoneys[] = moneySplitService.splitMoney(peopleNum, spendMoney);
		
		//토큰 생성
		String token = tokenGenerateService.generateToken(userInfo.getRoomId());
		
		//저장
		List<SpendMoneyDetail> details = new ArrayList();
		LocalDateTime spendMoneyTime = LocalDateTime.now();
		
		SpendInfo spendInfo = SpendInfo.builder()
				.token(token)
				.spendUserId(userInfo.getUserId())
				.roomId(userInfo.getRoomId())
				.spendTime(spendMoneyTime)
				.totalMoney(spendInfoRequest.getSpendMoney())
				.allReceivedYn("N")
				.build();
		
		spendRepository.save(spendInfo);
		
		int spendId = 1;
		for(long onetimeMoney: splitedMoneys) {
			SpendMoneyDetail detail = SpendMoneyDetail.builder()
					.token(token)
					.spendId(spendId)
					.onetimeMoney(onetimeMoney)
					.receivedYn("N")
					.build();
			details.add(detail);
			spendId++;
		}
		
		spendDetailRepository.saveAll(details);
		
		//return
		TokenResponse response = TokenResponse.builder()
				.token(token)
				.build();
		
		return response;
	}

	public MoneyResponse receiveMoney(String token, UserInfo userInfo) {
		
		LocalDateTime nowTime = LocalDateTime.now();
		
		SpendInfo spendInfo = spendRepository.findById(token)
				.orElseThrow(() -> new SpendException(SpendErrorCode.NOT_ALLOWED_TOKEN));
		
			
		//자신이 뿌리기한 건은 자신이 받을 수 없음
		spendInfo.checkUserId(userInfo.getUserId());
		
		//뿌린 건은 10분 간만 유효
		if(spendInfo.isSpendExpired(nowTime)) {throw new SpendException(SpendErrorCode.SPEND_EXPIRED);};
		
		//뿌리기 당 한 사용자는 한번만 받음
		int receivedcnt = spendDetailRepository.countUserReceived(token, userInfo.getUserId());
		if(receivedcnt > 0) {
			throw new SpendException(SpendErrorCode.ALREADY_RECEIVED_USER);
		}
		
		List<SpendMoneyDetail> moneyDetails = spendDetailRepository.findByToken(token);
		int memberCnt = moneyDetails.size();

		int startIdx = random.nextInt(memberCnt);
		
		//뿌리기 건 중 누구에게도 할당되지 않은 분배건 하나를 호출한 사용자에게 할당하고, 그 금액을 응답값으로 내려줌
		SpendMoneyDetail distribution = distributeToUser(startIdx, moneyDetails);
		distribution.toBuilder()
					.receivedUserId(userInfo.getUserId())
					.receivedYn("Y")
					.build();
		
		spendDetailRepository.save(distribution);
		
		MoneyResponse response = MoneyResponse.builder()
				.money(distribution.getOnetimeMoney())
				.build();
		
		return response;
	}

	public SpendStatusResponse inquirySpendingStatus(String token, UserInfo userInfo) {
		
		LocalDateTime nowDate = LocalDateTime.now();
		
		//토큰 조회 - 뿌린 사람 자신, 방
		SpendInfo spendInfo = spendRepository.findById(token)
				.orElseThrow(() -> new SpendException(SpendErrorCode.NOT_ALLOWED_TOKEN));

		//다른 사람의 뿌리기 건 체크
		spendInfo.checkUserId(userInfo.getUserId());
		
		//뿌리기가 호출된 대화방과 동일한 대화방에 속한 사용자만이 받을 수 있음
		spendInfo.checkRoomId(userInfo.getRoomId());
				
		//7일 동안 조회 가능
		if(spendInfo.isInquiryExpired(nowDate)) {throw new SpendException(SpendErrorCode.INQUIRY_EXPIRED);}
		
		List<SpendMoneyDetail> moneyDetails = spendDetailRepository.findByToken(token);
		List<ReceivedInfo> receivedInfos = new ArrayList<>();
		
		//받기 완료된 정보 세팅
		setReceivedInfo(moneyDetails, receivedInfos);
		
		SpendStatusResponse response = SpendStatusResponse.builder()
				.spendTime(spendInfo.getSpendTime())
				.spendMoney(spendInfo.getTotalMoney())
				.receivedInfos(receivedInfos)
				.build();
		
		//받기 완료된 금액 계산
		response.calReceivedMoney();
		
		return response;
	}

	/**
	 * 랜덤하게 분배건을 할당하는 함수
	 */
	private SpendMoneyDetail distributeToUser(int startIdx, List<SpendMoneyDetail> spendMoneyDetails) {
		SpendMoneyDetail target = null;
		boolean findTarget = false;
		
		int index = startIdx;
		
		while (findTarget) {
			target = spendMoneyDetails.get(index);
			if("N".equals(target.getReceivedYn())) {
				findTarget = true;
			}
			
			index ++;
			if(index == spendMoneyDetails.size()) {
				index = 0;
			}
			
			if(index == startIdx) {
				throw new SpendException(SpendErrorCode.SPEND_FINISHED);
			}
		}
		
		return target;
	}
	
	private void setReceivedInfo(List<SpendMoneyDetail> details, List<ReceivedInfo> receivedInfos) {
		// TODO Auto-generated method stub
		
		for(SpendMoneyDetail detail : details) {
			if("Y".equals(detail.getReceivedYn())) {
				ReceivedInfo info = ReceivedInfo.builder()
						.money(detail.getOnetimeMoney())
						.userId(detail.getReceivedUserId())
						.build();
				
				receivedInfos.add(info);
			}
		}
	}
}
