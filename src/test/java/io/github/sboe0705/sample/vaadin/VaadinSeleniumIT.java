package io.github.sboe0705.sample.vaadin;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VaadinSeleniumIT {

    private static final Logger log = LoggerFactory.getLogger(VaadinSeleniumIT.class);

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    void setup() {
        FirefoxOptions options = new FirefoxOptions();
        String firefoxPath = System.getenv("FIREFOX_PATH");
        if (firefoxPath != null) {
            log.info("Using firefox from {} in headless mode", firefoxPath);
            options.setBinary(firefoxPath);
            options.addArguments("--headless", "--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
        }
        driver = new FirefoxDriver(options);
    }

    @AfterAll
    void teardown() {
        driver.quit();
    }

    @Test
    void testNavigationToBooksView() {
        driver.get("http://localhost:" + port + "/");

        WebElement heading = waitUntilVisibilityOfElementById("authors-heading");
        assertEquals("Authors", heading.getText());

        WebElement cell = findGridCellWithText("Joanne");
        performDoubleClickOn(cell);

        heading = waitUntilVisibilityOfElementById("books-heading");
        assertEquals("Joanne Rowling's Books", heading.getText());

        assertThatGridContainsAllText(
                "Philosopher's Stone",
                "Chamber of Secrets",
                "Prisoner of Azkaban",
                "Goblet of Fire",
                "Order of the Phoenix",
                "Half-Blood Prince",
                "Deathly Hallows"
        );

        WebElement backButton = driver.findElement(By.id("books-back-button"));
        backButton.click();

        heading = waitUntilVisibilityOfElementById("authors-heading");
        assertEquals("Authors", heading.getText());
    }

    private WebElement waitUntilVisibilityOfElementById(String id) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        return driver.findElement(By.id(id));
    }

    private void performDoubleClickOn(WebElement webElement) {
        Actions actions = new Actions(driver);
        actions.doubleClick(webElement).perform();
    }

    private WebElement findGridCellWithText(String text) {
        List<WebElement> elements = driver.findElements(By.tagName("vaadin-grid-cell-content"));
        return elements.stream()
                .filter(e -> Objects.equals(text, e.getText()))
                .findFirst().orElse(null);
    }

    private void assertThatGridContainsAllText(String... texts) {
        assertTrue(Stream.of(texts)
                .map(this::findGridCellWithText)
                .allMatch(Objects::nonNull));
    }
}
