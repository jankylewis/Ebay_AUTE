package se.commonHandler.constantHouse.uiConstant;

import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;

public class WaitConstant {

    public final WaitUntilState DOMCONTENTWAITER = WaitUntilState.DOMCONTENTLOADED;
    public final WaitUntilState COMMITWAITER = WaitUntilState.COMMIT;
    public final WaitUntilState NETWORKIDLEWAITER = WaitUntilState.NETWORKIDLE;
    public final WaitUntilState LOADWAITER = WaitUntilState.LOAD;

    public final WaitForSelectorState SELECTORATTACHED = WaitForSelectorState.ATTACHED;
    public final WaitForSelectorState SELECTORDETACHED = WaitForSelectorState.DETACHED;
    public final WaitForSelectorState SELECTORHIDDEN = WaitForSelectorState.HIDDEN;
    public final WaitForSelectorState SELECTORVISIBLE = WaitForSelectorState.VISIBLE;

    //region Timeout by milliseconds

    public final int MAXTIMEOUT = 30000;
    public final int MINTIMEOUT = 500;
    public final int TIMEOUT1S = 1000;
    public final int TIMEOUT3S = 3000;
    public final int TIMEOUT5S = 5000;

    //endregion
}
