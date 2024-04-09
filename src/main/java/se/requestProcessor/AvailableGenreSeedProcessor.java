package se.requestProcessor;

import io.restassured.response.Response;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import se.model.apiModel.responseModel.AvailableGenreSeedModel;
import se.model.apiModel.responseModel.ErrorMessageModel;
import se.utility.GlobalVariableUtil;
import se.utility.JUtil;
import se.utility.JUtil.MapUtil;
import se.utility.StringUtil;
import se.utility.apiUtil.RestUtil;

import java.util.*;

public class AvailableGenreSeedProcessor extends BaseProcessor {

    //region Introducing constructors

    public AvailableGenreSeedProcessor() {
        super();
    }

    //endregion

    private final String getAvailableGenreSeedUri = "https://api.spotify.com/v1/recommendations/available-genre-seeds";

    //region Services regarding API requests

    public Response getAvailableGenreSeed() {

        Response response = _restUtil.sendAuthenticatedRequestWithResponse(
                getAvailableGenreSeedUri,
                null,
                null,
                RestUtil.EMethod.GET
        );

        return response;
    }

    public Response getAvailableGenreSeed(String dummyToken) {

        //Making request with an expected token
        Map<?, Response> response = _restUtil.sendAuthenticatedRequestWithResponse(
                dummyToken,
                getAvailableGenreSeedUri,
                null,
                null,
                RestUtil.EMethod.GET
        );

        return response.get(dummyToken);
    }

    public Response getAvailableGenreSeedWithBasicRequest() {

        //Making request with an expected token
        Response response = _restUtil.sendBasicRequest(
                getAvailableGenreSeedUri,
                null,
                null,
                RestUtil.EMethod.GET
        );

        return response;
    }

    //endregion

    //region Verifications

    public AvailableGenreSeedProcessor verifySeveralAvailableGenreSeedsListedInRespondedList(
            @NotNull Response response, @NotNull Map<Integer, String> expectedGenres
    ) {

        Map<Integer, String> unmodifiableGenresMap = Collections.unmodifiableMap(expectedGenres);
        Collection<String> unmodifiableGenresCollection =
                Collections.unmodifiableCollection(new ArrayList<>(unmodifiableGenresMap.values()));

        List<String> genres = response.getBody()
                .as(AvailableGenreSeedModel.class)
                .getGenres();

        Collection<String> _unmodifiableGenresCollection = Collections.unmodifiableCollection(genres);

        unmodifiableGenresCollection.forEach(actGenre -> {

            if (!_unmodifiableGenresCollection.contains(actGenre)) {

                LOGGER.error(StringUtil.appendStrings(Arrays.asList(
                        "<", actGenre, ">",
                        " was not contained in the responded list:\n",
                        _unmodifiableGenresCollection.toString(),
                        "\nat the genre indexed <",
                        MapUtil.retrieveKeyFromValue(unmodifiableGenresMap, actGenre).toString(),
                        ">"
                        ))
                );

                verificationWentFailed();
            }
        });

        LOGGER.info(StringUtil.appendStrings(Arrays.asList(
                "All genres in the expected list \n",
                unmodifiableGenresCollection.toString(),
                " \nwere contained within the actual list \n",
                _unmodifiableGenresCollection.toString()
        )));

        verificationWentPassed();

        return this;
    }

