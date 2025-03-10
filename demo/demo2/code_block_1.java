import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PolicyRenewalReminderTest {
    private WebDriver driver;
    private SoftAssert softAssert;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        softAssert = new SoftAssert();
    }

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][] {
            {"Admin123", "password123"}
        };
    }

    @Test(dataProvider = "loginData")
    public void verifyPolicyRenewalReminder(String loginID, String password) {
        driver.get("http://myvehicleinsurance.com/policylist");
        softAssert.assertTrue(driver.getTitle().contains("Policy List"), "URL did not open correctly in Chrome");

        WebElement loginField = driver.findElement(By.id("loginID"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginButton"));

        loginField.sendKeys(loginID);
        passwordField.sendKeys(password);
        loginButton.click();

        softAssert.assertTrue(driver.getTitle().contains("Policy Dashboard"), "Login failed");

        WebElement policyTypeDropdown = driver.findElement(By.id("policyType"));
        policyTypeDropdown.click();
        WebElement vehicleInsuranceOption = driver.findElement(By.xpath("//option[text()='Vehicle Insurance']"));
        vehicleInsuranceOption.click();
        
        WebElement dateFilter = driver.findElement(By.id("dateFilter"));
        dateFilter.click();
        WebElement dateOption = driver.findElement(By.xpath("//option[text()='In next 90 days']"));
        dateOption.click();

        WebElement enterButton = driver.findElement(By.id("enterButton"));
        enterButton.click();

        WebElement reminderActiveColumn = driver.findElement(By.xpath("//td[@class='reminderActive']"));
        softAssert.assertEquals(reminderActiveColumn.getText(), "Yes", "Reminder Active column value mismatch");

        WebElement reminderTypeColumn = driver.findElement(By.xpath("//td[@class='reminderType']"));
        softAssert.assertEquals(reminderTypeColumn.getText(), "Renewal", "Reminder Type column value mismatch");

        WebElement reminderAlertModeColumn = driver.findElement(By.xpath("//td[@class='reminderAlertMode']"));
        softAssert.assertTrue(reminderAlertModeColumn.getText().matches("Call|Text|Email"), "Reminder Alert Mode column value mismatch");

        WebElement logoutButton = driver.findElement(By.id("logoutButton"));
        logoutButton.click();
        softAssert.assertTrue(driver.getTitle().contains("Login"), "Logout failed");

        softAssert.assertAll();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}