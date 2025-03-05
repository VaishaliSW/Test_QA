package com.example.tests;

import com.example.pages.LoginPage;
import com.example.pages.RegistrationPage;
import com.example.pages.VaccinationSchedulingPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class VaccinationTests {
    private WebDriver driver;
    private Properties prop;

    @BeforeMethod
    public void setUp() throws IOException {
        prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);

        String browser = prop.getProperty("browser");
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "vaccinationDates")
    public Object[][] vaccinationDates() {
        return new Object[][]{
                {"earliest"}, {"earliest+1"}, {"latest"}, {"latest-1"},
                {"beforeEarliest"}, {"afterLatest"}, {"earliest+1Month"}, {"latest-1Month"}
        };
    }

    @Test(dataProvider = "vaccinationDates")
    public void testVaccinationScheduling(String dateScenario) {
        driver.get(prop.getProperty("baseUrl"));

        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterUsername(prop.getProperty("parentUsername"));
        loginPage.enterPassword(prop.getProperty("parentPassword"));
        loginPage.clickLogin();

        VaccinationSchedulingPage schedulingPage = new VaccinationSchedulingPage(driver);
        schedulingPage.navigateToChildProfile();
        schedulingPage.clickScheduleVaccination();

        switch (dateScenario) {
            case "earliest":
                schedulingPage.selectEarliestDate();
                break;
            case "earliest+1":
                schedulingPage.selectDateAfterEarliest();
                break;
            case "latest":
                schedulingPage.selectLatestDate();
                break;
            case "latest-1":
                schedulingPage.selectDateBeforeLatest();
                break;
            case "beforeEarliest":
                schedulingPage.selectDateBeforeEarliest();
                schedulingPage.verifyDateNotAvailable();
                break;
            case "afterLatest":
                schedulingPage.selectDateAfterLatest();
                schedulingPage.verifyDateNotAvailable();
                break;
            case "earliest+1Month":
                schedulingPage.selectDateOneMonthAfterEarliest();
                schedulingPage.verifyDateNotAvailable();
                break;
            case "latest-1Month":
                schedulingPage.selectDateOneMonthBeforeLatest();
                schedulingPage.verifyDateNotAvailable();
                break;
        }
        schedulingPage.confirmAppointment();
    }

    @DataProvider(name = "registrationData")
    public Object[][] registrationData() {
        return new Object[][]{
                {"0", "John Doe", "2023-10-15", "Jane Doe", "jane@example.com"},
                {"4", "Alice Smith", "2018-10-16", "Robert Smith", "robert@example.com"},
                {"1", "A", "2021-05-20", "Sarah Connor", "sarah@example.com"},
                {"50", "A very long name that exceeds fifty characters", "2022-07-14", "John Smith", "john@example.com"},
                {"5", "Michael Johnson", "2020-03-12", "Linda Johnson", "lj@ex"},
                {"50", "Emma Williams", "2019-11-25", "David Williams", "david@example.com"}
        };
    }

    @Test(dataProvider = "registrationData")
    public void testChildRegistration(String age, String childName, String dob, String parentName, String parentContact) {
        driver.get(prop.getProperty("baseUrl") + "/register");

        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.enterChildName(childName);
        registrationPage.enterChildDOB(dob);
        registrationPage.enterParentName(parentName);
        registrationPage.enterParentContact(parentContact);
        registrationPage.submitForm();
    }
}