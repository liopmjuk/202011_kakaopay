package com.kakaopay.pretask.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kakaopay.pretask.entity.SpendKey;
import com.kakaopay.pretask.entity.SpendMoneyDetail;

public interface SpendDetailRepository extends JpaRepository<SpendMoneyDetail, SpendKey> {

	public static final String FIND_BY_TOKEN = "select * from spend_money_detail d where d.token = :token";
	public static final String COUNT_BY_TOKEN_AND_USERID = "select count(*) from spend_money_detail d where d.token = :token and d.received_user_id = :receivedUserId";
	
	@Query(value = FIND_BY_TOKEN, nativeQuery = true)
	public List<SpendMoneyDetail> findByToken(@Param("token") String token);

	@Query(value = COUNT_BY_TOKEN_AND_USERID, nativeQuery = true)
	public int countUserReceived(@Param("token") String token, @Param("receivedUserId") int receivedUserId);
}
