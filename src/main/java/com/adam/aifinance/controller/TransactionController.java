package com.adam.aifinance.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

import com.adam.aifinance.service.TransactionService;

import jakarta.persistence.EntityNotFoundException;

import com.adam.aifinance.model.Transaction;
import com.adam.aifinance.repository.TransactionRepository;

import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;
    private final TransactionRepository repository;

    @GetMapping
    public List<Transaction> getAll() {
        return service.getAll();
    }

    @GetMapping("/recent")
    public List<Transaction> getRecentTransactions() {
        return service.getRecentTransactions();
    }

    @PostMapping
    public Transaction create(@RequestBody Transaction tx) {
        return service.save(tx);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Transaction> delete(@PathVariable("id") Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Transaction not found with id: " + id);
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> update(
            @PathVariable("id") Long id,
            @RequestBody Transaction tx) {
        try {
            Transaction updated = service.update(id, tx);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
