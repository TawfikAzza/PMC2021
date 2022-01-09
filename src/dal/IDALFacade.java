package dal;

import be.CategoryMovie;
import be.Movie;
import bll.exceptions.MovieException;

import java.sql.SQLException;
import java.util.List;

public interface IDALFacade {
    /**
     * Movie Data Access Section
     *
     * */

    public List<Movie> getAllMovies() throws MovieException;
    public Movie createMovie(Movie movie) throws  MovieException;
    public void updateMovie(Movie movie) throws  MovieException;
    public void deleteMovie(Movie movie) throws MovieException;

    public List<CategoryMovie> getAllCategories() throws SQLException;
    public CategoryMovie getCategory(int idCategory) throws SQLException;
    public List<CategoryMovie> getCategoryFromMovie(Movie movie) throws SQLException;
    public void addCategoryFromMovie(Movie movie) throws SQLException;
    public CategoryMovie addCategory(CategoryMovie categoryMovie) throws SQLException;
    public void removeCategory(CategoryMovie categoryMovie) throws SQLException;
    public void updateCategory(CategoryMovie categoryMovie) throws SQLException;
    public void deleteCategory(CategoryMovie category) throws SQLException;
}
