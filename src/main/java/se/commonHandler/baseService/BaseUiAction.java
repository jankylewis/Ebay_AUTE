package se.commonHandler.baseService;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.jetbrains.annotations.NotNull;
import se.commonHandler.baseService.waitHelper.BaseWaitHelper;
import se.commonHandler.constantHouse.uiConstant.ActionConstant;
import se.commonHandler.constantHouse.uiConstant.WaitConstant;

import java.util.ArrayList;
import java.util.Collection;

public class BaseUiAction {

    // region Introducing objects

    public Page page;
    private BaseWaitHelper waitHelper;
    private WaitConstant waitConst;
    private ActionConstant actionConst;

    // endregion

    // region Introducing constructor

    public BaseUiAction(Page page) {
        this.page = page;
        waitHelper = new BaseWaitHelper(page);
    }

    // endregion

    {
       waitConst = new WaitConstant();
    }

    public void navigateToUrl(String expUrl) {
        page.navigate(expUrl, waitHelper.navOpts.setWaitUntil(waitConst.DOMCONTENTWAITER));
    }

    public void clearAllValues(@NotNull Locator expLocator) {
        expLocator.clear(actionConst.clOpts
                .setForce(actionConst.getClearByForced(actionConst.clOpts))
                .setNoWaitAfter(actionConst.getNoWaitAfter(actionConst.clOpts)));
    }

    public void sendKeyToElement(Locator expLocator, String expText) {

        waitHelper.waitForElementToBeVisible(expLocator, false);

        clearAllValues(expLocator);

        expLocator.fill(expText, actionConst.fillOpts
                .setForce(actionConst.getClearByForced(actionConst.fillOpts))
                .setNoWaitAfter(actionConst.getNoWaitAfter(actionConst.fillOpts)));
    }

    public void clickOnVisibleElement(Locator expLocator) {

        waitHelper.waitForElementToBeVisible(expLocator);

        expLocator.click(actionConst.clickOpts
                .setForce(actionConst.getClearByForced(actionConst.clickOpts))
                .setNoWaitAfter(actionConst.getNoWaitAfter(actionConst.clickOpts)));
    }

    public void clickOnEnabledElement(Locator expLocator) {

        waitHelper.waitForElementToBeVisible(expLocator);

        expLocator.click(actionConst.clickOpts
                .setForce(actionConst.getClearByForced(actionConst.clickOpts))
                .setNoWaitAfter(actionConst.getNoWaitAfter(actionConst.clickOpts)));
    }

    public void clickOnVisibleElementHandle(@NotNull ElementHandle expLocator) {
        expLocator.click();
    }

    public void clickOnElementByForcing(Locator expLocator) {
        waitHelper.waitForElementToBeVisible(expLocator);
        expLocator.click(actionConst.clickOpts.setForce(true));
    }

    public void clickOnElementByForcing(Locator expLocator, Double delayTimeOut) {
        waitHelper.waitForElementToBeVisible(expLocator);
        expLocator.click(actionConst.clickOpts.setForce(true).setDelay(delayTimeOut));
    }

    public void clickOnVisibleElement(Locator expLocator, boolean isLongWaitUsed) {

        waitHelper.waitForElementToBeVisible(expLocator, isLongWaitUsed);

        expLocator.click(actionConst.clickOpts
                .setForce(actionConst.getClearByForced(actionConst.clickOpts))
                .setNoWaitAfter(actionConst.getNoWaitAfter(actionConst.clickOpts)));
    }

    public void clickOnVisibleElement(Locator expLocator, Double delayTimeOut) {           //Giving a hard-coded delay

        waitHelper.waitForElementToBeVisible(expLocator, false);

        expLocator.click(actionConst.clickOpts
                .setForce(actionConst.getClearByForced(actionConst.clickOpts))
                .setNoWaitAfter(actionConst.getNoWaitAfter(actionConst.clickOpts))
                .setDelay(delayTimeOut));
    }

    public void clickOnVisibleElement(Locator expLocator,
                                      Double delayTimeOut,
                                      boolean isLongWaitUsed) {                           //Giving a hard-coded delay

        waitHelper.waitForElementToBeVisible(expLocator, isLongWaitUsed);

        expLocator.click(actionConst.clickOpts
                .setForce(actionConst.getClearByForced(actionConst.clickOpts))
                .setNoWaitAfter(actionConst.getNoWaitAfter(actionConst.clickOpts))
                .setDelay(delayTimeOut));
    }

    public void clickOnRadioButton(Locator expLocator) {
        waitHelper.waitForElementToBeVisible(expLocator);
        expLocator.check();
    }

    public void selectDropdown(Locator expLocator, String expOption) {
        waitHelper.waitForElementToBeVisible(expLocator);
        expLocator.selectOption(expOption);
    }

    public void hoverElement(Locator expLocator) {
        expLocator.hover();
    }

    public void clickOnVisibleElement(@NotNull Locator expLocator, int expCounter) {
        waitHelper.waitForElementToBeVisible(expLocator, false);

        expLocator.click(actionConst.clickOpts
                .setForce(actionConst.getClearByForced(actionConst.clickOpts))
                .setNoWaitAfter(actionConst.getNoWaitAfter(actionConst.clickOpts))
                .setClickCount(actionConst.getClickCount(actionConst.clickOpts, expCounter)));
    }

    public void clickOnElementWithSelected(Locator expLocator, boolean isChecked) {
        waitHelper.waitForElementToBeVisible(expLocator, false);

        if (expLocator.isChecked() != isChecked) {
            expLocator.click(actionConst.clickOpts
                    .setForce(actionConst.getClearByForced(actionConst.clickOpts))
                    .setNoWaitAfter(actionConst.getNoWaitAfter(actionConst.clickOpts)));
        }
    }

    public void pressKey(String expKey) {
        page.keyboard().press(expKey);
    }

    public Collection<String> getTextsFromLocators(@NotNull Collection<Locator> expLocators) {

        Collection<String> actTexts = new ArrayList<>();

        for (Locator locator : expLocators) {
            actTexts.add(locator.textContent());
        }

        return actTexts;
    }
}