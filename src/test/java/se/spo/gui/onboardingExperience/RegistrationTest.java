package se.spo.gui.onboardingExperience;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.business.RegistrationPage;
import se.business.RegistrationPage.RegistrationAtPreStep;
import se.business.RegistrationPage.RegistrationAtFirstStep;
import se.business.RegistrationPage.RegistrationAtSecondStep;
import se.business.RegistrationPage.RegistrationAtLatters;
import se.model.uiModel.UserInformationModel;
import se.spo.gui.BaseTestService;
import se.utility.GlobalVariableUtil.UserCredential;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Test(suiteName = "Registration Tests")
public class RegistrationTest extends BaseTestService {

    //region Introducing objects

    private UserInformationModel usrModel;
    private RegistrationPage registrationPage;
    private RegistrationAtPreStep signUpAtPreStepPage;
    private RegistrationAtFirstStep signUpAtFirstStepPage;
    private RegistrationAtSecondStep signUpAtSecondStep;
    private RegistrationAtLatters signUpAtLatters;
    private Random randomizer;
    private int randomizedNumber;
    private final List<String> listOfGenders = Arrays.asList(
            "man", "woman", "non-binary", "something else", "prefer not to say");

    //endregion

    //region Verifying User's Registration experience

    //region Verifying unsuccessful Registration flows

    @Test(priority = 2,
            testName = "SWSIGNUP_01: Step 1 > Verify User has signed-up unsuccessfully with an invalid email address")
    protected void spotifyUiTest_verifyUserHasUnsuccessfullySignedUpWithInvalidEmailAddress() {

        LOGGER.info("Preparing test data");
        usrModel = new UserInformationModel(
                faker.produceFakeEmail() + "_"
        );

        LOGGER.info("Step: Step 1 > Inputting an invalid email address");
        LOGGER.info("Step: Step 1 > Verifying that an error message came up!");
        landOnTheFirstStep(usrModel.getUserEmail());
        signUpAtPreStepPage.verifyErrorMessagePresented();
    }

    @Test(priority = 2,
            testName = "SWSIGNUP_02: Step 2 > Verify User has signed-up unsuccessfully with an invalid password")
    protected void spotifyUiTest_verifyUserHasUnsuccessfullySignedUpWithInvalidPassword() {

        LOGGER.info("Preparing test data");
        usrModel = new UserInformationModel(
                randomizedNumber + faker.produceFakeEmail(),
                faker.producePassword(
                        1, 7, true, true, true));

        LOGGER.info("Step: Step 2 > Inputting an invalid password");
        landOnTheSecondStep(
                usrModel.getUserEmail(),
                usrModel.getUserPassword()
        );

        LOGGER.info("Step: Step 2 > Verifying that an error message came up!");
        signUpAtFirstStepPage.verifyErrorMessagePresented();
    }

    @Test(priority = 2,
            testName = "SWSIGNUP_03: Step 3 > Verify User has signed-up unsuccessfully with empty credentials")
    protected void spotifyUiTest_verifyUserHasUnsuccessfullySignedUpWithEmptyCredentials() {

        LOGGER.info("Preparing test data");
        usrModel = new UserInformationModel(
                randomizedNumber + faker.produceFakeEmail(),
                faker.producePassword(
                        8, 16, true, true, true));

        signUpAtSecondStep = registrationPage.new RegistrationAtSecondStep(page);

        LOGGER.info("Step: Step 3 > Leaving blanks to all fields -> clicking on Next button");
        landOnTheSecondStep(
                usrModel.getUserEmail(),
                usrModel.getUserPassword()
                );

        LOGGER.info("Step: Step 3 > Verifying that all required error messages came up!");
        registrationPage.clickOnNextBtn();
        signUpAtSecondStep.verifyErrorMessagePresented();
    }

    @Test(priority = 2,
            testName = "SWSIGNUP_04: Step 3 > Verify User has signed-up unsuccessfully with an empty date of birth",
            invocationCount = 1,
            enabled = true
    )
    protected void spotifyUiTest_verifyUserHasUnsuccessfullySignedUpWithEmptyDateOfBirth() {

        LOGGER.info("Preparing test data");
        LocalDate userBirthDate = LocalDate.parse(UserCredential.USER_DOB, dateTimeUtil.DATE_FORMATTER);
        usrModel = new UserInformationModel(
                randomizedNumber + faker.produceFakeEmail(),
                faker.producePassword(
                        8, 16, true, true, true),
                faker.produceFakeName(),
                dateTimeUtil.getMonthOfCurrentDate(userBirthDate),
                dateTimeUtil.getYearOfCurrentDate(userBirthDate),
                listOfGenders.get(randomizer.nextInt(0, listOfGenders.size() - 1))
        );

        signUpAtSecondStep = registrationPage.new RegistrationAtSecondStep(page);

        LOGGER.info("Step: Navigating to Step 3");
        landOnTheSecondStep(
                usrModel.getUserEmail(),
                usrModel.getUserPassword()
        );

        LOGGER.info("Step: Step 3 > Inputting all fields except date of birth field");
        LOGGER.info("Step: Step 3 > Verifying that an error message at date of birth field came up!");
        signUpAtSecondStep.fulfillThirdStep(usrModel, true)
                .verifyErrorMessagePresentedAtBirthDateField();
    }

