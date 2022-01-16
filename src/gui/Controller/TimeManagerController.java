package gui.Controller;

import be.Time;
import dal.db.TimeDAO;
import gui.Model.TimeManagerModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class TimeManagerController implements Initializable {
    public DatePicker secondDatePicker;
    public DatePicker firstDatePicker;
    public Label totalTime;
    public Label totalMovies;


    TimeManagerModel timeManagerModel;
    Instant start;


    public void getFirstDate(ActionEvent actionEvent) {
    }

    public void getSecondDate(ActionEvent actionEvent) throws SQLException {
        timeManagerModel.updateTime(start);
        Time timeElipsed =timeManagerModel.calculateTimeElipsed(firstDatePicker,secondDatePicker);
            totalTime.setText(calculateTime(timeElipsed.getSeconds()));
            totalMovies.setText(String.valueOf(timeElipsed.getMovies()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            timeManagerModel= new TimeManagerModel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String calculateTime(long seconds) {
        int day = (int)TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

        return (day+" days "+hours+ " hours " + minute+" minutes " + second + " seconds.");

    }

    public void setInstant(Instant start) {
        this.start=start;
    }
}
