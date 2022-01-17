package gui.Controller;

import be.Stats;
import gui.Model.TimeManagerModel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
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
    public DatePicker secondDatePicker;
    public DatePicker firstDatePicker;
    public Label totalTime;
    public Label totalMovies;
    public Button closeWindowButton;
    public AreaChart areaChart;


    TimeManagerModel timeManagerModel;
    Instant start;


    public void getFirstDate(ActionEvent actionEvent) throws SQLException {
        timeManagerModel.updateTime(start);
        try {
            drawAreaChart();
            Stats timeElipsed =timeManagerModel.calculateTime(firstDatePicker,secondDatePicker);
            totalTime.setText(calculateTime(timeElipsed.getSeconds()));
            totalMovies.setText(String.valueOf(timeElipsed.getMovies()));
        }catch (NullPointerException ignored){}

    }

    public void getSecondDate(ActionEvent actionEvent) throws SQLException {
        timeManagerModel.updateTime(start);
        try {
            drawAreaChart();
            Stats timeElipsed =timeManagerModel.calculateTime(firstDatePicker,secondDatePicker);
            totalTime.setText(calculateTime(timeElipsed.getSeconds()));
            totalMovies.setText(String.valueOf(timeElipsed.getMovies()));
        }catch (NullPointerException ignored){}

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstDatePicker.getEditor().setDisable(true);
        secondDatePicker.getEditor().setDisable(true);

        try {
            timeManagerModel= new TimeManagerModel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LocalDate firstDate = null;
        try {
            firstDate = timeManagerModel.getFirstDate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LocalDate finalFirstDate = firstDate;
        firstDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(empty || item.compareTo(finalFirstDate) < 0||item.compareTo(LocalDate.now()) > 0);
                    }
                };
            }
        });
        LocalDate finalFirstDate1 = firstDate;
        secondDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(empty || item.compareTo(finalFirstDate) < 0||item.compareTo(LocalDate.now()) > 0);
                    }
                };
            }
        });
    }
    public static String calculateTime(long seconds) {
        int day = (int)TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);
        return (day+"d"+hours+ "h" + minute+"m" + second + "s.");

    }

    public void setInstant(Instant start) {
        this.start=start;
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) closeWindowButton.getScene().getWindow();
        stage.close();
    }
    private void drawAreaChart() throws SQLException {
        XYChart.Series series= new XYChart.Series();
        List<Stats>allStats=timeManagerModel.getAllStats(firstDatePicker,secondDatePicker);
        Date firstDate=allStats.get(0).getDate();
        XYChart.Data data = new XYChart.Data<>(firstDate.toLocalDate().toString(),allStats.get(0).getSeconds());
        allStats.remove(allStats.get(0));
        for (Stats stats:allStats){
            if(stats.getDate().equals(firstDate))
                data.YValueProperty().setValue((Long)data.YValueProperty().get()+stats.getSeconds());
            else {
                series.getData().add(data);
                firstDate=stats.getDate();
                data=new XYChart.Data<>(firstDate.toLocalDate().toString(),stats.getSeconds());
            }
        }
        series.getData().add(data);
        areaChart.getData().add(series);

    }

}
