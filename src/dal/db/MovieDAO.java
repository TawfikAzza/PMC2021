package dal.db;

import be.CategoryMovie;
import be.Movie;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.ConnectionManager;
import dal.interfaces.IMovieDataAccess;

import java.io.File;
import java.io.IOException;
import java.sql.*;
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
                        ,rs.getString("lastView"));

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

    public Movie createMovie(Movie movie) throws SQLException {
        Movie movieCreated = null;
        try(Connection con = cm.getConnection()) {
            String sqlCreate = "INSERT INTO MOVIE VALUES (?,?,?,?,getDate())";
            PreparedStatement statementCreate = con.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
            statementCreate.setString(1,movie.getName());
            statementCreate.setDouble(2,movie.getRating());
            statementCreate.setDouble(3,movie.getImdbRating());
            statementCreate.setString(4,movie.getFileLink().toString());
            ResultSet rs = statementCreate.executeQuery();
            while(rs.next()) {
                movieCreated = new Movie(rs.getInt(1)
                        ,movie.getName()
                        ,movie.getRating()
                        ,movie.getImdbRating()
                        ,movie.getFileLink()
                        ,"test");
            }
        }
        return movieCreated;
    }
    public void updateMovie(Movie movie) throws SQLException {
        try(Connection con = cm.getConnection()) {
            String sqlUpdate = "UPDATE MOVIE SET name = ?, rating = ?, imdbRating = ? , fileLink = ? WHERE id = ?";
            PreparedStatement statementUpdate = con.prepareStatement(sqlUpdate);
            statementUpdate.setString(1,movie.getName());
            statementUpdate.setDouble(2,movie.getRating());
            statementUpdate.setDouble(3,movie.getImdbRating());
            statementUpdate.setString(4,movie.getFileLink().toString());
            statementUpdate.setInt(5,movie.getId());

            int i = statementUpdate.executeUpdate();

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
}
