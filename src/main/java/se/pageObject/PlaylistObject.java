package se.pageObject;

import com.microsoft.playwright.Page;
import se.business.BasePage;

public class PlaylistObject extends BasePage {

    public PlaylistObject(Page page) {
        super(page);
    }

    protected final String BTN_MORE = "button[data-testid='more-button']";
    protected final String BTN_DELETE = "(//button[@role='menuitem'])[3]";
    protected final String BTN_DELETE_ON_CONFIRMATION_MODAL =
            "div[class*='GenericModal'][aria-label] button[data-encore-id='buttonPrimary']";
    protected final String LBL_CREATE_YOUR_FIRST_PLAYLIST =
            "xpath=(//section)[1]//span[@data-encore-id = 'text' and contains(@class, 'bold')]";
}
