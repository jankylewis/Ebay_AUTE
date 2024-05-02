package se.spo.api.testDataProvider.dbTestDataProvider;

public class DbTableList {

    protected static class UserAuthenticationTable {

        protected final String USER_AUTHENTICATION_TABLE = "userAuthenticationTb";

        //Listing columns
        protected String clientId = "clientId";
        protected String clientSecret = "clientSecret";
        protected String grantType = "grantType";
        protected String beCreatedAt = "beCreatedAt";
        protected String beUsed = "beUsed";
        protected String lastUsedAt = "lastUsedAt";
    }
}
