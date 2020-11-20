package com.kakaopay.pretask.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class SpendKey implements Serializable {
	@Id
	@Column(name="TOKEN")
	private String token;
	
	@Id
	@Column(name="SPEND_ID")
	private long spendId;
}
