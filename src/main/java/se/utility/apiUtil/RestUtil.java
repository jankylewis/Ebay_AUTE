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

public class RestUtil {

    //region Processing RestUtil instance

    protected static final RestUtil INSTANCE = getInstance();

    private RestUtil() {}

    public static RestUtil getInstance() {
        return BaseRestUtilHelper._INSTANCE;
    }

    private static final class BaseRestUtilHelper {
        private static final RestUtil _INSTANCE = new RestUtil();
    }

    //endregion

    //region Introducing variables

    private final RequestSpecification requestSpecification = given();
    private Response response;
    private String _requestedUri;
    private JsonPath jsonPath;
    private Gson gson;

    //endregion

    private RestUtil setRequestedUri(String requestedUri) {
        _requestedUri = requestedUri;
        return INSTANCE;
    }

    //region Processing payloads

    private RequestSpecification buildUrlencodedForm(
            @NotNull Collection<Pair<Object, Object>> pairs
    ) {

        Collection<Pair<Object, Object>> unmodifiablePairs =
                Collections.unmodifiableCollection(pairs);

        for (Pair<?, ?> pair : unmodifiablePairs) {

            String name = pair.getValue0().toString();
            String value = pair.getValue1().toString();

            requestSpecification.formParam(name, value);
        }

        return requestSpecification;
    }

    //endregion

    //region Methods' sending services

    private Response sendGetRequest() {
        return response = requestSpecification.get(_requestedUri);
    }

    private Response sendPostRequest() {
        return response = requestSpecification.post(_requestedUri);
    }

    private Response sendPatchRequest() {
        return response = requestSpecification.patch(_requestedUri);
    }

    private Response sendHeadRequest() {
        return response = requestSpecification.head(_requestedUri);
    }

    private Response sendOptionsRequest() {
        return response = requestSpecification.options(_requestedUri);
    }

    private Response sendPutRequest() {
        return response = requestSpecification.put(_requestedUri);
    }

    private Response sendDeleteRequest() {
        return response = requestSpecification.delete(_requestedUri);
    }

    //endregion

    //region Processing requests

    public HashMap<RestUtil, Response> sendAuthenticatedRequestWithResponse(
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
            case POST -> sendPostRequest();
            case PUT -> sendPutRequest();
            case HEAD -> sendHeadRequest();
            case PATCH -> sendPatchRequest();
            case DELETE -> sendDeleteRequest();
            case OPTIONS -> sendOptionsRequest();
        }
        return new HashMap<>(){{
            put(INSTANCE, response);
        }};
    }

    public RestUtil sendBasicRequest(
            String requestedUri,
            @Nullable Collection<Pair<Object, Object>> requestedBody,
            @Nullable ContentType requestedContentType,
            EMethod requestedMethod
            ) {

        setRequestedUri(requestedUri);

        //Invoking a requested body if needed
        if (requestedContentType != null && requestedBody != null) {
            switch (requestedContentType) {
                case URLENC -> buildUrlencodedForm(requestedBody);
            }
        }

        switch (requestedMethod) {
            case GET -> sendGetRequest();
            case POST -> sendPostRequest();
            case PUT -> sendPutRequest();
            case HEAD -> sendHeadRequest();
            case PATCH -> sendPatchRequest();
            case DELETE -> sendDeleteRequest();
            case OPTIONS -> sendOptionsRequest();
        }

        return INSTANCE;
    }

    public RestUtil sendAuthenticatedRequest(
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
            case POST -> sendPostRequest();
            case PUT -> sendPutRequest();
            case HEAD -> sendHeadRequest();
            case PATCH -> sendPatchRequest();
            case DELETE -> sendDeleteRequest();
            case OPTIONS -> sendOptionsRequest();
        }

        return INSTANCE;
    }

    public HashMap<RestUtil, Response> sendAuthenticatedRequestWithResponse(
            String expectedToken,
            String requestedUri,
            @Nullable Collection<Pair<Object, Object>> requestedBody,
            @Nullable ContentType requestedContentType,
            EMethod requestedMethod
    ) {

        setAccessToken(expectedToken);

        setRequestedUri(requestedUri);

        //Invoking a requested body if needed
        if (requestedContentType != null && requestedBody != null) {
            switch (requestedContentType) {
                case URLENC -> buildUrlencodedForm(requestedBody);
            }
        }

        switch (requestedMethod) {
            case GET -> sendGetRequest();
            case POST -> sendPostRequest();
            case PUT -> sendPutRequest();
            case HEAD -> sendHeadRequest();
            case PATCH -> sendPatchRequest();
            case DELETE -> sendDeleteRequest();
            case OPTIONS -> sendOptionsRequest();
        }
        return new HashMap<>(){{
            put(INSTANCE, response);
        }};
    }

    //endregion

    //region Processing tokens

    private RestUtil setAccessToken() {

        String accessToken = AuthenticationUtil.getAccessToken();

        requestSpecification
                .given()
                .header("Authorization", "Bearer " + accessToken);

        return INSTANCE;
    }

    private RestUtil setAccessToken(String expectedToken) {

        requestSpecification
                .given()
                .header("Authorization", "Bearer " + expectedToken);

        return INSTANCE;
    }

    //endregion

    //region Processing responses

    protected String getPropertyValue(String property) {
        jsonPath = new JsonPath(response.asPrettyString());
        return jsonPath.get(property);
    }

    //endregion

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
