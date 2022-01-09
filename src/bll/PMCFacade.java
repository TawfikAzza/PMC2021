package bll;

import be.CategoryMovie;
import be.Movie;
import bll.exceptions.MovieException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface PMCFacade {
    List<Movie> getAllMovies() throws MovieException;
    List<CategoryMovie> getAllCategories() throws SQLException;

    Movie createMovie(Movie movie) throws  MovieException;

    void addCategoryFromMovie(Movie movie) throws SQLException;

    public List<CategoryMovie> getCategoryFromMovie(Movie movie) throws SQLException;

    void updateMovie(Movie movie) throws  MovieException;

    CategoryMovie addNewCategory(CategoryMovie categoryMovie) throws SQLException;

    void deleteMovie(Movie selectedItem) throws MovieException;

    void updateCategory(CategoryMovie category) throws SQLException;

    void deleteCategory(CategoryMovie category) throws SQLException;

    void playMovie(Movie movie) throws IOException;
}
