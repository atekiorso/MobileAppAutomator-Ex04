import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

public class MainTest {
    private final long defaultTimeoutInSeconds = 5;
    AndroidDriver<AndroidElement> driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "8.0");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "G:\\github\\MobileAppAutomator\\MobileAppAutomator-Ex04\\apks\\org.wikipedia.apk");

        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void searchResultsContainText() {
        final String searchText = "Java";
        final String searchXpath = "//*[@text='Search Wikipedia']";
        final String searchInputId = "org.wikipedia:id/search_src_text";
        final String titleXpath = "//*[@resource-id='org.wikipedia:id/page_list_item_title']";

        // Тапаем по элементу поиска для открытия формы поиска
        waitForElementAndClick(By.xpath(searchXpath));
        
        // Вводим в поле ввода строку для поиска
        waitForElementAndSendKeys(By.id(searchInputId), searchText);
        
        // Проверяем, что результат поиска не пуст и в каждом результате поиска содержится искомый текст
        List<WebElement> elements = waitForElementsPresent(By.xpath(titleXpath));

        Assert.assertTrue("Search result has no articles!",
                elements.size() > 0);

        for (WebElement element: elements) {
            Assert.assertTrue("Element \"" + element.getText() + "\" does not contain expected text!",
                    element.getText().contains(searchText));
        }
    }

    private WebElement waitForElementAndClick(By by) {
        return waitForElementAndClick(by, defaultTimeoutInSeconds);
    }

    private WebElement waitForElementAndClick(By by, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, timeoutInSeconds);
        element.click();

        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String charSequences) {
        return waitForElementAndSendKeys(by, charSequences, defaultTimeoutInSeconds);
    }

    private WebElement waitForElementAndSendKeys(By by, String charSequences, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, timeoutInSeconds);
        element.sendKeys(charSequences);

        return element;
    }

    private WebElement waitForElementPresent(By by, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage("Element \"" + by + "\" not found!");

        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private List<WebElement> waitForElementsPresent(By by) {
        return waitForElementsPresent(by, defaultTimeoutInSeconds);
    }

    private List<WebElement> waitForElementsPresent(By by, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage("Elements \"" + by + "\" not found!");

        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }
}
