package se.utility.apiUtil;

import io.restassured.http.ContentType;
import org.javatuples.Pair;
import se.model.apiModel.requestModel.AuthenticationModel;

import java.util.Collection;
import java.util.List;

public class AuthenticationProcessor {

    protected AuthenticationProcessor(){}

    private static final String AUTHENTICATION_URI = "https://accounts.spotify.com/api/token";
    private RestProcessorUtil _restProcessorUtil;

    protected String getAccessToken() {

        _restProcessorUtil = new RestProcessorUtil();

        AuthenticationModel authenticationModel = new AuthenticationModel();

        Collection<Pair<Object, Object>> requestForm = List.of(
                Pair.with("grant_type", authenticationModel.getGrantType()),
                Pair.with("client_id", authenticationModel.getClientId()),
                Pair.with("client_secret", authenticationModel.getClientSecret())
        );

        _restProcessorUtil.sendBasicRequest(
                AUTHENTICATION_URI,
                requestForm,
                ContentType.URLENC,
                RestProcessorUtil.EMethod.POST
        );

        return _restProcessorUtil.getPropertyValue("access_token");
    }
}
