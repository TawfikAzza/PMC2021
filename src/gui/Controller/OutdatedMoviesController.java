package gui.Controller;

import be.Movie;
import bll.exceptions.MovieException;
import gui.Model.OutdatedMoviesModel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OutdatedMoviesController implements Initializable {
    public TableView<Movie> moviesTableView;
    public TableColumn<Movie,String> title,lastview;
    OutdatedMoviesModel outdatedMoviesModel;
    public OutdatedMoviesController() throws IOException {
        outdatedMoviesModel= new OutdatedMoviesModel();
    }

    public void deleteMovie(ActionEvent actionEvent) {
    }

    public void openMainWindow(ActionEvent actionEvent) {
    }

    public void next(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateTableView();

    }

    private void populateTableView() {
        title.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastview.setCellValueFactory(new PropertyValueFactory<>("lastWatched"));
            try {
                moviesTableView.getItems().setAll(outdatedMoviesModel.getAllOutdatedMovies());
            }catch (SQLException ignored){}
    }
}
