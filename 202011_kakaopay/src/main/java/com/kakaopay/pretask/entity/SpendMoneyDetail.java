package com.kakaopay.pretask.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@IdClass(SpendKey.class)
@Table(name="SPEND_MONEY_DETAIL")
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SpendMoneyDetail {
	@Id
	private String token;

	@Id
	private long spendId;
	
	@Column(name="ONE_TIME_MONEY")
	private long onetimeMoney;
	
	@Column(name="RECEIVED_YN")
	private String receivedYn;
	
	@Column(name="RECEIVED_USER_ID")
	private int receivedUserId;
}