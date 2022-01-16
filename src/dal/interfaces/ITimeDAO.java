package dal.interfaces;

import be.Time;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

public interface ITimeDAO {
    void newTime(int movies,long seconds) throws SQLException;
    Time elipsedtime(LocalDate firstDate, LocalDate secondDate)throws SQLException;
    void updateTime(long seconds) throws SQLException;
    void updateMovies()throws SQLException;
    Date getFirstDate()throws SQLException;
}
