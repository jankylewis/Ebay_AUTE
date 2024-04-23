package se.spo.api.testDataProvider.dbTestDataProvider;

import org.jetbrains.annotations.Nullable;
import se.model.dbModel.UserAuthenticationDbModel;
import se.utility.dbUtil.DbConnectionService;
import se.utility.dbUtil.DbManipulationUtil;
import se.utility.dbUtil.DbResultProcessing;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class DbTestDataFactory {

    public DbTestDataFactory(){}

    //region Utilities declaration

    private DbConnectionService _dbConnectionService;
    private DbManipulationUtil _dbManipulationUtil;
    private DbResultProcessing _dbResultProcessing;

    private Connection _connection;
    private ResultSet _resultSet;

    private DbQueryList _dbQueryList;
    private DbTableList _dbTableList;

    //endregion

    //region Initializing services

    {
        _dbConnectionService = new DbConnectionService();
        _dbManipulationUtil = new DbManipulationUtil();
        _dbResultProcessing = new DbResultProcessing();
        _dbQueryList = new DbQueryList();
        _dbTableList = new DbTableList();
    }

    //endregion

    //region Preparing user authentication data

    private @Nullable UserAuthenticationDbModel getUserAuthenticationDbModel()
            throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        _connection = _dbConnectionService.makeConnection();

        _dbManipulationUtil.setQuery(_dbQueryList.SELECT_ALL);

        _resultSet = _dbManipulationUtil.executeQuery(_connection);

        _dbResultProcessing.getNumberOfRecords(_resultSet);

        if (_dbResultProcessing.getNumberOfRecords(_resultSet) != -1) {

            //Mapping data to UserAuthenticationDbModel if data existed
            return _dbResultProcessing.mapResultSetToModelList(_resultSet, UserAuthenticationDbModel.class)
                    .stream()
                    .filter(usr -> usr.grantType != "sample_grant_type")
                    .toList()
                    .get(0);
        } else {

            DbTableList.UserAuthenticationTable userAuthenticationTb = new DbTableList.UserAuthenticationTable();
            String insertQuery = _dbQueryList.INSERT_VALUES(
                    userAuthenticationTb.USER_AUTHENTICATION_TABLE,
                    Arrays.asList(userAuthenticationTb.clientId.toString(), userAuthenticationTb.clientSecret.toString(), userAuthenticationTb.beCreatedAt.toString(), userAuthenticationTb.beUsed.toString()),
                    Arrays.asList("ff4df67329594a35a57ad06dcd53605f", "363912205fc049b3b49d6210c08182f8", "client_credentials", "2024-04-22 10:30:00", 0)
            );

            //Fulfilling new data into table if table was empty
            _dbManipulationUtil.setQuery(insertQuery);
            _resultSet = _dbManipulationUtil.executeQuery(_connection);
        }

        return null;
    }

    //endregion Preparing user authentication data

    //region Cleaning data

    private void cleanUserAuthenticationDb(){}

    //endregion

    public static void main(String[] agrs) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        new DbTestDataFactory().getUserAuthenticationDbModel();
    }
}
