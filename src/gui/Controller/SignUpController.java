package gui.Controller;

import bll.exceptions.UserException;
import dal.db.MovieDAO;
import gui.Model.SignUpModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class SignUpController {
    public TextField userName;
    public TextField passWord;
    public DatePicker age;
    public RadioButton terms_conditions;
    public Button signUpButton;
    SignUpModel signUpModel;
    MovieDAO movieDAO;
    public SignUpController() throws IOException {
        signUpModel= new SignUpModel();
        movieDAO = new MovieDAO();
    }

    public void signUp(ActionEvent actionEvent) throws SQLException, UserException, IOException {
        try {
            signUpModel.signUp(userName.getText(),passWord.getText(), age,terms_conditions);
        }catch (UserException ue){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error");
            alert.setHeaderText(ue.getExceptionMessage());
            ButtonType okButton = new ButtonType("OK");
            alert.getButtonTypes().setAll(okButton);
            alert.showAndWait();
            return;
        }
        {
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.close();}
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Views/MainWindow.fxml"));
        FXMLLoader loader1=new FXMLLoader(getClass().getResource("/gui/Views/outdatedMovies.fxml"));
        Parent root = loader.load();
        Parent root1=loader1.load();
        Stage stage= new Stage();
        if (movieDAO.getAllOutdatedMovies().isEmpty()){
            Scene scene = new Scene(root,1110,600);
            stage.setScene(scene);
        }
        else{
            Scene scene = new Scene(root1);
            stage.setScene(scene);
        }
        stage.setTitle("PMC 2022");
        File file = new File("data/playImagotype.png");
        Image image = new Image(file.toURI().toString());
        stage.getIcons().add(image);
        stage.setResizable(true);
        stage.show();
    }
}
