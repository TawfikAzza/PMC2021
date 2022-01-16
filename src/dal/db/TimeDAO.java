package dal.db;

import be.Time;
import dal.ConnectionManager;
import dal.interfaces.ITimeDAO;

import java.io.IOException;
import java.sql.*;
import java.util.Date;

public class TimeDAO implements ITimeDAO {
    ConnectionManager connectionManager;
    public TimeDAO() throws IOException {
        connectionManager= new ConnectionManager();
    }

    @Override
    public void newTime(int movies, long seconds) throws SQLException {
        String sql = "INSERT INTO ElipsedTime VALUES (?,?,getDate())";
        try (Connection connection= connectionManager.getConnection()){
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setInt(1,movies);
            preparedStatement.setLong(2,seconds);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public Time elipsedtime(Date firstDate, Date secondDate) throws SQLException{
        Time time = null;
        long totalSeconds=0;
        int totalMovies=0;
        String sql="SELECT * FROM ElipsedTime WHERE [date] BETWEEN ? AND ?";
        try (Connection connection= connectionManager.getConnection()){
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setDate(1, (java.sql.Date) firstDate);
            preparedStatement.setDate(2, (java.sql.Date) secondDate);
            preparedStatement.execute();
            ResultSet resultSet= preparedStatement.getResultSet();
            while (resultSet.next()){
                totalMovies+= resultSet.getInt("movies");
                totalSeconds+=resultSet.getLong("elipsedTime");
                time= new Time(totalMovies,totalSeconds);
            }
        }


        return time;
    }

    @Override
    public void updateTime(long seconds) throws SQLException {
        String sql = "UPDATE ElipsedTime SET elipsedTime=elipsedTime+? WHERE ID= (SELECT MAX(id) FROM ElipsedTime)";
        try (Connection connection= connectionManager.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1,seconds);
            preparedStatement.executeUpdate();
                }
            }

    @Override
    public void updateMovies() throws SQLException {
        String sql = "UPDATE ElipsedTime SET movies=movies+1 WHERE ID= (SELECT MAX(id) FROM ElipsedTime)";
        try (Connection connection= connectionManager.getConnection()){
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
    }

}
