package se.requestProcessor;

import io.restassured.response.Response;
import org.javatuples.Pair;
import se.utility.apiUtil.RestUtil;

import java.util.Map;

public class TrackRecommendationProcessor extends BaseProcessor {

    public TrackRecommendationProcessor() {
        super();
    }

    private final String trackRecommendationUri = "https://api.spotify.com/v1/recommendations";

    public Response getTrackRecommendationsSuccessfully() {

        Response response = _restUtil.sendAuthenticatedRequestWithResponse(
                trackRecommendationUri,
                null,
                null,
                RestUtil.EMethod.GET
        );

        return response;
    }

    public Response getTrackRecommendationsUnsuccessfullyWithAnInvalidToken(String invalidToken) {

        Map<String, Response> response =
                (Map<String, Response>) _restUtil.sendAuthenticatedRequestWithResponse(
                invalidToken,
                trackRecommendationUri,
                null,
                null,
                RestUtil.EMethod.GET
        );

        return response.get(invalidToken);
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

    public void verifyBrowseCategoriesRequestRespondedWith401SttCode(Response response) {

        Pair<Boolean, Integer> responseHealth = verifyResponseStatusCodeWent401(response);

        if (responseHealth.getValue0()) verificationWentPassed();
        else {
            LOGGER.error("Response status code came different with the expected status code! ");
            LOGGER.info("Actual status code >< Expected status code: " + responseHealth.getValue1() + " >< " + apiConstant.UNAUTHORIZED);
            verificationWentFailed();
        }
    }
}
