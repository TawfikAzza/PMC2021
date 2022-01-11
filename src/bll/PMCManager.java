package bll;

import be.CategoryMovie;
import be.Movie;
import be.User;
import bll.exceptions.CategoryException;
import bll.exceptions.MovieException;
import bll.utils.VideoPlayer;
import dal.DALController;
import gui.Model.CategoryModel;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PMCManager implements PMCFacade{
    DALController dalController;
    VideoPlayer videoPlayer;
    public PMCManager() throws IOException {
        videoPlayer = VideoPlayer.getInstance();
        dalController = new DALController();
    }

    @Override
    public List<Movie> getAllMovies(User user) throws MovieException {
        return dalController.getAllMovies(user);
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
    public void playMovie(Movie movie) throws IOException {
        File file = new File(movie.getFileLink().toString());
        Desktop.getDesktop().open(file);
    }

    @Override
    public List<Movie> getAllOutdatedMovies() throws SQLException{
        return dalController.getAllOutdatedMovies();
    }
}
