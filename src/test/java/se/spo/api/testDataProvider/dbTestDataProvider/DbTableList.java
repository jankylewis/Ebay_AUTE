package se.spo.api.testDataProvider.dbTestDataProvider;

public class DbTableList {

    protected static class UserAuthenticationTable {

        protected final String USER_AUTHENTICATION_TABLE = "userAuthenticationTb";

        //Listing columns
        protected String clientId;
        protected String clientSecret;
        protected String grantType;
        protected String beCreatedAt;
        protected String beUsed;
    }
}
