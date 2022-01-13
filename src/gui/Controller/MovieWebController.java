package gui.Controller;

import bll.utils.IMDBScraper;
import javafx.event.ActionEvent;
import javafx.scene.web.WebView;

import java.io.IOException;

public class MovieWebController {

    public WebView movieView;
    ManageMovieController manageMovieController;

    public void handleFoundMovie(ActionEvent actionEvent) throws IOException {
        String url = movieView.getEngine().getLocation();
        if (url.contains("https://www.imdb.com/title/tt")){
            IMDBScraper scraper = new IMDBScraper(url);
            manageMovieController.fillFields(scraper);
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
