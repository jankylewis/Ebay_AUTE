package se.spo.api;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import se.commonHandler.baseService.BaseApiService;
import se.model.dbModel.UserAuthenticationDbModel;
import se.spo.api.testDataProvider.dbTestDataProvider.DbTestDataFactory;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class BaseApiTestService extends BaseApiService {

    protected UserAuthenticationDbModel userAuthenticationDbModel;
    protected DbTestDataFactory dbTestDataFactory;

    @BeforeTest
    protected void testInitialization()
            throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        dbTestDataFactory = new DbTestDataFactory();
        userAuthenticationDbModel = dbTestDataFactory.getUserAuthenticationDbModel();
    }

    @AfterTest
    protected void testCleaning() throws SQLException {
        dbTestDataFactory.cleanUserAuthenticationDb(userAuthenticationDbModel);
    }
}
