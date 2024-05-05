package se.spo.api;

import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.model.apiModel.requestModel.AuthenticationModel;
import se.requestProcessor.TrackRecommendationProcessor;

import java.util.HashMap;

public class TrackRecommendationTest extends BaseApiTestService {

    private TrackRecommendationProcessor trackRecommendationProcessor;

    @Test(
            priority = 1,
            testName = "SATRACKRECOMMENDATIONS_01",
            description = "Verify Api was SUCCESSFULLY processed when being hit by a request with query parameters"
    )
    protected void spotifyApiTest_VerifyApiProcessedRequestWithGreenResponseCode() {

        Response dataRetrieved = trackRecommendationProcessor.getTrackRecommendationsSuccessfully(
                new HashMap<>(){{
                    put("seed_genres", "classical, country");
                }});

        trackRecommendationProcessor.verifyGettingTrackRecommendationsSuccessfully(dataRetrieved);
    }

    @Test(
            priority = 2,
            testName = "SATRACKRECOMMENDATIONS_02",
            description = "Verify Api was UNSUCCESSFULLY processed when being hit by a request without query parameters"
    )
    protected void spotifyApiTest_VerifyApiProcessedRequestWithRedResponseCode() {

        Response dataRetrieved = trackRecommendationProcessor.getTrackRecommendationsUnsuccessfullyWithoutQueryParameters();
        trackRecommendationProcessor.verifyTrackRecommendationsRequestRespondedWith400SttCode(dataRetrieved);
    }

    @BeforeMethod
    protected void testPreparation() {
        System.out.println("TR THREAD ID = " + Thread.currentThread().getId());
        trackRecommendationProcessor = new TrackRecommendationProcessor(
                new AuthenticationModel(
                        userAuthenticationDbModel.getClientId(),
                        userAuthenticationDbModel.getClientSecret(),
                        userAuthenticationDbModel.getGrantType())
        );
    }
}