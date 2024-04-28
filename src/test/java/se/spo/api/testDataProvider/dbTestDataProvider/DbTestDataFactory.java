package se.spo.api.testDataProvider.dbTestDataProvider;

import org.jetbrains.annotations.Nullable;
import se.credentialFactory.ApiCredentialFactory;
import se.model.dbModel.UserAuthenticationDbModel;
import se.utility.CaseFormatUtil;
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

        _dbConnectionService = new DbConnectionService(GlobalVariableUtil.DbConfigs.DB_NAME_USED);
        _connection = _dbConnectionService.makeConnection();
        _dbManipulationUtil.setQuery(_dbQueryList.SELECT_ALL);
        _resultSet = _dbManipulationUtil.executeGetQuery(_connection, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

        if (_dbResultProcessing.getNumberOfRecords(_resultSet, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY) != -1) {

            //Mapping data to UserAuthenticationDbModel if data existed
            return _dbResultProcessing.mapResultSetToModelList(_resultSet, UserAuthenticationDbModel.class)
                    .stream()
                    .filter(usr -> !usr.grantType.equals("sample_grant_type"))
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

            //Preparing an INSERT query
            String insertQuery = _dbQueryList.INSERT_VALUES(
                    userAuthenticationTb.USER_AUTHENTICATION_TABLE,
                    Arrays.asList(clientIdStr, clientSecretStr, grantTypeStr, beCreatedAtStr, beUsedAtStr),
                    Arrays.asList(
                            _anApiCredential.get(CaseFormatUtil.convertCamelToSnakeCase(clientIdStr)),
                            _anApiCredential.get(CaseFormatUtil.convertCamelToSnakeCase(clientSecretStr)),
                            _anApiCredential.get(CaseFormatUtil.convertCamelToSnakeCase(grantTypeStr)),
                            _dateTimeUtil.getCurrentDateAsString(_dateTimeUtil.YMDHMS_FORMAT),
                            0)
            );

            _dbManipulationUtil.setQuery(insertQuery);

            //Fulfilling new data into table if table was empty
            _dbManipulationUtil.executeCudQuery(_connection);
        }

        return null;
    }

    //endregion Preparing user authentication data

    //region Cleaning data

    private void cleanUserAuthenticationDb(){}

    //endregion

    public static void main(String[] args) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, FileNotFoundException {
        new DbTestDataFactory().getUserAuthenticationDbModel();
    }
}
