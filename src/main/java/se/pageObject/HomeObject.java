package se.pageObject;

import com.microsoft.playwright.Page;
import se.business.BasePage;

public class HomeObject extends BasePage {

    protected HomeObject(Page page) {
        super(page);
    }

    protected final String LBL_MADE_FOR_USERNAME =
            "xpath=//section[contains(@aria-label, 'Made For Jan')]//a[contains(text(), 'Made For Jan')]";
    protected final String BTN_LOG_IN = "xpath=//button[contains(@data-testid, 'login-button')]";
    protected final String BTN_INSTALL_APP = "css=a[href*='download'][data-encore-id='buttonPrimary']";
}
