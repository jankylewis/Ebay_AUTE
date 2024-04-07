package se.spo.api.categoryBrowsingTest;

import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.requestProcessor.categoryProcessor.SingleCategoryProcessor;
import se.spo.api.BaseApiTestService;

public class SingleCategoryBrowsingTest extends BaseApiTestService {

    private volatile SingleCategoryProcessor singleBrowseCategoryProcessor;

    @Test(
            priority = 1,
            testName = "SASINGLEBROWSECATEGORIES_08"
    )
    protected void spotifyApiTest_VerifyApiProcessedRequestWithNoTokenProvided() {
        Response apiResponse =
                singleBrowseCategoryProcessor.getSingleCategoriesWithInvalidAccessToken("kpop", "");
        singleBrowseCategoryProcessor.verifyUserWasNotAuthenticatedWhenNoTokenProvided(apiResponse);
    }

    @Test(
            priority = 1,
            testName = "SASINGLEBROWSECATEGORIES_07"
    )
    protected void spotifyApiTest_VerifyApiProcessedRequestWithInvalidAccessToken() {
        Response apiResponse =
                singleBrowseCategoryProcessor.getSingleCategoriesWithInvalidAccessToken("kpop", "Bearer ");
        singleBrowseCategoryProcessor.verifySingleBrowseCategoryApiDisallowedInvalidAccessToken(apiResponse);
    }

    @Test(
            priority = 1,
            testName = "SASINGLEBROWSECATEGORIES_01"
    )
    protected void spotifyApiTest_VerifyApiProcessedRequestSuccessfully() {
        Response apiResponse = singleBrowseCategoryProcessor.getSingleCategoriesSuccessfully("kpop");
        singleBrowseCategoryProcessor.verifySingleBrowseCategoryApiRespondedGreenly(apiResponse);
    }

    @BeforeMethod
    protected void testPreparation() {
        singleBrowseCategoryProcessor = new SingleCategoryProcessor();
    }
}
