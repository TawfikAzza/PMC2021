package gui.Controller;

import bll.utils.IMDBScraper;
import javafx.event.ActionEvent;
import javafx.scene.web.WebView;

import java.io.IOException;

public class MovieWebController {

    public WebView movieView;
    ManageMovieController manageMovieController;

    public void handleFoundMovie(ActionEvent actionEvent) throws IOException {
        IMDBScraper scraper = new IMDBScraper(movieView.getEngine().getLocation());
        manageMovieController.fillFields(scraper);
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
