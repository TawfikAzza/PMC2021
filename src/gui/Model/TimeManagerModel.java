package gui.Model;

import be.Stats;
import bll.PMCFacade;
import bll.PMCManager;
import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class TimeManagerModel {
    PMCFacade pmcFacade;
    public TimeManagerModel() throws IOException {
        pmcFacade= new PMCManager();
    }

    public Stats calculateTime(DatePicker firstDatePicker, DatePicker secondDatePicker) throws SQLException {
        int totalTime=0,totalMovies=0;
        for(Stats stats:pmcFacade.getAllStats(firstDatePicker.getValue().minusDays(1),secondDatePicker.getValue().plusDays(1))){
           totalTime+=stats.getSeconds() ;
           totalMovies+=stats.getMovies();
        }
        return new Stats(totalMovies,totalTime);
    }

    public void updateTime(Instant start) throws SQLException {
        pmcFacade.updateTime(Duration.between(start,Instant.now()).getSeconds());
    }

    public LocalDate getFirstDate() throws SQLException {
        return Instant.ofEpochMilli(pmcFacade.getFirstDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public List<Stats> getAllStats(DatePicker firstDatePicker, DatePicker secondDatePicker) throws SQLException {
        return pmcFacade.getAllStats(firstDatePicker.getValue().minusDays(1),secondDatePicker.getValue().plusDays(1));
    }
}
