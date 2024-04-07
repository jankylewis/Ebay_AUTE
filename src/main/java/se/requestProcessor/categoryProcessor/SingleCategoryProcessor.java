package se.requestProcessor.categoryProcessor;

import io.restassured.response.Response;
import org.javatuples.Pair;
import se.requestProcessor.BaseProcessor;
import se.utility.apiUtil.RestProcessorUtil;

import java.util.Map;

public class SingleCategoryProcessor extends BaseProcessor {

    public SingleCategoryProcessor(){}

    private final String baseSingleCategoryBrowsingUri = "https://api.spotify.com/v1/browse/categories/";

    //region Processing API requests

    public Response getSingleCategoriesSuccessfully(String expCategory) {

        Response apiResponse = _restProcessorUtil.sendAuthenticatedRequestWithResponse(
                baseSingleCategoryBrowsingUri + expCategory,
                null,
                null,
                RestProcessorUtil.EMethod.GET
        );

        return apiResponse;
    }

    public Response getSingleCategoriesWithInvalidAccessToken(String expCategory, String expToken) {

        Map<?, Response> responseMap = _restProcessorUtil.sendAuthenticatedRequestWithResponse(
                expToken,
                baseSingleCategoryBrowsingUri + expCategory,
                null,
                null,
                RestProcessorUtil.EMethod.GET
        );;

        return responseMap.get(expToken);
    }

    //endregion

    //region Verifying requests

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
            LOGGER.info("Actual status code equals Expected status code: " + result.getValue1() + " >< " + apiConstant.UNAUTHORIZED);
            verificationWentPassed();
        }
        else {
            LOGGER.error("Response status code came different with the expected status code! ");
            LOGGER.info("Actual status code >< Expected status code: " + result.getValue1() + " >< " + apiConstant.UNAUTHORIZED);
            verificationWentFailed();
        }
    }

    public void verifyUserWasNotAuthenticatedWhenNoTokenProvided(Response response) {

        Pair<Boolean, Integer> result = verifyResponseStatusCodeWent401(response);

        if (result.getValue0()) {
            LOGGER.info("Actual status code equals Expected status code: "
                    + result.getValue1() + " >< " + apiConstant.UNSUPPORTED_AUTHENTICATION_SERVICE);

            verificationWentPassed();
        }
        else {
            LOGGER.error("Response status code came different with the expected status code! ");
            LOGGER.info("Actual status code >< Expected status code: "
                    + result.getValue1() + " >< " + apiConstant.UNSUPPORTED_AUTHENTICATION_SERVICE);

            verificationWentFailed();
        }
    }

    //endregion
}
