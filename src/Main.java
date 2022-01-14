import dal.db.MovieDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class Main extends Application {
    MovieDAO movieDAO;

    public Main() throws IOException {
        movieDAO = new MovieDAO();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Views/MainWindow.fxml"));
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/gui/Views/outdatedMovies.fxml"));
        Parent root = loader.load();
        Parent root1 = loader1.load();
        Scene scene;
        if (movieDAO.getAllOutdatedMovies().isEmpty()) {
            scene = new Scene(root, 1085, 600);
        } else {
            scene = new Scene(root1);
        }
        root.getStylesheets().add("css/main.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("PMC 2022");
        File file = new File("src/css/data/playImagotype.png");
        Image imagotype = new Image(file.toURI().toString());
        primaryStage.getIcons().add(imagotype);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Instant start = Instant.now();
        Application.launch();
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end).getSeconds());
    }
}
