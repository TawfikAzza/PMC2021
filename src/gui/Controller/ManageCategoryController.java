package gui.Controller;

import be.CategoryMovie;
import bll.exceptions.CategoryException;
import gui.Model.CategoryModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
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

    public void createCategory(ActionEvent actionEvent)  {
        if(operationType.equals("creation")) {
            if(newCatName.getText()!=""){
               try {
                   CategoryMovie categoryCreated = categoryModel.addNewCategory(new CategoryMovie(0,newCatName.getText()));
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

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setFields(CategoryMovie selectedItem) {
        new_editLabel.setText("Edit existing category");
        currentCategory=selectedItem;
        newCatName.setText(selectedItem.getName());

    }
}
