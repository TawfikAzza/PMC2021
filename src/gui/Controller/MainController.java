package gui.Controller;

import be.CategoryMovie;
import be.Movie;
import bll.exceptions.CategoryException;
import bll.exceptions.MovieException;
import gui.Model.ListModel;
import gui.Model.MovieModel;
import gui.Model.VideoModel;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public CheckComboBox<Object> categoriesCheckComboBox;
    @FXML
    public Button searchButton;
    @FXML
    private Button search;
    @FXML
    private TextField keywordTextField;
    @FXML
    private TextFlow txtSummary;
    @FXML
    private ListView<Movie> lstMyList;
    @FXML
    private Button watchMovBtn;
    @FXML
    private Button aboutMeBtn;
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
    private TableColumn<Movie, String> title, imdbRating, lastViewed, rating;
    @FXML
    private TableView<Movie> tableMovie;
    @FXML
    private WebView trailerView;
    @FXML
    private Text txtAllMovies,txtTrailerPreview,txtComments;

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
            updateTableMovie(movieModel.getAllMovies());
        } catch (MovieException e) {
            e.printStackTrace();
        }

        try {
            setUpCheckComboBox();
        } catch (CategoryException e) {
            e.printStackTrace();
        }

        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Movie> filteredData;
        filteredData = new FilteredList<>(tableMovie.getItems(), b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        FilteredList<Movie> finalFilteredData = filteredData;
        keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            finalFilteredData.setPredicate(movie -> {
                // If filter text is empty, display all persons.

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (movie.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else return false;
            });
        });

        // 3. Wrap the FilteredList in a SortedList.

        SortedList<Movie> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        //    Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(tableMovie.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableMovie.setItems(sortedData);
    }

    @FXML
    public void playPauseVideo(ActionEvent actionEvent) {
        if (tableMovie.getSelectionModel().getSelectedIndex() != -1) {
            trailerView.getEngine().load(tableMovie.getSelectionModel().getSelectedItem().getTrailerLink());
        }
    }

    public void updateTableMovie(ObservableList<Movie> moviesList) throws MovieException {
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
        manageMovieController.setMainController(this);
        Stage stage = new Stage();
        stage.setTitle("New Movie");
        stage.setScene(new Scene(root));
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
            manageMovieController.setMainController(this);
            manageMovieController.setupFields(tableMovie.getSelectionModel().getSelectedItem());
            Stage stage = new Stage();
            stage.setTitle("Edit Movie");
            stage.setScene(new Scene(root));
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
            updateTableMovie(movieModel.getAllMovies());
            try {
                movieModel.playMovie(movie);
            } catch (IllegalArgumentException iae) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error window");
                alert.setHeaderText("Sorry, the file does not exist");
                alert.show();
            }
        }
    }

    public void displaySummary(MouseEvent mouseEvent) {
        System.out.println(tableMovie.getSelectionModel().getSelectedItem().getSummary());
        txtSummary.setAccessibleText(tableMovie.getSelectionModel().getSelectedItem().getSummary());
    }


    public void openStatsWindow(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("gui/Views/Stats.fxml"));
        Parent root;
        root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("About me");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void setUpCheckComboBox() throws CategoryException {
        categoriesCheckComboBox.getItems().setAll(movieModel.getAllCategories());
    }

    public void searchCategories(ActionEvent actionEvent) throws SQLException, MovieException {
        if (searchButton.getText().equals("Search")) {
            if (!categoriesCheckComboBox.getCheckModel().getCheckedItems().isEmpty()) {
                ObservableList<Movie> allMovies = FXCollections.observableArrayList();
                for (Object category : categoriesCheckComboBox.getCheckModel().getCheckedItems())
                    allMovies.addAll(movieModel.allMoviesCategory((CategoryMovie) category));
                tableMovie.setItems(allMovies);
                searchButton.setText("Clear");
            }
        } else {
            updateTableMovie(movieModel.getAllMovies());
            for (Object category : categoriesCheckComboBox.getItems()) {
                if (categoriesCheckComboBox.getCheckModel().isChecked(category))
                    categoriesCheckComboBox.getCheckModel().clearCheck(category);
            }
            searchButton.setText("Search");
        }
    }

    public void search(ActionEvent actionEvent) throws MovieException {
        if (search.getText().equals("Search")) {
            ObservableList<Movie> moviesFiltered = FXCollections.observableArrayList();
            for (Movie movie : tableMovie.getItems()) {
                if (movie.getName().toLowerCase().contains(keywordTextField.getText().toLowerCase()))
                    moviesFiltered.add(movie);
                if (!keywordTextField.getText().isEmpty())
                    search.setText("Clear");
            }
            tableMovie.setItems(moviesFiltered);
        } else if (search.getText().equals("Clear")) {
            keywordTextField.setText("");
            updateTableMovie(movieModel.getAllMovies());
            search.setText("Search");
        }
    }
}

