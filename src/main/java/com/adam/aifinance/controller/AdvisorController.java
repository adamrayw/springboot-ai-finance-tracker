package com.adam.aifinance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.adam.aifinance.model.Transaction;
import com.adam.aifinance.repository.TransactionRepository;
import com.adam.aifinance.service.AdvisorService;
import com.adam.aifinance.service.GeminiService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/advisor")
public class AdvisorController {

    private final TransactionRepository repository;
    private final AdvisorService advisorService;
    private final GeminiService geminiService;

    public AdvisorController(TransactionRepository repository, AdvisorService advisorService, GeminiService geminiService) {
        this.repository = repository;
        this.advisorService = advisorService;
        this.geminiService = geminiService;
    }

    @GetMapping
    public ResponseEntity<String> getAdvice(@RequestParam(name = "param") String param) {
        List<Transaction> transactions = repository.findAll();
        String advice = advisorService.getAdvice(transactions);
        return ResponseEntity.ok(advice);
    }

    @GetMapping("/ai")
    public Mono<ResponseEntity<String>> getAiAdvice() {
        List<Transaction> txs = repository.findAll();
        return geminiService.askGemini(txs)
                .map(ResponseEntity::ok);
    }

}