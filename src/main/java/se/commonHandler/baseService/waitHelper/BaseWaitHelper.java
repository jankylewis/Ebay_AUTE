package se.commonHandler.baseService.waitHelper;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Page.NavigateOptions;
import com.microsoft.playwright.Page.WaitForURLOptions;
import com.microsoft.playwright.Page.WaitForSelectorOptions;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.Locator.WaitForOptions;

import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import se.commonHandler.constantHouse.uiConstant.PollingConstant;
import se.commonHandler.constantHouse.uiConstant.WaitConstant;

public class BaseWaitHelper {

    private Page page;
    protected WaitConstant waitConst;
    protected PollingConstant pollingConst;

    public BaseWaitHelper(Page page) {
        this.page = page;
    }

    {
        waitConst = new WaitConstant();
        pollingConst = new PollingConstant();
    }

    //region Introducing vars

    public NavigateOptions navOpts = new NavigateOptions();
    protected WaitForURLOptions waitUrlOpts = new WaitForURLOptions();

    private WaitForSelectorOptions waitForSelectorOptions = new WaitForSelectorOptions();
    private WaitForOptions waitForOptions = new WaitForOptions();

    //endregion

    //region Wait helpers used for Locator type

    public void waitForElementAttached(@NotNull Locator expLocator) {

        //Waiting for locator to be attached on DOM
        expLocator.waitFor(waitForOptions.setState(WaitForSelectorState.ATTACHED));
    }

    public void waitForElementToBeVisible(@NotNull Locator expLocator) {

        //Waiting for locator to be visible
        expLocator.waitFor(waitForOptions.setState(WaitForSelectorState.VISIBLE));
    }

    public void waitForElementToBeVisible(@NotNull Locator expLocator, boolean isLongWait) {

        //Time-out settings
        int selTimeOut = isLongWait ? waitConst.MAXTIMEOUT : waitConst.TIMEOUT3S;

        //Waiting for locator to be visible with time-out
        expLocator.waitFor(waitForOptions.setState(WaitForSelectorState.VISIBLE).setTimeout(selTimeOut));
    }

    //endregion Wait helpers used for Locator type

    public void waitForElementToBeVisible(@NotNull Pair<ElementHandle, String> pairOfElement) {

        //Waiting for locator to be visible with time-out
        pairOfElement.getValue0().waitForSelector(pairOfElement.getValue1(),
                new ElementHandle.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
    }

    public void waitForElementToBeDetached(String expLocator) {
        page.waitForSelector(expLocator, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.DETACHED));
    }

    public void waitForElementToBeEnabled(String expLocator) {
        page.waitForSelector(expLocator, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.DETACHED));
    }

    //region Hard-coded waiting

    public static void forcedWait(int millisecondsToWait) {
        try {
            Thread.sleep(millisecondsToWait);
        } catch (Exception ex) {
            throw new RuntimeException("Found an unexpected exception: "
                    + ex.getCause() + " - " + ex.getMessage());
        }
    }

    //endregion
}
