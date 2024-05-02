package se.spo.api;

import io.restassured.response.Response;
import org.javatuples.Pair;
import org.testng.annotations.AfterMethod;
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
            description = "Verify Api was SUCCESSFULLY processed when being hit by a request"
    )
    protected void spotifyApiTest_VerifyApiProcessedRequestWithGreenResponseCode() {

        Response dataRetrieved = trackRecommendationProcessor.getTrackRecommendationsSuccessfully(
                new HashMap<>(){{
                    put("seed_genres", "classical, country");
                }});

        trackRecommendationProcessor.verifyGettingTrackRecommendationsSuccessfully(dataRetrieved);
    }

    @BeforeMethod
    protected void testPreparation() {

        trackRecommendationProcessor = new TrackRecommendationProcessor(
                new AuthenticationModel(
                        userAuthenticationDbModel.getClientId(),
                        userAuthenticationDbModel.getClientSecret(),
                        userAuthenticationDbModel.getGrantType())
        );
    }

    @AfterMethod
    protected void testCleaning() {

    }
}