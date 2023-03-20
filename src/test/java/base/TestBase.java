package base;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBase {
    protected static final Logger logger = LoggerFactory.getLogger(TestBase.class);
    protected WebDriver webDriver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();

    }

    @BeforeEach
    public void setup() {

        logger.info("Initializing chrome webdriver");
        ChromeOptions options = new ChromeOptions(); options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);
    }


    @AfterEach
    public void tearDown() {
        webDriver.close();
    }

}

