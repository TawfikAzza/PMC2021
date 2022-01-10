package dal.db;

import be.CategoryMovie;
import be.Movie;
import bll.exceptions.CategoryException;
import bll.exceptions.MovieException;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dal.ConnectionManager;
import dal.interfaces.ICategoryDataAccess;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryDAO implements ICategoryDataAccess {
    private final ConnectionManager cm;
    public CategoryDAO() throws IOException {
        cm = new ConnectionManager();
    }
    public List<CategoryMovie> getAllCategories() throws SQLException {
        List<CategoryMovie> listCategories = new ArrayList<>();
        try(Connection con = cm.getConnection()){
            String sqlSelectAll = "SELECT * FROM CATEGORY";
            PreparedStatement statementSelectALl = con.prepareStatement(sqlSelectAll);
            ResultSet rs = statementSelectALl.executeQuery();
            while(rs.next()) {
                CategoryMovie categoryMovie = new CategoryMovie(rs.getInt("id"),rs.getString("name"));
                listCategories.add(categoryMovie);
            }
        }
        return listCategories;
    }
    public CategoryMovie getCategory(int idCategory) throws SQLException {
        CategoryMovie categorySearched = null;
        try(Connection con = cm.getConnection()){
            String sqlSelectCat = "SELECT * FROM CATEGORY WHERE id = ?";
            PreparedStatement statementSelectCat = con.prepareStatement(sqlSelectCat);
            statementSelectCat.setInt(1,idCategory);
            ResultSet rs = statementSelectCat.executeQuery();
            while(rs.next()){
                categorySearched = new CategoryMovie(rs.getInt("id"),rs.getString("name"));
            }
        }
        return categorySearched;
    }
    public List<CategoryMovie> getCategoryFromMovie(Movie movie) throws SQLException {
        List<CategoryMovie> allCategories = new ArrayList<>();
        try(Connection con = cm.getConnection()){
            String sqlSelectCat = "SELECT CATEGORY.id as idCat, CATEGORY.name as nameCat FROM CATEGORY" +
                                " INNER JOIN CATMOVIE ON CATMOVIE.CategoryID = CATEGORY.ID " +
                                "WHERE CatMOVIE.MovieID = ?";
            PreparedStatement statementSelectCat = con.prepareStatement(sqlSelectCat);
            statementSelectCat.setInt(1,movie.getId());
            ResultSet rs = statementSelectCat.executeQuery();
            while(rs.next()){
               CategoryMovie category= new CategoryMovie(rs.getInt("idCat"),rs.getString("nameCat"));
               allCategories.add(category);
            }
        }
        return allCategories;
    }
    public void addCategoryFromMovie(Movie movie) throws SQLException {
        try(Connection con = cm.getConnection()) {
            String sqlDeleteRelation = "DELETE FROM CATMOVIE WHERE MovieID = ?;";
            PreparedStatement statementDeleteRelation = con.prepareStatement(sqlDeleteRelation);
            statementDeleteRelation.setInt(1,movie.getId());
            statementDeleteRelation.execute();

            String sqlInsertCatMovies = "INSERT INTO CatMovie VALUES (?,?)";
            PreparedStatement statementInsertCatMovie = con.prepareStatement(sqlInsertCatMovies);
            for (Map.Entry entry : movie.getMovieGenres().entrySet()) {
                CategoryMovie categoryMovie = (CategoryMovie) entry.getValue();
                statementInsertCatMovie.setInt(1, categoryMovie.getId());
                statementInsertCatMovie.setInt(2,movie.getId());
                statementInsertCatMovie.addBatch();
            }
            statementInsertCatMovie.executeBatch();
        }
    }
    public CategoryMovie addCategory(CategoryMovie categoryMovie) throws SQLException, CategoryException {
        categoryException(categoryMovie);
        CategoryMovie categoryCreated = null;
        try(Connection con = cm.getConnection()){
            String sqlInsert = "INSERT INTO CATEGORY VALUES (?)";
            PreparedStatement statementInsert = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            statementInsert.setString(1,categoryMovie.getName());
            ResultSet rs = statementInsert.executeQuery();
            while(rs.next()){
                categoryCreated = new CategoryMovie(rs.getInt(1),categoryMovie.getName());
            }
        }
        return categoryCreated;
    }
    public void removeCategory(CategoryMovie categoryMovie) throws SQLException {
        try(Connection con = cm.getConnection()){
            String sqlDelete = "DELETE FROM CATEGORY WHERE id = ?";
            PreparedStatement statementDeleteCat = con.prepareStatement(sqlDelete);
            statementDeleteCat.setInt(1,categoryMovie.getId());

            String sqlDeleteRelation = "DELETE FROM CatMovie WHERE CategoryID=?";
            PreparedStatement statementDeleteRelation = con.prepareStatement(sqlDeleteRelation);
            statementDeleteRelation.setInt(1,categoryMovie.getId());

            statementDeleteCat.execute();
            statementDeleteCat.execute();
        }
    }
    public void updateCategory(CategoryMovie categoryMovie) throws SQLException, CategoryException {
        categoryException(categoryMovie);
        try(Connection con = cm.getConnection()){
            String sqlUpdateCategory = "UPDATE CATEGORY SET name = ? WHERE id = ?";
            PreparedStatement statementUpdate = con.prepareStatement(sqlUpdateCategory);
            statementUpdate.setString(1,categoryMovie.getName());
            statementUpdate.setInt(2,categoryMovie.getId());
            statementUpdate.executeUpdate();
        }
    }
    private void categoryException(CategoryMovie categoryMovie)throws CategoryException{
        try {
            if(categoryAlreadyExists(categoryMovie))
                throw new CategoryException("Category already exists.\nPlease find another name.",new Exception());
        }catch (SQLException e){
            throw new CategoryException("Something wrong went in the database,\nPlease try again.",new Exception());
        }

    }

    private boolean categoryAlreadyExists(CategoryMovie categoryMovie) throws SQLException {
        String sql="SELECT * FROM Category WHERE name =?";
        try (Connection connection= cm.getConnection()){
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setString(1,categoryMovie.getName());
            preparedStatement.execute();
            ResultSet resultSet= preparedStatement.getResultSet();
            if (resultSet.next())
                return true;
        }
        return false;
    }
}
