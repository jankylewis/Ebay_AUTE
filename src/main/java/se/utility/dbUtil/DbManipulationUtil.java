package se.utility.dbUtil;

import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;

import java.sql.SQLException;
import java.sql.SQLDataException;
import java.util.List;

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

    public DbManipulationUtil executeCudQuery(Connection connection) throws SQLException {

        prepareStatement(connection);

        if (_query == null)
            throw new SQLDataException("The query to be executed was empty!     ");

        _statement.executeQuery(_query);

        return this;
    }

    //region Internal services

    private Statement prepareStatement(@NotNull Connection connection) throws SQLException {
        return _statement = _statement == null ? connection.createStatement() : _statement;
    }

    private Statement prepareStatement(@NotNull Connection connection, int resultSetType, int resultSetConcurrencyType) throws SQLException {
        return _statement = _statement == null ? connection.createStatement(resultSetType, resultSetConcurrencyType) : _statement;
    }

    private Statement prepareStatement(@NotNull Connection connection, int resultSetType, int resultSetConcurrencyType, int resultSetHoldabilityType) throws SQLException {
        return _statement = _statement == null ? connection.createStatement(resultSetType, resultSetConcurrencyType, resultSetHoldabilityType) : _statement;
    }

    protected DbManipulationUtil disposeStatement() throws SQLException {
        _statement.close();
        return this;
    }

    //endregion
}


