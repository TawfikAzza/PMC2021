package gui.Controller;

import be.CategoryMovie;
import be.Movie;
import bll.exceptions.CategoryException;
import bll.exceptions.MovieException;
import gui.Model.MovieModel;
import gui.Model.TimeManagerModel;
import gui.Model.VideoModel;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private CheckComboBox<Object> categoriesCheckComboBox;
    @FXML
    public Button searchButton;
    @FXML
    private AnchorPane topPane;
    @FXML
    private TextField keywordTextField;
    @FXML
    private TextFlow txtSummary;
    @FXML
    private TableColumn<Movie, String> title, imdbRating, lastViewed, rating;
    @FXML
    private TableView<Movie> tableMovie;
    @FXML
    private WebView trailerView;
    private MovieModel movieModel;
    Instant start;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        topPane.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
        try {
            movieModel = new MovieModel();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setUpTable();

        try {
            updateTableMovie();
        } catch (MovieException e) {
            e.printStackTrace();
        }

        try {
            setUpCheckComboBox();
        } catch (CategoryException e) {
            e.printStackTrace();
        }

        ObservableList<Movie> allMovies = FXCollections.observableArrayList();
        try {
            allMovies.setAll(movieModel.getAllMovies());
        } catch (MovieException e) {
            e.printStackTrace();
        }
        addFilter(allMovies);
    }

    private void setUpTable() {
        tableMovie.setEditable(true);
        title.setCellFactory(TextFieldTableCell.forTableColumn());
        title.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Movie, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Movie, String> event) {
                Movie movie = event.getRowValue();
                movie.setName(event.getNewValue());
                try {
                    movieModel.updateMovie(movie);
                } catch (MovieException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error window");
                    alert.setHeaderText(e.getExceptionMessage());
                    alert.show();
                }
            }
        });
    }

    @FXML
    public void playPauseVideo(ActionEvent actionEvent) {
        if (tableMovie.getSelectionModel().getSelectedIndex() != -1) {
            trailerView.getEngine().load(tableMovie.getSelectionModel().getSelectedItem().getTrailerLink());
        }
    }

    public void updateTableMovie() throws MovieException {
        title.setCellValueFactory(new PropertyValueFactory<>("name"));
        rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        imdbRating.setCellValueFactory(new PropertyValueFactory<>("imdbRating"));
        lastViewed.setCellValueFactory(new PropertyValueFactory<>("lastWatched"));

        tableMovie.setItems(movieModel.getAllMovies());
    }

    @FXML
    public void addMovie(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("gui/Views/newMovie.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
        }
        ManageMovieController manageMovieController = loader.getController();
        assert root != null;
        root.getStylesheets().add("css/manageMovie.css");
        manageMovieController.setMainController(this);
        Stage stage = new Stage();
        stage.setTitle("PMC 2022");
        File file = new File("src/css/data/playImagotype.png");
        Image imagotype = new Image(file.toURI().toString());
        stage.getIcons().add(imagotype);
        stage.setTitle("New Movie");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public void editMovie(ActionEvent actionEvent) throws CategoryException {
        if (tableMovie.getSelectionModel().getSelectedIndex() != -1) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("gui/Views/newMovie.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException ignored) {
            }
            ManageMovieController manageMovieController = loader.getController();
            assert root != null;
            root.getStylesheets().add("css/manageMovie.css");
            manageMovieController.setMainController(this);
            manageMovieController.setupFields(tableMovie.getSelectionModel().getSelectedItem());
            Stage stage = new Stage();
            stage.setTitle("PMC 2022");
            File file = new File("src/css/data/playImagotype.png");
            Image imagotype = new Image(file.toURI().toString());
            stage.getIcons().add(imagotype);
            stage.setTitle("Edit Movie");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        }
    }

    public void deleteMovie(ActionEvent actionEvent) throws MovieException {
        if (tableMovie.getSelectionModel().getSelectedIndex() != -1) {
            movieModel.deleteMovie(tableMovie.getSelectionModel().getSelectedItem());
            tableMovie.getItems().remove(tableMovie.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    void playMovie(ActionEvent event) throws IOException, SQLException, MovieException {
        if (tableMovie.getSelectionModel().getSelectedIndex() != -1) {
            Movie movie = tableMovie.getSelectionModel().getSelectedItem();
            movieModel.updateLastView(tableMovie.getSelectionModel().getSelectedItem());
            updateTableMovie();
            try {
                movieModel.playMovie(movie);
                movieModel.updateMovies();
            } catch (IllegalArgumentException iae) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error window");
                alert.setHeaderText("Sorry, the file does not exist");
                alert.show();
            }
        }
    }

    public void displaySummary(MouseEvent mouseEvent) {
        txtSummary.getChildren().clear();
        try {
            Text summary = new Text(tableMovie.getSelectionModel().getSelectedItem().getSummary());
            txtSummary.getChildren().add(summary);
        } catch (NullPointerException ignored) {
        }
    }


    public void openStatsWindow(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("gui/Views/Stats.fxml"));
        Parent root;
        root = loader.load();
        TimeManagerController timeManagerController = loader.getController();
        timeManagerController.setInstant(start);
        Stage stage = new Stage();
        stage.setTitle("Stats");
        File file = new File("src/css/data/playImagotype.png");
        Image imagotype = new Image(file.toURI().toString());
        stage.getIcons().add(imagotype);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void setUpCheckComboBox() throws CategoryException {
        categoriesCheckComboBox.getItems().setAll(movieModel.getAllCategories());
    }

    public void searchCategories(ActionEvent actionEvent) throws SQLException, MovieException, IOException {
        ObservableList<Movie> allMovies = FXCollections.observableArrayList();
        tableMovie.getItems().clear();
        tableMovie.setItems(movieModel.getAllMovies());
        if (!categoriesCheckComboBox.getCheckModel().getCheckedItems().isEmpty()) {
            List<Movie> tmpMovies = new ArrayList<>();
            for (Movie mov : tableMovie.getItems()) {
                boolean fullCheck = true;
                for (Object cat : categoriesCheckComboBox.getCheckModel().getCheckedItems()) {

                    if (mov.getMovieGenres().size() != categoriesCheckComboBox.getCheckModel().getCheckedItems().size()) {
                        fullCheck = false;
                        break;
                    }
                    if (mov.getMovieGenres().get(((CategoryMovie) cat).getId()) == null) {
                        fullCheck = false;
                        break;
                    }
                }
                if (!fullCheck) {
                    tmpMovies.add(mov);
                }
            }
            for (Movie mov : tmpMovies) {
                tableMovie.getItems().remove(mov);
            }
            allMovies.addAll(tableMovie.getItems());

        } else {
            updateTableMovie();
            for (Object category : categoriesCheckComboBox.getItems()) {
                if (categoriesCheckComboBox.getCheckModel().isChecked(category))
                    categoriesCheckComboBox.getCheckModel().clearCheck(category);
                allMovies.setAll(movieModel.getAllMovies());
            }
        }
        keywordTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                ObservableList<Movie> moviesFiltered = FXCollections.observableArrayList();
                for (Movie movie : allMovies) {
                    if (movie.getName().toLowerCase().contains(keywordTextField.getText().toLowerCase()))
                        moviesFiltered.add(movie);
                }
                tableMovie.setItems(moviesFiltered);
            }
        });
    }

    public void setInstant(Instant start) {
        this.start = start;
    }

    private void addFilter(ObservableList<Movie> allMovies) {
        keywordTextField.setOnKeyTyped(event -> {
            ObservableList<Movie> moviesFiltered = FXCollections.observableArrayList();
            for (Movie movie : allMovies) {
                if (movie.getName().toLowerCase().contains(keywordTextField.getText().toLowerCase())) {
                    moviesFiltered.add(movie);
                }

            }
            tableMovie.setItems(moviesFiltered);
        });

    }
}


