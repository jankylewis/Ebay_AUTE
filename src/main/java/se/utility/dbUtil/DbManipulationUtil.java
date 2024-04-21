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

    public ResultSet executeQuery(Connection connection) throws SQLException {

        prepareStatement(connection);

        if (_query == null)
            throw new SQLDataException("The query to be executed was empty!     ");

        _resultSet = _statement.executeQuery(_query);

//        int count = 0;
//        while (_resultSet.next()){
//            count = _resultSet.getRow();
//        }

        return _resultSet;
    }

    //region Internal services

    private Statement prepareStatement(@NotNull Connection connection) throws SQLException {
        return _statement = connection.createStatement();
    }

    protected DbManipulationUtil disposeStatement() throws SQLException {
        _statement.close();
        return this;
    }

    //endregion
}


