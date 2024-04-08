package se.commonHandler.baseService.waitHelper;

import com.microsoft.playwright.Page;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import se.commonHandler.baseService.BaseVerification;

import java.time.Duration;
import java.util.concurrent.Callable;
import org.awaitility.Awaitility;

public class PollingWaitHelper extends BaseWaitHelper {

    private BaseVerification baseVerification;

    //region Introducing constructors + class initializer

    public PollingWaitHelper(Page page) {
        super(page);
        baseVerification = new BaseVerification(page);
    }

    //endregion

    //region Wait using Custom Polling approach

    //region Polling with times
    public Boolean waitForElementToBeEnabledWithPollings(
            @NotNull String expLocatorString, @Nullable Integer maxAttempts) {

        maxAttempts = maxAttempts == null ? pollingConst.POLLING_MAX_ATTEMPTS : maxAttempts;

        Boolean isElementEnabled = null;
        int idx = 0;
        while (idx < maxAttempts) {

            if (!baseVerification.verifyIfElementEnabled(expLocatorString)){
                isElementEnabled = true;
            }

            idx++;
        }

        return isElementEnabled;
    }

    public Boolean waitForElementToBeVisible(
            @NotNull String expLocatorString, @Nullable Integer maxAttempts) {

        maxAttempts = maxAttempts == null ? pollingConst.POLLING_MAX_ATTEMPTS : maxAttempts;

        Boolean isElementVisible = null;

        int idx = 0;
        while (idx < maxAttempts) {

            if (!baseVerification.verifyIfElementVisible(expLocatorString)){
                isElementVisible = true;
            }

            idx++;
        }

        return isElementVisible;
    }

    public Boolean waitForElementToBeDetachedWithPollings(
            @NotNull String expLocatorString, @Nullable Integer maxAttempts) {

        maxAttempts = maxAttempts == null ? pollingConst.POLLING_MAX_ATTEMPTS : maxAttempts;

        Boolean isElementDetached = null;

        int idx = 0;
        while (idx < maxAttempts) {

            if (!baseVerification.verifyIfElementVisible(expLocatorString)) {
                isElementDetached = true;
            }

            idx++;
        }

        return isElementDetached;
    }

    //endregion Polling with times

    //region Polling with awaitility

//    public Boolean waitForElementToBe

    private void waitUntilConditionMatched(
            @NotNull Callable<Boolean> condition,
            @Nullable Integer delayedTimeOut,
            @Nullable Integer delayedEachPolling) {

        delayedTimeOut = delayedTimeOut == null ? waitConst.MAXTIMEOUT : delayedTimeOut;
        delayedEachPolling = delayedEachPolling == null ? waitConst.MINTIMEOUT : delayedEachPolling;

        Awaitility.waitAtMost(
                Duration.ofMillis(delayedTimeOut))
                .pollDelay(Duration.ofMillis(delayedEachPolling))
                .until(condition);
    }

    //endregion

    //endregion
}
