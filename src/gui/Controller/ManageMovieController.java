package gui.Controller;

import be.CategoryMovie;
import be.Movie;
import gui.Model.CategoryModel;
import gui.Model.MovieModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
    private TextField fileTextField,txtImdb,txtRating,txtTitle;
    @FXML
    private ListView<CategoryMovie> listCategory,movieCategory;
    @FXML
    private Button cancelBtn,confirmBtn;

    private String operationType="creation";
    private MainController mainController;
    private Movie currentMovie;
    private MovieModel movieModel;
    private CategoryModel categoryModel;
    public void setOperationType(String operationType){
        this.operationType=operationType;
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
    private boolean checkInputs() {
        if (txtImdb.getText().isEmpty() || txtTitle.getText().isEmpty() || txtRating.getText().isEmpty()) {
            return false;
        }if(movieCategory.getItems().size()==0) {
            return false;
        }
        return true;
    }
    public void saveMovie(ActionEvent actionEvent) throws SQLException {
        if(!checkInputs()){
            return;
        }
        HashMap<Integer,CategoryMovie> categoryMovieHashMap = new HashMap<>();
        for (CategoryMovie cat: movieCategory.getItems()) {
            categoryMovieHashMap.put(cat.getId(),cat);
        }
        if(operationType.equals("creation")) {
            Movie movie = new Movie(0, txtTitle.getText()
                    , Double.parseDouble(txtRating.getText())
                    , Double.parseDouble(txtImdb.getText())
                    , new File(fileTextField.getText())
                    , "date");

            movie = movieModel.createMovie(movie);
            movie.setMovieGenres(categoryMovieHashMap);
            categoryModel.addCategoryFromMovie(movie);

        }
        if(operationType.equals("modification")) {
            Movie movie = new Movie(currentMovie.getId(), txtTitle.getText()
                    , Double.parseDouble(txtRating.getText())
                    , Double.parseDouble(txtImdb.getText())
                    , new File(fileTextField.getText())
                    , "date");
            movie.setMovieGenres(categoryMovieHashMap);
            movieModel.updateMovie(movie);
            categoryModel.addCategoryFromMovie(movie);
        }
        mainController.updateTableMovie();
        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();

    }
    public void setupFields(Movie movie) throws SQLException {
        currentMovie = movie;
        txtTitle.setText(movie.getName());
        txtImdb.setText(String.valueOf(movie.getImdbRating()));
        txtRating.setText(String.valueOf(movie.getRating()));
        fileTextField.setText(movie.getFileLink().toString());
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
      //  Stage stage = (Stage) cancelBtn.get
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
        stage.setScene(new Scene(root));
        stage.show();
    }
}
