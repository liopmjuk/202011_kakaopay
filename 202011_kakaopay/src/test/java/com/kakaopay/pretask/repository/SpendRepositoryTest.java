package com.kakaopay.pretask.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.kakaopay.pretask.entity.SpendInfo;
import com.kakaopay.pretask.entity.SpendMoneyDetail;
import com.kakaopay.pretask.repository.SpendDetailRepository;
import com.kakaopay.pretask.repository.SpendRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SpendRepositoryTest {
	@Autowired
	SpendRepository spendRepository;
	
	@Autowired
	SpendDetailRepository spendDetailRepository;

	@Test
	public void 뿌리기내역조회() {
		final Optional<SpendInfo> spendInfo = spendRepository.findById("AAA");
		assertThat(spendInfo.get().getToken()).isEqualTo("AAA");
		assertThat(spendInfo.get().getSpendUserId()).isEqualTo(10);
	}
	
	@Test
	public void 뿌리기상세금액조회() {
		final List<SpendMoneyDetail> spendMoneyDetail = spendDetailRepository.findByToken("AAA");
		assertEquals(spendMoneyDetail.size(), 5);
		
		final int count = spendDetailRepository.countUserReceived("BBB", 21);
		assertEquals(count, 1);
	}
}
