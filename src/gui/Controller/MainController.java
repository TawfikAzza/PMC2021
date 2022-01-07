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
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.font.ImageGraphicAttribute;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TextFlow txtSummary;
    @FXML
    private ListView<Movie> lstMyList;
    @FXML
    private Button watchMovBtn;
    @FXML
    private Label lblWelcomeText;
    @FXML
    private Button pushMeButton;
    @FXML
    private ListModel listModel;
    @FXML
    private Button watchTRBtn;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Slider vidSlider;
    @FXML
    private MediaView vidScreen;
    @FXML
    private TableColumn<Movie, String> title,imdbRating,lastViewed,rating;
    @FXML
    private TableView<Movie> tableMovie;
    @FXML
    private WebView trailerView;

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
        initButtons();
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
        /*mediaPlayer = VideoPlayer.getInstance().getPlayer();
        videoModel.setCurrentMovie(tableMovie.getSelectionModel().getSelectedItem());
        System.out.println(tableMovie.getSelectionModel().getSelectedItem().getFileLink());
        videoModel.playStopMovie();
        generateListener();
        vidScreen.setMediaPlayer(mediaPlayer);*/
        if(tableMovie.getSelectionModel().getSelectedIndex()!=-1) {
            trailerView.getEngine().load(tableMovie.getSelectionModel().getSelectedItem().getTrailerLink());
        }

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

    public void deleteMovie(ActionEvent actionEvent) throws SQLException {
        if(tableMovie.getSelectionModel().getSelectedIndex()!=-1) {
            movieModel.deleteMovie(tableMovie.getSelectionModel().getSelectedItem());
            tableMovie.getItems().remove(tableMovie.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    void playMovie(ActionEvent event) throws IOException {
        if(tableMovie.getSelectionModel().getSelectedIndex()!=-1){
            Movie movie = tableMovie.getSelectionModel().getSelectedItem();
            movieModel.playMovie(movie);

        }
    }

    //Down here are the settings for the buttons (code on top of this if possible :) )

    public void initButtons(){
        setDeleteImg();
        setAddImg();
        setEditImg();
    }

    public void setDeleteImg(){
        File img = new File("data/thrashPMC.png");
        Image thrashPNG = new Image(img.toURI().toString());
        ImageView iv = new ImageView(thrashPNG);
        deleteButton.setGraphic(iv);
    }

    public void setDeleteAnimation(){
        File file = new File("data/thrash2PMC.png");
        Image img = new Image(file.toURI().toString());
        ImageView iv = new ImageView(img);
        deleteButton.setGraphic(iv);
    }

    @FXML
    void delBtnE(MouseEvent event) {
        setDeleteAnimation();
    }

    @FXML
    void delBtnX(MouseEvent event) {
        setDeleteImg();
    }

    public void setAddImg(){
        File file = new File("data/addPMC.png");
        Image img = new Image(file.toURI().toString());
        ImageView iv = new ImageView(img);
        addButton.setGraphic(iv);
    }

    public void setAddAnimation(){
        File file = new File("data/add2PMC.png");
        Image img = new Image(file.toURI().toString());
        ImageView iv = new ImageView(img);
        addButton.setGraphic(iv);
    }

    @FXML
    void addBtnE(MouseEvent event) {
        setAddAnimation();
    }

    @FXML
    void addBtnX(MouseEvent event) {
        setAddImg();
    }

    public void setEditImg(){
        File file = new File("data/editPMC.png");
        Image img = new Image(file.toURI().toString());
        ImageView iv = new ImageView(img);
        editButton.setGraphic(iv);
    }

    public void setEditAnimation(){
        File file = new File("data/edit2PMC.png");
        Image img = new Image(file.toURI().toString());
        ImageView iv = new ImageView(img);
        editButton.setGraphic(iv);
    }

    @FXML
    void editBtnE(MouseEvent event) {
        setEditAnimation();
    }

    @FXML
    void editBtnX(MouseEvent event) {
        setEditImg();
    }

    @FXML
    void watchTRE(MouseEvent event) {
        watchTRBtn.setEffect(new Glow(0.5));
    }

    @FXML
    void watchTRX(MouseEvent event) {
        watchTRBtn.setEffect(new Glow(0.0));
    }

    @FXML
    void watchMOE(MouseEvent event) {
        watchMovBtn.setEffect(new Glow(0.5));
    }

    @FXML
    void watchMOX(MouseEvent event) {
        watchMovBtn.setEffect(new Glow(0.0));
    }

    public void displaySummary(MouseEvent mouseEvent) {
        System.out.println(tableMovie.getSelectionModel().getSelectedItem().getSummary());
        txtSummary.setAccessibleText(tableMovie.getSelectionModel().getSelectedItem().getSummary());
    }
}
