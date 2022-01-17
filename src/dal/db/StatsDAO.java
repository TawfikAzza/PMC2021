package dal.db;

import be.Stats;
import dal.ConnectionManager;
import dal.interfaces.IStatsDAO;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StatsDAO implements IStatsDAO {
    ConnectionManager connectionManager;
    public StatsDAO() throws IOException {
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
    public List<Stats> getAllStats(LocalDate firstDate, LocalDate secondDate) throws SQLException{
        List<Stats>allStats= new ArrayList<>();

        String sql="SELECT * FROM ElipsedTime WHERE lastConnection BETWEEN ? AND ?";
        try (Connection connection= connectionManager.getConnection()){
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setDate(1, Date.valueOf(firstDate));
            preparedStatement.setDate(2, Date.valueOf(secondDate));
            preparedStatement.execute();
            ResultSet resultSet= preparedStatement.getResultSet();
            while (resultSet.next()){
                allStats.add(new Stats(resultSet.getInt("movies"),resultSet.getLong("elipsedTime"),resultSet.getDate("lastConnection")));
            }
        }
        return allStats;
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

    @Override
    public java.util.Date getFirstDate() throws SQLException {
        Date firstDate=null;
        String sql= "SELECT TOP 1 * FROM ElipsedTime";
        try (Connection connection= connectionManager.getConnection()){
            Statement statement = connection.createStatement();
            statement.execute(sql);
            ResultSet resultSet= statement.getResultSet();
            while (resultSet.next())
                firstDate=resultSet.getDate("lastConnection");
        }
        return firstDate;
    }

}
