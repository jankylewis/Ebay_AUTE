package se.business;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.jetbrains.annotations.NotNull;
import se.commonHandler.baseService.BaseVerification.IErrorVerification;
import se.commonHandler.baseService.BaseVerification.IVerification;
import se.model.uiModel.UserInformationModel;
import se.pageObject.RegistrationObject;
import se.utility.GlobalVariableUtil.Environment;

import java.util.Arrays;
import java.util.List;

public class RegistrationPage extends RegistrationObject implements IVerification {

    public RegistrationPage(Page page) {
        super(page);
    }

    //region At Pre-step

    public class RegistrationAtPreStep extends RegistrationObjectAtPreStep implements IErrorVerification {

        public RegistrationAtPreStep(Page page) {
            super(page);
        }

        public RegistrationAtPreStep inputUsrEmail(String usrEmail) {
            baseUi.sendKeyToElement(findLocator(TXT_EMAIL), usrEmail);
            return this;
        }

        @Override
        public void verifyErrorMessagePresented() {

            Locator invalidEmailAddressLbl = findLocator(LBL_USERNAME_ERRMSG);

            waitHelper.waitForElementToBeVisible(invalidEmailAddressLbl);

            if (baseVerification.verifyStringEquality(msgConst.LBL_INVALID_USERNAME, invalidEmailAddressLbl.textContent())) {
                verificationWentPassed();
            }
        }
    }

    //endregion

    //region At First-step

    public class RegistrationAtFirstStep extends RegistrationObjectAtFirstStep implements IErrorVerification {

        public RegistrationAtFirstStep(Page page) {
            super(page);
        }

        public RegistrationAtFirstStep inputPassword(String usrPwd) {
            baseUi.sendKeyToElement(findLocator(TXT_PASSWORD), usrPwd);
            return this;
        }

        @Override
        public void verifyErrorMessagePresented() {

            Locator invalidPwdLbl = findLocator(LBL_PASSWORD_ERRMSG);

            waitHelper.waitForElementToBeVisible(invalidPwdLbl);

            if (baseVerification.verifyStringEquality(msgConst.LBL_INVALID_PASSWORD, invalidPwdLbl.textContent())) {
                verificationWentPassed();
            }
        }
    }

    //endregion

    //region At Second-step

