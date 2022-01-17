package gui.Controller;

import be.CategoryMovie;
import be.Movie;
import bll.exceptions.CategoryException;
import bll.exceptions.MovieException;
import bll.utils.IMDBScraper;
import gui.Model.CategoryModel;
import gui.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ManageMovieController implements Initializable {

    @FXML
    public Button chooseFileButton;
    @FXML
    private TextArea txtSummary;
    @FXML
    private TextField fileTextField, txtImdb, txtRating, txtTitle, txtTrailerLink;
    @FXML
    private ListView<CategoryMovie> listCategory, movieCategory;
    @FXML
    private Button cancelBtn, confirmBtn;
    @FXML
    private Label commentsTxt,trailerTxt,movieTxt,ratingTxt,imdbTxt,titleTxt,topTxt,topTxt1,topTxt2;

    private Boolean newMovie = true;
    private MainController mainController;
    private Movie currentMovie;
    private MovieModel movieModel;
    private CategoryModel categoryModel;


    public void chooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions =
                new FileChooser.ExtensionFilter(
                        "Movie Files (*.mp4 , *.mpeg4)", "*.mp4", "*.mpeg4");

        fileChooser.getExtensionFilters().add(fileExtensions);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            fileTextField.setText(selectedFile.toString());

        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            movieModel = new MovieModel();
            categoryModel = new CategoryModel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            listCategory.getItems().addAll(categoryModel.getAllCategories());

        } catch (CategoryException e) {
            e.printStackTrace();
        }
    }

    public void addCategory(ActionEvent actionEvent) {
        if (listCategory.getSelectionModel().getSelectedIndex() != -1) {
            movieCategory.getItems().add(listCategory.getSelectionModel().getSelectedItem());
            listCategory.getItems().remove(listCategory.getSelectionModel().getSelectedItem());
        }
    }


    public void removeCategory(ActionEvent actionEvent) throws CategoryException {
        if(movieCategory.getSelectionModel().getSelectedIndex()!=-1){
            listCategory.getItems().add(movieCategory.getSelectionModel().getSelectedItem());
            movieCategory.getItems().remove(movieCategory.getSelectionModel().getSelectedItem());
        }
    }

    public void saveMovie(ActionEvent actionEvent) throws SQLException {
        double rating, imdbRating;
        HashMap<Integer, CategoryMovie> categoryMovieHashMap = new HashMap<>();
        for (CategoryMovie cat : movieCategory.getItems()) {
            categoryMovieHashMap.put(cat.getId(), cat);
        }
        try {
            rating = Double.parseDouble(txtRating.getText());
            imdbRating = Double.parseDouble(txtImdb.getText());
        } catch (NumberFormatException numberFormatException) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Please find a number for ratings.");
            ButtonType okButton = new ButtonType("OK");
            alert.getButtonTypes().setAll(okButton);
            alert.showAndWait();
            return;
        }
        if (newMovie) {
            Movie movie = new Movie(0, txtTitle.getText()
                    , rating
                    , imdbRating
                    , new File(fileTextField.getText())
                    , "date"
                    , txtTrailerLink.getText()
                    , txtSummary.getText());

            try {
                movie = movieModel.createMovie(movie);
                mainController.updateTableMovie();
                movie.setMovieGenres(categoryMovieHashMap);
                categoryModel.addCategoryFromMovie(movie);

            } catch (MovieException movieException) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error");
                alert.setHeaderText(movieException.getExceptionMessage());
                ButtonType okButton = new ButtonType("OK");
                alert.getButtonTypes().setAll(okButton);
                alert.showAndWait();
                return;
            } catch (CategoryException e) {
                e.printStackTrace();
            }


        } else {
            Movie movie = new Movie(currentMovie.getId(), txtTitle.getText()
                    , Double.parseDouble(txtRating.getText())
                    , Double.parseDouble(txtImdb.getText())
                    , new File(fileTextField.getText())
                    , "date"
                    , txtTrailerLink.getText()
                    , txtSummary.getText());
            movie.setMovieGenres(categoryMovieHashMap);
            try {
                movieModel.updateMovie(movie);
                categoryModel.addCategoryFromMovie(movie);
                mainController.updateTableMovie();
            } catch (MovieException movieException) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error");
                alert.setHeaderText(movieException.getExceptionMessage());
                ButtonType okButton = new ButtonType("OK");
                alert.getButtonTypes().setAll(okButton);
                alert.showAndWait();
                return;
            } catch (CategoryException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();

    }

    public void setupFields(Movie movie) throws CategoryException {
        newMovie = false;
        fileTextField.setDisable(true);
        chooseFileButton.disableProperty().set(true);
        currentMovie = movie;
        txtTitle.setText(movie.getName());
        txtImdb.setText(String.valueOf(movie.getImdbRating()));
        txtRating.setText(String.valueOf(movie.getRating()));
        fileTextField.setText(movie.getFileLink().toString());
        if (movie.getTrailerLink() != null) {
            txtTrailerLink.setText(movie.getTrailerLink());
        }
        if (movie.getSummary() != null) {
            txtSummary.setText(movie.getSummary());
        }
        movieCategory.getItems().addAll(categoryModel.getCategoryFromMovie(movie));
        //Taking out the Categories already attributed to the movie currently being modified
        for (CategoryMovie cat : movieCategory.getItems()) {
            listCategory.getItems().removeIf(catList -> cat.getId() == (catList.getId()));
        }
        if (!txtTrailerLink.getText().isEmpty())
            txtTrailerLink.setDisable(true);
    }

    public void addCategoryToList(CategoryMovie categoryMovie) {
        listCategory.getItems().add(categoryMovie);
    }

    public void setupListCategory() throws CategoryException {
        listCategory.getItems().clear();
        listCategory.getItems().addAll(categoryModel.getAllCategories());
        for (CategoryMovie cat : movieCategory.getItems()) {
            listCategory.getItems().removeIf(catList -> cat.getId() == (catList.getId()));
        }
    }

    public void cancelEntry(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert window");
        alert.setHeaderText("Do you want to close this window?");


        if (alert.showAndWait().get() == ButtonType.OK) {
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        }
    }

    public void createCategory(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("gui/Views/ManageCategoryView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            // displayError(e);
        }
        ManageCategoryController manageCategoryController = loader.getController();
        manageCategoryController.setMainController0(mainController);
        manageCategoryController.setMainController(this);
        manageCategoryController.setOperationType("creation");
        // manageMovieController.setTheme(topPane);
        Stage stage = new Stage();
        stage.setTitle("New Category");
        File file = new File("src/css/data/playImagotype.png");
        Image imagotype = new Image(file.toURI().toString());
        stage.getIcons().add(imagotype);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    public void editCategory(ActionEvent actionEvent) {
        if (listCategory.getSelectionModel().getSelectedIndex() != -1) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("gui/Views/ManageCategoryView.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                // displayError(e);
            }
            ManageCategoryController manageCategoryController = loader.getController();
            manageCategoryController.setMainController0(mainController);
            manageCategoryController.setMainController(this);
            manageCategoryController.setOperationType("modification");
            manageCategoryController.setFields(listCategory.getSelectionModel().getSelectedItem());
            Stage stage = new Stage();
            stage.setTitle("Edit Category");
            File file = new File("src/css/data/playImagotype.png");
            Image imagotype = new Image(file.toURI().toString());
            stage.getIcons().add(imagotype);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        }
    }


    public void deleteCategory(ActionEvent actionEvent) throws  CategoryException {
        if(listCategory.getSelectionModel().getSelectedIndex()!=-1) {
            categoryModel.removeCategory(listCategory.getSelectionModel().getSelectedItem());
            categoryModel.deleteCategory(listCategory.getSelectionModel().getSelectedItem());
            listCategory.getItems().remove(listCategory.getSelectionModel().getSelectedIndex());
            mainController.setUpCheckComboBox();
        }
    }


    public void handleSearchMovie(ActionEvent actionEvent) throws IOException {
        String movieTitle = txtTitle.getText();
        if (movieTitle == null || movieTitle.isEmpty())
            return;

        FXMLLoader loader = new FXMLLoader((getClass().getResource("../Views/MovieWebView.fxml")));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        File file = new File("src/css/data/playImagotype.png");
        root.getStylesheets().add("css/manageMovie.css");
        stage.setTitle("Search IMDB Rating");
        Image imagotype = new Image(file.toURI().toString());
        stage.getIcons().add(imagotype);
        stage.setScene(scene);
        stage.setResizable(false);
        MovieWebController controller = loader.getController();
        controller.loadPage("https://www.imdb.com/find?q=" + txtTitle.getText() + "&ref_=nv_sr_sm");
        controller.provideController(this);
        stage.show();


    }

    public void fillFields(IMDBScraper scraper) {
        try{
            txtTitle.setText(scraper.extractTitle());
            txtSummary.setText(scraper.extractPlot());
            txtImdb.setText(scraper.extractRating());
        }
        catch (IndexOutOfBoundsException i){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error window");
            alert.setHeaderText("Some information about this movie could not be accessed");
            alert.show();
        }
    }
}
