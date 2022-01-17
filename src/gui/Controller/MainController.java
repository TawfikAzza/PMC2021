package gui.Controller;

import be.CategoryMovie;
import be.Movie;
import be.Time;
import bll.exceptions.CategoryException;
import bll.exceptions.MovieException;
import dal.db.TimeDAO;
import gui.Model.MovieModel;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;
import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public CheckComboBox<Object> categoriesCheckComboBox;
    @FXML
    public Button searchButton;
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

    private ChangeListener<Duration> progressListener;
    private MovieModel movieModel;
    Instant start;
    TimeDAO timeDAO= new TimeDAO();

    public MainController() throws IOException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            movieModel = new MovieModel();
        } catch (IOException e) {
            e.printStackTrace();
        }

        VideoModel videoModel = new VideoModel();
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

        ObservableList<Movie>allMovies=FXCollections.observableArrayList();
        try {
            allMovies.setAll(movieModel.getAllMovies());
        } catch (MovieException e) {
            e.printStackTrace();
        }
        keywordTextField.setOnKeyPressed(event -> {
            ObservableList<Movie>moviesFiltered=FXCollections.observableArrayList();
            for (Movie movie:allMovies){
                    if (movie.getName().toLowerCase().contains(keywordTextField.getText().toLowerCase()))
                        moviesFiltered.add(movie);
            }
            tableMovie.setItems(moviesFiltered);
        });

        /*** Wrap the ObservableList in a FilteredList (initially display all data).
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
        tableMovie.setItems(sortedData);*/
    }

    private void setUpTable() {
        tableMovie.setEditable(true);
        title.setCellFactory(TextFieldTableCell.forTableColumn());
        title.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Movie,String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Movie,String> event) {
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
        List<Movie> testMovie = movieModel.getAllMovies();


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
                timeDAO.updateMovies();
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
        System.out.println(tableMovie.getSelectionModel().getSelectedItem().getSummary());
        Text summary = new Text(tableMovie.getSelectionModel().getSelectedItem().getSummary());
        txtSummary.getChildren().add(summary);
    }


    public void openStatsWindow(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("gui/Views/Stats.fxml"));
        Parent root;
        root = loader.load();
        TimeManagerController timeManagerController= loader.getController();
        timeManagerController.setInstant(start);
        Stage stage = new Stage();
        stage.setTitle("About me");
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
           /* for (Object category : categoriesCheckComboBox.getCheckModel().getCheckedItems())
                allMovies.addAll(movieModel.allMoviesCategory((CategoryMovie) category));
            */
            List<Movie> tmpMovies = new ArrayList<>();
            for(Movie mov: tableMovie.getItems()) {
                boolean fullCheck= true;
                 for(Object cat: categoriesCheckComboBox.getCheckModel().getCheckedItems()){
                        cat = (CategoryMovie) cat;
                        if(mov.getMovieGenres().size()!=categoriesCheckComboBox.getCheckModel().getCheckedItems().size()) {
                            fullCheck=false;
                        }
                        if(mov.getMovieGenres().get(((CategoryMovie) cat).getId())==null) {
                            fullCheck= false;
                        }
                 }
                 if(!fullCheck) {
                     tmpMovies.add(mov);
                 }
                 fullCheck=true;
            }
            for(Movie mov:tmpMovies) {
                tableMovie.getItems().remove(mov);
            }
            allMovies.addAll(tableMovie.getItems());
           // tableMovie.setItems(allMovies);
            keywordTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    ObservableList<Movie>moviesFiltered=FXCollections.observableArrayList();
                    for (Movie movie:allMovies){
                        if (movie.getName().toLowerCase().contains(keywordTextField.getText().toLowerCase()))
                            moviesFiltered.add(movie);
                    }
                    tableMovie.setItems(moviesFiltered);
                }
            });

        } else {
            updateTableMovie();
            for (Object category : categoriesCheckComboBox.getItems()) {
                if (categoriesCheckComboBox.getCheckModel().isChecked(category))
                    categoriesCheckComboBox.getCheckModel().clearCheck(category);
                allMovies.setAll(movieModel.getAllMovies());
                keywordTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        ObservableList<Movie>moviesFiltered=FXCollections.observableArrayList();
                        for (Movie movie:allMovies){
                            if (movie.getName().toLowerCase().contains(keywordTextField.getText().toLowerCase()))
                                moviesFiltered.add(movie);
                        }
                        tableMovie.setItems(moviesFiltered);
                    }
                });
            }
        }


    }

    public void setInstant(Instant start) {
        this.start=start;
    }

    /***public void search(ActionEvent actionEvent) throws MovieException, SQLException {
        ObservableList<Movie> moviesFiltered = FXCollections.observableArrayList();
        ObservableList<Movie> moviesBeforeFilter = FXCollections.observableArrayList();
        moviesBeforeFilter.setAll(tableMovie.getItems());
        if (!keywordTextField.getText().isEmpty()) {
            for (Movie movie : tableMovie.getItems()) {
                if (movie.getName().toLowerCase().contains(keywordTextField.getText().toLowerCase()))
                    moviesFiltered.add(movie);
            }
            tableMovie.setItems(moviesFiltered);
        } else {
            for (Object category : categoriesCheckComboBox.getCheckModel().getCheckedItems())
                moviesFiltered.addAll(movieModel.allMoviesCategory((CategoryMovie) category));
            tableMovie.setItems(moviesFiltered);
        }*/
    }


