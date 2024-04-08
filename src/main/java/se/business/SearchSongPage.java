package se.business;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import se.commonHandler.baseService.BaseVerification.IVerification;
import se.model.uiModel.SearchSongModel.ETab;
import se.pageObject.SearchSongObject;
import se.utility.GlobalVariableUtil;
import se.utility.ParallelUtil;

import java.util.*;

public class SearchSongPage extends SearchSongObject
        implements IVerification {
    public SearchSongPage(Page page) {
        super(page);
    }

    Map<String, Collection<String>> verifiedData;

    public SearchSongPage navigateToSearchSongPage() {

        baseUi.navigateToUrl(GlobalVariableUtil.Environment.PUBLIC_URL +
                localPathConst.SEARCH_SONG_PATH);

        return this;
    }

    public SearchSongPage searchSong(String songName) {

        baseUi.sendKeyToElement(findLocator(TXT_SEARCH_SONG), songName);
        baseUi.pressKey("Enter");

        return this;
    }

    public SearchSongPage switchToTab(@NotNull ETab tab) {
        baseUi.clickOnVisibleElement(
                findLocator(LNK_TAB(getTabIndex(tab.getEValue()))));
        return this;
    }

    private int getTabIndex(@NotNull String tab) {

        for (ETab _tab : ETab.values()) {
            if (Objects.equals(_tab.getEValue(), tab)) {
                return _tab.ordinal()+1;
            }
        }

        throw new IllegalArgumentException("The inputted tab was not valid!");
    }

    //region Verifications section

    //region At All Tab

    private class SearchSongAtAllTab extends SearchSongObjectAtAllTab {

        private SearchSongAtAllTab(Page page) {
            super(page);
        }

        private void verifySongNameAtAllTab(final String verifiedSearchKey) throws InterruptedException {

            boolean wasSuccessfullyVerified;

            //Preparing data for verifications
            verifiedData = new HashMap<>() {{
                put("top song", Collections.singletonList(findLocator(LBL_TOP_RESULT(verifiedSearchKey)).textContent()));
                put("songs", baseUi.getTextsFromLocators(findListOfLocators(LBL_SONGS)));
                put("artists", baseUi.getTextsFromLocators(findListOfLocators(LBL_ARTISTS)));
                put("albums", baseUi.getTextsFromLocators(findListOfLocators(LBL_ALBUMS)));
                put("playlists", baseUi.getTextsFromLocators(findListOfLocators(LBL_PLAYLISTS)));
                put("podcasts", baseUi.getTextsFromLocators(findListOfLocators(LBL_PODCASTS)));
                put("episodes", baseUi.getTextsFromLocators(findListOfLocators(LBL_EPISODES)));
                put("profiles", baseUi.getTextsFromLocators(findListOfLocators(LBL_PROFILES)));
                put("genres and moods", baseUi.getTextsFromLocators(findListOfLocators(LBL_GENRES_AND_MOODS)));
            }};

            //Executing verifications
            ParallelUtil.parallelizeFunctions(new ArrayList<>() {{
                //Task verifying Top Song
                add(() -> verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("top song"))));

                //Task verifying Songs
                add(() -> verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("songs"))));

                //Task verifying Artists
                add(() -> verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("artists"))));

                //Task verifying Albums
                add(() -> verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("albums"))));

                //Task verifying Playlists
                add(() -> verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("playlists"))));

                //Task verifying Podcasts
                add(() -> verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("podcasts"))));

                //Task verifying Episodes
                add(() -> verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("episodes"))));

                //Task verifying Profiles
                add(() -> verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("profiles"))));

                //Task verifying Genres and Moods
                add(() -> verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("genres and moods"))));
            }});

            wasSuccessfullyVerified = true;
            if (wasSuccessfullyVerified) {
                verificationWentPassed();
            }
        }
    }

    //endregion

    //region At Artists Tab

    private class SearchSongAtArtistsTab extends SearchSongObjectAtArtistsTab {

        private SearchSongAtArtistsTab(Page page) {
            super(page);
        }

        private void verifySongNameAtArtistsTab(final String verifiedSearchKey) {

            waitHelper.waitForElementAttached(findLocator(E_INFINITE_SCROLL_LIST));

            boolean wasSuccessfullyVerified;

            verifiedData = new HashMap<>() {{
                put("artists", baseUi.getTextsFromLocators(findListOfLocators(LBL_ARTISTS)));
            }};

            verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("artists")));

            wasSuccessfullyVerified = true;
            if (wasSuccessfullyVerified) {
                verificationWentPassed();
            }
        }
    }

    //endregion

    //region At Songs Tab

    private class SearchSongAtSongsTab extends SearchSongObjectAtSongsTab {

        private SearchSongAtSongsTab(Page page) {
            super(page);
        }

        private void verifySongNameAtSongsTab(final String verifiedSearchKey) {

            waitHelper.waitForElementAttached(findLocator(E_INFINITE_SCROLL_LIST));

            boolean wasSuccessfullyVerified;

            verifiedData = new HashMap<>() {{
                put("songs", baseUi.getTextsFromLocators(findListOfLocators(LBL_SONGS)));
            }};

            verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("songs")));

            wasSuccessfullyVerified = true;
            if (wasSuccessfullyVerified) {
                verificationWentPassed();
            }
        }
    }

    //endregion

    //region At Playlists Tab

    private class SearchSongAtPlaylistsTab extends SearchSongObjectAtPlaylistsTab {

        private SearchSongAtPlaylistsTab(Page page) {
            super(page);
        }

        private void verifySongNameAtPlaylistsTab(final String verifiedSearchKey) {

            waitHelper.waitForElementAttached(findLocator(E_INFINITE_SCROLL_LIST));

            boolean wasSuccessfullyVerified;

            verifiedData = new HashMap<>() {{
                put("playlists", baseUi.getTextsFromLocators(findListOfLocators(LBL_PLAYLISTS)));
            }};

            verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("playlists")));

            wasSuccessfullyVerified = true;
            if (wasSuccessfullyVerified) {
                verificationWentPassed();
            }
        }
    }

    //endregion

    //region At Albums Tab

    private class SearchSongAtAlbumsTab extends SearchSongObjectAtAlbumsTab {

        private SearchSongAtAlbumsTab(Page page) {
            super(page);
        }

        private void verifySongNameAtAlbumsTab(final String verifiedSearchKey) {

            waitHelper.waitForElementAttached(findLocator(E_INFINITE_SCROLL_LIST));

            boolean wasSuccessfullyVerified;

            verifiedData = new HashMap<>() {{
                put("albums", baseUi.getTextsFromLocators(findListOfLocators(LBL_ALBUMS)));
            }};

            verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("albums")));

            wasSuccessfullyVerified = true;
            if (wasSuccessfullyVerified) {
                verificationWentPassed();
            }
        }
    }

    //endregion

    //region At Podcasts and Shows Tab

    private class SearchSongAtPodcastsAndShowsTab extends SearchSongObjectAtPodcastsAndShowsTab {


        private SearchSongAtPodcastsAndShowsTab(Page page) {
            super(page);
        }

        private void verifySongNameAtPodcastAndShowsSection(final String verifiedSearchKey) {

            waitHelper.waitForElementAttached(findLocator(E_INFINITE_SCROLL_LIST));

            boolean wasSuccessfullyVerified;

            verifiedData = new HashMap<>() {{
                put("podcasts and shows", baseUi.getTextsFromLocators(findListOfLocators(LBL_PODCASTS_AND_SHOWS)));
            }};

            verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("podcasts and shows")));

            wasSuccessfullyVerified = true;
            if (wasSuccessfullyVerified) {
                verificationWentPassed();
            }
        }

        private void verifySongNameAtEpisodesSection(final String verifiedSearchKey) {

            boolean wasSuccessfullyVerified;

            verifiedData = new HashMap<>() {{
                put("episodes", baseUi.getTextsFromLocators(findListOfLocators(LBL_EPISODES)));
            }};

            verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("episodes")));

            wasSuccessfullyVerified = true;
            if (wasSuccessfullyVerified) {
                verificationWentPassed();
            }
        }
    }

    //endregion

    //region At Genres and Moods Tab

    private class SearchSongAtGenresAndMoodsTab extends SearchSongObjectAtGenresAndMoodsTab {

        private SearchSongAtGenresAndMoodsTab(Page page) {
            super(page);
        }

        private void verifySongNameAtGenresAndMoodsTab(final String verifiedSearchKey) {

            waitHelper.waitForElementAttached(findLocator(E_INFINITE_SCROLL_LIST));

            boolean wasSuccessfullyVerified;

            verifiedData = new HashMap<>() {{
                put("genres and moods", baseUi.getTextsFromLocators(findListOfLocators(LBL_GENRES)));
            }};

            verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("genres and moods")));

            wasSuccessfullyVerified = true;
            if (wasSuccessfullyVerified) {
                verificationWentPassed();
            }
        }
    }

    //endregion

    //region At Profiles Tab

    private class SearchSongAtProfilesTab extends SearchSongObjectAtProfilesTab {


        private SearchSongAtProfilesTab(Page page) {
            super(page);
        }

        private void verifySongNameAtProfilesTab(final String verifiedSearchKey) {

            waitHelper.waitForElementAttached(findLocator(E_INFINITE_SCROLL_LIST));

            boolean wasSuccessfullyVerified;

            verifiedData = new HashMap<>() {{
                put("profiles", baseUi.getTextsFromLocators(findListOfLocators(LBL_PROFILES)));
            }};

            verifyTextsContained(Pair.with(verifiedSearchKey, verifiedData.get("profiles")));

            wasSuccessfullyVerified = true;
            if (wasSuccessfullyVerified) {
                verificationWentPassed();
            }
        }
    }

    //endregion

    private boolean verifyTextsContained(@NotNull final Pair<String, Collection<String>> verifiedPairs) {

        for (String actText : verifiedPairs.getValue1()) {
            baseVerification.verifyExpectedStringContained(verifiedPairs.getValue0(), actText);
        }

        return true;
    }

    public void verifySongNameMatchedKeyword(@NotNull ETab verifiedTab,
                                             final String verifiedSearchKey) throws InterruptedException {

        switch (verifiedTab) {
            case ALL -> new SearchSongAtAllTab(page).verifySongNameAtAllTab(verifiedSearchKey);
            case SONGS -> new SearchSongAtSongsTab(page).verifySongNameAtSongsTab(verifiedSearchKey);
            case ARTISTS -> new SearchSongAtArtistsTab(page).verifySongNameAtArtistsTab(verifiedSearchKey);
            case ALBUMS -> new SearchSongAtAlbumsTab(page).verifySongNameAtAlbumsTab(verifiedSearchKey);
            case PODCASTS_AND_SHOWS -> {
                SearchSongAtPodcastsAndShowsTab searchSongAtPodcastsAndShowsTab = new SearchSongAtPodcastsAndShowsTab(page);
                searchSongAtPodcastsAndShowsTab.verifySongNameAtPodcastAndShowsSection(verifiedSearchKey);
                searchSongAtPodcastsAndShowsTab.verifySongNameAtEpisodesSection(verifiedSearchKey);
            }
            case GENRES_AND_MOODS -> new SearchSongAtGenresAndMoodsTab(page).verifySongNameAtGenresAndMoodsTab(verifiedSearchKey);
            case PROFILES -> new SearchSongAtProfilesTab(page).verifySongNameAtProfilesTab(verifiedSearchKey);
            case PLAYLISTS -> new SearchSongAtPlaylistsTab(page).verifySongNameAtPlaylistsTab(verifiedSearchKey);
        }
    }

    public void verifyNoResultsMessageFound(String searchKey) {

        Locator noResultsFoundLbl = findLocator(LBL_NO_RESULTS_FOUND);
        Locator useDifferentKeywords = findLocator(LBL_USE_DIFFERENT_KEYWORDS);

        if (baseVerification.verifyIfElementVisible(noResultsFoundLbl) &&
                baseVerification.verifyStringEquality(msgConst.LBL_NO_RESULTS_FOUND(searchKey), noResultsFoundLbl.textContent()) &&
                baseVerification.verifyIfElementVisible(useDifferentKeywords) &&
                baseVerification.verifyStringEquality(msgConst.LBL_USE_DIFFERENT_KEYWORDS, useDifferentKeywords.textContent())) {

            verificationWentPassed();
        }
    }

    //region IVerification

    @Override
    public void verificationWentPassed() {
        assert true;
    }

    @Override
    public void verificationWentFailed() {
        assert false;
    }

    //endregion

    //endregion
}