    public AvailableGenreSeedProcessor verifyTheExpectedListMatchedAccuratelyTheRespondedList(
            @NotNull Response response, @NotNull Map<Integer, String> expectedGenres
    ) {

        //Reversing the list of genres
        List<String> genres = (List<String>) JUtil.CollectionUtil
                .reverseListOrder(new ArrayList<>(expectedGenres.values()));

        List<String> unmodifiableGenres = Collections.unmodifiableList(genres);

        List<String> _genres = response.getBody()
                .as(AvailableGenreSeedModel.class)
                .getGenres();

        List<String> _unmodifiableGenres = Collections.unmodifiableList(_genres);

        Pair<Map<Integer, String>, Map<Integer, String>> differentGenres = null;

        //Finding differences between two maps: if there shall be any differences => assertion goes FAILED
        if (_unmodifiableGenres.size() != unmodifiableGenres.size() && GlobalVariableUtil.ScriptConfiguration.TROUBLESHOOTING_MODE) {

            throw new IllegalArgumentException(
                    StringUtil.appendStrings(Arrays.asList(
                            "The actual list's size and the expected list's size was not equal: ",
                            String.valueOf(_unmodifiableGenres.size()),
                            " >< ",
                            String.valueOf(unmodifiableGenres.size())
                    )));
        }
        else {
            Map<Integer, String> actualFailedGenres = new Hashtable<>();
            Map<Integer, String> expectedFailedGenres = new Hashtable<>();

            //Utilizing stream to find out the difference between two maps
            int[] genreIndex = { 0 };
            _unmodifiableGenres.stream().forEachOrdered(
                    actualGenre -> {

                        int _genreIndex = genreIndex[0]++;

                        if (!(_unmodifiableGenres.get(_genreIndex).equalsIgnoreCase(unmodifiableGenres.get(_genreIndex)))) {
                            actualFailedGenres.put(_genreIndex+1, _unmodifiableGenres.get(_genreIndex));
                            expectedFailedGenres.put(_genreIndex+1, unmodifiableGenres.get(_genreIndex));
                        }
                    }
            );

            differentGenres = Pair.with(actualFailedGenres, expectedFailedGenres);
        }

        //Assertions
        if (!(differentGenres.getValue0().isEmpty() && differentGenres.getValue1().isEmpty())) {

            LOGGER.error(StringUtil.appendStrings(Arrays.asList(
                    "There were differences between the expected list of genres and the actual list of genres: \n",
                    differentGenres.getValue0().toString(),
                    " >< ",
                    differentGenres.getValue1().toString()
            )));

            verificationWentFailed();
        }
        else {

            LOGGER.info("The expected list of genres matched impeccably the actual list of genres");
            verificationWentPassed();
        }

        return this;
    }

    public AvailableGenreSeedProcessor verifyGenreWasPresentedInTheListOfAvailableGenreSeeds(
            @NotNull Response response, String expectedGenreType) {

        AvailableGenreSeedModel availableGenreSeedModel = response.getBody().as(AvailableGenreSeedModel.class);

        String existedGenreType = availableGenreSeedModel.getGenres()
                .stream()
                .filter(gt -> gt.equalsIgnoreCase(expectedGenreType))
                .findFirst()
                .orElse(null);

        if (existedGenreType != null) {
            verificationWentPassed();
        }
        else {
            LOGGER.error("The expected genre type {" + expectedGenreType + "} was not contained in the responded genre list");
            verificationWentFailed();
        }

        return this;
    }

    //region Tokens verification

    public AvailableGenreSeedProcessor verifyInvalidTokenErrorMessageResponded(@NotNull Response response) {

        ErrorMessageModel errorMessageModel = response.getBody().as(ErrorMessageModel.class);

        ErrorMessageModel.Error errorModel = errorMessageModel.getError();

        int respondedStatusCode = errorModel.getStatus();
        String respondedErrorMessage = errorModel.getMessage();

        if (respondedStatusCode == apiConstant.UNAUTHORIZED &&
                Objects.equals(respondedErrorMessage, apiMessageConstant.INVALID_TOKEN_ERROR_MESSAGE)) {
            LOGGER.info(StringUtil.appendStrings(Arrays.asList(
                    "\nThe response was successfully matched the expectations: ",
                    "\nStatus code: ",
                    String.valueOf(apiConstant.UNAUTHORIZED),
                    "\nError message: ",
                    apiMessageConstant.INVALID_TOKEN_ERROR_MESSAGE
            )));

            verificationWentPassed();
        }
        else {
            LOGGER.error(StringUtil.appendStrings(Arrays.asList(
                    "\nThe response was not matched the expectations :(",
                    "\nStatus code: [", String.valueOf(respondedStatusCode), "] >< [", String.valueOf(apiConstant.UNAUTHORIZED), "]",
                    "\nError message: [", respondedErrorMessage, "] >< [", apiMessageConstant.INVALID_TOKEN_ERROR_MESSAGE, "]"
            )));

            verificationWentFailed();
        }

        return this;
    }

