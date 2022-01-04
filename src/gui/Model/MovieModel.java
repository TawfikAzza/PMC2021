package gui.Model;

import be.Movie;
import bll.PMCFacade;
import bll.PMCManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;

public class MovieModel {
    PMCFacade pmcFacade;
    public MovieModel() throws IOException {
        pmcFacade = new PMCManager();
    }

    public ObservableList<Movie> getAllMovies() throws SQLException {
        ObservableList<Movie> allObsMovies = FXCollections.observableArrayList();
        allObsMovies.addAll(pmcFacade.getAllMovies());
        return allObsMovies;
    }

    public Movie createMovie(Movie movie) throws SQLException {
        return pmcFacade.createMovie(movie);

    }

    public void updateMovie(Movie movie) throws SQLException {
        pmcFacade.updateMovie(movie);
    }
}
