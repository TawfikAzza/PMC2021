package dal.interfaces;

import be.Stats;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IStatsDAO {
    void newTime(int movies,long seconds) throws SQLException;
    List<Stats> getAllStats(LocalDate firstDate, LocalDate secondDate)throws SQLException;
    void updateTime(long seconds) throws SQLException;
    void updateMovies()throws SQLException;
    Date getFirstDate()throws SQLException;
}
