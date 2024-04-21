package se.utility.dbUtil;

import com.microsoft.sqlserver.jdbc.ISQLServerConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

public final class DbConnectionService {

    public DbConnectionService(){}

    private final String _dbConnectionString =
            "jdbc:sqlserver://JANKYLEWIS\\SQLEXPRESS:1433;databaseName=SpotifyAutomationTestDb;trustServerCertificate=true;integratedSecurity=false";
    private final String _username = "sa";
    private final String _password = "123123";

    private final String _jdbcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private Connection _connection;

    private UUID _clientConnectionId;

    public Connection makeConnection() throws ClassNotFoundException, SQLException {

        //Loading the JDBC driver class
        Class.forName(_jdbcDriver);

        //Connection establishment
        try {
            _connection = DriverManager.getConnection(_dbConnectionString, _username, _password);
            _clientConnectionId = ((ISQLServerConnection)_connection).getClientConnectionId();
        } catch(SQLException sqlException) {
            throw new SQLException(
                    "Error came up with SQL Server Connection: Client Connection ID = <" + _clientConnectionId + ">     "
            );
        }
        return _connection;
    }

    public DbConnectionService disposeResourcedSqlServices() throws SQLException {
        _connection.close();
        return this;
    }
}