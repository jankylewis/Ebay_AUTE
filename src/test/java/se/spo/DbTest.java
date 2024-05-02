package se.spo;

//import se.utility.dbUtil.DbConnectionService;
import se.model.dbModel.UserAuthenticationDbModel;
import se.utility.GlobalVariableUtil;
import se.utility.dbUtil.DbConnectionService;
import se.utility.dbUtil.DbManipulationUtil;
import se.utility.dbUtil.DbResultProcessing;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DbTest {

//    public static void main(String[] args) {
//
//
//        String dbUrl =
//                "jdbc:sqlserver://JANKYLEWIS\\SQLEXPRESS:1433;databaseName=SpotifyAutomationTestDb;trustServerCertificate=true;integratedSecurity=false";
//        String usrname = "sa";
//        String pwd = "123123";
//        String query = "SELECT * FROM userAuthenticationTb";
//
//        try {
//            // Load the JDBC driver
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//
//            // Get a connection to the database
//            Connection connection = DriverManager.getConnection(dbUrl, usrname, pwd);
//
//            System.out.println("Connection established");
//
//            // Use the connection object to execute queries, manipulate data, etc.
//
//
//            // Close the connection after use
//            connection.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    public static void main (String[] args)
            throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        DbManipulationUtil dbManipulationUtil = new DbManipulationUtil();
        DbConnectionService dbConnectionService = new DbConnectionService(GlobalVariableUtil.DbConfigs.DB_NAME_USED);
        DbResultProcessing dbResultProcessing = new DbResultProcessing();

        Connection connection = dbConnectionService.makeConnection();

        dbManipulationUtil.setQuery("SELECT * FROM userAuthenticationTb");

        ResultSet resultSet = dbManipulationUtil.executeGetQuery(connection);

        List<UserAuthenticationDbModel> userAuthenticationDbModels = dbResultProcessing.mapResultSetToModelList(resultSet, UserAuthenticationDbModel.class);

        for (UserAuthenticationDbModel userAuthenticationDbModel : userAuthenticationDbModels) {
            System.out.println(userAuthenticationDbModel.getId());
            System.out.println(userAuthenticationDbModel.getClientId());
            System.out.println(userAuthenticationDbModel.getClientSecret());
            System.out.println(userAuthenticationDbModel.getGrantType());
        }

        dbConnectionService.disposeResourcedSqlServices(dbManipulationUtil, dbResultProcessing);
    }
}


