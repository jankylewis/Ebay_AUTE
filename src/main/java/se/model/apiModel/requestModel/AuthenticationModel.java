package se.model.apiModel.requestModel;

import se.utility.GlobalVariableUtil.ApiCredential;

public class AuthenticationModel {

    private String grantType = "client_credentials";
    private String clientId;
    private String clientSecret;

    public AuthenticationModel() {
        clientId = ApiCredential.CLIENT_ID;
        clientSecret = ApiCredential.CLIENT_SECRET;
    }

    public AuthenticationModel(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public AuthenticationModel(String clientId, String clientSecret, String grantType) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantType = grantType;
    }

    //region Getters & setters

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    //endregion
}
