package se.spo.api.categoryBrowsingTest;

import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.requestProcessor.categoryProcessor.GeneralCategoryProcessor;
import se.spo.api.BaseApiTestService;

public class GeneralCategoryBrowsingTest extends BaseApiTestService {

    private GeneralCategoryProcessor browseCategoryProcessor;

    @Test(
            priority = 2,
            testName = "SABROWSECATEGORIES_01",
            description = "Verify Api was SUCCESSFULLY processed when being hit by a request",
            groups = "singleThreaded"
    )
    protected void spotifyApiTest_VerifyApiProcessedRequestWithGreenResponseCode() {
        Response dataRetrieved = browseCategoryProcessor.getBrowseCategoriesSuccessfully();
        browseCategoryProcessor.verifyBrowseCategoriesRequestResponseSttCode(dataRetrieved);
    }

    @Test(
            priority = 3,
            testName = "SABROWSECATEGORIES_02",
            description = "Verify Api was UNSUCCESSFULLY processed when being hit by a request"
    )
    protected void spotifyApiTest_VerifyApiProcessedRequestWithBadResponseCode() {
        Response dataRetrieved = browseCategoryProcessor.getBrowseCategoriesUnsuccessfully();
        browseCategoryProcessor.verifyBrowseCategoriesRequestRespondedWith404SttCode(dataRetrieved);
    }

    @BeforeMethod
    protected void testPreparation() {
        browseCategoryProcessor = new GeneralCategoryProcessor();
    }
}
