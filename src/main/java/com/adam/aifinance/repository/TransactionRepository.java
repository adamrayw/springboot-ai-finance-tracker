package com.adam.aifinance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adam.aifinance.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository <Transaction, Long> {}