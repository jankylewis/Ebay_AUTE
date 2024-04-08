package se.spo.api.testDataProvider;

import org.javatuples.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.testng.annotations.DataProvider;
import se.commonHandler.baseService.BaseApiService;
import se.utility.JUtil;
import se.utility.StringUtil;
import se.utility.fileUtil.fileReaderUtil.CsvFileReader;
import se.utility.fileUtil.fileWriterUtil.CsvFileWriter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class TestDataFactory extends BaseApiService {

    //region Introducing variables

    private String methodName;
    private String className;
    private Pair<String, String> testContext;
    private final String baseApiTestDataFolder = "apiTestDataFiles/";

    //endregion

    //region Introducing instance

    public static final TestDataFactory INSTANCE = getInstance();

    private static class TestDataProviderFactoryHelper {
        private static final TestDataFactory _INSTANCE = new TestDataFactory();
    }

    private static TestDataFactory getInstance() {
        return TestDataProviderFactoryHelper._INSTANCE;
    }

    public TestDataFactory(){}

    //endregion

    @Contract(value = " -> new", pure = true)
    @DataProvider(
            name = "ExpiredTokensProvider"
    )
    private Object @NotNull [] prepareExpiredTokens(@NotNull Method method)
            throws IOException {

        logRuntimeInformation(method);

        List<Pair<String, String>> expiredTokens = CsvFileReader
                .INSTANCE
                .getValuesWithHeaders(
                        baseApiTestDataFolder + "expired_tokens.csv", new String[]{"Tokens' unique ids", "Expired tokens provided"});

        return JUtil.ListUtil.convertListToArrayObject(expiredTokens);
    }

    public Collection<?> prepareModifiableTokensWithCsv(@NotNull Method method)
            throws IOException {

        Collection<?> values =  new HashSet<>();

        logRuntimeInformation(method);

        switch (methodName) {
            case "spotifyApiTest_VerifyApiThrewErrorIfNoTokenProvided" -> {

                //No token prepared
                CsvFileWriter.INSTANCE
                        .modifyCsvFile(baseApiTestDataFolder + "modified_tokens.csv", new String[]{}, values);
            }
            case "spotifyApiTest_VerifyApiRejectedIfUserRequestedAnotherAuthenticationService" -> {

                Collection<Pair<?, ?>> _values = new ArrayList<>(){{
                    add(Pair.with(apiFaker.produceFakeUuid().toString(), ""));
                    add(Pair.with(apiFaker.produceFakeUuid().toString(), ""));
                }};

                values = CsvFileWriter.INSTANCE
                                .modifyCsvFileWithPairOfValues(baseApiTestDataFolder + "modified_tokens.csv",
                                        new String[]{"Tokens' unique ids", "Empty tokens provided"}, _values);
            }
        }

        return values;
    }

    @DataProvider(
            name = "InvalidTokensProvider"
    )
    private Object @NotNull [] prepareInvalidTokens() {

        List<List<String>> listOfDummyTokens = new LinkedList<>();

        for (int i = 0; i < apiFaker.getRandomInstance().nextInt(6, 10); ++i) {

            List<String> dummyTokens = new LinkedList<>();

            for (int _i = 0; _i < apiFaker.getRandomInstance().nextInt(10, 11); ++_i) {
                dummyTokens.add(apiFaker.generateSpotifyDummyTokens(apiFaker.getSecureRandomInstance()));
            }

            listOfDummyTokens.add(dummyTokens);
        }

        //Converting list of tokens to array object
        return JUtil.ListUtil.convertListToArrayObject(listOfDummyTokens);
    }

    private TestDataFactory logRuntimeInformation(@NotNull Method callingMethod) {

        LOGGER.info(StringUtil.appendStrings(Arrays.asList(
                "\n",
                "Method named <",
                methodName = callingMethod.getName(),
                "> ",
                "and from class <",
                className = callingMethod.getDeclaringClass().getName(),
                "> calling request(s)"
        )));

        return INSTANCE;
    }
    public static class AvailableGenreSeedDataProvider {

        @DataProvider(
                parallel = false,
                name = "AvailableGenreSeedsProvider"
        )
        private Object @Nullable [] prepareAvailableGenreSeedsData(@NotNull Method method) {

            INSTANCE.logRuntimeInformation(method);

            //region All genres existed in Spotify

            Map<Integer, String> availableGenreSeedsHashTable = new Hashtable<>() {{
                put(1, "acoustic");
                put(2, "afrobeat");
                put(3, "alt-rock");
                put(4, "alternative");
                put(5, "ambient");
                put(6, "anime");
                put(7, "black-metal");
                put(8, "bluegrass");
                put(9, "blues");
                put(10, "bossanova");
                put(11, "brazil");
                put(12, "breakbeat");
                put(13, "british");
                put(14, "cantopop");
                put(15, "chicago-house");
                put(16, "children");
                put(17, "chill");
                put(18, "classical");
                put(19, "club");
                put(20, "comedy");
                put(21, "country");
                put(22, "dance");
                put(23, "dancehall");
                put(24, "death-metal");
                put(25, "deep-house");
                put(26, "detroit-techno");
                put(27, "disco");
                put(28, "disney");
                put(29, "drum-and-bass");
                put(30, "dub");
                put(31, "dubstep");
                put(32, "edm");
                put(33, "electro");
                put(34, "electronic");
                put(35, "emo");
                put(36, "folk");
                put(37, "forro");
                put(38, "french");
                put(39, "funk");
                put(40, "garage");
                put(41, "german");
                put(42, "gospel");
                put(43, "goth");
                put(44, "grindcore");
                put(45, "groove");
                put(46, "grunge");
                put(47, "guitar");
                put(48, "happy");
                put(49, "hard-rock");
                put(50, "hardcore");
                put(51, "hardstyle");
                put(52, "heavy-metal");
                put(53, "hip-hop");
                put(54, "holidays");
                put(55, "honky-tonk");
                put(56, "house");
                put(57, "idm");
                put(58, "indian");
                put(59, "indie");
                put(60, "indie-pop");
                put(61, "industrial");
                put(62, "iranian");
                put(63, "j-dance");
                put(64, "j-idol");
                put(65, "j-pop");
                put(66, "j-rock");
                put(67, "jazz");
                put(68, "k-pop");
                put(69, "kids");
                put(70, "latin");
                put(71, "latino");
                put(72, "malay");
                put(73, "mandopop");
                put(74, "metal");
                put(75, "metal-misc");
                put(76, "metalcore");
                put(77, "minimal-techno");
                put(78, "movies");
                put(79, "mpb");
                put(80, "new-age");
                put(81, "new-release");
                put(82, "opera");
                put(83, "pagode");
                put(84, "party");
                put(85, "philippines-opm");
                put(86, "piano");
                put(87, "pop");
                put(88, "pop-film");
                put(89, "post-dubstep");
                put(90, "power-pop");
                put(91, "progressive-house");
                put(92, "psych-rock");
                put(93, "punk");
                put(94, "punk-rock");
                put(95, "r-n-b");
                put(96, "rainy-day");
                put(97, "reggae");
                put(98, "reggaeton");
                put(99, "road-trip");
                put(100, "rock");
                put(101, "rock-n-roll");
                put(102, "rockabilly");
                put(103, "romance");
                put(104, "sad");
                put(105, "salsa");
                put(106, "samba");
                put(107, "sertanejo");
                put(108, "show-tunes");
                put(109, "singer-songwriter");
                put(110, "ska");
                put(111, "sleep");
                put(112, "songwriter");
                put(113, "soul");
                put(114, "soundtracks");
                put(115, "spanish");
                put(116, "study");
                put(117, "summer");
                put(118, "swedish");
                put(119, "synth-pop");
                put(120, "tango");
                put(121, "techno");
                put(122, "trance");
                put(123, "trip-hop");
                put(124, "turkish");
                put(125, "work-out");
                put(126, "world-music");
            }};

            //endregion

            switch (method.getName()) {
                case "spotifyApiTest_VerifyRespondedListMatchedAccuratelyExpectedList" -> {

                    return new Object[] { availableGenreSeedsHashTable };
                }
                case "spotifyApiTest_VerifySeveralAvailableGenreSeedsWillBeListed" -> {

                    Random random = new Random();

                    int hashTableSize = availableGenreSeedsHashTable.size();

                    //Randomizing from 1 -> 126
                    int numberOfGenresPrepared = random.nextInt(1, hashTableSize);

                    Hashtable<Integer, String> _availableGenreSeedsHashTable = new Hashtable<>();

                    int _genreIndex = 0;
                    do {
                        _availableGenreSeedsHashTable.put(_genreIndex+1, availableGenreSeedsHashTable.get(_genreIndex+1));
                        _genreIndex++;
                    } while (_genreIndex != numberOfGenresPrepared);

                    return new Object[]{ _availableGenreSeedsHashTable };
                }
            }

            throw new RuntimeException("Data provider got an unexpected error!      ");
        }
    }
}
