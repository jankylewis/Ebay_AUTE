package se.utility.apiUtil;

import io.restassured.http.ContentType;
import org.javatuples.Pair;
import se.model.apiModel.requestModel.AuthenticationModel;

import java.util.Collection;
import java.util.List;

public class _AuthenticationUtil {

    public _AuthenticationUtil(){}

//    private static final RestUtil REST_UTIL = RestUtil.INSTANCE;
    private final String AUTHENTICATION_URI = "https://accounts.spotify.com/api/token";

    protected String getAccessToken() {

        _RestUtil _restUtil = new _RestUtil();

        AuthenticationModel authenticationModel = new AuthenticationModel();

        Collection<Pair<Object, Object>> requestedForm =
                List.of(
                        Pair.with("grant_type", authenticationModel.getGrantType()),
                        Pair.with("client_id", authenticationModel.getClientId()),
                        Pair.with("client_secret", authenticationModel.getClientSecret())
                );

        _restUtil.sendBasicRequest(
                AUTHENTICATION_URI,
                requestedForm,
                ContentType.URLENC,
                _RestUtil.EMethod.POST
        );

        String token =_restUtil.getPropertyValue("access_token");
        System.out.println(token);

        return _restUtil.getPropertyValue("access_token");
    }
}
