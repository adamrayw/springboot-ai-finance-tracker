package com.adam.aifinance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.adam.aifinance.model.Transaction;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final ObjectMapper mapper = new ObjectMapper();

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent")
            .build();

    public Mono<String> askGemini(List<Transaction> transactions) {
        String prompt = buildPrompt(transactions);

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
                .header("Content-Type", "application/json")
                .bodyValue("""
                            {
                              "contents": [{
                                "parts": [{
                                  "text": "%s"
                                }]
                              }]
                            }
                        """.formatted(prompt))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> extractTextFromResponse(response));
    }

    private String buildPrompt(List<Transaction> txs) {
        StringBuilder sb = new StringBuilder("Berikan saran keuangan secara singkat berdasarkan transaksi berikut:\n");
        for (Transaction tx : txs) {
            sb.append("- ")
                    .append(tx.getDate()).append(" | ")
                    .append(tx.getDescription()).append(" | ")
                    .append(tx.getType()).append(" | ")
                    .append(tx.getAmount()).append("\n");
        }
        return sb.toString();
    }

    private String extractTextFromResponse(String response) {
        try {
            JsonNode root = mapper.readTree(response);
            return root
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Gagal memproses respon dari Gemini.";
        }
    }

}
