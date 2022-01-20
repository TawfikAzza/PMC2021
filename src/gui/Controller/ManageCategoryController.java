package gui.Controller;

import be.CategoryMovie;
import bll.exceptions.CategoryException;
import gui.Model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManageCategoryController implements Initializable {
    @FXML
    private Label new_editLabel;
    @FXML
    private Button acceptBtn,closeBtn;
    @FXML
    private TextField newCatName;

    @FXML
    private AnchorPane pane;
    private CategoryMovie currentCategory;
    private String operationType;
    private CategoryModel categoryModel;
    private ManageMovieController manageMovieController;
    private MainController mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pane.getStylesheets().add(getClass().getResource("/css/categories.css").toExternalForm());
        try {
            categoryModel = new CategoryModel();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createCategory(ActionEvent actionEvent)  {
        if(operationType.equals("creation")) {
            if(newCatName.getText()!=""){
               try {
                   CategoryMovie categoryCreated = categoryModel.addNewCategory(new CategoryMovie(0,newCatName.getText()));
                   mainController.setUpCheckComboBox();
                   manageMovieController.addCategoryToList(categoryCreated);
               }catch (CategoryException e){
                   Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                   alert.setTitle("Error");
                   alert.setHeaderText(e.getExceptionMessage());
                   ButtonType okButton = new ButtonType("OK");
                   alert.getButtonTypes().setAll(okButton);
                   alert.showAndWait();
                   return;
               }
            }

            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        }
        if(operationType.equals("modification")) {
            if(newCatName.getText()!=""){
                currentCategory.setName(newCatName.getText());
                try {
                    categoryModel.updateCategory(currentCategory);
                    mainController.setUpCheckComboBox();
                    manageMovieController.setupListCategory();
                }catch (CategoryException e){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText(e.getExceptionMessage());
                    ButtonType okButton = new ButtonType("OK");
                    alert.getButtonTypes().setAll(okButton);
                    alert.showAndWait();
                    return;
                }
            }
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        }
    }

    public void closeWindow(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert window");
        alert.setHeaderText("Do you want to close this window?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        }
    }

    public void setMainController(ManageMovieController manageMovieController) {
        this.manageMovieController = manageMovieController;
    }
    public void setMainController0(MainController mainController) {
        this.mainController = mainController;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setFields(CategoryMovie selectedItem) {
        new_editLabel.setText("Edit existing category");
        currentCategory=selectedItem;
        newCatName.setText(selectedItem.getName());
    }
}
