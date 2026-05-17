package com.example.moviefetcher.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDetail {

    private String title;
    private String overview;
    private String tagline;
    private String status;
    private String homepage;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("original_title")
    private String originalTitle;

    @JsonProperty("original_language")
    private String originalLanguage;

    @JsonProperty("vote_average")
    private double voteAverage;

    @JsonProperty("vote_count")
    private int voteCount;

    private double popularity;
    private int budget;
    private int revenue;
    private int runtime;

    @JsonProperty("imdb_id")
    private String imdbId;

    private List<Genre> genres;

    @JsonProperty("production_companies")
    private List<ProductionCompany> productionCompanies;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Genre {
        private int id;
        private String name;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductionCompany {
        private int id;
        private String name;

        @JsonProperty("origin_country")
        private String originCountry;
    }
}
