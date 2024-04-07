package se.utility.apiUtil;

import io.restassured.http.ContentType;
import org.javatuples.Pair;
import se.model.apiModel.requestModel.AuthenticationModel;

import java.util.Collection;
import java.util.List;

public class AuthenticationService {

    private static final RestUtil REST_UTIL = RestUtil.INSTANCE;
    private static final String AUTHENTICATION_URI = "https://accounts.spotify.com/api/token";

    protected static String getAccessToken() {

        AuthenticationModel authenticationModel = new AuthenticationModel();

        Collection<Pair<Object, Object>> requestedForm =
                List.of(
                        Pair.with("grant_type", authenticationModel.getGrantType()),
                        Pair.with("client_id", authenticationModel.getClientId()),
                        Pair.with("client_secret", authenticationModel.getClientSecret())
                );

        REST_UTIL.sendBasicRequest(
                AUTHENTICATION_URI,
                requestedForm,
                ContentType.URLENC,
                RestUtil.EMethod.POST
        );

        return REST_UTIL.getPropertyValue("access_token");
    }
}
