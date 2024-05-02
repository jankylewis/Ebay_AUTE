package se.utility.apiUtil;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.model.apiModel.requestModel.AuthenticationModel;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestUtil {

    //region Introducing constructors

    public RestUtil() {}

    //endregion

    //region Introducing variables

    private AuthenticationService _authenticationService;

    private RequestSpecification _requestSpecification;

    private Response _response;
    private JsonPath _jsonPath;

    private String _requestUri;

//    private HttpClientConfig _httpClientConfig;

    //endregion

    //region Initializing class

    {
        _requestSpecification = given();
        _authenticationService = new AuthenticationService();
    }

    //endregion

    //region Assigning request uri

    private String setRequestUri(String requestUri) {
        return _requestUri = requestUri;
    }

    //endregion

    //region Pre-request services

    //region Processing query parameters

    private RequestSpecification mapQueryParameters(Map<String, Object> parametersMap) {

        _requestSpecification.queryParams(parametersMap);
        return _requestSpecification;
    }

    //endregion Processing query parameters

    //region Processing payloads

    private RequestSpecification buildUrlencodedForm(@NotNull Collection<Pair<Object, Object>> pairs) {

        Collection<Pair<Object, Object>> unmodifiablePairs = Collections.unmodifiableCollection(pairs);

        for (Pair<?, ?> pair : unmodifiablePairs) {
            String name = pair.getValue0().toString();
            String value = pair.getValue1().toString();

            _requestSpecification.formParam(name, value);
        }

        return _requestSpecification;
    }

    //endregion

    //endregion Pre-request services

    //region Services for sending requests

    private Response sendGetRequest() {
        return _response = _requestSpecification.get(_requestUri);
    }

    private Response sendPostRequest() {
        return _response = _requestSpecification.post(_requestUri);
    }

    private Response sendPatchRequest() {
        return _response = _requestSpecification.patch(_requestUri);
    }

    private Response sendHeadRequest() {
        return _response = _requestSpecification.head(_requestUri);
    }

    private Response sendOptionsRequest() {
        return _response = _requestSpecification.options(_requestUri);
    }

    private Response sendPutRequest() {
        return _response = _requestSpecification.get(_requestUri);
    }

    private Response sendDeleteRequest() {
        return _response = _requestSpecification.delete(_requestUri);
    }

    //endregion

    //region Processing tokens

    private String setAccessToken() {

        String accessToken = _authenticationService.getAccessToken();

        _requestSpecification
                .given()
                .header("Authorization", "Bearer " + accessToken);

        return accessToken;
    }

    private String setAccessToken(String expectedToken) {

        _requestSpecification
                .given()
                .header("Authorization", "Bearer " + expectedToken);

        return expectedToken;
    }

    private String setAccessToken(AuthenticationModel apiAuthenticationModel) {

        String accessToken = _authenticationService.getAccessToken(apiAuthenticationModel);

        _requestSpecification
                .given()
                .header("Authorization", "Bearer " + accessToken);

        return accessToken;
    }

    //endregion Processing tokens

    //region Processing requests

    //region Sending with a desired token

    public HashMap<?, Response> sendAuthenticatedRequestWithResponse(
            String expectedToken,
            String requestUri,
            @Nullable Collection<Pair<Object, Object>> requestBody,
            @Nullable ContentType requestContentType,
            EMethod requestMethod
            ) {

        setAccessToken(expectedToken);

        setRequestUri(requestUri);

        //Invoking a requested body if needed
        if (requestContentType != null && requestBody != null) {
            switch (requestContentType) {
                case URLENC -> buildUrlencodedForm(requestBody);
            }
        }

        switch (requestMethod) {
            case GET -> sendGetRequest();
            case POST -> sendPostRequest();
            case PUT -> sendPutRequest();
            case HEAD -> sendHeadRequest();
            case PATCH -> sendPatchRequest();
            case DELETE -> sendDeleteRequest();
            case OPTIONS -> sendOptionsRequest();
            default -> throw new IllegalArgumentException("Checking the inputted Request Method once it could be invalid!    ");
        }
        
        return new HashMap<>(){{
            put(expectedToken, _response);
        }};
    }

    //endregion Sending with a desired token

    //region Processing request with an API Authentication Model

    public Response sendAuthenticatedRequestWithResponse(
            AuthenticationModel apiAuthenticationModel,
            String requestUri,
            Map<String, Object> parametersMap,
            Collection<Pair<Object, Object>> requestBody,
            ContentType requestContentType,
            EMethod requestMethod
    ) {

        //Retrieving access token with a desired API Authentication Model
        setAccessToken(apiAuthenticationModel);

        setRequestUri(requestUri);

        if (parametersMap != null)
            mapQueryParameters(parametersMap);

        //Invoking a requested body if needed
        if (requestContentType != null && requestBody != null)
            switch (requestContentType) {
                case URLENC -> buildUrlencodedForm(requestBody);
            }

        switch (requestMethod) {
            case GET -> sendGetRequest();
            case POST -> sendPostRequest();
            case PUT -> sendPutRequest();
            case HEAD -> sendHeadRequest();
            case PATCH -> sendPatchRequest();
            case DELETE -> sendDeleteRequest();
            case OPTIONS -> sendOptionsRequest();

            default -> throw new IllegalArgumentException("Checking the inputted Request Method once it could be invalid!    ");
        }

        return _response;
    }

    //endregion Sending with an API Authentication Model

    //region Sending with an authenticated token

    public Response sendAuthenticatedRequestWithResponse(
            String requestUri,
            @Nullable Collection<Pair<Object, Object>> requestBody,
            @Nullable ContentType requestContentType,
            EMethod requestMethod
    ) {

        setAccessToken();

        setRequestUri(requestUri);

        //Invoking a requested body if needed
        if (requestContentType != null && requestBody != null) {
            switch (requestContentType) {
                case URLENC -> buildUrlencodedForm(requestBody);
            }
        }

        switch (requestMethod) {
            case GET -> sendGetRequest();
            case POST -> sendPostRequest();
            case PUT -> sendPutRequest();
            case HEAD -> sendHeadRequest();
            case PATCH -> sendPatchRequest();
            case DELETE -> sendDeleteRequest();
            case OPTIONS -> sendOptionsRequest();
            default -> throw new IllegalArgumentException("Checking the inputted Request Method once it could be invalid!    ");
        }

        return _response;
    }

    //endregion

    //region Sending a basic request

    public Response sendBasicRequest(
            String requestUri,
            @Nullable Collection<Pair<Object, Object>> requestBody,
            @Nullable ContentType requestContentType,
            EMethod requestMethod
    ) {

        setRequestUri(requestUri);

        //Invoking a request body if needed
        if (requestContentType != null && requestBody != null) {
            switch (requestContentType) {
                case URLENC -> buildUrlencodedForm(requestBody);
            }
        }

        switch (requestMethod) {
            case GET -> sendGetRequest();
            case POST -> sendPostRequest();
            case PUT -> sendPutRequest();
            case HEAD -> sendHeadRequest();
            case PATCH -> sendPatchRequest();
            case DELETE -> sendDeleteRequest();
            case OPTIONS -> sendOptionsRequest();
            default -> throw new IllegalArgumentException("Checking the inputted Request Method once it could be invalid!    ");
        };

        return _response;
    }

    //endregion

    //endregion

    //region Processing responses

    protected String getPropertyValue(String property) {
        _jsonPath = new JsonPath(_response.asPrettyString());
        return _jsonPath.get(property);
    }

    //endregion

    //region Pre-request > Preparing a Connection Manager

//    public void configureConnectionManager() {
//        _httpClientConfig = new HttpClientConfig();
//        _httpClientConfig.httpClientFactory(() -> HttpClients
//                .custom()
//                .setConnectionManager(new PoolingHttpClientConnectionManager())
//                .build());

//        RestAssured.config = ((RestAssuredConfig) _httpClientConfig);
//    }

    //endregion

    //region Post-request > Releasing connections properly after making a request

//    public void releaseConnection() {
//        RestAssured.reset();
//    }

    //endregion

    //region Listing API methods

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