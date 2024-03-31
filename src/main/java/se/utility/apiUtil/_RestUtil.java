package se.utility.apiUtil;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.google.gson.Gson;

import java.util.*;

import static io.restassured.RestAssured.given;

public class _RestUtil {

    //region Processing RestUtil instance

//    protected static final RestUtil INSTANCE = getInstance();

    public _RestUtil() {}

//    public static RestUtil getInstance() {
//        return RestUtilHelper._INSTANCE;
//    }

//    private static final class RestUtilHelper {
//        private static final RestUtil _INSTANCE = new _RestUtil();
//    }

    //endregion

    //region Introducing variables

    private RequestSpecification _requestSpecification;
    private Response response;
    private String _requestedUri;
    private JsonPath jsonPath;
    private Gson gson;

    //endregion

    //Handling Thread Local to Http Client Request
    {
        _requestSpecification = given();
    }

    private void setRequestedUri(String requestedUri) {
        _requestedUri = requestedUri;
    }

    private void setAccessToken() {

        String accessToken = AuthenticationUtil.getAccessToken();

        _requestSpecification
                .given()
                .header("Authorization", "Bearer " + accessToken);
    }
    private RequestSpecification buildUrlencodedForm(
            @NotNull Collection<Pair<Object, Object>> pairs
    ) {

        Collection<Pair<Object, Object>> unmodifiablePairs =
                Collections.unmodifiableCollection(pairs);

        for (Pair<?, ?> pair : unmodifiablePairs) {

            String name = pair.getValue0().toString();
            String value = pair.getValue1().toString();

            _requestSpecification.formParam(name, value);
        }

        return _requestSpecification;
    }

    private Response sendGetRequest() {
        return response = _requestSpecification.get(_requestedUri);
    }

    public Response sendAuthenticatedRequestWithResponse(
            String requestedUri,
            @Nullable Collection<Pair<Object, Object>> requestedBody,
            @Nullable ContentType requestedContentType,
            EMethod requestedMethod
    ) {

        setAccessToken();

        setRequestedUri(requestedUri);

        //Invoking a requested body if needed
        if (requestedContentType != null && requestedBody != null) {
            switch (requestedContentType) {
                case URLENC -> buildUrlencodedForm(requestedBody);
            }
        }

        switch (requestedMethod) {
            case GET -> sendGetRequest();
//            case POST -> sendPostRequest();
//            case PUT -> sendPutRequest();
//            case HEAD -> sendHeadRequest();
//            case PATCH -> sendPatchRequest();
//            case DELETE -> sendDeleteRequest();
//            case OPTIONS -> sendOptionsRequest();
        }
        return response;
    }

    //region API methods

    public enum EMethod {
        GET,
        POST,
        PUT,
        PATCH,
        DELETE,
        OPTIONS,
        HEAD
    }

    //endregion
}
