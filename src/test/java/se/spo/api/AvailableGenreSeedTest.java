package se.spo.api;

import io.restassured.response.Response;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.requestProcessor.AvailableGenreSeedProcessor;
import se.spo.api.testDataProvider.TestDataFactory;
import se.spo.api.testDataProvider.TestDataFactory.AvailableGenreSeedDataProvider;
import se.utility.ParallelUtil;
import se.utility.apiUtil.RestUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class AvailableGenreSeedTest extends BaseApiTestService {

    private AvailableGenreSeedProcessor availableGenreSeedProcessor;

    @Test(
            priority = 2,
            testName = "SAAVAILABLEGENRESEED_01",
            description = "Verify Gospel type was included in the response when User made a request"
    )
    protected void spotifyApiTest_VerifyGospelTypeWasPresentedInTheResponse() {
        final String genreType = "Gospel";
        Response dataResponded = availableGenreSeedProcessor.getAvailableGenreSeed();
        availableGenreSeedProcessor.verifyGenreWasPresentedInTheListOfAvailableGenreSeeds(dataResponded, genreType);
    }

    @Test(
            priority = 1,
            testName = "SAAVAILABLEGENRESEED_02",
            description = "Verify that the responded list of genre seeds shall match accurately the expected list",
            dataProvider = "AvailableGenreSeedsProvider",
            dataProviderClass = AvailableGenreSeedDataProvider.class
    )
    protected void spotifyApiTest_VerifyRespondedListMatchedAccuratelyExpectedList(
            @NotNull Hashtable availableGenreSeedsHashTable
    ) {
        //Making a request headed toward getting available genre seeds API
        Response dataResponded = availableGenreSeedProcessor.getAvailableGenreSeed();

        availableGenreSeedProcessor.verifyTheExpectedListMatchedAccuratelyTheRespondedList(
                dataResponded,
                availableGenreSeedsHashTable
        );
    }

    @Test(
            priority = 2,
            testName = "SAAVAILABLEGENRESEED_03",
            description = "Verify that several available genre seeds will be comprised of the responded list",
            dataProvider = "AvailableGenreSeedsProvider",
            dataProviderClass = AvailableGenreSeedDataProvider.class
    )
    protected void spotifyApiTest_VerifySeveralAvailableGenreSeedsWillBeListed(
            @NotNull Hashtable availableGenreSeedsHashTable
    ) {
        //Making a request headed toward getting available genre seeds API
        Response dataResponded = availableGenreSeedProcessor.getAvailableGenreSeed();

        //Verifying the several genres listed in the responded list of genres
        availableGenreSeedProcessor.verifySeveralAvailableGenreSeedsListedInRespondedList(
                dataResponded, availableGenreSeedsHashTable
        );
    }

    @Test(
            priority = 3,
            testName = "SAAVAILABLEGENRESEED_04",
            description = "Verify that the invalid tokens shall not be authenticated",
            dataProviderClass = TestDataFactory.class,
            dataProvider = "InvalidTokensProvider"
    )
    protected void spotifyApiTest_VerifyTheInvalidTokensWereNotAuthenticated(
            @NotNull List<String> invalidTokens) {

        int numberOfTokensProcessed = invalidTokens.size();
        int tokenIdx = 0;

        do {

            //Making a request headed toward getting available genre seeds API with dummy tokens
            Response dataResponded = availableGenreSeedProcessor.getAvailableGenreSeed(invalidTokens.get(tokenIdx));

            //Verifying User shall not be authenticated with dummy tokens
            availableGenreSeedProcessor.verifyInvalidTokenErrorMessageResponded(dataResponded);

            tokenIdx++;         //Index increment
        } while (tokenIdx < numberOfTokensProcessed);
    }

    @Test(
            priority = 3,
            testName = "SAAVAILABLEGENRESEED_05",
//            parallel = "methods",  // This should work with TestNG 7.9.0
            description = "Verify that the expired tokens shall not be authenticated",
            dataProviderClass = TestDataFactory.class,
            dataProvider = "ExpiredTokensProvider"
    )
    protected void spotifyApiTest_VerifyTheExpiredTokensWereNotAuthenticated(
            @NotNull Pair<String, String> expiredTokens) {

        //Making a request headed toward getting available genre seeds API with an expired token
        Response dataResponded = availableGenreSeedProcessor.getAvailableGenreSeed(expiredTokens.getValue1());

        availableGenreSeedProcessor.verifyExpiredTokenErrorMessageResponded(dataResponded, expiredTokens.getValue0());
    }

//    @Test(
//            priority = 3,
//            testName = "SAAVAILABLEGENRESEED_08",
//            description = "Verify that the expired tokens shall not be authenticated"
//    )
//    protected void spotifyApiTest_VerifyTheExpiredTokensWereNotAuthenticated_WithParallelMode()
//            throws IOException, InterruptedException {
//
//        TestDataFactory.AvailableGenreSeedDataProvider availableGenreSeedDataProvider = new AvailableGenreSeedDataProvider();
//        List<Pair<String, String>> expiredTokenPairs = availableGenreSeedDataProvider.prepareExpiredTokens();
//
//        List<String> expiredTokens = expiredTokenPairs.stream().map(Pair::getValue1).collect(Collectors.toList());
//        List<String> expiredTokenUniqueIDs = expiredTokenPairs.stream().map(Pair::getValue0).collect(Collectors.toList());
//
//        List<Response> responses = new ArrayList<>();
//        List<Callable<Boolean>> tasks = new ArrayList<>();
//
//        for (int idx = 0; idx < expiredTokens.size(); idx++) {
//
//            int _idx = idx;
//            String _expiredTokens = expiredTokens.get(_idx);
//
//            tasks.add(() -> {
//
//                //Making a request headed toward getting available genre seeds API with an expired token
//                Response dataResponded = availableGenreSeedProcessor.getAvailableGenreSeed(_expiredTokens);
//
//                return responses.add(dataResponded);
//            });
//        }
//
//        new ParallelUtil()._parallelizeFunctions(tasks);
//
//        availableGenreSeedProcessor.verifyExpiredTokenErrorMessageResponded_WithParallelTest(responses, expiredTokenUniqueIDs);
//    }

    @Test(
            priority = 3,
            testName = "SAAVAILABLEGENRESEED_06",
            description = "Verify that Api responded a certain error if there was no token provided"
    )
    protected void spotifyApiTest_VerifyApiThrewErrorIfNoTokenProvided() throws IOException {

        //Generating an empty HashSet
        TestDataFactory.INSTANCE.prepareModifiableTokensWithCsv(
                new Object() {}.getClass().getEnclosingMethod());

        Response dataResponded =
                availableGenreSeedProcessor.getAvailableGenreSeedWithBasicRequest();

        availableGenreSeedProcessor.verifyNoneOfTokenProvidedErrorMessageResponded(dataResponded);
    }

    @Test(
            priority = 3,
            testName = "SAAVAILABLEGENRESEED_07",
            description = "Verify that Api rejected when User requested other authentication services"
    )
    protected void spotifyApiTest_VerifyApiRejectedIfUserRequestedAnotherAuthenticationService()
            throws IOException {

        Collection<Pair<String, String>> unmodifiableTokens =
                Collections.unmodifiableCollection((Collection<Pair<String, String>>)
                        TestDataFactory
                                .INSTANCE
                                .prepareModifiableTokensWithCsv(new Object() {}.getClass().getEnclosingMethod()));

        for (int idx = 0; idx < unmodifiableTokens.size(); idx++) {

            Response dataResponded = availableGenreSeedProcessor.getAvailableGenreSeed("");

            availableGenreSeedProcessor.verifyUnsupportedAuthenticationServiceErrorMessageResponded(
                    unmodifiableTokens.stream().toList().get(idx), dataResponded);
        }
    }

    @BeforeMethod
    protected void testPreparation() {
        availableGenreSeedProcessor = new AvailableGenreSeedProcessor();
    }
}
