package se.utility.dbUtil;

import com.microsoft.sqlserver.jdbc.ISQLServerConnection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import se.credentialFactory.DbCredentialFactory;

//This class manipulates the connection to SQL Server namely creating or repelling connection
public final class DbConnectionService {

    //A DbConnectionService constructor represents a DB manipulation going along within a program
    public DbConnectionService(){}
    public DbConnectionService(String dbName){

        try { _dbCredentialMap = _dbCredentialFactory.retrieveADbCredential(dbName); }
        catch (FileNotFoundException fileNotFoundException) { throw new RuntimeException("Could not find the DB file: " + fileNotFoundException); }

        _dbConnectionString = _dbCredentialMap.get("db_connection_string");
        _username = _dbCredentialMap.get("username");
        _password = _dbCredentialMap.get("password");
        _jdbcDriver = _dbCredentialMap.get("jdbc_driver");
    }

    private DbCredentialFactory _dbCredentialFactory;
    private Map<String, String> _dbCredentialMap;

    private String _dbConnectionString;
    private String _username;
    private String _password;
    private String _jdbcDriver;

    private Connection _connection;

    private UUID _clientConnectionId;

    //region Pre-setting DB services

    {
        _dbCredentialFactory = new DbCredentialFactory();
    }

    //endregion

    public Connection makeConnection() throws ClassNotFoundException, SQLException {

        //Loading the JDBC driver class
        Class.forName(_jdbcDriver);

        //Connection establishment
        try {
            _connection = DriverManager.getConnection(_dbConnectionString, _username, _password);
            _clientConnectionId = ((ISQLServerConnection)_connection).getClientConnectionId();
        } catch(SQLException sqlException) {
            throw new SQLException(
                    "SQL Error came up with SQL Server Connection: Client Connection ID = <" + _clientConnectionId + ">     "
            );
        }
        return _connection;
    }

    //Releasing all allocated SQL services
    @Contract("_, _ -> this")
    public DbConnectionService disposeResourcedSqlServices(
            @NotNull DbManipulationUtil dbManipulationUtilInstance,
            @NotNull DbResultProcessing dbResultProcessingInstance
            ) throws SQLException {

        dbManipulationUtilInstance.disposeStatement();
        dbResultProcessingInstance.disposeResultSetService();
        _connection.close();

        return this;
    }
}