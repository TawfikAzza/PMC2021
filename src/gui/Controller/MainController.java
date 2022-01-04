package gui.Controller;

import be.Movie;
import bll.utils.VideoPlayer;
import gui.Model.ListModel;
import gui.Model.MovieModel;
import gui.Model.VideoModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private ListView<Movie> lstMyList;
    @FXML
    private Label lblWelcomeText;
    @FXML
    private Button pushMeButton;
    private ListModel listModel;
    @FXML
    private Button btnStart;
    @FXML
    private Slider vidSlider;
    @FXML
    private MediaView vidScreen;
    @FXML
    private TableColumn<Movie, String> title,imdbRating,lastViewed,rating;
    @FXML
    private TableView<Movie> tableMovie;

    MediaPlayer mediaPlayer;
    private ChangeListener<Duration> progressListener;
    private VideoModel videoModel;
    private MovieModel movieModel;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            movieModel = new MovieModel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoModel = new VideoModel();
        try {
            updateTableMovie();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void moveProgressSlider(MouseEvent mouseEvent) {
        mediaPlayer.currentTimeProperty().removeListener(progressListener);
    }

    @FXML
    private void setProgress(MouseEvent mouseEvent) {
        mediaPlayer.seek(Duration.seconds(vidSlider.getValue()));
        mediaPlayer.currentTimeProperty().addListener(progressListener);
    }
    private void generateListener() {
        VideoPlayer videoPlayer = VideoPlayer.getInstance();
        if(progressListener!=null)
            mediaPlayer.currentTimeProperty().removeListener(progressListener);
        mediaPlayer = VideoPlayer.getInstance().getPlayer();
        mediaPlayer.setOnReady(() -> {
            vidSlider.maxProperty().set(mediaPlayer.getTotalDuration().toSeconds());
        });
        vidSlider.maxProperty().set(mediaPlayer.getTotalDuration().toSeconds());

        progressListener = new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                vidSlider.setValue((double) newValue.toSeconds());
            }
        };
        mediaPlayer.currentTimeProperty().addListener(progressListener);
    }
    public void playPauseVideo(ActionEvent actionEvent) {
        mediaPlayer = VideoPlayer.getInstance().getPlayer();
        videoModel.setCurrentMovie(tableMovie.getSelectionModel().getSelectedItem());
        System.out.println(tableMovie.getSelectionModel().getSelectedItem().getFileLink());
        videoModel.playStopMovie();
        generateListener();
        vidScreen.setMediaPlayer(mediaPlayer);


    }

    public void updateTableMovie() throws SQLException {
        tableMovie.getItems().clear();
        title.setCellValueFactory(new PropertyValueFactory<>("name"));
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        imdbRating.setCellValueFactory(new PropertyValueFactory<>("imdbRating"));
        lastViewed.setCellValueFactory(new PropertyValueFactory<>("lastWatched"));
        tableMovie.getItems().setAll(movieModel.getAllMovies());
    }

    public void addMovie(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("gui/Views/newMovie.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
           // displayError(e);
        }
        ManageMovieController manageMovieController = loader.getController();
        manageMovieController.setMainController(this);
        manageMovieController.setOperationType("creation");
       // manageMovieController.setTheme(topPane);
        Stage stage = new Stage();
        stage.setTitle("New/Edit Movie");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void editMovie(ActionEvent actionEvent) throws SQLException {
        if(tableMovie.getSelectionModel().getSelectedIndex()!=-1) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("gui/Views/newMovie.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                // displayError(e);
            }
            ManageMovieController manageMovieController = loader.getController();
            manageMovieController.setMainController(this);
            manageMovieController.setOperationType("modification");
            manageMovieController.setupFields(tableMovie.getSelectionModel().getSelectedItem());
            // manageMovieController.setTheme(topPane);
            Stage stage = new Stage();
            stage.setTitle("New/Edit Movie");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }
}