    public class RegistrationAtSecondStep extends RegistrationObjectAtSecondStep
            implements IErrorVerification {

        private final String emptyString = "";

        public RegistrationAtSecondStep(Page page) {
            super(page);
        }

        public RegistrationAtSecondStep inputDisplayedName(String displayedName) {
            baseUi.sendKeyToElement(findLocator(TXT_DISPLAYED_NAME), displayedName);
            return this;
        }

        public RegistrationAtSecondStep selectGenderByClicking(String gender) {
            baseUi.clickOnElementByForcing(findLocator(RBTN_GENDER(gender)),
                    Double.valueOf(waitConst.MINTIMEOUT));
            return this;
        }

        public RegistrationAtSecondStep selectGenderByKeyboard(@NotNull String gender) {

            List<String> listOfUiGenders = Arrays.asList(
                    "man", "woman", "non-binary", "something else", "prefer not to say");

            //RBTN_GENDER might not work => utilizing keyboard
            SelectGender: {
                for (int i = 0; i < listOfUiGenders.size(); i++) {
                    if (i == 0 && gender == listOfUiGenders.get(i)) {
                        baseUi.pressKey("Tab");
                        baseUi.pressKey(" ");           //Pressing Space-bar

                        break;
                    }

                    for (int tabI = 1; tabI < listOfUiGenders.size(); tabI++) {

                        if (gender == listOfUiGenders.get(tabI)) {
                            baseUi.pressKey("Tab");
                            for (int arrowI = 0; arrowI < tabI; arrowI++) {
                                baseUi.pressKey("ArrowRight");
                            }

                            break SelectGender;                 //Terminating action
                        }
                    }
                }
            }

            return this;
        }

        private RegistrationAtSecondStep inputBirthDate(int birthDate) {
            baseUi.sendKeyToElement(findLocator(TXT_BIRTHDATE),
                    birthDate != -1 ? String.valueOf(birthDate) : emptyString);
            return this;
        }

        public RegistrationAtSecondStep selectBirthMonth(String birthMonth) {
            baseUi.selectDropdown(findLocator(DDL_BIRTHMONTH), birthMonth);
            return this;
        }

        public RegistrationAtSecondStep inputBirthYear(int birthYear) {
            baseUi.sendKeyToElement(findLocator(TXT_BIRTHYEAR),
                    birthYear != -1 ? String.valueOf(birthYear) : emptyString);
            return this;
        }

        public RegistrationAtSecondStep fulfillThirdStep(@NotNull UserInformationModel usrModel,
                                                         boolean isGenderSelectedByKeyboard) {

            inputDisplayedName(usrModel.getDisplayedName());
            selectBirthMonth(String.valueOf(usrModel.getBirthMonth()));
            inputBirthDate(usrModel.getBirthDate());
            inputBirthYear(usrModel.getBirthYear());

            if (isGenderSelectedByKeyboard) {
                selectGenderByKeyboard(usrModel.getGender());
            } else {
                selectGenderByClicking(usrModel.getGender());
            }

            clickOnNextBtn();

            return this;
        }

        public void verifyErrorMessagePresentedAtYearField() {
            Locator invalidBirthYearErrMsg = findLocator(LBL_BIRTHDATE_YEAR_TOO_YOUNG_ERRMSG);

            waitHelper.waitForElementToBeVisible(invalidBirthYearErrMsg);

            if (baseVerification.verifyExpectedStringContained(msgConst.LBL_INVALID_BIRTHYEAR, invalidBirthYearErrMsg.textContent())) {
                verificationWentPassed();
            }
        }

        public void verifyErrorMessagePresentedAtBirthDateField() {
            Locator invalidBirthDateErrMsg = findLocator(LBL_BIRTHDATE_DAY_ERRMSG);

            waitHelper.waitForElementToBeVisible(invalidBirthDateErrMsg);
            if (baseVerification.verifyStringEquality(msgConst.LBL_INVALID_BIRTHDATE, invalidBirthDateErrMsg.textContent())) {
                verificationWentPassed();
            }
        }

        @Override
        public void verifyErrorMessagePresented() {
            Locator displayedNameRequiredMsgLbl = findLocator(LBL_DISPLAYED_NAME_ERRMSG);
            Locator dobRequiredMsgLbl = findLocator(LBL_BIRTHDATE_ERRMSG);
            Locator genderRequiredMsgLbl = findLocator(LBL_GENDER_ERRMSG);

            waitHelper.waitForElementToBeVisible(displayedNameRequiredMsgLbl);

            if (baseVerification.verifyStringEquality(msgConst.LBL_REQUIRED_DISPLAYED_NAME, displayedNameRequiredMsgLbl.textContent()) &&
                    baseVerification.verifyStringEquality(msgConst.LBL_REQUIRED_DOB, dobRequiredMsgLbl.textContent()) &&
                    baseVerification.verifyStringEquality(msgConst.LBL_REQUIRED_GENDER, genderRequiredMsgLbl.textContent())) {
                verificationWentPassed();
            }
        }
    }

    //endregion

    //region At the latters

    public class RegistrationAtLatters extends RegistrationObjectAtLatters {

        public RegistrationAtLatters(Page page) {
            super(page);
        }

        public RegistrationAtLatters tickMarketingCheckbox() {
            baseUi.clickOnVisibleElementHandle(findFirstLocatorVisible(CHK_MARKETING));
            return this;
        }

        public RegistrationAtLatters tickPrivacyCheckbox() {
            baseUi.clickOnVisibleElementHandle(findFirstLocatorVisible(CHK_PRIVACY));
            return this;
        }

        public RegistrationAtLatters clickOnSignUpBtn() {
            baseUi.clickOnVisibleElement(findLocator(BTN_SIGN_UP));
            return this;
        }

        public RegistrationAtLatters authenticateRecaptchaService() {

            //Code shall be added here

            return this;
        }

        public void verifyHumanRecognitionLabelDisplayed() {
            baseVerification.verifyStringEquality(msgConst.LBL_HUMAN_RECOGNITION,
                    findLocator(LBL_HUMAN_RECOGNITION).textContent());
        }
    }

    //endregion

    public RegistrationPage clickOnNextBtn() {
        baseUi.clickOnVisibleElement(findLocator(BTN_NEXT), Double.valueOf(waitConst.MINTIMEOUT));
        return this;
    }

    public RegistrationPage navigateToSignUpPage() {

        baseUi.navigateToUrl(Environment.BASE_URL +
                Environment.END_POINT_LOCALIZATION +
                localPathConst.SIGN_UP_PATH);

        return this;
    }

    //region IVerification

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
