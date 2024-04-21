package se.utility.dbUtil;

import com.microsoft.sqlserver.jdbc.ISQLServerConnection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import se.utility.GlobalVariableUtil.DbCredential;

//This class manipulates the connection to SQL Server namely creating or repelling connection
public final class DbConnectionService {

    public DbConnectionService(){}

    private final String _dbConnectionString = DbCredential.DB_CONNECTION_STRING;
    private final String _username = DbCredential.USERNAME;
    private final String _password = DbCredential.PASSWORD;

    private final String _jdbcDriver = DbCredential.JDBC_DRIVER;
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