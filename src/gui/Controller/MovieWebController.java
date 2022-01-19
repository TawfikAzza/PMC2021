package gui.Controller;

import bll.utils.IMDBScraper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class MovieWebController {

    public WebView movieView;
    @FXML
    private Button cancelBtn;
    ManageMovieController manageMovieController;

    public void handleFoundMovie(ActionEvent actionEvent) throws IOException {
        String url = movieView.getEngine().getLocation();
        if (url.contains("https://www.imdb.com/title/tt")) {
            IMDBScraper scraper = new IMDBScraper(url);
            manageMovieController.fillFields(scraper);
            Stage stage = ((Stage) movieView.getParent().getScene().getWindow());
            stage.close();
        }
    }

    public void handleCancel(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert window");
        alert.setHeaderText("Are you sure you want to close the window ?");


        if (alert.showAndWait().get() == ButtonType.OK) {
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        }
    }

    public void provideController(ManageMovieController manageMovieController) {
        this.manageMovieController = manageMovieController;
    }

    public void loadPage(String url) {
        movieView.getEngine().load(url);
    }
}
