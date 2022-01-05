package gui.Model;

import be.CategoryMovie;
import be.Movie;
import bll.PMCFacade;
import bll.PMCManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;

public class CategoryModel {
    PMCFacade pmcFacade;
    public CategoryModel() throws IOException {
        pmcFacade= new PMCManager();
    }

    public ObservableList<CategoryMovie> getAllCategories() throws SQLException {
        ObservableList<CategoryMovie> allCatObs = FXCollections.observableArrayList();
        allCatObs.addAll(pmcFacade.getAllCategories());
        return allCatObs;
    }

    public void addCategoryFromMovie(Movie movie) throws SQLException {
        pmcFacade.addCategoryFromMovie(movie);
    }

    public ObservableList<CategoryMovie> getCategoryFromMovie(Movie movie) throws SQLException {
        ObservableList<CategoryMovie> categoryMovies = FXCollections.observableArrayList();
        categoryMovies.addAll(pmcFacade.getCategoryFromMovie(movie));
        return categoryMovies;
    }

    public CategoryMovie addNewCategory(CategoryMovie categoryMovie) throws SQLException {
        return pmcFacade.addNewCategory(categoryMovie);
    }

    public void updateCategory(CategoryMovie category) throws SQLException {
        pmcFacade.updateCategory(category);
    }

    public void deleteCategory(CategoryMovie category) throws SQLException {
        pmcFacade.deleteCategory(category);
    }
}
