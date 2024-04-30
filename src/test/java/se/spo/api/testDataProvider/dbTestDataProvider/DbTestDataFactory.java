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
import java.util.Date;
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
    private DbTableList.UserAuthenticationTable _userAuthenticationTable;
    private UserAuthenticationDbModel _userAuthenticationModel;

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
        _userAuthenticationTable = new DbTableList.UserAuthenticationTable();

        UserAuthenticationDbModel userAuthenticationDbModel = new UserAuthenticationDbModel();

        _connection = _dbConnectionService.makeConnection();

        _dbManipulationUtil.setQuery(_dbQueryList.SELECT_ALL);
        _resultSet = _dbManipulationUtil.executeGetQuery(_connection, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

        if (_dbResultProcessing.getNumberOfRecords(_resultSet, ResultSet.TYPE_SCROLL_SENSITIVE) != -1) {

            //Mapping data to UserAuthenticationDbModel if data existed
            return _dbResultProcessing.mapResultSetToModelList(_resultSet, UserAuthenticationDbModel.class)
                    .stream()
                    .filter(usr -> !usr.grantType.equals("sample_grant_type"))
                    .toList()
                    .get(0);
        } else {

            _anApiCredential = _apiCredentialFactory.retrieveAnApiCredential(GlobalVariableUtil.ApiRunConfigs.IDENTIFICATION_USED);

            String clientIdStr = _userAuthenticationTable.clientId.toString();
            String clientSecretStr = _userAuthenticationTable.clientSecret.toString();
            String grantTypeStr = _userAuthenticationTable.grantType.toString();
            String beCreatedAtStr = _userAuthenticationTable.beCreatedAt.toString();
            String beUsedAtStr = _userAuthenticationTable.beUsed.toString();

            Date currentDate = _dateTimeUtil.getCurrentDate();
            userAuthenticationDbModel.beCreatedAt = currentDate;

            //Preparing an INSERT query
            String insertQuery = _dbQueryList.INSERT_VALUES(
                    _userAuthenticationTable.USER_AUTHENTICATION_TABLE,
                    Arrays.asList(clientIdStr, clientSecretStr, grantTypeStr, beCreatedAtStr, beUsedAtStr),
                    Arrays.asList(
                            userAuthenticationDbModel.clientId = _anApiCredential.get(CaseFormatUtil.convertCamelToSnakeCase(clientIdStr)),
                            userAuthenticationDbModel.clientSecret = _anApiCredential.get(CaseFormatUtil.convertCamelToSnakeCase(clientSecretStr)),
                            userAuthenticationDbModel.grantType = _anApiCredential.get(CaseFormatUtil.convertCamelToSnakeCase(grantTypeStr)),
                            _dateTimeUtil.convertDateToCorrectFormat(currentDate, _dateTimeUtil.YMDHMS_FORMAT),
                            userAuthenticationDbModel.beUsed = 0
                    )
            );

            _dbManipulationUtil.resetStatementToDefaultValue()
                    .setQuery(insertQuery);

            //Fulfilling new data into table if table was empty
            _dbManipulationUtil.executeCreateQuery(_connection);
        }

        return userAuthenticationDbModel;
    }

    //endregion Preparing user authentication data

    //region Cleaning data

    public void cleanUserAuthenticationDb(UserAuthenticationDbModel userAuthenticationDbModel) throws SQLException {

        //Preparing an UPDATE query
        String updateQuery = _dbQueryList.UPDATE(
                _userAuthenticationTable.USER_AUTHENTICATION_TABLE,
                _userAuthenticationTable.beUsed.toString(),
                _userAuthenticationTable.beCreatedAt,
                userAuthenticationDbModel.beUsed += 1,
                _dateTimeUtil.convertDateToCorrectFormat(userAuthenticationDbModel.getBeCreatedAt(), _dateTimeUtil.HYPHENATED_YMDHMS_FORMAT)
        );

        _dbManipulationUtil.setQuery(updateQuery);


        //Re-indexing the beUsed column
        _dbManipulationUtil.executeUpdateQuery(_connection);
    }

    //endregion

    public static void main(String[] args) throws SQLException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, FileNotFoundException {

        DbTestDataFactory dbTestDataFactory = new DbTestDataFactory();

        UserAuthenticationDbModel userAuthenticationDbModel = dbTestDataFactory.getUserAuthenticationDbModel();

        dbTestDataFactory.cleanUserAuthenticationDb(userAuthenticationDbModel);
    }
}
