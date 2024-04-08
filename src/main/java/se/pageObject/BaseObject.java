package se.pageObject;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class BaseObject {

    protected Page page;
    private Locator foundLocator;
    private List<ElementHandle> listOfFoundELocators;
    private List<Locator> listOfFoundLocators;

    public BaseObject(Page page) {
        this.page = page;
    }

    protected final String BTN_LOGO = "xpath=//a[img[@id = 'gh-logo']]";
    protected final String BTN_ACCOUNT_MENU = "css=button[aria-controls='profileMenu']";
    protected final String BTN_LOG_OUT = "xpath=//button[contains(@data-testid, 'logout')]";
    protected final String DIV_PLAYLISTS = "css=div[data-encore-id='listRow']";     //This could be a list of locators
    protected final String SECTION_SKELETON_LOADING =
            "xpath=//section[@data-testid='home-page'][//*[contains(@data-testid, 'skeleton')]]";

    //region Locating locators service

    protected Locator findLocator(String expLocator) {

        int timesOfRetrying = 3;

        do {
            if (timesOfRetrying == 1) {
                throw new NoSuchElementException("The desired element has not been found!");
            }

            foundLocator = page.locator(expLocator);
            try {
                foundLocator.isVisible();
                break;
            }
            catch (PlaywrightException plwEx) {
                if (!(plwEx.getMessage().contains("DOMException"))) {
                    throw plwEx;
                }
            }
        }
        while (timesOfRetrying-- > 0);

        return foundLocator;
    }

    protected List<Locator> findListOfLocators(String expLocator) {

        listOfFoundLocators = new ArrayList<>();
        int timesOfRetrying = 3;

        do {
            if (timesOfRetrying == 1) {
                throw new NoSuchElementException("The desired element has not been found!");
            }

            listOfFoundLocators = page.locator(expLocator).all();

            if (listOfFoundLocators.isEmpty()) {
                timesOfRetrying--;
            } else {
                break;
            }
        }
        while (timesOfRetrying > 0);

        return listOfFoundLocators;
    }

    protected List<Locator> findListOfLocators(String expLocator, int numberOfLocatorsTaken) {
        listOfFoundLocators = new ArrayList<>();
        int timesOfRetrying = 3;

        do {
            if (timesOfRetrying == 1) {
                throw new NoSuchElementException("The desired element has not been found!");
            }

            listOfFoundLocators = page.locator(expLocator).all();

            if (listOfFoundLocators.isEmpty()) {
                timesOfRetrying--;
            } else {
                break;
            }
        }
        while (timesOfRetrying > 0);

        return listOfFoundLocators.stream()
                .limit(numberOfLocatorsTaken)
                .toList();
    }

    protected ElementHandle findFirstLocatorVisible(String expLocator) {

        listOfFoundELocators = new ArrayList<>();
        int timesOfRetrying = 3;

        do {
            if (timesOfRetrying == 1) {
                throw new NoSuchElementException("The desired element has not been found!");
            }

            listOfFoundELocators = page.querySelectorAll(expLocator);

            if (listOfFoundELocators.isEmpty()) {
                timesOfRetrying--;
            }
            else {
                break;
            }
        }
        while (timesOfRetrying > 0);

        return listOfFoundELocators.stream()             //Getting the FIRST displayed element
                .filter(ElementHandle::isVisible)
                .toList()
                .get(0);
    }

    protected List<ElementHandle> findListOfELocators(String expLocator) {

        listOfFoundELocators = new ArrayList<>();
        int timesOfRetrying = 3;

        do {
            if (timesOfRetrying == 1) {
                throw new NoSuchElementException("The desired element has not been found!");
            }

            listOfFoundELocators = page.querySelectorAll(expLocator);

            if (listOfFoundELocators.isEmpty()) {
                timesOfRetrying--;
            }
            else {
                break;
            }
        }
        while (timesOfRetrying > 0);

        return listOfFoundELocators;
    }

    //endregion

    //region

    protected int getNumberOfLocatorsFound(String expLocator) {
        return page.locator(expLocator).all().size();
    }

    //endregion
}