package dal;

import be.CategoryMovie;
import be.Movie;
import be.Stats;
import bll.exceptions.CategoryException;
import bll.exceptions.MovieException;
import dal.db.CategoryDAO;
import dal.db.MovieDAO;
import dal.db.StatsDAO;
import dal.interfaces.ICategoryDataAccess;
import dal.interfaces.IMovieDataAccess;
import dal.interfaces.IStatsDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class DALController implements IDALFacade {
    IMovieDataAccess iMovieDataAccess;
    ICategoryDataAccess iCategoryDataAccess;
    IStatsDAO iTimeDAO;

    public DALController() throws IOException {
        iMovieDataAccess = new MovieDAO();
        iCategoryDataAccess = new CategoryDAO();
        iTimeDAO= new StatsDAO();
    }


    @Override
    public List<Movie> getAllMovies() throws MovieException {
        try {
            return iMovieDataAccess.getAllMovies();
        }catch (SQLException e){
            throw new MovieException("Something went wrong in the database.",new Exception());
        }
    }

    @Override
    public Movie createMovie(Movie movie) throws MovieException {
        try {
            return iMovieDataAccess.createMovie(movie);
        }catch (SQLException e){
            throw new MovieException("Something went wrong in the database.",new Exception());
        }
    }

    @Override
    public void updateMovie(Movie movie) throws  MovieException {
        try {
            iMovieDataAccess.updateMovie(movie);
        }catch (SQLException e){
            throw new MovieException("Something went wrong in the database.",new Exception());
        }
    }

    @Override
    public void deleteMovie(Movie movie) throws MovieException {
        try {
            iMovieDataAccess.deleteMovie(movie);
        }catch (SQLException e){
            throw new MovieException("Something went wrong in the database.",new Exception());
        }
    }

    @Override
    public List<CategoryMovie> getAllCategories() throws CategoryException {
        try {
            return iCategoryDataAccess.getAllCategories();
        }catch (SQLException e){
            throw new CategoryException("Something wrong went in the database.\nWe will fix it as soon as possible.",new Exception());
        }
    }

    @Override
    public CategoryMovie getCategory(int idCategory) throws CategoryException {
        try {
            return iCategoryDataAccess.getCategory(idCategory);
        }catch (SQLException e){
            throw new CategoryException("Something wrong went in the database.\nPlease try again.",new Exception());
        }
    }

    @Override
    public List<CategoryMovie> getCategoryFromMovie(Movie movie) throws CategoryException {
        try {
            return iCategoryDataAccess. getCategoryFromMovie(movie);
        }catch (SQLException e){
            throw new CategoryException("Something wrong went in the database.\nPlease try again.",new Exception());
        }
    }

    @Override
    public void addCategoryFromMovie(Movie movie) throws CategoryException {
        try {
            iCategoryDataAccess.addCategoryFromMovie(movie);
        }catch (SQLException e){
            throw new CategoryException("Something wrong went in the database.\nPlease try again.",new Exception());
        }
    }

    @Override
    public CategoryMovie addCategory(CategoryMovie categoryMovie) throws CategoryException {
        try {
            return iCategoryDataAccess.addCategory(categoryMovie);
        }catch (SQLException e){
            throw new CategoryException("Something wrong went in the database.\nPlease try again.",new Exception());
        }
    }

    @Override
    public void removeCategory(CategoryMovie categoryMovie) throws CategoryException {
        try {
            iCategoryDataAccess.removeCategory(categoryMovie);
        }catch (SQLException e){
            throw new CategoryException("Something wrong went in the database.\nPlease try again.",new Exception());
        }
    }

    @Override
    public void updateCategory(CategoryMovie categoryMovie) throws CategoryException {
        try {
            iCategoryDataAccess.updateCategory(categoryMovie);
        }catch (SQLException e){
            throw new CategoryException("Something wrong went in the database.\nPlease try again.",new Exception());
        }
    }

    @Override
    public void deleteCategory(CategoryMovie category) throws CategoryException {
        try {
            iCategoryDataAccess.removeCategory(category);
        }catch (SQLException e){
            throw new CategoryException("Something wrong went in the database.\nPlease try again.",new Exception());
        }
    }

    @Override
    public List<Movie> getAllOutdatedMovies()throws SQLException {
        return iMovieDataAccess.getAllOutdatedMovies();
    }

    @Override
    public List<Movie> getAllMoviesCategory(CategoryMovie categoryMovie) throws SQLException {
        return iMovieDataAccess.getAllMoviesCategory(categoryMovie);
    }

    @Override
    public void updateLastView(Movie movie) throws SQLException {
        iMovieDataAccess.updateLastView(movie);
    }

    @Override
    public void newTime(int movies, long seconds) throws SQLException {
        iTimeDAO.newTime(movies,seconds);
    }

    @Override
    public List<Stats> getAllStats(LocalDate firstDate, LocalDate secondDate) throws SQLException {
        return iTimeDAO.getAllStats(firstDate,secondDate);
    }

    @Override
    public void updateTime(long seconds) throws SQLException {
        iTimeDAO.updateTime(seconds);
    }

    @Override
    public void updateMovies() throws SQLException {
        iTimeDAO.updateMovies();
    }

    @Override
    public Date getFirstDate() throws SQLException {
        return iTimeDAO.getFirstDate();
    }
}
