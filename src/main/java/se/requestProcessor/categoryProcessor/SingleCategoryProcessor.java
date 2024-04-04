package se.requestProcessor.categoryProcessor;

import io.restassured.response.Response;
import org.javatuples.Pair;
import se.requestProcessor.BaseProcessor;
import se.utility.apiUtil._RestUtil;

public class SingleCategoryProcessor extends BaseProcessor {

    //This classes being created for testing purposes
    public SingleCategoryProcessor(){}

    private final String baseSingleCategoryBrowsingUri = "https://api.spotify.com/v1/browse/categories/";
    private final _RestUtil _restUtil = new _RestUtil();

    public Response getSingleCategoriesSuccessfully(String expCategory) {

//        _restUtil = new _RestUtil();

        Response apiResponse = _restUtil.sendAuthenticatedRequestWithResponse(
                baseSingleCategoryBrowsingUri + expCategory,
                null,
                null,
                _RestUtil.EMethod.GET
        );

        System.out.println(_restUtil.hashCode());

        return apiResponse;
    }

    public void verifySingleBrowseCategoryApiRespondedGreenly(Response response) {

        Pair<Boolean, Integer> result = verifyResponseStatusCodeWentGreen(response);

        if (result.getValue0()) {
            verificationWentPassed();
        }
        else {
            LOGGER.error("Response status code came different with the expected status code! ");
            LOGGER.info("Actual status code >< Expected status code: " + result.getValue1() + " >< " + apiConstant.GREEN_STATUS);
            verificationWentFailed();
        }
    }

    public void verifySingleBrowseCategoryApiDisallowedInvalidAccessToken(Response response) {

        Pair<Boolean, Integer> result = verifyApiThrownErrorWithInvalidAccessToken(response);

        if (result.getValue0()) {
            verificationWentPassed();
        }
        else {
            LOGGER.error("Response status code came different with the expected status code! ");
            LOGGER.info("Actual status code >< Expected status code: " + result.getValue1() + " >< " + apiConstant.UNAUTHORIZED);
            verificationWentFailed();
        }
    }
}
