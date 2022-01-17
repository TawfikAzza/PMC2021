package bll;

import be.CategoryMovie;
import be.Movie;
import be.Stats;
import bll.exceptions.CategoryException;
import bll.exceptions.MovieException;
import bll.utils.VideoPlayer;
import dal.DALController;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class PMCManager implements PMCFacade{
    DALController dalController;
    VideoPlayer videoPlayer;
    public PMCManager() throws IOException {
        videoPlayer = VideoPlayer.getInstance();
        dalController = new DALController();
    }

    @Override
    public List<Movie> getAllMovies() throws MovieException {
        return dalController.getAllMovies();
    }

    @Override
    public List<CategoryMovie> getAllCategories() throws CategoryException {
        return dalController.getAllCategories();
    }

    @Override
    public Movie createMovie(Movie movie) throws  MovieException {
        return dalController.createMovie(movie);
    }

    @Override
    public void addCategoryFromMovie(Movie movie) throws CategoryException {
        dalController.addCategoryFromMovie(movie);
    }

    @Override
    public List<CategoryMovie> getCategoryFromMovie(Movie movie) throws CategoryException {
        return dalController.getCategoryFromMovie(movie);
    }

    @Override
    public void updateMovie(Movie movie) throws MovieException {
        dalController.updateMovie(movie);
    }

    @Override
    public CategoryMovie addNewCategory(CategoryMovie categoryMovie) throws CategoryException {
        return dalController.addCategory(categoryMovie);
    }

    @Override
    public void deleteMovie(Movie selectedItem) throws MovieException {
        dalController.deleteMovie(selectedItem);
    }

    @Override
    public void updateCategory(CategoryMovie category) throws CategoryException {
        dalController.updateCategory(category);
    }

    @Override
    public void deleteCategory(CategoryMovie category) throws CategoryException {
        dalController.deleteCategory(category);
    }

    @Override
    public void playMovie(Movie movie) throws IOException, SQLException {
        File file = new File(movie.getFileLink().toString());
        Desktop.getDesktop().open(file);

    }

    @Override
    public List<Movie> getAllOutdatedMovies() throws SQLException{
        return dalController.getAllOutdatedMovies();
    }

    @Override
    public List<Movie> allMoviesCategory(CategoryMovie categoryMovie) throws SQLException {
        return dalController.getAllMoviesCategory(categoryMovie);
    }

    @Override
    public void updateLastView(Movie movie) throws SQLException {
        dalController.updateLastView(movie);
    }

    @Override
    public void newTime(int movies, long seconds) throws SQLException {
        dalController.newTime(movies,seconds);
    }

    @Override
    public List<Stats> getAllStats(LocalDate firstDate, LocalDate secondDate) throws SQLException {
        return dalController.getAllStats(firstDate,secondDate);
    }

    @Override
    public void updateTime(long seconds) throws SQLException {
    dalController.updateTime(seconds);
    }

    @Override
    public void updateMovies() throws SQLException {
    dalController.updateMovies();
    }

    @Override
    public Date getFirstDate() throws SQLException {
        return dalController.getFirstDate();
    }

    @Override
    public void removeCategory(CategoryMovie categoryMovie) throws CategoryException {
        dalController.removeCategory(categoryMovie);
    }
}
