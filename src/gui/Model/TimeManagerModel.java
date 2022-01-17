package gui.Model;

import be.Time;
import bll.PMCFacade;
import bll.PMCManager;
import dal.db.TimeDAO;
import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class TimeManagerModel {
    PMCFacade pmcFacade;
    public TimeManagerModel() throws IOException {
        pmcFacade= new PMCManager();
    }

    public Time calculateTimeElipsed(DatePicker firstDatePicker, DatePicker secondDatePicker) throws SQLException {
        return pmcFacade.elipsedtime(firstDatePicker.getValue().minusDays(1),secondDatePicker.getValue().plusDays(1));
    }

    public void updateTime(Instant start) throws SQLException {
        pmcFacade.updateTime(Duration.between(start,Instant.now()).getSeconds());
    }

    public LocalDate getFirstDate() throws SQLException {
        return Instant.ofEpochMilli(pmcFacade.getFirstDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();

    }
}
