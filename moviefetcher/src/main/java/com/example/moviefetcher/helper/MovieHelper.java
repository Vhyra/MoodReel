package com.example.moviefetcher.helper;

import java.util.List;

import com.example.moviefetcher.data.Movie;
import com.example.moviefetcher.data.MovieDetail;
import com.example.moviefetcher.data.MovieDiscover;

public class MovieHelper {

    public MovieHelper(){}

    public String parseTheMovies(List<Movie> response){

        String result = "";

        for(Movie movie: response){
            String title = movie.getTitle();
            String releaseDate = movie.getRelease_date();
            int id = movie.getId();
            result += String.format("[%d] |-| %s - [%s]\n", id, title, releaseDate);
        }

        return result;

    }

    public String parseTheSpecific(MovieDetail movie){
        
        String result = String.format("""
                                \n
                                ----------------------------
                                Title: %s (%s [%s])
                                Score: %s
                                Budget: %s
                                ----------------------------
                                \n
                                """,
                                movie.getTitle(),
                                movie.getStatus(),
                                movie.getReleaseDate(),
                                String.valueOf(movie.getVoteAverage()),
                                String.valueOf(movie.getBudget())
                            );
        return result;
    }

    public String parseDiscover(List<MovieDiscover> movies){
        
        String result = "";

        for(MovieDiscover movie: movies){
            result += String.format("""
                                ----------------------------
                                Title: %s ([%s])
                                Overview: 
                                %s
                                ----------------------------
                                \n\n
                                """,
                                movie.getTitle(),
                                movie.getRelease_date(),
                                movie.getOverview()
                            );
        }
        
        
        return result;
    }
    
}
