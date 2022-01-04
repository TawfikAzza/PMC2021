package dal;

import be.CategoryMovie;
import be.Movie;
import dal.db.CategoryDAO;
import dal.db.MovieDAO;
import dal.interfaces.ICategoryDataAccess;
import dal.interfaces.IMovieDataAccess;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DALController implements IDALFacade {
    IMovieDataAccess iMovieDataAccess;
    ICategoryDataAccess iCategoryDataAccess;

    public DALController() throws IOException {
        iMovieDataAccess = new MovieDAO();
        iCategoryDataAccess = new CategoryDAO();
    }


    @Override
    public List<Movie> getAllMovies() throws SQLException {
        return iMovieDataAccess.getAllMovies();
    }

    @Override
    public Movie createMovie(Movie movie) throws SQLException {
        return iMovieDataAccess.createMovie(movie);
    }

    @Override
    public void updateMovie(Movie movie) throws SQLException {
        iMovieDataAccess.updateMovie(movie);
    }

    @Override
    public List<CategoryMovie> getAllCategories() throws SQLException {
        return iCategoryDataAccess.getAllCategories();
    }

    @Override
    public CategoryMovie getCategory(int idCategory) throws SQLException {
        return iCategoryDataAccess.getCategory(idCategory);
    }

    @Override
    public List<CategoryMovie> getCategoryFromMovie(Movie movie) throws SQLException {
        return iCategoryDataAccess.getCategoryFromMovie(movie);
    }

    @Override
    public void addCategoryFromMovie(Movie movie) throws SQLException {
        iCategoryDataAccess.addCategoryFromMovie(movie);
    }

    @Override
    public CategoryMovie addCategory(CategoryMovie categoryMovie) throws SQLException {
        return iCategoryDataAccess.addCategory(categoryMovie);
    }

    @Override
    public void removeCategory(CategoryMovie categoryMovie) throws SQLException {
        iCategoryDataAccess.removeCategory(categoryMovie);
    }

    @Override
    public void updateCategory(CategoryMovie categoryMovie) throws SQLException {
        iCategoryDataAccess.updateCategory(categoryMovie);
    }
}
