package dal;

import be.CategoryMovie;
import be.Movie;
import bll.exceptions.CategoryException;
import bll.exceptions.MovieException;

import java.sql.SQLException;
import java.util.List;

public interface IDALFacade {
    /**
     * Movie Data Access Section
     *
     * */

     List<Movie> getAllMovies() throws MovieException;
     Movie createMovie(Movie movie) throws  MovieException;
     void updateMovie(Movie movie) throws  MovieException;
     void deleteMovie(Movie movie) throws MovieException;

     List<CategoryMovie> getAllCategories() throws CategoryException;
     CategoryMovie getCategory(int idCategory) throws CategoryException;
     List<CategoryMovie> getCategoryFromMovie(Movie movie) throws CategoryException;
     void addCategoryFromMovie(Movie movie) throws CategoryException;
     CategoryMovie addCategory(CategoryMovie categoryMovie) throws CategoryException;
     void removeCategory(CategoryMovie categoryMovie) throws CategoryException;
     void updateCategory(CategoryMovie categoryMovie) throws CategoryException;
     void deleteCategory(CategoryMovie category) throws CategoryException;
     List<Movie>getAllOutdatedMovies() throws SQLException;
     List<Movie>getAllMoviesCategory(CategoryMovie categoryMovie)throws SQLException;
    void updateLastView(Movie movie)throws SQLException;
}
