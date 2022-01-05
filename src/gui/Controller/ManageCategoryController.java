package gui.Controller;

import be.CategoryMovie;
import bll.PMCManager;
import gui.Model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManageCategoryController implements Initializable {
    @FXML
    private Button closeBtn,createBtn;
    @FXML
    private TextField newCatName;
    private CategoryMovie currentCategory;
    private String operationType;
    private CategoryModel categoryModel;
    ManageMovieController manageMovieController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            categoryModel = new CategoryModel();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createCategory(ActionEvent actionEvent) throws SQLException {
        if(operationType.equals("creation")) {
            if(newCatName.getText()!=""){
               CategoryMovie categoryCreated = categoryModel.addNewCategory(new CategoryMovie(0,newCatName.getText()));
                manageMovieController.addCategoryToList(categoryCreated);
            }

            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();
        }
        if(operationType.equals("modification")) {
            if(newCatName.getText()!=""){
                currentCategory.setName(newCatName.getText());
                categoryModel.updateCategory(currentCategory);
                manageMovieController.setupListCategory();
            }

            Stage stage = (Stage) closeBtn.getScene().getWindow();
            stage.close();

        }
    }

    public void closeWindow(ActionEvent actionEvent) {

    }

    public void setMainController(ManageMovieController manageMovieController) {
        this.manageMovieController = manageMovieController;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;

    }

    public void setFields(CategoryMovie selectedItem) {
        currentCategory=selectedItem;
        newCatName.setText(selectedItem.getName());

    }
}
