package gui.Model;
import bll.exceptions.UserException;
import dal.db.UserDAO;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import java.io.IOException;
import java.sql.SQLException;

public class SignUpModel {
    UserDAO userDAO;
    public SignUpModel() throws IOException {
        userDAO= new UserDAO();
    }
    public void signUp(String text, String text1, DatePicker age, RadioButton rb) throws SQLException, UserException {
        userDAO.signUp(text,text1,age,rb);
    }
}
