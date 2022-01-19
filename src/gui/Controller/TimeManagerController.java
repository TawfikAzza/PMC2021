package gui.Controller;

import be.CategoryMovie;
import be.Movie;
import be.Stats;
import bll.exceptions.CategoryException;
import bll.exceptions.MovieException;
import gui.Model.MovieModel;
import gui.Model.TimeManagerModel;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class TimeManagerController implements Initializable {
    @FXML
    public AnchorPane pane;
    @FXML
    private DatePicker secondDatePicker;
    @FXML
    private DatePicker firstDatePicker;
    @FXML
    private Label totalTime;
    @FXML
    private Label totalMovies;
    @FXML
    private Button closeWindowButton;
    @FXML
    private AreaChart areaChart;
    @FXML
    private PieChart moviesPerCategories;
    @FXML
    private Label moviesAvailable;
    @FXML
    private Label personalAverageRating;
    @FXML
    private Label ImdbAverageRating;
    @FXML
    private Label highestRatedMovie;
    @FXML
    private Label lowestRatedMovie;
    @FXML
    private ComboBox<CategoryMovie> selectCategoriesComboBox;
    private MovieModel movieModel;
    private TimeManagerModel timeManagerModel;
    private Instant start;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pane.getStylesheets().add(getClass().getResource("/css/stats.css").toExternalForm());
        try {
            movieModel = new MovieModel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            timeManagerModel = new TimeManagerModel();
        } catch (IOException e) {
            e.printStackTrace();
        }

        firstDatePicker.getEditor().setDisable(true);
        secondDatePicker.getEditor().setDisable(true);
        setLimitsDatePicker(firstDatePicker);
        setLimitsDatePicker(secondDatePicker);
        drawPieChart();

        try {
            selectCategoriesComboBox.getItems().addAll(movieModel.getAllCategories());
        } catch (CategoryException e) {
            e.printStackTrace();
        }
    }


    public void getFirstDate(ActionEvent actionEvent) throws SQLException {
        timeManagerModel.updateTime(start);
        try {
            drawAreaChart();
            Stats timeElipsed = timeManagerModel.calculateTime(firstDatePicker, secondDatePicker);
            totalTime.setText(calculateTime(timeElipsed.getSeconds()));
            totalMovies.setText(String.valueOf(timeElipsed.getMovies()));
        } catch (NullPointerException ignored) {
        }

    }

    public void getSecondDate(ActionEvent actionEvent) throws SQLException {
        timeManagerModel.updateTime(start);
        try {
            drawAreaChart();
            Stats timeElipsed = timeManagerModel.calculateTime(firstDatePicker, secondDatePicker);
            totalTime.setText(calculateTime(timeElipsed.getSeconds()));
            totalMovies.setText(String.valueOf(timeElipsed.getMovies()));
        } catch (NullPointerException ignored) {
        }

    }

    public static String calculateTime(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);
        return (day + "d" + hours + "h" + minute + "m" + second + "s.");

    }

    public void setInstant(Instant start) {
        this.start = start;
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) closeWindowButton.getScene().getWindow();
        stage.close();
    }

    /**
     * In this method, we try to add the time elapsed by the user in the program for every day,
     * which will be represented later in an areaChart.
     * fx: if the user used the program twice a day, and spent respectively 1, and  2 hours,
     * than the total time he spent during the whole day is 3 hours.
     * We take that into consideration all the user infos during the selected period of time.
     *
     * @throws SQLException
     */
    private void drawAreaChart() throws SQLException {
        XYChart.Series series = new XYChart.Series();
        List<Stats> allStats = timeManagerModel.getAllStats(firstDatePicker, secondDatePicker);
        Date firstDate = allStats.get(0).getDate();
        XYChart.Data data = new XYChart.Data<>(firstDate.toLocalDate().toString(), allStats.get(0).getSeconds());
        allStats.remove(allStats.get(0));
        for (Stats stats : allStats) {
            if (stats.getDate().equals(firstDate))
                data.YValueProperty().setValue((Long) data.YValueProperty().get() + stats.getSeconds());
            else {
                series.getData().add(data);
                firstDate = stats.getDate();
                data = new XYChart.Data<>(firstDate.toLocalDate().toString(), stats.getSeconds());
            }
        }
        series.getData().add(data);
        areaChart.getData().add(series);

    }

    public void selectCategory(ActionEvent actionEvent) throws MovieException {
        double counter = 0, imdbRating = 0, personalRating = 0;
        double lowestRating = 11;
        double highestRating = -1;
        String highestRatedMovieName = "", lowestRatedMovieName = "";
        List<Movie> allMovies = movieModel.getAllMovies();
        for (Movie movie : allMovies) {
            if (movie.getMovieGenres().get(((CategoryMovie) selectCategoriesComboBox.getSelectionModel().getSelectedItem()).getId()) != null) {
                counter += 1;
                imdbRating += movie.getImdbRating();
                personalRating += movie.getRating();
                if (movie.getRating() > highestRating) {
                    highestRatedMovieName = movie.getName();
                    highestRating = movie.getRating();
                }
                if (movie.getRating() < lowestRating) {
                    lowestRatedMovieName = movie.getName();
                    lowestRating = movie.getRating();
                }
            }
        }
        moviesAvailable.setText(String.valueOf(counter));
        highestRatedMovie.setText(highestRatedMovieName);
        lowestRatedMovie.setText(lowestRatedMovieName);
        ImdbAverageRating.setText(String.format("%.2f", imdbRating / counter));
        personalAverageRating.setText(String.format("%.2f", personalRating / counter));

    }

    private void setLimitsDatePicker(DatePicker datePicker) {
        LocalDate firstDate = null;
        try {
            firstDate = timeManagerModel.getFirstDate();
        } catch (SQLException ignored) {
        }
        LocalDate finalFirstDate = firstDate;
        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(empty || item.compareTo(finalFirstDate) < 0 || item.compareTo(LocalDate.now()) > 0);
                    }
                };
            }
        });
    }

    /**
     * in this method, we are drawing a pie representing how many movies we have per category in %.
     * We filter for each category and check if a movie belongs to that category or not,
     * if it is the case, we increase the counter by one and then calculate all the movies available,
     * so we can divide later to get the average movies per category.
     */
    private void drawPieChart() {
        int total = 0;
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try {
            List<Movie> allMovies = movieModel.getAllMovies();
            List<CategoryMovie> allCategories = movieModel.getAllCategories();
            for (CategoryMovie categoryMovie : allCategories) {
                int counter = 0;
                for (Movie movie : allMovies) {
                    if (movie.getMovieGenres().get(categoryMovie.getId()) != null) {
                        counter += 1;
                        total += 1;
                    }
                }
                pieChartData.add(new PieChart.Data(categoryMovie.getName(), counter));
            }
            int finalTotal = total;
            pieChartData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(), " ", Bindings.format("%.2f", data.pieValueProperty().divide(finalTotal).multiply(100)), "%.")));
            moviesPerCategories.getData().addAll(pieChartData);

        } catch (CategoryException | MovieException e) {
            e.printStackTrace();
        }
    }
}

