package com.example.moviefetcher.commands;

import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;
import org.springframework.stereotype.Component;

import com.example.moviefetcher.data.Movie;
import com.example.moviefetcher.data.MovieDetail;
import com.example.moviefetcher.data.MovieDiscover;
import com.example.moviefetcher.helper.LogHelper;
import com.example.moviefetcher.helper.MovieHelper;
import com.example.moviefetcher.responses.DiscoverResponse;
import com.example.moviefetcher.responses.MovieResponse;
import com.example.moviefetcher.service.MovieService;

import java.util.List;

@Component
public class MovieCommands {

    private MovieService movieService;
    private MovieHelper parser;

    public MovieCommands(MovieService movieService){
        this.movieService = movieService;
        parser = new MovieHelper();
    }

    @Command(name = "find-movie", description = "Cerca un film")
    public String findMovies(@Option(required = true, description = "Movie's title") String title){
        try{
            LogHelper.getInstance().writeLog("Movie searcherd: "+title);
            MovieResponse movieRes = movieService.getMovie(title);
            List<Movie> results = movieRes.getResults();

            return parser.parseTheMovies(results);

        } catch (Exception e) {
            return "Errore nella ricerca: " + e.getMessage();
        }
    }

    @Command(name = "find-spec", description = "Cerca un film specifico")
    public String findSpecific(@Option(required = true, description = "Specific movie using id") int id){
        try{
            LogHelper.getInstance().writeLog("Movie id: "+id);
            MovieDetail movieRes = movieService.getSpecId(id);

            return parser.parseTheSpecific(movieRes);

        } catch (Exception e) {
            return "Errore nella ricerca: " + e.getMessage();
        }
    }

    @Command(name = "discover", description = "Film in uscita prossimamente")
    public String discoverMovie(){
        try{
            LogHelper.getInstance().writeLog("Discover");
            DiscoverResponse movieRes = movieService.discover();
            List<MovieDiscover> discover = movieRes.getResults();

            return parser.parseDiscover(discover);

        } catch (Exception e) {
            return "Errore nella ricerca: " + e.getMessage();
        }
    }
    
}
