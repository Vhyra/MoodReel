package com.example.moviefetcher.controller;

import com.example.moviefetcher.data.Movie;
import com.example.moviefetcher.data.MovieDetail;
import com.example.moviefetcher.data.MovieDiscover;
import com.example.moviefetcher.helper.LogHelper;
import com.example.moviefetcher.responses.DiscoverResponse;
import com.example.moviefetcher.service.MovieService;
import com.example.moviefetcher.service.RecommendationService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MovieWebController {

    private final MovieService movieService;
    private final RecommendationService recService;

    public MovieWebController(MovieService movieService, RecommendationService recService) {
        this.movieService = movieService;
        this.recService = recService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("soon", movieService.soon().getResults());
        model.addAttribute("nowPlaying", movieService.discover().getResults());
        return "index";
    }

    @GetMapping("/feeling")
    public String feeling() {
        return "feeling";
    }

    @PostMapping("/recommend")
    public String searchRecommend(@RequestParam String text, Model model){
        List<String> recList = recService.recommend(text);
        List<Movie> allMovies = new ArrayList<>();

        for(String title: recList){
            LogHelper.getInstance().writeLog("Titolo ricevuto: " + title);
            List<Movie> movies = movieService.getMovie(title).getResults();
            if(!movies.isEmpty()){
                allMovies.add(movies.get(0));
            }
            
        }
        model.addAttribute("movies", allMovies);
        model.addAttribute("query", text);
        return "results";
    }

    @GetMapping("/search")
    public String searchMovies(@RequestParam String title, Model model) {
        List<Movie> movies = movieService.getMovie(title).getResults();
        model.addAttribute("movies", movies);
        model.addAttribute("query", title);
        return "results";
    }

    @GetMapping("/movie/{id}")
    public String getMovie(@PathVariable int id, Model model) {
        MovieDetail movie = movieService.getSpecId(id);
        model.addAttribute("movie", movie);
        return "detail";
    }
        
    @GetMapping("/discover")
    public String discover(Model model) {
        DiscoverResponse response = movieService.discover();
        model.addAttribute("movies", response.getResults());
        model.addAttribute("query", "NOW PLAYING");
        return "results";
    }

    @GetMapping("/soon")
    public String soon(Model model) {
        DiscoverResponse response = movieService.soon();
        model.addAttribute("movies", response.getResults());
        model.addAttribute("query", "NOW PLAYING");
        return "results";
    }
}