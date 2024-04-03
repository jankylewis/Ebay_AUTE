package se.spo.api.categoryBrowsingTest;

import io.restassured.response.Response;
import org.testng.annotations.Test;
import se.requestProcessor.categoryProcessor.SingleCategoryProcessor;
import se.spo.api.BaseApiTestService;

public class SingleCategoryBrowsingTest extends BaseApiTestService {

    private SingleCategoryProcessor singleBrowseCategoryProcessor = new SingleCategoryProcessor();

    @Test(
            priority = 1,
            testName = "SASINGLEBROWSECATEGORIES_01"
    )
    protected void spotifyApiTest_VerifyApiProcessedRequestSuccessfully() {
        Response apiResponse = singleBrowseCategoryProcessor.getSingleCategoriesSuccessfully("kpop");
        singleBrowseCategoryProcessor.verifySingleBrowseCategoryApiRespondedGreenly(apiResponse);
    }

    @Test(
            priority = 1,
            testName = "SASINGLEBROWSECATEGORIES_02"
    )
    protected void spotifyApiTest_VerifyApiProcessedRequestSuccessfully1() {
        Response apiResponse = singleBrowseCategoryProcessor.getSingleCategoriesSuccessfully("kpop");
        singleBrowseCategoryProcessor.verifySingleBrowseCategoryApiRespondedGreenly(apiResponse);
    }

    @Test(
            priority = 1,
            testName = "SASINGLEBROWSECATEGORIES_03"
    )
    protected void spotifyApiTest_VerifyApiProcessedRequestSuccessfully2() {
        Response apiResponse = singleBrowseCategoryProcessor.getSingleCategoriesSuccessfully("kpop");
        singleBrowseCategoryProcessor.verifySingleBrowseCategoryApiRespondedGreenly(apiResponse);
    }
}
