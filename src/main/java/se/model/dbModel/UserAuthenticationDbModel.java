package se.model.dbModel;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class UserAuthenticationDbModel {

    public @NotNull Integer id;
    public @NotNull String clientId;
    public @NotNull String clientSecret;
    public @NotNull String grantType;
    public @NotNull Date beCreatedAt;
    public @NotNull Integer beUsed;

    public Date getBeCreatedAt() {
        return beCreatedAt;
    }

    public void setBeCreatedAt(Date beCreatedAt) {
        this.beCreatedAt = beCreatedAt;
    }

    public int getBeUsed() {
        return beUsed;
    }

    public void setBeUsed(Integer beUsed) {
        this.beUsed = beUsed;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
}
