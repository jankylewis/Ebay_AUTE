package se.spo.gui;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.business.HomePage;
import se.business.LogInPage;
import se.business.PlaylistPage;
import se.model.uiModel.UserInformationModel;
import se.utility.GlobalVariableUtil;

public class PlaylistTest extends BaseTestService {

    //region Introducing objects

    private PlaylistPage playlistPage;

    //endregion

    //region Verifying all User's playlists were deleted

    @Test(
            priority = 1,
            testName = "SWPLAYLIST_01: Verify User has the ability to delete all their existing playlists"
    )
    protected void spotifyUiTest_verifyUserDeletedSuccessfullyAllTheirExistingPlaylists() {
        playlistPage.deleteAllExistingPlaylists()
                .verifyAllExistingPlaylistsDeleted();
    }

    //endregion

    //region Test preparation

    @BeforeMethod
    protected void testPreparation() {

        playlistPage = new PlaylistPage(page);

        //Signing in to Spotify
        new LogInPage(page).navigateToLogInPage()
                .logInToSpotifyGateway(
                        new UserInformationModel(
                                GlobalVariableUtil.UserCredential.USER_EMAIL,
                                GlobalVariableUtil.UserCredential.USER_PASSWORD)
                )
                .verifyAccountMenuPresented();

        //Heading back to home page
        playlistPage.navigateToBaseUrl();

        new HomePage(page).waitForInstallAppButtonPresented();
    }

    //endregion
}
