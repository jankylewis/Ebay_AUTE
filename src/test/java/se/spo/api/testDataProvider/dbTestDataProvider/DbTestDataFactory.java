package se.spo.api.testDataProvider.dbTestDataProvider;

import org.jetbrains.annotations.Nullable;
import se.credentialFactory.ApiCredentialFactory;
import se.model.dbModel.UserAuthenticationDbModel;
import se.utility.DateTimeUtil;
import se.utility.GlobalVariableUtil;
import se.utility.dbUtil.DbConnectionService;
import se.utility.dbUtil.DbManipulationUtil;
import se.utility.dbUtil.DbResultProcessing;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

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

    private ApiCredentialFactory _apiCredentialFactory;
    private Map<String, String> _anApiCredential;

    private DateTimeUtil _dateTimeUtil;

    //endregion

    //region Initializing services

    {
        _dbConnectionService = new DbConnectionService();
        _dbManipulationUtil = new DbManipulationUtil();
        _dbResultProcessing = new DbResultProcessing();
        _dbQueryList = new DbQueryList();
        _dbTableList = new DbTableList();
        _apiCredentialFactory = new ApiCredentialFactory();
        _dateTimeUtil = new DateTimeUtil();
    }

    //endregion

    //region Preparing user authentication data

    private @Nullable UserAuthenticationDbModel getUserAuthenticationDbModel()
            throws SQLException, ClassNotFoundException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException,
            FileNotFoundException {

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

            _anApiCredential = _apiCredentialFactory.retrieveAnApiCredential(GlobalVariableUtil.ApiRunConfigs.IDENTIFICATION_USED);

            String clientIdStr = userAuthenticationTb.clientId.toString();
            String clientSecretStr = userAuthenticationTb.clientSecret.toString();
            String grantTypeStr = userAuthenticationTb.grantType.toString();
            String beCreatedAtStr = userAuthenticationTb.beCreatedAt.toString();
            String beUsedAtStr = userAuthenticationTb.beUsed.toString();

            String insertQuery = _dbQueryList.INSERT_VALUES(
                    userAuthenticationTb.USER_AUTHENTICATION_TABLE,
                    Arrays.asList(clientIdStr, clientSecretStr, grantTypeStr, beCreatedAtStr, beUsedAtStr),
//                    Arrays.asList("ff4df67329594a35a57ad06dcd53605f", "363912205fc049b3b49d6210c08182f8", "client_credentials", "2024-04-22 10:30:00", 0)
                    Arrays.asList(_anApiCredential.get(clientIdStr), _anApiCredential.get(clientSecretStr),
                            _anApiCredential.get(grantTypeStr), _dateTimeUtil.getCurrentDateAsString(_dateTimeUtil.YMDHMS_FORMAT),
                            0)
            );

            _dbManipulationUtil.setQuery(insertQuery);
            //Fulfilling new data into table if table was empty
            _resultSet = _dbManipulationUtil.executeQuery(_connection);
        }

        return null;
    }

    //endregion Preparing user authentication data

    //region Cleaning data

    private void cleanUserAuthenticationDb(){}

    //endregion

    public static void main(String[] agrs) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, FileNotFoundException {
        new DbTestDataFactory().getUserAuthenticationDbModel();
    }
}
