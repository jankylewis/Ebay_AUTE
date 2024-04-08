package se.spo.api;

import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.requestProcessor.MarketProcessor;

public class MarketTest extends BaseApiTestService {

    private MarketProcessor marketProcessor;

    @Test(
            priority = 1,
            testName = "SAMARKETS_01",
            description = "Verify Api went GREEN when being hit by a normal request"
    )
    protected void spotifyApiTest_VerifyApiProcessedRetrievingMarketsWithGreenCode() {
        Response dataReturned = marketProcessor.getMarketsWithNormalUri();
        marketProcessor.verifyRetrievingMarketsSuccessfully(dataReturned);
    }

    @Test(
            priority = 2,
            testName = "SAMARKETS_02",
            description = "Verify Api went RED when being hit by an abnormal URI"
    )
    protected void spotifyApiTest_VerifyApiProcessedRetrievingMarketsWith404Code() {
        Response dataReturned = marketProcessor.getMarketsWithNormalUri(apiFaker.produceFakeUuid().toString());
        marketProcessor.verifyApiResponded404(dataReturned);
    }

    @Test(
            priority = 2,
            testName = "SAMARKETS_03",
            description = "Verify Api went RED when being hit by an invalid access token"
    )
    protected void spotifyApiTest_VerifyApiProcessedRetrievingMarketsWithRedCodeByAnInvalidAccessToken() {
        Response dataReturned =
                marketProcessor.getMarketsWithExpiredToken(
                        apiFaker.generateSpotifyDummyTokens(apiFaker.getSecureRandomInstance()));

        marketProcessor.verifyApiResponded401(dataReturned);
    }

    @BeforeMethod
    protected void testPreparation() {
        marketProcessor = new MarketProcessor();
    }
}
