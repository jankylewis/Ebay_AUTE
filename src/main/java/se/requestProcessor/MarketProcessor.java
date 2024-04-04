package se.requestProcessor;

import io.restassured.response.Response;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import se.model.apiModel.responseModel.ErrorMessageModel;
import se.utility.apiUtil.RestUtil;

import java.util.HashMap;
import java.util.Objects;

public class MarketProcessor extends BaseProcessor {

    //region Introducing constructors

    private MarketProcessor() {
        super();
    }

    private MarketProcessor(RestUtil restUtil) {
        super(restUtil);
    }

    //endregion

    //region Handling instance

    public static final MarketProcessor INSTANCE = getInstance();

    private static MarketProcessor getInstance() {
        _requestProcessor = RestUtil.getInstance();
        return MarketProcessorHelper.INSTANCE;
    }

    private static final class MarketProcessorHelper {
        private static final MarketProcessor INSTANCE = new MarketProcessor();
    }

    //endregion

    //region URIs

    private final String marketBrowsingUri = "https://api.spotify.com/v1/markets";

    //endregion

    //region Making requests

    public Pair<MarketProcessor, Response> getMarketsWithNormalUri() {       //Normal request

        HashMap<RestUtil, Response> response = _requestProcessor.sendAuthenticatedRequestWithResponse(
            marketBrowsingUri,
                null,
                null,
                RestUtil.EMethod.GET
        );

        return Pair.with(INSTANCE, response.get(_requestProcessor));
    }

    public Pair<MarketProcessor, Response> getMarketsWithNormalUri(String abnormalSuffix) {       //Abnormal request

        HashMap<RestUtil, Response> response = _requestProcessor.sendAuthenticatedRequestWithResponse(
                marketBrowsingUri + "/" + abnormalSuffix,
                null,
                null,
                RestUtil.EMethod.GET
        );

        System.out.println("_  " + _requestProcessor.hashCode());

        return Pair.with(INSTANCE, response.get(_requestProcessor));
    }

    public Pair<MarketProcessor, Response> getMarketsWithExpiredToken(String expiredToken) {

        HashMap<RestUtil, Response> response = _requestProcessor.sendAuthenticatedRequestWithResponse(
                expiredToken,
                marketBrowsingUri,
                null,
                null,
                RestUtil.EMethod.GET
        );

        System.out.println("_  " + _requestProcessor.hashCode());

        return Pair.with(INSTANCE, response.get(_requestProcessor));
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