    @Test(priority = 2,
            testName = "SWSIGNUP_05: Step 3 > Verify User has signed-up unsuccessfully with a futuristic year",
            invocationCount = 1,
            enabled = true
    )
    protected void spotifyUiTest_verifyUserHasUnsuccessfullySignedUpWithFuturisticYear() {

        LOGGER.info("Preparing test data");
        LocalDate userBirthDate = LocalDate.parse(UserCredential.USER_DOB, dateTimeUtil.DATE_FORMATTER);
        usrModel = new UserInformationModel(
                randomizedNumber + faker.produceFakeEmail(),
                faker.producePassword(
                        8, 16, true, true, true),
                faker.produceFakeName(),
                dateTimeUtil.getDayOfCurrentDate(userBirthDate),
                dateTimeUtil.getMonthOfCurrentDate(userBirthDate),
                dateTimeUtil.getCurrentLocalDate().plusYears(1).getYear(),
                listOfGenders.get(randomizer.nextInt(0, listOfGenders.size() - 1))
        );

        signUpAtSecondStep = registrationPage.new RegistrationAtSecondStep(page);

        LOGGER.info("Step: Navigating to Step 3");
        landOnTheSecondStep(
                usrModel.getUserEmail(),
                usrModel.getUserPassword()
        );

        LOGGER.info("Step: Step 3 > Inputting all fields except year of birth field");
        LOGGER.info("Step: Step 3 > Verifying that an error message at year of birth field came up!");
        signUpAtSecondStep.fulfillThirdStep(usrModel, true)
                .verifyErrorMessagePresentedAtYearField();
    }

    //endregion

    //region Verifying successful Registration flows

    @Test(priority = 1,
            testName = "SWSIGNUP_06: Verify User has signed-up successfully",
            groups = "singleThreaded"
    )
    protected void spotifyUiTest_verifyUserHasSuccessfullySignedUp() {

        LOGGER.info("Preparing test data");
        List<String> listOfGenders = Arrays.asList(
                "man", "woman", "non-binary", "something else", "prefer not to say");
        LocalDate userBirthDate = LocalDate.parse(UserCredential.USER_DOB, dateTimeUtil.DATE_FORMATTER);

        usrModel = new UserInformationModel(
                randomizedNumber + faker.produceFakeEmail(),
                faker.producePassword(
                        8, 16, true, true, true),
                faker.produceFakeName(),
                dateTimeUtil.getDayOfCurrentDate(userBirthDate),
                dateTimeUtil.getMonthOfCurrentDate(userBirthDate),
                dateTimeUtil.getYearOfCurrentDate(userBirthDate),
                listOfGenders.get(randomizer.nextInt(0, listOfGenders.size() - 1))
        );

        signUpAtSecondStep = registrationPage.new RegistrationAtSecondStep(page);
        signUpAtLatters = registrationPage.new RegistrationAtLatters(page);

        LOGGER.info("Step: Navigating to Step 3");
        landOnTheSecondStep(
                usrModel.getUserEmail(),
                usrModel.getUserPassword()
        );

        LOGGER.info("Step: Step 3 > Inputting valid values into all fields");
        signUpAtSecondStep.fulfillThirdStep(usrModel, true);
        signUpAtLatters.tickMarketingCheckbox()
                .tickPrivacyCheckbox()
                .clickOnSignUpBtn()
                .verifyHumanRecognitionLabelDisplayed();

        signUpAtLatters.authenticateRecaptchaService();
    }

    //endregion

    //endregion

    //region Test services

    private void landOnTheFirstStep(String emailAddress) {

        signUpAtPreStepPage = registrationPage.new RegistrationAtPreStep(page);

        signUpAtPreStepPage.inputUsrEmail(emailAddress);
        registrationPage.clickOnNextBtn();
    }

    private void landOnTheSecondStep(String emailAddress, String password) {

        signUpAtFirstStepPage = registrationPage.new RegistrationAtFirstStep(page);

        landOnTheFirstStep(emailAddress);

        signUpAtFirstStepPage.inputPassword(password);
        registrationPage.clickOnNextBtn();
    }

    //endregion

    //region Test preparation and cleaning

    @BeforeMethod
    protected void testPreparation() {

        randomizer = new Random();

        registrationPage = new RegistrationPage(page);

        registrationPage.navigateToSignUpPage();

        randomizedNumber = randomizer.nextInt(0, 1000000);
    }

    //endregion

}

