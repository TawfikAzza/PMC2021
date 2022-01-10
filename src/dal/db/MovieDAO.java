package dal.db;

import be.CategoryMovie;
import be.Movie;

import bll.exceptions.MovieException;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.ConnectionManager;
import dal.interfaces.IMovieDataAccess;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieDAO implements IMovieDataAccess {
    ConnectionManager cm;
    CategoryDAO categoryDAO;
    public MovieDAO() throws IOException {
        cm = new ConnectionManager();
        categoryDAO = new CategoryDAO();
    }
    public List<Movie> getAllMovies() throws SQLException {
        List<Movie> allMovies = new ArrayList<>();
        try(Connection con = cm.getConnection()) {
            String sqlSelectAllMovies = "SELECT * FROM MOVIE";
            PreparedStatement statementSelect = con.prepareStatement(sqlSelectAllMovies);
            ResultSet rs = statementSelect.executeQuery();
            while(rs.next()){
                Movie movie = new Movie(rs.getInt("id")
                        ,rs.getString("name")
                        ,rs.getFloat("rating")
                        ,rs.getFloat("imdbRating")
                        ,new File(rs.getString("fileLink"))
                        ,rs.getString("lastView")
                        ,rs.getString("trailerLink")
                        ,rs.getString("summary"));

                allMovies.add(movie);
            }
            String sqlSelectCatMovie="SELECT CategoryID FROM CatMovie WHERE MovieId = ?;";
            PreparedStatement statementSelectCatMovie = con.prepareStatement(sqlSelectCatMovie);
            HashMap<Integer,CategoryMovie> mapCatMovie = new HashMap<>();
            mapCatMovie= getAllCategories();
            for (Movie movie : allMovies) {
                statementSelectCatMovie.setInt(1,movie.getId());
                ResultSet rsSelectCatMovie = statementSelectCatMovie.executeQuery();

                while (rsSelectCatMovie.next()) {
                    movie.getMovieGenres().put(rsSelectCatMovie.getInt("CategoryID")
                                            ,mapCatMovie.get(rsSelectCatMovie.getInt("CategoryID")));
                }
            }
        }
        return allMovies;
    }

    public Movie createMovie(Movie movie) throws SQLException, MovieException {
        exceptionCreationUpdate(movie,true);
        Movie movieCreated = null;
        try(Connection con = cm.getConnection()) {
            String sqlCreate = "INSERT INTO MOVIE VALUES (?,?,?,?,getDate(),?,?)";
            PreparedStatement statementCreate = con.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
            statementCreate.setString(1,movie.getName());
            statementCreate.setDouble(2,movie.getRating());
            statementCreate.setDouble(3,movie.getImdbRating());
            statementCreate.setString(4,movie.getFileLink().toString());
            statementCreate.setString(5,movie.getTrailerLink());
            statementCreate.setString(6,movie.getSummary());

            ResultSet rs = statementCreate.executeQuery();
            while(rs.next()) {
                movieCreated = new Movie(rs.getInt(1)
                        ,movie.getName()
                        ,movie.getRating()
                        ,movie.getImdbRating()
                        ,movie.getFileLink()
                        ,"test"
                        ,movie.getTrailerLink()
                        ,movie.getSummary());
            }
        }
        return movieCreated;
    }
    public void updateMovie(Movie movie) throws SQLException, MovieException {
        exceptionCreationUpdate(movie,false);
        try(Connection con = cm.getConnection()) {
            String sqlUpdate = "UPDATE MOVIE SET name = ?, rating = ?, imdbRating = ? , fileLink = ? , trailerLink = ? , summary = ? WHERE id = ?";
            PreparedStatement statementUpdate = con.prepareStatement(sqlUpdate);
            statementUpdate.setString(1,movie.getName());
            statementUpdate.setDouble(2,movie.getRating());
            statementUpdate.setDouble(3,movie.getImdbRating());
            statementUpdate.setString(4,movie.getFileLink().toString());
            statementUpdate.setString(5,movie.getTrailerLink());
            statementUpdate.setString(6,movie.getSummary());
            statementUpdate.setInt(7,movie.getId());

            statementUpdate.executeUpdate();

        }
    }
    public void deleteMovie(Movie movie) throws SQLException {
        try(Connection con = cm.getConnection()) {
            String sqlDeleteFromMovie = "DELETE FROM MOVIE WHERE ID=?";
            PreparedStatement statementDeleteFromMovie = con.prepareStatement(sqlDeleteFromMovie);
            statementDeleteFromMovie.setInt(1,movie.getId());
            String sqlDeleteFromCatMovie = "DELETE FROM CATMOVIE WHERE MovieID=?";
            PreparedStatement statementDeleteFromCatMovie = con.prepareStatement(sqlDeleteFromCatMovie);
            statementDeleteFromCatMovie.setInt(1,movie.getId());
            statementDeleteFromMovie.execute();
            statementDeleteFromCatMovie.execute();

        }
    }
    private HashMap<Integer, CategoryMovie> getAllCategories() throws SQLException {
        List<CategoryMovie> allCats =categoryDAO.getAllCategories();
        HashMap<Integer,CategoryMovie> mapCategories= new HashMap<>();
        for(CategoryMovie cat: allCats) {
            mapCategories.put(cat.getId(),cat);
        }
        return mapCategories;
    }
    private boolean movieAlreadyExists(Movie movie)throws SQLException{
        String sql="SELECT * FROM Movie WHERE fileLink=?";
        try (Connection connection=cm.getConnection()){
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setString(1, movie.getFileLink().toString());
            preparedStatement.execute();
            ResultSet resultSet= preparedStatement.getResultSet();
            if (resultSet.next())
                return true;
        }
        return false;
    }

    private void exceptionCreationUpdate(Movie movie,Boolean create) throws MovieException {
        if (movie.getName().isEmpty())
            throw new MovieException("Please find a name for your movie",new Exception());
        if (movie.getRating()>10||movie.getRating()<0)
            throw new MovieException("Ratings go from 0 to 10",new Exception());
        if(movie.getImdbRating()>10||movie.getImdbRating()<0)
            throw new MovieException("Imdb ratings go from 0 to 10",new Exception());
        if (create){
        if (!movie.getFileLink().isFile())
            throw new MovieException("Please find a path for your movie",new Exception());

        try {
                if(movieAlreadyExists(movie))
                    throw new MovieException("Movie already Exists. \nFind an other path.",new Exception());
            }catch (SQLException e){throw new MovieException("Something went wrong in the database.",new Exception());}}
    }
    public List<Movie>getAllOutdatedMovies() throws SQLException{
        List<Movie>allOutdatedMovies=new ArrayList<>();
        String sql= "SELECT *, DATEDIFF(year, lastView, GETDATE()) AS difference FROM Movie";
        try (Connection connection= cm.getConnection()){
            ResultSet resultSet= connection.createStatement().executeQuery(sql);
            while (resultSet.next()){
                if (resultSet.getInt("difference")>=1){
                    Movie movie = new Movie(resultSet.getInt("id")
                            ,resultSet.getString("name")
                            ,resultSet.getFloat("rating")
                            ,resultSet.getFloat("imdbRating")
                            ,new File(resultSet.getString("fileLink"))
                            ,resultSet.getString("lastView")
                            ,resultSet.getString("trailerLink")
                            ,resultSet.getString("summary"));
                    allOutdatedMovies.add(movie);
                }
            }
        }
        return allOutdatedMovies;
    }
}
