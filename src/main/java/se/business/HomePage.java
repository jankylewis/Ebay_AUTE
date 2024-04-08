package se.business;

import com.microsoft.playwright.Page;
import se.pageObject.HomeObject;

public class HomePage extends HomeObject {

    public HomePage(Page page) {
        super(page);
    }

    public HomePage waitForLogInButtonPresented() {
        waitHelper.waitForElementToBeVisible(findLocator(BTN_LOG_IN), false);
        return this;
    }

    public HomePage waitForInstallAppButtonPresented() {
        waitHelper.waitForElementToBeVisible(findLocator(BTN_INSTALL_APP));
        return this;
    }
}
