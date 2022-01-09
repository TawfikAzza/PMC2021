package gui.Controller;

import be.CategoryMovie;
import be.Movie;
import bll.exceptions.MovieException;
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
    private Button chooseFileButton;
    @FXML
    private  TextArea txtSummary;
    @FXML
    private TextField fileTextField,txtImdb,txtRating,txtTitle,txtTrailerLink;
    @FXML
    private ListView<CategoryMovie> listCategory,movieCategory;
    @FXML
    private Button cancelBtn,confirmBtn;

    private Boolean newMovie=true;
    private MainController mainController;
    private Movie currentMovie;
    private MovieModel movieModel;
    private CategoryModel categoryModel;
    public void editMovie(){
        newMovie=false;
        fileTextField.setDisable(true);
        chooseFileButton.disableProperty().set(true);
        if (!txtTrailerLink.getText().isEmpty())
            txtTrailerLink.setDisable(true);
    }

    public void chooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileExtensions =
                new FileChooser.ExtensionFilter(
                        "Movie Files (*.mp4 , *.mpeg4)", "*.mp4", "*.mpeg4");

        fileChooser.getExtensionFilters().add(fileExtensions);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null){
            fileTextField.setText(selectedFile.toString());

        }
    }

    public void setMainController(MainController mainController) {
        this.mainController=mainController;
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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCategory(ActionEvent actionEvent) {
        if(listCategory.getSelectionModel().getSelectedIndex()!=-1) {
            movieCategory.getItems().add(listCategory.getSelectionModel().getSelectedItem());
            listCategory.getItems().remove(listCategory.getSelectionModel().getSelectedItem());
        }
    }

    public void removeCategory(ActionEvent actionEvent) {
        if(movieCategory.getSelectionModel().getSelectedIndex()!=-1){
            listCategory.getItems().add(movieCategory.getSelectionModel().getSelectedItem());
            movieCategory.getItems().remove(movieCategory.getSelectionModel().getSelectedItem());
        }
    }
    /*private boolean checkInputs() {
        if (txtImdb.getText().isEmpty() || txtTitle.getText().isEmpty() || txtRating.getText().isEmpty() || txtTrailerLink.getText().isEmpty() || txtSummary.getText().isEmpty()) {
            return false;
        }if(movieCategory.getItems().size()==0) {
            return false;
        }
        return true;
    }*/
    public void saveMovie(ActionEvent actionEvent) throws SQLException {
        double rating ,imdbRating;
        /*if(!checkInputs()){
            return;
        }*/
        HashMap<Integer,CategoryMovie> categoryMovieHashMap = new HashMap<>();
        for (CategoryMovie cat: movieCategory.getItems()) {
            categoryMovieHashMap.put(cat.getId(),cat);
        }
        try {
             rating=Double.parseDouble(txtRating.getText());
             imdbRating=Double.parseDouble(txtImdb.getText());
        }catch (NumberFormatException numberFormatException){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Please find a number for ratings.");
            ButtonType okButton = new ButtonType("OK");
            alert.getButtonTypes().setAll(okButton);
            alert.showAndWait();
            return;
        }
        if(newMovie) {
            Movie movie = new Movie(0, txtTitle.getText()
                    , rating
                    , imdbRating
                    , new File(fileTextField.getText())
                    , "date"
                    ,txtTrailerLink.getText()
                    ,txtSummary.getText());

            try {
                movie = movieModel.createMovie(movie);
                mainController.updateTableMovie();
                movie.setMovieGenres(categoryMovieHashMap);
                categoryModel.addCategoryFromMovie(movie);

            }catch (MovieException movieException){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error");
                alert.setHeaderText(movieException.getExceptionMessage());
                ButtonType okButton = new ButtonType("OK");
                alert.getButtonTypes().setAll(okButton);
                alert.showAndWait();
                return;
            }


        }
        else  {
            Movie movie = new Movie(currentMovie.getId(), txtTitle.getText()
                    , Double.parseDouble(txtRating.getText())
                    , Double.parseDouble(txtImdb.getText())
                    , new File(fileTextField.getText())
                    , "date"
                    ,txtTrailerLink.getText()
                    ,txtSummary.getText());
            movie.setMovieGenres(categoryMovieHashMap);
            try {
                movieModel.updateMovie(movie);
                categoryModel.addCategoryFromMovie(movie);
                mainController.updateTableMovie();
            }catch (MovieException movieException){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error");
                alert.setHeaderText(movieException.getExceptionMessage());
                ButtonType okButton = new ButtonType("OK");
                alert.getButtonTypes().setAll(okButton);
                alert.showAndWait();
                return;
            }
        }
        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();

    }
    public void setupFields(Movie movie) throws SQLException {
        currentMovie = movie;
        txtTitle.setText(movie.getName());
        txtImdb.setText(String.valueOf(movie.getImdbRating()));
        txtRating.setText(String.valueOf(movie.getRating()));
        fileTextField.setText(movie.getFileLink().toString());
        if(movie.getTrailerLink()!=null) {
        txtTrailerLink.setText(movie.getTrailerLink());}
        if(movie.getSummary()!=null) {
        txtSummary.setText(movie.getSummary());}
        movieCategory.getItems().addAll(categoryModel.getCategoryFromMovie(movie));
        //Taking out the Categories already attributed to the movie currently being modified
        for (CategoryMovie cat: movieCategory.getItems()) {
            listCategory.getItems().removeIf(catList -> cat.getId()==(catList.getId()));
        }
    }
    public void addCategoryToList(CategoryMovie categoryMovie) {
        listCategory.getItems().add(categoryMovie);
    }
    public void setupListCategory() throws SQLException {
        listCategory.getItems().clear();
        listCategory.getItems().addAll(categoryModel.getAllCategories());
        for (CategoryMovie cat: movieCategory.getItems()) {
            listCategory.getItems().removeIf(catList -> cat.getId()==(catList.getId()));
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
        manageCategoryController.setMainController(this);
        manageCategoryController.setOperationType("creation");
        // manageMovieController.setTheme(topPane);
        Stage stage = new Stage();
        stage.setTitle("New/Edit Category");
        File file = new File("data/playImagotype.png");
        Image imagotype = new Image(file.toURI().toString());
        stage.getIcons().add(imagotype);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void editCategory(ActionEvent actionEvent) {
        if(listCategory.getSelectionModel().getSelectedIndex()!=-1) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("gui/Views/ManageCategoryView.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                // displayError(e);
            }
            ManageCategoryController manageCategoryController = loader.getController();
            manageCategoryController.setMainController(this);
            manageCategoryController.setOperationType("modification");
            manageCategoryController.setFields(listCategory.getSelectionModel().getSelectedItem());
            Stage stage = new Stage();
            stage.setTitle("New/Edit Category");
            File file = new File("data/playImagotype.png");
            Image imagotype = new Image(file.toURI().toString());
            stage.getIcons().add(imagotype);
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    public void deleteCategory(ActionEvent actionEvent) throws SQLException {
        if(listCategory.getSelectionModel().getSelectedIndex()!=-1) {
            categoryModel.deleteCategory(listCategory.getSelectionModel().getSelectedItem());
            listCategory.getItems().remove(listCategory.getSelectionModel().getSelectedIndex());
        }
    }


}
