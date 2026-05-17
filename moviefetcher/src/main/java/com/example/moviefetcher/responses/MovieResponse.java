package com.example.moviefetcher.responses;

import java.util.List;

import com.example.moviefetcher.data.Movie;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResponse {

    private int page;
    private List<Movie> results;
    
}