    public AvailableGenreSeedProcessor verifyExpiredTokenErrorMessageResponded(
            @NotNull Response response, String expiredTokenUniqueId) {

        ErrorMessageModel errorMessageModel = response.getBody().as(ErrorMessageModel.class);

        ErrorMessageModel.Error errorModel = errorMessageModel.getError();

        int respondedStatusCode = errorModel.getStatus();
        String respondedErrorMessage = errorModel.getMessage();

        if (respondedStatusCode == apiConstant.UNAUTHORIZED &&
                Objects.equals(respondedErrorMessage, apiMessageConstant.EXPIRED_TOKEN_ERROR_MESSAGE)) {

            LOGGER.info(StringUtil.appendStrings(Arrays.asList(
                    "\nExpired token's unique id = ",
                    expiredTokenUniqueId
            )));
            LOGGER.info(StringUtil.appendStrings(Arrays.asList(
                    "\nThe response was successfully matched the expectations: ",
                    "\nStatus code: ",
                    String.valueOf(apiConstant.UNAUTHORIZED),
                    "\nError message: ",
                    apiMessageConstant.EXPIRED_TOKEN_ERROR_MESSAGE
            )));

            verificationWentPassed();
        }
        else {
            LOGGER.error(StringUtil.appendStrings(Arrays.asList(
                    "\nThe response was not matched the expectations :(",
                    "\nStatus code: [", String.valueOf(respondedStatusCode), "] >< [", String.valueOf(apiConstant.UNAUTHORIZED), "]",
                    "\nError message: [", respondedErrorMessage, "] >< [", apiMessageConstant.EXPIRED_TOKEN_ERROR_MESSAGE, "]"
            )));
            LOGGER.error("Token's unique id that was expected to be an expired token = " + expiredTokenUniqueId);

            verificationWentFailed();
        }

        return this;
    }

    public AvailableGenreSeedProcessor verifyNoneOfTokenProvidedErrorMessageResponded(
            @NotNull Response response) {

        ErrorMessageModel errorMessageModel = response.getBody().as(ErrorMessageModel.class);

        ErrorMessageModel.Error errorModel = errorMessageModel.getError();

        int respondedStatusCode = errorModel.getStatus();
        String respondedErrorMessage = errorModel.getMessage();

        if (respondedStatusCode == apiConstant.UNAUTHORIZED &&
                Objects.equals(respondedErrorMessage, apiMessageConstant.NO_TOKEN_PROVIDED)) {

            LOGGER.info(StringUtil.appendStrings(Arrays.asList(
                    "\nThe response was successfully matched the expectations: ",
                    "\nStatus code: ",
                    String.valueOf(apiConstant.UNAUTHORIZED),
                    "\nError message: ",
                    apiMessageConstant.NO_TOKEN_PROVIDED
            )));

            verificationWentPassed();
        }
        else {
            LOGGER.error(StringUtil.appendStrings(Arrays.asList(
                    "\nThe response was not matched the expectations :(",
                    "\nStatus code: [", String.valueOf(respondedStatusCode), "] >< [", String.valueOf(apiConstant.UNAUTHORIZED), "]",
                    "\nError message: [", respondedErrorMessage, "] >< [", apiMessageConstant.NO_TOKEN_PROVIDED, "]"
            )));

            verificationWentFailed();
        }

        return this;
    }

    public AvailableGenreSeedProcessor verifyUnsupportedAuthenticationServiceErrorMessageResponded(
            @NotNull Pair<String, String> unmodifiableToken, @NotNull Response response
    ) {

        ErrorMessageModel errorMessageModel = response.getBody().as(ErrorMessageModel.class);

        ErrorMessageModel.Error errorModel = errorMessageModel.getError();

        int respondedStatusCode = errorModel.getStatus();
        String respondedErrorMessage = errorModel.getMessage();

        LOGGER.info(StringUtil.appendStrings(Arrays.asList(
                "\nAsserting the token with unique id = ",
                unmodifiableToken.getValue0()
        )));

        if (respondedStatusCode == apiConstant.UNSUPPORTED_AUTHENTICATION_SERVICE &&
                Objects.equals(respondedErrorMessage, apiMessageConstant.UNSUPPORTED_AUTHENTICATION_SERVICE)) {

            LOGGER.info(StringUtil.appendStrings(Arrays.asList(
                    "\nThe response was successfully matched the expectations: ",
                    "\nStatus code: ",
                    String.valueOf(apiConstant.UNSUPPORTED_AUTHENTICATION_SERVICE),
                    "\nError message: ",
                    apiMessageConstant.UNSUPPORTED_AUTHENTICATION_SERVICE
            )));

            verificationWentPassed();
        }
        else {
            LOGGER.error(StringUtil.appendStrings(Arrays.asList(
                    "\nThe response was not matched the expectations :(",
                    "\nStatus code: [", String.valueOf(respondedStatusCode), "] >< [", String.valueOf(apiConstant.UNSUPPORTED_AUTHENTICATION_SERVICE), "]",
                    "\nError message: [", respondedErrorMessage, "] >< [", apiMessageConstant.UNSUPPORTED_AUTHENTICATION_SERVICE, "]"
            )));

            verificationWentFailed();
        }

        return this;
    }

    //endregion Tokens verification

    //endregion Verifications
}