package dal.interfaces;

import be.Movie;

import java.sql.SQLException;
import java.util.List;

public interface IMovieDataAccess {
    public List<Movie> getAllMovies() throws SQLException;
    public Movie createMovie(Movie movie) throws SQLException;
    public void updateMovie(Movie movie) throws SQLException;
    public void deleteMovie(Movie movie) throws SQLException;
}
