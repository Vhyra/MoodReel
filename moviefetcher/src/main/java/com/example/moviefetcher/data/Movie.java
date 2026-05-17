package com.example.moviefetcher.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    private String title;
    private String overview;
    private String release_date;
    private int id;


}
