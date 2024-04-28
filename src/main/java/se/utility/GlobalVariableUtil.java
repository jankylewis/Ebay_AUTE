package se.utility;

import se.utility.fileUtil.fileReaderUtil.PropertyFileReader;

public class GlobalVariableUtil extends PropertyFileReader {

    private static final ResourceReader RESOURCE_READER = ResourceReader.getInstance();

    //Nested class needs to be declared in static state

    //region Variables come from environment.properties
    public static class Environment {

        //region Introducing global variables

        //Providing environment's path
        private static final String FILE_PATH = "./src/main/java/se/globalVariable/environment.properties";
        public static final String BASE_URL = RESOURCE_READER.getPropertyFromGV("base_url", FILE_PATH);
        public static final String PUBLIC_URL = RESOURCE_READER.getPropertyFromGV("public_url", FILE_PATH);
        public static final String END_POINT_LOCALIZATION =
                RESOURCE_READER.getPropertyFromGV("end_point_localization", FILE_PATH);

        //endregion

    }

    //endregion Variables come from environment.properties

    //region Variables come from browser_configuration.properties

    public static class BrowserConfiguration {

        //region Introducing global variables

        //Providing browser config's path
        private static final String FILE_PATH = "./src/main/java/se/globalVariable/browser_configuration.properties";
        public static final String BROWSER_TYPE = RESOURCE_READER.getPropertyFromGV("browser_type", FILE_PATH);
        public static final Boolean HEADLESS =
                Boolean.parseBoolean(RESOURCE_READER.getPropertyFromGV("is_headless", FILE_PATH));

        //endregion
    }

    //endregion Variables come from browser_configuration.properties

    //region Variables come from user_credential.properties

    public static class UserCredential {

        //region Introducing global variables

        //Providing user infor's path
        private static final String FILE_PATH = "./src/main/java/se/globalVariable/user_credential.properties";
        public static final String USER_NATION = RESOURCE_READER.getPropertyFromGV("user_nation", FILE_PATH);
        public static final String USER_DOB = RESOURCE_READER.getPropertyFromGV("user_dob", FILE_PATH);
        public static final String USER_EMAIL = RESOURCE_READER.getPropertyFromGV("user_email", FILE_PATH);
        public static final String USER_PASSWORD = RESOURCE_READER.getPropertyFromGV("user_password", FILE_PATH);
        public static final Boolean REMEMBERED =
                Boolean.parseBoolean(RESOURCE_READER.getPropertyFromGV("is_remembered", FILE_PATH));
        public static final String USER_GENDER = RESOURCE_READER.getPropertyFromGV("user_gender", FILE_PATH);

        //endregion
    }

    //endregion Variables come from user_credential.properties

    //region Variables come from script_configuration.properties

    public static class ScriptConfiguration {

        //region Introducing global variables

        //Providing script config's path
        private static final String FILE_PATH = "./src/main/java/se/globalVariable/script_configuration.properties";
        public static final Boolean RECORDED =
                Boolean.parseBoolean(RESOURCE_READER.getPropertyFromGV("is_recorded", FILE_PATH));
        public static final Boolean SCREENSHOTTED =
                Boolean.parseBoolean(RESOURCE_READER.getPropertyFromGV("is_screenshotted", FILE_PATH));

        public static final Boolean TROUBLESHOOTING_MODE =
                Boolean.parseBoolean(RESOURCE_READER.getPropertyFromGV("is_troubleshooting_mode", FILE_PATH));

        //endregion
    }

    //endregion Variables come from script_configuration.properties

    //region Variables come from api_credentials.properties

    public static class ApiCredential {

        //region Introducing global variables

        //Providing api credentials' path
        private static final String FILE_PATH = "./src/main/java/se/globalVariable/api_config.properties";
        public static final String CLIENT_ID = RESOURCE_READER.getPropertyFromGV("client_id", FILE_PATH);
        public static final String CLIENT_SECRET = RESOURCE_READER.getPropertyFromGV("client_secret", FILE_PATH);
        public static final String CLIENT_APP = RESOURCE_READER.getPropertyFromGV("client_app", FILE_PATH);

        //endregion
    }

    //endregion Variables come from api_credentials.properties

    //region Variables come from db_credentials.properties file

    public static class DbCredential {

        //region Introducing global variables

        private static final String FILE_PATH = "./src/main/java/se/globalVariable/db_credential.properties";

        public static final String DB_CONNECTION_STRING = RESOURCE_READER.getPropertyFromGV("db_connection_string", FILE_PATH);
        public static final String USERNAME = RESOURCE_READER.getPropertyFromGV("username", FILE_PATH);
        public static final String PASSWORD = RESOURCE_READER.getPropertyFromGV("password", FILE_PATH);
        public static final String JDBC_DRIVER = RESOURCE_READER.getPropertyFromGV("jdbc_driver", FILE_PATH);

        //endregion
    }

    //endregion Variables come from db_credentials.properties file

    //region Variables come from api_config.properties file

    public static class ApiRunConfigs {
        private static final String FILE_PATH = "./src/main/java/se/dataManager/globalVariable/api_config.properties";

        //region Introducing global variables

        public static final String IDENTIFICATION_USED = RESOURCE_READER.getPropertyFromGV("identification_used", FILE_PATH);

        //endregion Introducing global variables
    }

    //endregion Variables come from api_config.properties file
}


