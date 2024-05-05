package se.requestProcessor;

import io.restassured.response.Response;
import org.javatuples.Pair;
import se.model.apiModel.requestModel.AuthenticationModel;
import se.utility.apiUtil.RestUtil;

import java.util.Map;

public class TrackRecommendationProcessor extends BaseProcessor {

    private AuthenticationModel _authenticationModel;
    private final String trackRecommendationUri = "https://api.spotify.com/v1/recommendations";

    public TrackRecommendationProcessor() {
        super();
    }

    //API credentials assignment
    public TrackRecommendationProcessor(AuthenticationModel authenticationModel) {
        _authenticationModel = authenticationModel;
    }

    public Response getTrackRecommendationsSuccessfully(Map<String, Object> expParametersMap) {

        Response response = _restUtil.sendAuthenticatedRequestWithResponse(
                _authenticationModel,
                trackRecommendationUri,
                expParametersMap,
                null,
                null,
                RestUtil.EMethod.GET
        );

        return response;
    }

    public Response getTrackRecommendationsUnsuccessfullyWithoutQueryParameters() {

        Response response = _restUtil.sendAuthenticatedRequestWithResponse(
                _authenticationModel,
                trackRecommendationUri,
                null,
                null,
                RestUtil.EMethod.GET
        );

        return response;
    }

    public void verifyGettingTrackRecommendationsSuccessfully(Response response) {

        Pair<Boolean, Integer> responseHealth = verifyResponseStatusCodeWentGreen(response);

        if (responseHealth.getValue0()) verificationWentPassed();
        else {
            LOGGER.error("Response status code came different with the expected status code! ");
            LOGGER.info("Actual status code >< Expected status code: " + responseHealth.getValue1() + " >< " + apiConstant.GREEN_STATUS);

            verificationWentFailed();
        }
    }

    public void verifyTrackRecommendationsRequestRespondedWith401SttCode(Response response) {

        Pair<Boolean, Integer> responseHealth = verifyResponseStatusCodeWent401(response);

        if (responseHealth.getValue0()) verificationWentPassed();
        else {
            LOGGER.error("Response status code came different with the expected status code! ");
            LOGGER.info("Actual status code >< Expected status code: " + responseHealth.getValue1() + " >< " + apiConstant.UNAUTHORIZED);
            verificationWentFailed();
        }
    }

    public void verifyTrackRecommendationsRequestRespondedWith400SttCode(Response response) {

        Pair<Boolean, Integer> responseHealth = verifyResponseStatusCodeWent400(response);

        if (responseHealth.getValue0()) verificationWentPassed();
        else {
            LOGGER.error("Response status code came different with the expected status code! ");
            LOGGER.info("Actual status code >< Expected status code: " + responseHealth.getValue1() + " >< " + apiConstant.INVALID_REQUEST);

            verificationWentFailed();
        }
    }
}
