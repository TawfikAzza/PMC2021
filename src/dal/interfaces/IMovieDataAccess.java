package dal.interfaces;

import be.CategoryMovie;
import be.Movie;
import bll.exceptions.MovieException;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.SQLException;
import java.util.List;

public interface IMovieDataAccess {
    public List<Movie> getAllMovies() throws SQLException;
    public Movie createMovie(Movie movie) throws SQLException, MovieException;
    public void updateMovie(Movie movie) throws SQLException, MovieException;
    public void deleteMovie(Movie movie) throws SQLException;
    List<Movie>getAllMoviesCategory(CategoryMovie categoryMovie)throws SQLException;

    List<Movie> getAllOutdatedMovies() throws SQLException;
}
