package com.sii.sup.widget;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.sii.sup.base.Attributes;
import com.sii.sup.base.TestBase;
import com.sii.sup.helper.PropertyHelper;
import com.sii.sup.helper.TestHelper;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

import static com.sii.sup.staticvalues.StaticValues.MenuTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class MenuTest extends TestBase {
    private final Logger logger = new LoggerContext().getLogger(MenuTest.class);
    private User userToCreate;

    private void initProperties() {
        this.propertyHelper = TestHelper.getPropertyHelper(MenuTest.class.getSimpleName());
        logger.info("Menu test properties loaded");

    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/menu-item.php"})
    void moderJazzMusicTest(String url) {
        initProperties();
        pageHelper.init(url);
        WebElement menuWebElement = pageHelper.findElementById(getStringProperty(MENU_PROPERTY));
        WebElement jazzMenuItem = menuWebElement.findElement(By.xpath(getStringProperty(JAZZ_MENU_ITEM_XPATH_PROPERTY)));
        jazzMenuItem.click();
        WebElement modernSubMenu = jazzMenuItem.findElement(By.xpath(getStringProperty(MODERN_JAZZ_MENU_ITEM_XPATH_PROPERTY)));
        modernSubMenu.click();
        assertThat(modernSubMenu.isDisplayed()).isTrue();
    }

    private int getNoOfUsersFromUserTable() {
        return pageHelper.findElementById(getStringProperty(USER_TABLE_PROPERTY)).findElements(By.tagName(Attributes.TABLE_ROW.getValue())).size() - 1;
    }

    @ParameterizedTest
    @MethodSource("com.sii.sup.helper.DataSources#menuTestDataSource")
    void createUserModal(String url, String name, String email, String password) {
        userToCreate = new User(name, email, password);
        logger.info(String.format("Test will create user:%s", userToCreate));
        int initNoOfUsers = initializeTestData(url);
        logger.info("Initial no of users : " + initNoOfUsers);
        inputDataToModal();
        sendFormAndWaitForUserToAppear(initNoOfUsers);
        List<WebElement> tableRows = pageHelper.getWebDriver().
                findElement(By.id(getStringProperty(USER_TABLE_PROPERTY))).
                findElements(By.tagName(Attributes.TABLE_ROW.getValue()));
        List<User> users = new ArrayList<>();
        logger.info("------------------------------Users------------------------------");
        for (int i = 1; i < tableRows.size(); i++) {
            List<WebElement> cells = tableRows.get(i).findElements(By.tagName(Attributes.TABLE_COLUMN.getValue()));
            User user = new User(cells.get(0).getText(), cells.get(1).getText(), cells.get(2).getText());
            users.add(user);
            logger.info(String.format("User %s ", user));
        }
        logger.info("------------------------------End------------------------------");
        logger.info(String.format("Created user %s", userToCreate));
        users.forEach(System.out::println);
        assertThat(users).as(String.format("Created user %s didn't appeared in table", userToCreate)).anyMatch(userToCreate::equals);

    }

    private int initializeTestData(String url) {
        initProperties();
        pageHelper.init(url);
        return getNoOfUsersFromUserTable();
    }

    private void sendFormAndWaitForUserToAppear(int initNoOfUsers) {
        List<WebElement> buttonElements = pageHelper.getWebDriver().findElements(By.cssSelector(getStringProperty(BUTTON_CSS_SELECTOR_PROPERTY)));
        WebElement sendFormButton = buttonElements.stream().filter(el -> el.getText().equals(getStringProperty(CREATE_USER_BUTTON_TEXT_PROPERTY))).toList().get(0);
        logger.info(String.format("Clicking on %s button to send form", sendFormButton.getText().toLowerCase()));
        sendFormButton.click();
        WebDriverWait waitForModal = new WebDriverWait(pageHelper.getWebDriver(), Duration.ofSeconds(20));
        logger.info(String.format("Wait until user %s will be created", userToCreate));
        waitForModal.until((ExpectedCondition<Boolean>) driver -> {
            int currentNoOfUsers = pageHelper.getWebDriver().findElement(By.id(getStringProperty(USER_TABLE_PROPERTY))).findElements(By.tagName(Attributes.TABLE_ROW.getValue())).size();
            return currentNoOfUsers > initNoOfUsers; // Return true if a new row has been added to the table
        });
    }

    private void inputDataToModal() {
        WebElement createUserWebElement = pageHelper.findElementById(getStringProperty(CREATE_USER_PROPERTY));
        logger.info(String.format("Clicking on %s button to open modal dialog", createUserWebElement.getText().toLowerCase()));
        createUserWebElement.click();
        WebDriverWait modalWait = new WebDriverWait(pageHelper.getWebDriver(), Duration.ofSeconds(20));
        logger.info("Wait for modal window");
        modalWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(getStringProperty(NAME_PROPERTY))));
        logger.info(String.format("Fill form with user data: %s", userToCreate));
        pageHelper.findElementById("dialog-form").findElements(By.tagName(Attributes.INPUT.getValue())).forEach(el -> {
            if (!el.getAttribute(Attributes.TYPE.getValue()).equals("submit")) {
                el.clear();
            }
        });
        pageHelper.setInputValue(pageHelper.findElementById(getStringProperty(NAME_PROPERTY)), userToCreate.name);
        pageHelper.setInputValue(pageHelper.findElementById(getStringProperty(EMAIL_PROPERTY)), userToCreate.email);
        pageHelper.setInputValue(pageHelper.findElementById(getStringProperty(PASSWORD_PROPERTY)), userToCreate.password);
    }

    static class User {
        String name;
        String email;
        String password;

        public User(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", "[", "]")
                    .add("name='" + name + "'")
                    .add("email='" + email + "'")
                    .add("password='" + password + "'")
                    .toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            User user = (User) o;
            return Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(password, user.password);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, email, password);
        }
    }
}

