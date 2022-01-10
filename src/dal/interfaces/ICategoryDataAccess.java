package dal.interfaces;

import be.CategoryMovie;
import be.Movie;
import bll.exceptions.CategoryException;

import java.sql.SQLException;
import java.util.List;

public interface ICategoryDataAccess {
    public List<CategoryMovie> getAllCategories() throws SQLException;
    public CategoryMovie getCategory(int idCategory) throws SQLException;
    public List<CategoryMovie> getCategoryFromMovie(Movie movie) throws SQLException;
    public void addCategoryFromMovie(Movie movie) throws SQLException;
    public CategoryMovie addCategory(CategoryMovie categoryMovie) throws SQLException, CategoryException;
    public void removeCategory(CategoryMovie categoryMovie) throws SQLException;
    public void updateCategory(CategoryMovie categoryMovie) throws SQLException, CategoryException;
}
