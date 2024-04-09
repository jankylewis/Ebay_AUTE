package se.requestProcessor;

import io.restassured.response.Response;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import se.model.apiModel.responseModel.ErrorMessageModel;
import se.utility.apiUtil.RestUtil;

import java.util.Map;
import java.util.Objects;

public class MarketProcessor extends BaseProcessor {

    public MarketProcessor() {
        super();
    }

    //region URIs

    private final String marketBrowsingUri = "https://api.spotify.com/v1/markets";

    //endregion

    //region Making requests

    public Response getMarketsWithNormalUri() {       //Normal request

        Response response = _restUtil.sendAuthenticatedRequestWithResponse(
            marketBrowsingUri,
                null,
                null,
                RestUtil.EMethod.GET
        );

        return response;
    }

    public Response getMarketsWithNormalUri(String abnormalSuffix) {       //Abnormal request

        Response response = _restUtil.sendAuthenticatedRequestWithResponse(
                marketBrowsingUri + "/" + abnormalSuffix,
                null,
                null,
                RestUtil.EMethod.GET
        );

        return response;
    }

    public Response getMarketsWithExpiredToken(String expiredToken) {

        Map<?, Response> responseMap = _restUtil.sendAuthenticatedRequestWithResponse(
                expiredToken,
                marketBrowsingUri,
                null,
                null,
                RestUtil.EMethod.GET
        );

        return responseMap.get(expiredToken);
    }

    //endregion

    //region Verifications

    public void verifyRetrievingMarketsSuccessfully(Response response) {

        Pair<Boolean, Integer> result = verifyResponseStatusCodeWentGreen(response);

        if (result.getValue0()) {
            LOGGER.info("Status code went GREEN: " + apiConstant.GREEN_STATUS);
            verificationWentPassed();
            return;
        }

        LOGGER.error("Response status code came different with the expected status code! ");
        LOGGER.info("Actual status code >< Expected status code: " + result.getValue1() + " >< " + apiConstant.GREEN_STATUS);
        verificationWentFailed();
    }

    public void verifyApiResponded404(Response response) {      //Expectations: Status code was 404
        Pair<Boolean, Integer> result = verifyResponseStatusCodeWent404(response);

        if (result.getValue0()) {
            LOGGER.info("Status code went RED with: " + apiConstant.SERVICE_NOT_FOUND);
            verificationWentPassed();
            return;
        }

        LOGGER.error("Response status code came different with the expected status code! ");
        LOGGER.info("Actual status code >< Expected status code: "
                + result.getValue1() + " >< " + apiConstant.SERVICE_NOT_FOUND);

        verificationWentFailed();
    }

    public void verifyApiResponded401(@NotNull Response response) {

        if (response.statusCode() == apiConstant.UNAUTHORIZED) {

            ErrorMessageModel errorMessageModel = response.getBody().as(ErrorMessageModel.class);

            ErrorMessageModel.Error errorModel = errorMessageModel.getError();

            if (Objects.equals(errorModel.getMessage(), apiMessageConstant.INVALID_TOKEN_ERROR_MESSAGE)) {
                LOGGER.info("Status code went RED with: " + apiMessageConstant.INVALID_TOKEN_ERROR_MESSAGE);
                verificationWentPassed();
                return;
            }

            LOGGER.error("The actual error message did not match with the expected error message: " +
                    "<" + errorModel.getMessage() + ">" + " != " + "<" + apiMessageConstant.INVALID_TOKEN_ERROR_MESSAGE + ">"  );
            verificationWentFailed();
        }

        LOGGER.error("Response status code came different with the expected status code: " +
                response.statusCode() + " >< " + apiConstant.UNAUTHORIZED);
        verificationWentFailed();
    }

    //endregion
}
