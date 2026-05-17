package com.example.moviefetcher.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.moviefetcher.data.Movie;
import com.example.moviefetcher.data.MovieDetail;
import com.example.moviefetcher.data.MovieDiscover;
import com.example.moviefetcher.responses.DiscoverResponse;
import com.example.moviefetcher.responses.MovieResponse;
import com.example.moviefetcher.service.MovieService;

//Non fa uso dell'interfaccia, restituisce dati grezzi

@RestController
@RequestMapping("/movies")
public class MovieController {
        
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
    //RequestParam
    @GetMapping("/s/{title}")
    public List<Movie> searchMovies(@PathVariable String title) {
        MovieResponse response = movieService.getMovie(title);
        return response.getResults();
    }

    @GetMapping("/{id}")
    public String getMovie(@PathVariable int id) {
        MovieDetail response = movieService.getSpecId(id);
         String result = String.format("""
                                \n
                                ----------------------------
                                Title: %s (%s [%s])\n
                                Score: %s\n
                                Budget: %s\n
                                ----------------------------
                                \n
                                """,
                                response.getTitle(),
                                response.getStatus(),
                                response.getReleaseDate(),
                                String.valueOf(response.getVoteAverage()),
                                String.valueOf(response.getBudget())
                            );
        return result;
    }

    @GetMapping("/discover")
    public List<MovieDiscover> discover() {
        DiscoverResponse response = movieService.discover();
        return response.getResults();
    }

}
