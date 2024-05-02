package se.spo.api;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import se.spo.api.testDataProvider.dbTestDataProvider.DbTestDataFactory;

public class TrackRecommendationTest extends BaseApiTestService {

    @BeforeMethod
    protected void testPreparation() {
//        dbTestDataFactory
    }

    @AfterMethod
    protected void testCleaning() {

    }
}