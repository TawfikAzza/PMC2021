package gui.Controller;

import dal.db.TimeDAO;
import gui.Model.TimeManagerModel;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TimeManagerController implements Initializable {
    public DatePicker secondDatePicker;
    public DatePicker firstDatePicker;
    TimeManagerModel timeManagerModel;


    public void getFirstDate(ActionEvent actionEvent) {
    }

    public void getSecondDate(ActionEvent actionEvent) throws SQLException {
        timeManagerModel.calculateTimeElipsed(firstDatePicker,secondDatePicker);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            timeManagerModel= new TimeManagerModel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
