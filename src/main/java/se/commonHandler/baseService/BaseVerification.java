package se.commonHandler.baseService;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.jetbrains.annotations.NotNull;

public class BaseVerification {

    private Page page;

    public BaseVerification(Page page) {
        this.page = page;
    }

    //region Locator type verifications

    public boolean verifyIfElementVisible(@NotNull Locator expLocator) {
        return expLocator.isVisible();
    }

    //endregion Locator type verifications

    //region String verifications

    public boolean verifyStringEquality(@NotNull String expStr, @NotNull String actStr) {

        if (expStr.toLowerCase().trim().equals(actStr.toLowerCase().trim())) {
            return true;
        } else {
            throw new AssertionError(
                    "Expected string [" + expStr + "] did not match actual string [" + actStr + "]");
        }
    }

    public Boolean verifyExpectedStringContained(@NotNull String expStr, @NotNull String actStr) {
        if (actStr.trim().toLowerCase().contains(expStr.trim().toLowerCase())) {
            return true;
        } else {
            throw new AssertionError(
                    "Actual string [" + actStr + "] did not contain expected string [" + expStr + "]");
        }
    }

    //endregion String verifications

    //region Locators with String type verifications
    public boolean verifyIfElementEnabled(@NotNull String expLocatorString) {
        return page.isEnabled(expLocatorString);
    }

    public Boolean verifyIfElementVisible(@NotNull String expLocatorString) {
        return page.isVisible(expLocatorString);
    }

    //endregion Locators with String type verifications

    //region Verification services

    public interface IErrorVerification {
        void verifyErrorMessagePresented();
    }

    public interface IVerification {
        void verificationWentPassed();      //Using this abstract void to assert a PASSED script

        void verificationWentFailed();      //Using this abstract void to assert a script went FAILED (being used if needed)
    }

    //endregion
}
