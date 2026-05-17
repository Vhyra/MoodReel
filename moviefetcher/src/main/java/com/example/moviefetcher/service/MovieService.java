package com.example.moviefetcher.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.example.moviefetcher.data.MovieDetail;
import com.example.moviefetcher.responses.DiscoverResponse;
import com.example.moviefetcher.responses.MovieResponse;

import org.springframework.beans.factory.annotation.Value;

@Service
public class MovieService {

    private final RestClient restClient;

    public MovieService(RestClient.Builder builder,
        @Value("${api.external.url}") String baseUrl,
        @Value("${api.external.auth_token}") String auth_token) {

        this.restClient = builder
            .baseUrl(baseUrl)
            .defaultHeader("Authorization", "Bearer " + auth_token)
            .defaultHeader("Accept", "application/json")
            .build();
    }

    public MovieResponse getMovie(String title){
        return restClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/search/movie")
            .queryParam("query", title)
            .queryParam("language","it-IT")
            .build()
        )
        .retrieve()
        .body(MovieResponse.class);
    }

    public MovieDetail getSpecId(int id){
        return restClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/movie/{id}")
            .queryParam("language","it-IT")
            .build(id)
        )
        .retrieve()
        .body(MovieDetail.class);
    }

    public DiscoverResponse discover(){
        return restClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/movie/now_playing")
            .queryParam("language","it-IT")
            .build()
        )
        .retrieve()
        .body(DiscoverResponse.class);
    }

    public DiscoverResponse soon(){
        return restClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/movie/upcoming")
            .queryParam("language","it-IT")
            .build()
        )
        .retrieve()
        .body(DiscoverResponse.class);
    }

}