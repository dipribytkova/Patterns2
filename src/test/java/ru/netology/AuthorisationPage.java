package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class AuthorisationPage {
    private SelenideElement loginElement = $("[data-test-id=login] input");
    private SelenideElement passwordElement = $("[data-test-id=password] input");
    private SelenideElement buttonElement = $("[data-test-id=action-login]");
    private SelenideElement errorNotificationTitleElement = $("[data-test-id=error-notification] .notification__title");
    private SelenideElement errorNotificationContentElement = $("[data-test-id=error-notification] .notification__content");
    private SelenideElement h2Element = $("h2");

    public AuthorisationPage fillForm(@NotNull DataHelper.AuthorisationInfo info) {
        String login = info.getLogin();
        if (login != null) loginElement.shouldBe(Condition.visible).setValue(login);
        String pass = info.getPassword();
        if (pass != null) passwordElement.shouldBe(Condition.visible).setValue(pass);
        return this;
    }

    public AuthorisationPage clickSubmit() {
        buttonElement.shouldBe(Condition.visible).click();
        return this;
    }

    public void checkMessage(@NotNull String message) {
        errorNotificationTitleElement
                .shouldBe(Condition.visible)
                .shouldBe(Condition.text("Ошибка"));
        errorNotificationContentElement
                .shouldBe(Condition.text(message));
    }

    public void checkPersonalAccount() {
        h2Element
                .shouldBe(Condition.visible)
                .shouldBe(Condition.text("  Личный кабинет"));
    }
}