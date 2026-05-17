package com.example.moviefetcher.responses;

import java.util.List;

import com.example.moviefetcher.data.MovieDiscover;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscoverResponse {

    private int page;
    private List<MovieDiscover> results;
    
}
