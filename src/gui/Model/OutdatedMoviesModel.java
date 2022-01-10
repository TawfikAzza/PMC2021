package gui.Model;

import be.Movie;
import bll.PMCFacade;
import bll.PMCManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.SQLException;

public class OutdatedMoviesModel {
    PMCFacade pmcFacade;
    public OutdatedMoviesModel() throws IOException {
        pmcFacade = new PMCManager();
    }


    public ObservableList<Movie> getAllOutdatedMovies() throws SQLException {
        ObservableList<Movie> allOutdatedMovies = FXCollections.observableArrayList();
        allOutdatedMovies.addAll(pmcFacade.getAllOutdatedMovies());
        return allOutdatedMovies;
    }
}
