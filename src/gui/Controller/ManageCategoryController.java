package gui.Controller;

import be.CategoryMovie;
import bll.PMCManager;
import gui.Model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManageCategoryController implements Initializable {
    @FXML
    private Button acceptBtn,closeBtn;
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
        initButtons();
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

    public void initButtons(){
        setAcceptBtn();
        setCancelBtn();
    }

    public void setCancelBtn(){
        File file = new File("data/cancelPMC.png");
        Image img = new Image(file.toURI().toString());
        ImageView iv = new ImageView(img);
        iv.setFitWidth(35);
        iv.setFitHeight(35);
        closeBtn.setGraphic(iv);
    }

    public void setCancelAnim(){
        File file = new File("data/cancel2PMC.png");
        Image img = new Image(file.toURI().toString());
        ImageView iv = new ImageView(img);
        iv.setFitWidth(35);
        iv.setFitHeight(35);
        closeBtn.setGraphic(iv);
    }

    public void setAcceptBtn(){
        File file = new File("data/acceptPMC.png");
        Image img = new Image(file.toURI().toString());
        ImageView iv = new ImageView(img);
        iv.setFitWidth(35);
        iv.setFitHeight(35);
        acceptBtn.setGraphic(iv);
    }

    public void setAcceptAnim(){
        File file = new File("data/accept2PMC.png");
        Image img = new Image(file.toURI().toString());
        ImageView iv = new ImageView(img);
        iv.setFitWidth(35);
        iv.setFitHeight(35);
        acceptBtn.setGraphic(iv);
    }
    @FXML
    void acceptBtnE(MouseEvent event) {
        setAcceptAnim();
    }

    @FXML
    void acceptBtnX(MouseEvent event) {
        setAcceptBtn();
    }

    @FXML
    void cancelBtnE(MouseEvent event) {
        setCancelAnim();
    }

    @FXML
    void cancelBtnX(MouseEvent event) {
        setCancelBtn();
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
