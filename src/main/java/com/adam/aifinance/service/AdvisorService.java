package com.adam.aifinance.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.adam.aifinance.model.Transaction;
import com.adam.aifinance.repository.TransactionRepository;

import reactor.core.publisher.Mono;

@Service
public class AdvisorService {

    private final GeminiService geminiService;
    private final TransactionRepository repository;

    // ✅ Gunakan constructor injection agar Spring bisa menyediakan dependency-nya
    public AdvisorService(GeminiService geminiService, TransactionRepository repository) {
        this.geminiService = geminiService;
        this.repository = repository;
    }

    public String getAdvice(List<Transaction> transactions) {
        double income = transactions.stream()
                .filter(t -> t.getType().equals("INCOME"))
                .mapToDouble(t -> t.getAmount().doubleValue()).sum();

        double expense = transactions.stream()
                .filter(t -> t.getType().equals("EXPENSE"))
                .mapToDouble(t -> t.getAmount().doubleValue()).sum();

        double ratio = income == 0 ? 0 : (expense / income);

        if (ratio > 0.8) {
            return "Pengeluaran kamu melebihi 80% dari pemasukan. Coba kurangi pengeluaran rutin.";
        } else if (ratio > 0.5) {
            return "Pengeluaran kamu cukup besar. Pertimbangkan menabung lebih banyak.";
        } else {
            return "Keuanganmu sehat. Tetap pertahankan pengeluaran di bawah 50% dari pemasukan.";
        }
    }

    public Mono<ResponseEntity<String>> getAiAdvice() {
        List<Transaction> txs = repository.findAll();
        return geminiService.askGemini(txs)
                .map(ResponseEntity::ok);
    }
}