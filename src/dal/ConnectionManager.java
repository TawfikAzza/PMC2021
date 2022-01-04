package dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

/**
 *
 * @author jeppjleemoritzled
 * Used and abused by Team1, thanks again Jeppe ! o/
 */

public class ConnectionManager {
    private static final String CONFIG_FILE_NAME = "PCM2021.cfg";
    private final SQLServerDataSource ds;

    public ConnectionManager() throws IOException
    {
        Properties props = new Properties();
        props.load(new FileReader(CONFIG_FILE_NAME));

        ds = new SQLServerDataSource();
        ds.setServerName(props.getProperty("SERVER"));
        ds.setDatabaseName(props.getProperty("DATABASE"));
        ds.setPortNumber(Integer.parseInt(props.getProperty("PORT")));
        ds.setUser(props.getProperty("USER"));
        ds.setPassword(props.getProperty("PASSWORD"));
    }

    public Connection getConnection() throws SQLServerException
    {
        return ds.getConnection();
    }
}
