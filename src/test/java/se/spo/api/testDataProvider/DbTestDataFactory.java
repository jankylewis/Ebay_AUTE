package se.spo.api.testDataProvider;

import org.jetbrains.annotations.Nullable;
import se.model.dbModel.UserAuthenticationDbModel;
import se.utility.dbUtil.DbConnectionService;
import se.utility.dbUtil.DbManipulationUtil;
import se.utility.dbUtil.DbResultProcessing;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbTestDataFactory {

    public DbTestDataFactory(){}

    //region Utilities declaration

    private DbConnectionService _dbConnectionService;
    private DbManipulationUtil _dbManipulationUtil;
    private DbResultProcessing _dbResultProcessing;

    private Connection _connection;
    private ResultSet _resultSet;

    //endregion

    //region Queries introduction

    private final String SELECT_RECORD_1UP = "SELECT clientId FROM userAuthenticationTb WHERE (SELECT COUNT(clientId) FROM userAuthenticationTb) > 0";
    private final String SELECT_ALL = "SELECT * FROM userAuthenticationTb";

    //endregion

    {
        _dbConnectionService = new DbConnectionService();
        _dbManipulationUtil = new DbManipulationUtil();
        _dbResultProcessing = new DbResultProcessing();
    }

    //region Preparing user authentication data

    private @Nullable UserAuthenticationDbModel getUserAuthenticationDbModel()
            throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        _connection = _dbConnectionService.makeConnection();

        _dbManipulationUtil.setQuery(SELECT_ALL);

        _resultSet = _dbManipulationUtil.executeQuery(_connection);

        _dbResultProcessing.getNumberOfRecords(_resultSet);

        

        if (_dbResultProcessing.getNumberOfRecords(_resultSet) != -1) {
            return _dbResultProcessing.mapResultSetToModelList(_resultSet, UserAuthenticationDbModel.class)
                    .stream()
                    .filter(usr -> usr.grantType != "sample_grant_type")
                    .toList()
                    .get(0);
        }

        return null;
    }

    //endregion

    public static void main(String[] agrs) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        new DbTestDataFactory().getUserAuthenticationDbModel();
    }
}
