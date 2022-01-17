package gui.Model;

import be.CategoryMovie;
import be.Movie;
import bll.PMCFacade;
import bll.PMCManager;
import bll.exceptions.CategoryException;
import bll.exceptions.MovieException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;

public class CategoryModel {
    PMCFacade pmcFacade;
    public CategoryModel() throws IOException {
        pmcFacade= new PMCManager();
    }

    public ObservableList<CategoryMovie> getAllCategories() throws CategoryException {
        ObservableList<CategoryMovie> allCatObs = FXCollections.observableArrayList();
        allCatObs.addAll(pmcFacade.getAllCategories());
        return allCatObs;
    }

    public void addCategoryFromMovie(Movie movie) throws CategoryException {
        pmcFacade.addCategoryFromMovie(movie);
    }

    public ObservableList<CategoryMovie> getCategoryFromMovie(Movie movie) throws CategoryException {
        ObservableList<CategoryMovie> categoryMovies = FXCollections.observableArrayList();
        categoryMovies.addAll(pmcFacade.getCategoryFromMovie(movie));
        return categoryMovies;
    }

    public CategoryMovie addNewCategory(CategoryMovie categoryMovie) throws  CategoryException {
        return pmcFacade.addNewCategory(categoryMovie);
    }

    public void updateCategory(CategoryMovie category) throws CategoryException {
        pmcFacade.updateCategory(category);
    }

    public void deleteCategory(CategoryMovie category) throws CategoryException {
        pmcFacade.deleteCategory(category);
    }

    public void removeCategory(CategoryMovie selectedItem) throws CategoryException {
        pmcFacade.removeCategory(selectedItem);
    }
}
