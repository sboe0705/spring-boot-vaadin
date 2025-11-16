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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VaadinSeleniumIT {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    /*
        Install firefox and geckodriver with the following command:

        yum install firefox
        cd /tmp
        curl -s https://api.github.com/repos/mozilla/geckodriver/releases/latest \
          | grep "browser_download_url.*linux64.tar.gz" \
          | cut -d '"' -f 4 \
          | wget -i -
        tar -xzf geckodriver-v*-linux64.tar.gz
        sudo mv geckodriver /usr/local/bin/
     */
    @BeforeAll
    void setup() {
        FirefoxOptions options = new FirefoxOptions();
        Optional.ofNullable(System.getenv("FIREFOX_PATH"))
                .ifPresent(options::setBinary);
        driver = new FirefoxDriver(options);
    }

    @AfterAll
    void teardown() {
        driver.quit();
    }

    @Test
    void testNavigationToBooksView() throws InterruptedException {
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
