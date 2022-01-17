package gui.Controller;

import bll.utils.IMDBScraper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class MovieWebController {

    @FXML
    private Text txt1;

    @FXML
    private Text txt2;

    public WebView movieView;
    ManageMovieController manageMovieController;

    public void handleFoundMovie(ActionEvent actionEvent) throws IOException {
        String url = movieView.getEngine().getLocation();
        if (url.contains("https://www.imdb.com/title/tt")){
            IMDBScraper scraper = new IMDBScraper(url);
            manageMovieController.fillFields(scraper);
            Stage stage = ((Stage) movieView.getParent().getScene().getWindow());
            stage.close();
        }
    }

    public void handleCancel(ActionEvent actionEvent) {
    }

    public void provideController(ManageMovieController manageMovieController) {
        this.manageMovieController = manageMovieController;
    }

    public void loadPage(String url) {
        movieView.getEngine().load(url);
    }
}
