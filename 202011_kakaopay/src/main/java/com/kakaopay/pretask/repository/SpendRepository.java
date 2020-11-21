package com.kakaopay.pretask.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakaopay.pretask.entity.SpendInfo;

public interface SpendRepository extends JpaRepository<SpendInfo, String> {
}
