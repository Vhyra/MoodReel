package com.example.moviefetcher.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDiscover {

    private String title;
    private String release_date;
    private String overview;
    private int id;
    
}
