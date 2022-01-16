package gui.Model;

import dal.db.TimeDAO;
import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.sql.SQLException;

public class TimeManagerModel {
    TimeDAO timeDAO;
    public TimeManagerModel() throws IOException {
        timeDAO= new TimeDAO();
    }

    public void calculateTimeElipsed(DatePicker firstDatePicker, DatePicker secondDatePicker) throws SQLException {
        System.out.println(timeDAO.elipsedtime(firstDatePicker.getValue(),secondDatePicker.getValue()).getSeconds());

    }
}
