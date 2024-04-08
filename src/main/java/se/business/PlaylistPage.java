package se.business;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import se.commonHandler.baseService.BaseVerification.IVerification;
import se.pageObject.PlaylistObject;

import java.util.List;

public class PlaylistPage extends PlaylistObject implements IVerification {

    public PlaylistPage(Page page) {
        super(page);
    }

    //region Taking UI actions

    public PlaylistPage deleteAllExistingPlaylists() {

        //Waiting for skeletons to be vanished
        Boolean wasLoadingCompleted = waitForSkeletonLoadingToBeDetached(50);
        if (wasLoadingCompleted == null) {
            throw new RuntimeException("Loading was not completed!  ");
        }

        LOGGER.info(wasLoadingCompleted.toString());

        List<Locator> listOfPlaylistLocators = findListOfLocators(DIV_PLAYLISTS);

        int idx = 0;
        while (idx < listOfPlaylistLocators.size()) {           //Cleaning all existing playlists

            List<Locator> _listOfPlaylistLocators = findListOfLocators(DIV_PLAYLISTS);

            pollingWaitHelper.waitForElementToBeEnabledWithPollings(DIV_PLAYLISTS, null);

            baseUi.hoverElement(_listOfPlaylistLocators.get(0));
            baseUi.clickOnVisibleElement(_listOfPlaylistLocators.get(0));
            baseUi.clickOnVisibleElement(findLocator(BTN_MORE));
            baseUi.clickOnVisibleElement(findLocator(BTN_DELETE));
            baseUi.clickOnVisibleElement(findLocator(BTN_DELETE_ON_CONFIRMATION_MODAL));

            idx++;
        }

        return this;
    }

    //endregion

    //region Verifications

    public PlaylistPage verifyAllExistingPlaylistsDeleted() {

        if (baseVerification.verifyIfElementVisible(findLocator(LBL_CREATE_YOUR_FIRST_PLAYLIST))) {
            verificationWentPassed();
        }
        else {
            verificationWentFailed();
        }

        return this;
    }

    //endregion

    //region Verification services

    @Override
    public void verificationWentPassed() {
        assert true;
    }

    @Override
    public void verificationWentFailed() {
        assert false;
    }

    //endregion
}
