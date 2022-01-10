import dal.db.MovieDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
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

public class Main extends Application {
    MovieDAO movieDAO;

    public Main() throws IOException {
        movieDAO=new MovieDAO();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Views/MainWindow.fxml"));
        FXMLLoader loader1=new FXMLLoader(getClass().getResource("/gui/Views/outdatedMovies.fxml"));
        Parent root = loader.load();
        Parent root1=loader1.load();
        /*
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        float width = gd.getDisplayMode().getWidth();
        float height = gd.getDisplayMode().getHeight();
        width = width/3;
        height = height/3;
        Scene scene = new Scene(root,(width*2),(height*2)); //Responsive design
         */
        if (movieDAO.getAllOutdatedMovies().isEmpty()){
        Scene scene = new Scene(root,1110,600);
        primaryStage.setScene(scene);
        }
        else{
            Scene scene = new Scene(root1);
            primaryStage.setScene(scene);
        }
        primaryStage.setTitle("PMC 2022");
        File file = new File("data/playImagotype.png");
        Image imagotype = new Image(file.toURI().toString());
        primaryStage.getIcons().add(imagotype);
        primaryStage.setResizable(true);
        primaryStage.show();

        /*primaryStage.setTitle("Media");
        Group root = new Group();
        Media media = new Media(new File("C:/Users/EASV/Desktop/file_example_MP4_480_1_5MG.mp4").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();

        MediaView mediaView = new MediaView(mediaPlayer);

        root.getChildren().add(mediaView);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();*/
       /* WebView webview = new WebView();
        webview.getEngine().load(
                "C:/Users/EASV/Desktop/file_example_MP4_480_1_5MG.mp4"
        );
        webview.setPrefSize(640, 390);

        primaryStage.setScene(new Scene(webview));
        primaryStage.show();*/

    }

    public static void main(String[] args) {
        Application.launch();
    }
}
