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

    public List<Movie> getAllMovies() throws MovieException;
    public Movie createMovie(Movie movie) throws  MovieException;
    public void updateMovie(Movie movie) throws  MovieException;
    public void deleteMovie(Movie movie) throws MovieException;

    public List<CategoryMovie> getAllCategories() throws CategoryException;
    public CategoryMovie getCategory(int idCategory) throws CategoryException;
    public List<CategoryMovie> getCategoryFromMovie(Movie movie) throws CategoryException;
    public void addCategoryFromMovie(Movie movie) throws CategoryException;
    public CategoryMovie addCategory(CategoryMovie categoryMovie) throws CategoryException;
    public void removeCategory(CategoryMovie categoryMovie) throws CategoryException;
    public void updateCategory(CategoryMovie categoryMovie) throws CategoryException;
    public void deleteCategory(CategoryMovie category) throws CategoryException;
}
