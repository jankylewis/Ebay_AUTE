package se.spo.api;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import se.commonHandler.baseService.BaseApiService;
import se.model.dbModel.UserAuthenticationDbModel;
import se.spo.api.testDataProvider.dbTestDataProvider.DbTestDataFactory;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class BaseApiTestService extends BaseApiService {

    protected ThreadLocal<UserAuthenticationDbModel> userAuthenticationDbModelTl = new ThreadLocal<>();
    protected UserAuthenticationDbModel userAuthenticationDbModel;
    protected DbTestDataFactory dbTestDataFactory;

    protected void setUserAuthenticationDbModelTl()
            throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        userAuthenticationDbModelTl.set(dbTestDataFactory.getUserAuthenticationDbModel());
    }

    protected UserAuthenticationDbModel getUserAuthenticationDbModel() {
        return userAuthenticationDbModelTl.get();
    }

    @BeforeTest
    protected void baseTestInitialization()
            throws SQLException, FileNotFoundException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        System.out.println("THREAD ID = " + Thread.currentThread().getId());

        dbTestDataFactory = new DbTestDataFactory();
//        setUserAuthenticationDbModelTl();
        userAuthenticationDbModel = dbTestDataFactory.getUserAuthenticationDbModel();
//        userAuthenticationDbModel = getUserAuthenticationDbModel();
    }

    @AfterTest
    protected void baseTestCleaning() throws SQLException {
        dbTestDataFactory.tearUserAuthenticationDbDown(userAuthenticationDbModel);
    }
}
