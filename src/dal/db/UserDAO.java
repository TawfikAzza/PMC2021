package dal.db;

import be.User;
import bll.exceptions.UserException;
import dal.ConnectionManager;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class UserDAO {
    ConnectionManager connectionManager;
    public UserDAO() throws IOException {
        connectionManager=new ConnectionManager();
    }
    public User logIn(String userName,String passWord)throws SQLException,UserException {
        String sql="SELECT id,userName, password FROM [USER] WHERE userName=?";
        try (Connection connection= connectionManager.getConnection()){
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setString(1,userName);
            preparedStatement.execute();
            ResultSet resultSet= preparedStatement.getResultSet();
            while (resultSet.next()){
                String password=resultSet.getString(3);
                if (password.equals(passWord))
                return new User(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3));
                else throw new UserException("Password is not correct, please try again.",new Exception());
            }
        }
        return null;
    }

    public User signUp(String text, String text1, DatePicker age, RadioButton rb) throws SQLException, UserException {
        checkSignUp(text,text1,age,rb);
        String sql="INSERT INTO [USER] VALUES(?,?,?,0,0)";
        try (Connection connection= connectionManager.getConnection()){
            PreparedStatement preparedStatement= connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,text);
            preparedStatement.setString(2,text1);
            preparedStatement.setInt(3, Period.between(age.getValue(), LocalDate.now()).getYears());
            preparedStatement.executeUpdate();
            ResultSet resultSet= preparedStatement.getGeneratedKeys();
            while (resultSet.next()){
                return new User(resultSet.getInt(1),text,text1);
            }
        }
        return null;
    }

    private void checkSignUp(String text, String text1, DatePicker age, RadioButton rb) throws UserException {
        if(text.isEmpty())
            throw new UserException("Please find a userName", new Exception());
        try {
            if(userNameAlreadyExists(text))
                throw new UserException("UserName is already taken, please find another one",new Exception());
        }catch (SQLException sqlException){
            throw new UserException("Something wrong went in the database",new Exception());
        }
        if(text1.isEmpty())
            throw new UserException("Please find a password", new Exception());
        if (!isValid(text1)){
            throw new UserException("Password must have at least eight characters. consists of only letters and digits and must contain at least two digits.", new Exception());
        }
        try {
            if(Period.between(age.getValue(),LocalDate.now()).getYears()<7){
                throw new UserException("Sorry, this program is for +7", new Exception());
            }
        }catch (NullPointerException e){
            throw new UserException("Please select your date of birth",new Exception());
        }
        if (!rb.isSelected()){
            throw new UserException("Please accept terms and conditions before you can sign up", new Exception());
        }
    }

    private boolean userNameAlreadyExists(String userName) throws SQLException{
        String sql="SELECT * FROM [USER] WHERE userName=?";
        try (Connection connection= connectionManager.getConnection()){
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            preparedStatement.setString(1,userName);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()){
                return true;
            }
        }
        return false;
    }
    private static boolean isValid(String password) {
        //return true if and only if password:
        if (password.length() < 8) {
            return false;
        } else {
            char c;
            int count = 1;
            for (int i = 0; i < password.length() - 1; i++) {
                c = password.charAt(i);
                if (!Character.isLetterOrDigit(c)) {
                    return false;
                } else if (Character.isDigit(c)) {
                    count++;
                    if (count < 2)   {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
