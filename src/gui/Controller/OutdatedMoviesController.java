package gui.Controller;

import be.Movie;
import bll.exceptions.MovieException;
import gui.Model.MovieModel;
import gui.Model.OutdatedMoviesModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OutdatedMoviesController implements Initializable {
    @FXML
    public AnchorPane pane;
    @FXML
    public Text txt, txt2;
    @FXML
    private TableView<Movie> moviesTableView;
    @FXML
    private TableColumn<Movie, String> title, lastview;
    @FXML
    private Button cancelButton,nextButton, deleteBtn;

    public OutdatedMoviesModel outdatedMoviesModel;
    public MovieModel movieModel;

    public OutdatedMoviesController() throws IOException {
        outdatedMoviesModel = new OutdatedMoviesModel();
        movieModel = new MovieModel();
    }

    public void deleteMovie(ActionEvent actionEvent) throws MovieException, IOException {
        if (moviesTableView.getSelectionModel().getSelectedIndex() != -1) {
            movieModel.deleteMovie(moviesTableView.getSelectionModel().getSelectedItem());
            moviesTableView.getItems().remove(moviesTableView.getSelectionModel().getSelectedIndex());
        }
        if (moviesTableView.getItems().isEmpty()) {
            {
                Stage stage = (Stage) nextButton.getScene().getWindow();
                stage.close();
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Views/MainWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("PMC 2022");
            stage.setScene(new Scene(root));
            File file = new File("src/css/data/playImagotype.png");
            Image imagotype = new Image(file.toURI().toString());
            stage.getIcons().add(imagotype);
            stage.setResizable(false);
            stage.show();
        }
    }

    public void openMainWindow(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert window");
        alert.setHeaderText("Are you sure you do not want to delete outdated movies ?");


        if (alert.showAndWait().get() == ButtonType.OK) {
            {
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Views/MainWindow.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("PMC 2022");
            stage.setScene(new Scene(root));
            File file = new File("src/css/data/playImagotype.png");
            Image imagotype = new Image(file.toURI().toString());
            stage.getIcons().add(imagotype);
            stage.setResizable(false);
            stage.show();
        }


    }

    public void next(ActionEvent actionEvent) throws IOException {
        {
            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.close();
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Views/MainWindow.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("PMC 2022");
        stage.setScene(new Scene(root));
        File file = new File("src/css/data/playImagotype.png");
        Image imagotype = new Image(file.toURI().toString());
        stage.getIcons().add(imagotype);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pane.getStylesheets().add(getClass().getResource("/css/outdated.css").toExternalForm());
        populateTableView();
    }

    private void populateTableView() {
        title.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastview.setCellValueFactory(new PropertyValueFactory<>("lastWatched"));
        try {
            moviesTableView.getItems().setAll(outdatedMoviesModel.getAllOutdatedMovies());
        } catch (SQLException ignored) {
        }
    }
}
