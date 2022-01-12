package gui.Model;

import be.Movie;
import be.User;
import bll.PMCFacade;
import bll.PMCManager;
import bll.exceptions.MovieException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;

public class MovieModel {
    PMCFacade pmcFacade;
    public MovieModel() throws IOException {
        pmcFacade = new PMCManager();
    }

    public ObservableList<Movie> getAllMovies(User user) throws MovieException {
        ObservableList<Movie> allObsMovies = FXCollections.observableArrayList();
        allObsMovies.addAll(pmcFacade.getAllMovies(user));
        return allObsMovies;
    }

    public Movie createMovie(Movie movie) throws MovieException {
        return pmcFacade.createMovie(movie);

    }

    public void updateMovie(Movie movie) throws MovieException {
        pmcFacade.updateMovie(movie);
    }

    public void deleteMovie(Movie selectedItem) throws MovieException {
        pmcFacade.deleteMovie(selectedItem);
    }
    public void playMovie(Movie movie) throws IOException {
        pmcFacade.playMovie(movie);
    }
}
