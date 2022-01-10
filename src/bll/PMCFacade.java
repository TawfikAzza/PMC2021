package bll;

import be.CategoryMovie;
import be.Movie;
import bll.exceptions.CategoryException;
import bll.exceptions.MovieException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface PMCFacade {
    List<Movie> getAllMovies() throws MovieException;
    List<CategoryMovie> getAllCategories() throws CategoryException;

    Movie createMovie(Movie movie) throws  MovieException;

    void addCategoryFromMovie(Movie movie) throws CategoryException;

    public List<CategoryMovie> getCategoryFromMovie(Movie movie) throws CategoryException;

    void updateMovie(Movie movie) throws  MovieException;

    CategoryMovie addNewCategory(CategoryMovie categoryMovie) throws CategoryException;

    void deleteMovie(Movie selectedItem) throws MovieException;

    void updateCategory(CategoryMovie category) throws CategoryException;

    void deleteCategory(CategoryMovie category) throws CategoryException;

    void playMovie(Movie movie) throws IOException;

    List <Movie> getAllOutdatedMovies()throws SQLException;
}
