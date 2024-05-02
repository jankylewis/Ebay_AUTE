package se.utility.apiUtil;

import io.restassured.http.ContentType;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import se.model.apiModel.requestModel.AuthenticationModel;

import java.util.Collection;
import java.util.List;

public class AuthenticationService {

    protected AuthenticationService(){}

    private static final String AUTHENTICATION_URI = "https://accounts.spotify.com/api/token";
    private RestUtil _restUtil;

    protected String getAccessToken() {

        _restUtil = new RestUtil();

        AuthenticationModel authenticationModel = new AuthenticationModel();

        Collection<Pair<Object, Object>> requestForm = List.of(
                Pair.with("grant_type", authenticationModel.getGrantType()),
                Pair.with("client_id", authenticationModel.getClientId()),
                Pair.with("client_secret", authenticationModel.getClientSecret())
        );

        _restUtil.sendBasicRequest(
                AUTHENTICATION_URI,
                requestForm,
                ContentType.URLENC,
                RestUtil.EMethod.POST
        );

        return _restUtil.getPropertyValue("access_token");
    }

    protected String getAccessToken(@NotNull AuthenticationModel apiAuthenticationModel) {

        _restUtil = new RestUtil();

        Collection<Pair<Object, Object>> requestForm = List.of(
                Pair.with("grant_type", apiAuthenticationModel.getGrantType()),
                Pair.with("client_id", apiAuthenticationModel.getClientId()),
                Pair.with("client_secret", apiAuthenticationModel.getClientSecret())
        );

        _restUtil.sendBasicRequest(
                AUTHENTICATION_URI,
                requestForm,
                ContentType.URLENC,
                RestUtil.EMethod.POST
        );

        return _restUtil.getPropertyValue("access_token");
    }
}
