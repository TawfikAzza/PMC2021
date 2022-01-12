package gui.Controller;

import be.User;
import bll.exceptions.MovieException;
import bll.exceptions.UserException;
import dal.db.MovieDAO;
import gui.Model.LogInModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import dal.db.MovieDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class LogInController {
    public TextField userName;
    public Button logInButton;
    LogInModel logInModel;
    MovieDAO movieDAO;
    public LogInController() throws IOException {
        logInModel= new LogInModel();
        movieDAO= new MovieDAO();
    }
    public TextField passWord;

    public void logIn(ActionEvent actionEvent) throws SQLException, IOException, MovieException {
   User user;
    try {
         user = logInModel.logIn(userName.getText(),passWord.getText());
    }catch (UserException ue){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error");
        alert.setHeaderText(ue.getExceptionMessage());
        ButtonType okButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(okButton);
        alert.showAndWait();
        return;
    }
    if (user==null){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Error");
        alert.setHeaderText("User not found, please check your username.");
        ButtonType okButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(okButton);
        alert.showAndWait();
        return;
    }
        { Stage stage = (Stage) logInButton.getScene().getWindow();
        stage.close();}
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Views/MainWindow.fxml"));
        //FXMLLoader loader1=new FXMLLoader(getClass().getResource("/gui/Views/outdatedMovies.fxml"));
        Parent root = loader.load();
        MainController mainController=loader.getController();
        mainController.setMainController(this);
        mainController.setUser(user);

        //Parent root1=loader1.load();
        Stage stage= new Stage();
        /**if (movieDAO.getAllOutdatedMovies().isEmpty()){
            Scene scene = new Scene(root,1110,600);
            stage.setScene(scene);
        }
        else{*/
        //Scene scene = new Scene(root1);

        Scene scene = new Scene(root);
            stage.setScene(scene);
        //}
        stage.setTitle("PMC 2022");
        File file = new File("data/playImagotype.png");
        Image image = new Image(file.toURI().toString());
        stage.getIcons().add(image);
        stage.setResizable(true);
        stage.show();

    }




    public void signUp(ActionEvent actionEvent) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/gui/Views/SignUp.fxml"));
        Stage stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
