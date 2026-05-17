package com.example.moviefetcher.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
//import lombok.Value;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class RecommendationService {

    private final RestClient restClient;

    public RecommendationService(RestClient.Builder builder, 
        @Value("${ml.service.url}") String mlServiceUrl) {
            System.out.println(">>> ML Service URL: " + mlServiceUrl);
            this.restClient = builder
                .baseUrl(mlServiceUrl)
                .build();
    }

    public List<String> recommend(String query) {
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("top_n", 10);

        String recommends = restClient.post()
            .uri("/recommend")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Map.of("query", query, "top_n", 10))
            .retrieve()
            .body(String.class);
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(recommends);
        JsonNode results = root.path("results");
        
        List<String> titles = new ArrayList<>();
        for(JsonNode item : results){
            titles.add(item.path("title").asText());
        }
        return titles;
    }
}
