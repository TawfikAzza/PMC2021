package gui.Model;

import be.CategoryMovie;
import be.Movie;
import bll.PMCFacade;
import bll.PMCManager;
import bll.exceptions.CategoryException;
import bll.exceptions.MovieException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;

public class MovieModel {
    PMCFacade pmcFacade;
    ObservableList<Movie> allObsMovies ;

    public MovieModel() throws IOException {
        pmcFacade = new PMCManager();
    }

    public ObservableList<Movie> getAllMovies() throws MovieException {
        allObsMovies=FXCollections.observableArrayList();
        allObsMovies.addAll(pmcFacade.getAllMovies());
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

    public ObservableList<CategoryMovie> getAllCategories() throws CategoryException {
        ObservableList<CategoryMovie> allCategories = FXCollections.observableArrayList();
        allCategories.addAll(pmcFacade.getAllCategories());
        return allCategories;
    }

    public ObservableList<Movie> allMoviesCategory(CategoryMovie science_fiction) throws SQLException {
        ObservableList<Movie> allMoviesCategory = FXCollections.observableArrayList();
        allMoviesCategory.addAll( pmcFacade.allMoviesCategory(science_fiction));
        return allMoviesCategory;
    }

    public void updateLastView(Movie selectedItem) throws SQLException {
        pmcFacade.updateLastView(selectedItem);
    }
}
