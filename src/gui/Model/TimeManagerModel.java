package gui.Model;

import be.Time;
import dal.db.TimeDAO;
import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.sql.SQLException;

public class TimeManagerModel {
    TimeDAO timeDAO;
    public TimeManagerModel() throws IOException {
        timeDAO= new TimeDAO();
    }

    public Time calculateTimeElipsed(DatePicker firstDatePicker, DatePicker secondDatePicker) throws SQLException {

        return timeDAO.elipsedtime(firstDatePicker.getValue().minusDays(1),secondDatePicker.getValue().plusDays(1));

    }
}
