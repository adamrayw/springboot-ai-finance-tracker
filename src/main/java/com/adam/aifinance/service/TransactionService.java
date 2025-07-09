package com.adam.aifinance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.adam.aifinance.model.Transaction;
import com.adam.aifinance.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;

    public List<Transaction> getAll() {
        return repository.findAll();
    }

    public Transaction save(Transaction tx) {
        return repository.save(tx);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<Transaction> findById(Long id) {
        return repository.findById(id);
    }

    public Transaction update(Long id, Transaction newTx) {
        Transaction tx = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not Found"));

        tx.setDescription(newTx.getDescription());
        tx.setAmount(newTx.getAmount());
        tx.setType(newTx.getType());
        tx.setDate(newTx.getDate());

        return repository.save(tx);
    }
}
