package dal.interfaces;

import be.Movie;
import be.User;
import bll.exceptions.MovieException;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.SQLException;
import java.util.List;

public interface IMovieDataAccess {
    public List<Movie> getAllMovies(User user) throws SQLException;
    public Movie createMovie(Movie movie,User user) throws SQLException, MovieException;
    public void updateMovie(Movie movie) throws SQLException, MovieException;
    public void deleteMovie(Movie movie) throws SQLException;

    List<Movie> getAllOutdatedMovies() throws SQLException;
}
