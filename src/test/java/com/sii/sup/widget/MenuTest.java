package com.sii.sup.widget;

import com.sii.sup.base.Attributes;
import com.sii.sup.base.TestBase;
import com.sii.sup.helper.Helper;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
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
    private Properties properties;
    private String name;
    private String password;
    private String email;
    private User userToCreate;


    private void initProperties() {
        properties = Helper.readMenuProperties();
        logger.info("Menu test properties loaded");

    }

    private void initModalTestData() {
        name = (String) Helper.getRandomListElement(Helper.getNamesList().stream().filter(el -> el.length() > 3 && el.length() < 16).toList());
        email = (String) Helper.getRandomListElement(Helper.getEmails());
        password = Helper.generateRandomString(10);
        userToCreate = new User(name, email, password);
        logger.info(String.format("Test will create user:%s", userToCreate));
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/menu-item.php"})
    void moderJazzMusicTest(String url) {
        initProperties();
        webDriver.get(url);
        WebElement menuWebElement = webDriver.findElement(By.id(properties.getProperty(MENU_PROPERTY)));
        WebElement jazzMenuItem = menuWebElement.findElement(By.xpath(properties.getProperty(JAZZ_MENU_ITEM_XPATH_PROPERTY)));
        jazzMenuItem.click();
        WebElement modernSubMenu = jazzMenuItem.findElement(By.xpath(properties.getProperty(MODERN_JAZZ_MENU_ITEM_XPATH_PROPERTY)));
        modernSubMenu.click();
        assertThat(modernSubMenu.isDisplayed()).isTrue();
    }

    private int getNoOfUsersFromUserTable() {
        return webDriver.findElement(By.id(properties.getProperty(USER_TABLE_PROPERTY))).findElements(By.tagName(Attributes.TABLE_ROW.getValue())).size() - 1;
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/modal-dialog.php"})
    void createUserModal(String url) {
        int initNoOfUsers = initializeTestData(url);
        logger.info("Initial no of users : " + initNoOfUsers);
        inputDataToModal();
        sendFormAndWaitForUserToAppear(initNoOfUsers);
        List<WebElement> tableRows = webDriver.
                findElement(By.id(properties.getProperty(USER_TABLE_PROPERTY))).
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
        assertThat(users).as(String.format("Created user %s didn't appeared in table", userToCreate)).anyMatch(userToCreate::equals);

    }

    private int initializeTestData(String url) {
        initProperties();
        initModalTestData();
        webDriver.get(url);

        return   getNoOfUsersFromUserTable();
    }

    private void sendFormAndWaitForUserToAppear(int initNoOfUsers) {
        List<WebElement> buttonElements = webDriver.findElements(By.cssSelector(properties.getProperty(BUTTON_CSS_SELECTOR_PROPERTY)));
        WebElement sendFormButton = buttonElements.stream().filter(el -> el.getText().equals(properties.getProperty(CREATE_USER_BUTTON_TEXT_PROPERTY))).toList().get(0);
        logger.info(String.format("Clicking on %s button to send form", sendFormButton.getText().toLowerCase()));
        sendFormButton.click();
        WebDriverWait waitForModal = new WebDriverWait(webDriver, Duration.ofSeconds(20));
        logger.info(String.format("Wait until user %s will be created", userToCreate));
        waitForModal.until((ExpectedCondition<Boolean>) driver -> {
            int currentNoOfUsers = webDriver.findElement(By.id(properties.getProperty(USER_TABLE_PROPERTY))).findElements(By.tagName(Attributes.TABLE_ROW.getValue())).size();
            return currentNoOfUsers > initNoOfUsers; // Return true if a new row has been added to the table
        });
    }

    private void inputDataToModal() {
        WebElement createUserWebElement = webDriver.findElement(By.id(properties.getProperty(CREATE_USER_PROPERTY)));
        logger.info(String.format("Clicking on %s button to open modal dialog", createUserWebElement.getText().toLowerCase()));
        createUserWebElement.click();
        WebDriverWait modalWait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
        logger.info("Wait for modal window");
        modalWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(properties.getProperty(NAME_PROPERTY))));
        logger.info(String.format("Fill form with user data: %s", userToCreate));
        webDriver.findElement(By.id("dialog-form")).findElements(By.tagName(Attributes.INPUT.getValue())).forEach(el -> {
            if (!el.getAttribute(Attributes.TYPE.getValue()).equals("submit")) {
                el.clear();
            }
        });
        webDriver.findElement(By.id(properties.getProperty(NAME_PROPERTY))).sendKeys(name);
        webDriver.findElement(By.id(properties.getProperty(EMAIL_PROPERTY))).sendKeys(email);
        webDriver.findElement(By.id(properties.getProperty(PASSWORD_PROPERTY))).sendKeys(password);
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

