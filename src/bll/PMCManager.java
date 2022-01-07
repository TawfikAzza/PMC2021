package bll;

import be.CategoryMovie;
import be.Movie;
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
    public List<Movie> getAllMovies() throws SQLException {
        return dalController.getAllMovies();
    }

    @Override
    public List<CategoryMovie> getAllCategories() throws SQLException {
        return dalController.getAllCategories();
    }

    @Override
    public Movie createMovie(Movie movie) throws SQLException {
        return dalController.createMovie(movie);
    }

    @Override
    public void addCategoryFromMovie(Movie movie) throws SQLException {
        dalController.addCategoryFromMovie(movie);
    }

    @Override
    public List<CategoryMovie> getCategoryFromMovie(Movie movie) throws SQLException {
        return dalController.getCategoryFromMovie(movie);
    }

    @Override
    public void updateMovie(Movie movie) throws SQLException {
        dalController.updateMovie(movie);
    }

    @Override
    public CategoryMovie addNewCategory(CategoryMovie categoryMovie) throws SQLException {
        return dalController.addCategory(categoryMovie);
    }

    @Override
    public void deleteMovie(Movie selectedItem) throws SQLException {
        dalController.deleteMovie(selectedItem);
    }

    @Override
    public void updateCategory(CategoryMovie category) throws SQLException {
        dalController.updateCategory(category);
    }

    @Override
    public void deleteCategory(CategoryMovie category) throws SQLException {
        dalController.deleteCategory(category);
    }

    @Override
    public void playMovie(Movie movie) throws IOException {
        File file = new File(movie.getFileLink().toString());
        Desktop.getDesktop().open(file);
    }
}
