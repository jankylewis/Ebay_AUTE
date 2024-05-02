package se.utility.dbUtil;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;

import java.sql.SQLException;
import java.sql.SQLDataException;

import se.commonHandler.constantHouse.dbConstant.UserAuthenticationConstant;

//This class executes the expected queries from automated test scripts
public class DbManipulationUtil {

    public DbManipulationUtil(){}

    private Statement _statement;
    private ResultSet _resultSet;

    private String _query;

    private UserAuthenticationConstant _userAuthenticationConstant;

    //region Preparing needed services

    {
        _userAuthenticationConstant = new UserAuthenticationConstant();
    }

    //endregion

    public String setQuery(String expQuery) {
        _query = expQuery;
        return expQuery;
    }

    //region GET Query executions

    public ResultSet executeGetQuery(Connection connection) throws SQLException {

        prepareStatement(connection);

        if (_query == null)
            throw new SQLDataException("The query to be executed was empty!     ");

        _resultSet = _statement.executeQuery(_query);

        return _resultSet;
    }

    public ResultSet executeGetQuery(Connection connection, int resultSetType, int resultSetConcurrencyType) throws SQLException {

        prepareStatement(connection, resultSetType, resultSetConcurrencyType);

        if (_query == null)
            throw new SQLDataException("The query to be executed was empty!     ");

        _resultSet = _statement.executeQuery(_query);

        return _resultSet;
    }

    //endregion GET Query executions

    public DbManipulationUtil executeCreateQuery(Connection connection) throws SQLException {

        prepareStatement(connection);

        if (_query == null)
            throw new SQLDataException("The query to be executed was empty!     ");

        int rowsAffected = _statement.executeUpdate(_query);

        if (rowsAffected > 0) return this;
        else throw new SQLException("No rows affected and the insertion might have failed!   ");
    }

    public DbManipulationUtil executeUpdateQuery(Connection connection) throws SQLException {

        prepareStatement(connection);

        if (_query == null)
            throw new SQLDataException("The query to be executed was empty!     ");

        _statement.executeUpdate(_query);

        return this;
    }

    public DbManipulationUtil executeDeleteQuery(Connection connection) throws SQLException {

        prepareStatement(connection);

        if (_query == null)
            throw new SQLDataException("The query to be executed was empty!     ");

        _statement.executeUpdate(_query);

        return this;
    }

    public Statement prepareStatement(@NotNull Connection connection) throws SQLException {
        return _statement = _statement == null ? connection.createStatement() : _statement;
    }

    public Statement prepareStatement(@NotNull Connection connection, int resultSetType, int resultSetConcurrencyType) throws SQLException {
        return _statement = _statement == null ? connection.createStatement(resultSetType, resultSetConcurrencyType) : _statement;
    }

    public Statement prepareStatement(@NotNull Connection connection, int resultSetType, int resultSetConcurrencyType, int resultSetHoldabilityType) throws SQLException {
        return _statement = _statement == null ? connection.createStatement(resultSetType, resultSetConcurrencyType, resultSetHoldabilityType) : _statement;
    }

    public DbManipulationUtil resetStatementToDefaultValue() {
        _statement = null;
        return this;
    }

    //region Internal services

    protected DbManipulationUtil disposeStatement() throws SQLException {
        _statement.close();
        return this;
    }

    //endregion
}


