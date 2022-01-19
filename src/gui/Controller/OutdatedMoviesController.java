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
    private Button cancelButton, nextButton;

    private OutdatedMoviesModel outdatedMoviesModel;
    private MovieModel movieModel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            outdatedMoviesModel = new OutdatedMoviesModel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            movieModel = new MovieModel();
        } catch (IOException e) {
            e.printStackTrace();
        }

        nextButton.setDisable(true);
        nextButton.setVisible(false);
        pane.getStylesheets().add(getClass().getResource("/css/outdated.css").toExternalForm());
        populateTableView();

    }

    public void deleteMovie(ActionEvent actionEvent) throws MovieException, IOException {
        if (moviesTableView.getSelectionModel().getSelectedIndex() != -1) {
            movieModel.deleteMovie(moviesTableView.getSelectionModel().getSelectedItem());
            moviesTableView.getItems().remove(moviesTableView.getSelectionModel().getSelectedIndex());
            switchButtons();
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

    private void populateTableView() {
        title.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastview.setCellValueFactory(new PropertyValueFactory<>("lastWatched"));
        try {
            moviesTableView.getItems().setAll(outdatedMoviesModel.getAllOutdatedMovies());
        } catch (SQLException ignored) {
        }
    }

    private void switchButtons() {
        nextButton.setDisable(false);
        nextButton.setVisible(true);
        cancelButton.setDisable(true);
        cancelButton.setVisible(false);
    }
}
