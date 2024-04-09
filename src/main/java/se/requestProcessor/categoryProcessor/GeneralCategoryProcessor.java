package se.requestProcessor.categoryProcessor;

import io.restassured.response.Response;
import org.javatuples.Pair;
import se.requestProcessor.BaseProcessor;
import se.utility.StringUtil;
import se.utility.apiUtil.RestUtil;

import java.util.Arrays;

public class GeneralCategoryProcessor extends BaseProcessor {

    public GeneralCategoryProcessor() {
        super();
    }

    private final String categoriesBrowsingUri = "https://api.spotify.com/v1/browse/categories";

    //region Making requests to get list of browse categories

    //Blocking access to this method from others
    public Response getBrowseCategoriesSuccessfully() {

        Response response = _restUtil.sendAuthenticatedRequestWithResponse(
                categoriesBrowsingUri,
                null,
                null,
                RestUtil.EMethod.GET
        );

        return response;
    }

    public Response getBrowseCategoriesUnsuccessfully() {

        String x = StringUtil.appendStrings(Arrays.asList(categoriesBrowsingUri, "/", apiFaker.produceFakeUuid().toString()));

        Response response = _restUtil.sendAuthenticatedRequestWithResponse(
                StringUtil.appendStrings(Arrays.asList(categoriesBrowsingUri, "/", apiFaker.produceFakeUuid().toString())),
                null,
                null,
                RestUtil.EMethod.GET
        );

        return response;
    }

    //endregion

    //region Verifications

    public void verifyBrowseCategoriesRequestResponseSttCode(Response response) {

        Pair<Boolean, Integer> responseHealth = verifyResponseStatusCodeWentGreen(response);

        if (responseHealth.getValue0()) verificationWentPassed();
        else {
            LOGGER.error("Response status code came different with the expected status code! ");
            LOGGER.info("Actual status code >< Expected status code: " + responseHealth.getValue1() + " >< " + apiConstant.GREEN_STATUS);
            verificationWentFailed();
        }
    }

    public void verifyBrowseCategoriesRequestRespondedWith404SttCode(Response response) {

        Pair<Boolean, Integer> responseHealth = verifyResponseStatusCodeWent404(response);

        if (responseHealth.getValue0()) verificationWentPassed();
        else {
            LOGGER.error("Response status code came different with the expected status code! ");
            LOGGER.info("Actual status code >< Expected status code: " + responseHealth.getValue1() + " >< " + apiConstant.SERVICE_NOT_FOUND);
            verificationWentFailed();
        }
    }

    //endregion
}
