package dal.interfaces;

import be.Time;

import java.sql.SQLException;
import java.util.Date;

public interface ITimeDAO {
    void newTime(int movies,long seconds) throws SQLException;
    Time elipsedtime(Date firstDate, Date secondDate)throws SQLException;
    void updateTime(long seconds) throws SQLException;
    void updateMovies()throws SQLException;
}
