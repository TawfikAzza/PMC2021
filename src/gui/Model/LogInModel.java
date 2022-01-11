package gui.Model;

import be.User;
import bll.PMCFacade;
import bll.PMCManager;
import bll.exceptions.UserException;
import dal.db.UserDAO;

import java.io.IOException;
import java.sql.SQLException;

public class LogInModel {
    PMCFacade pmcFacade;
    UserDAO userDAO;
    public LogInModel() throws IOException {
        pmcFacade=new PMCManager();
        userDAO= new UserDAO();
    }

    public User logIn(String text, String text1) throws SQLException, UserException {
         return userDAO.logIn(text,text1);
    }
}
