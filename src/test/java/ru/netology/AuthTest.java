package ru.netology;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.netology.DataHelper.AuthStatuses;

import static ru.netology.DataHelper.getAuthorisationInfo;

@ExtendWith({ScreenShooterExtension.class})
public class AuthTest {
    private final String baseUrl = "http://localhost:9999";
    private AuthorisationPage page;

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        Configuration.baseUrl = baseUrl;
        Configuration.holdBrowserOpen = true;  // false не оставляет браузер открытым по завершению теста
        Configuration.reportsFolder = "build/reports/tests/test/screenshoots";
        Selenide.open("");
        page = new AuthorisationPage();
    }

    @DisplayName("Активен, креды верны")
    @Test
    void activeUserTest() {
        page
                .fillForm(DataHelper.setCredentials(getAuthorisationInfo(AuthStatuses.active)))
                .clickSubmit()
                .checkPersonalAccount();
    }

    @DisplayName("Активен, все креды не верны")
    @Test
    void activeBadCredentialsTest() {
        page
                .fillForm(
                        DataHelper.breakCredentials(
                                DataHelper.setCredentials(
                                        getAuthorisationInfo(AuthStatuses.active)),
                                DataHelper.BreakCredentialsType.BOTH))
                .clickSubmit()
                .checkMessage("Ошибка! Неверно указан логин или пароль");
    }

    @DisplayName("Заблокирован, креды верны")
    @Test
    void blockedUserTest() {
        page
                .fillForm(DataHelper.setCredentials(getAuthorisationInfo(AuthStatuses.blocked)))
                .clickSubmit()
                .checkMessage("Ошибка! Пользователь заблокирован");
    }

    @DisplayName("Заблокирован, все креды не верны")
    @Test
    void blockedBadCredentialsTest() {
        page
                .fillForm(
                        DataHelper.breakCredentials(
                                DataHelper.setCredentials(getAuthorisationInfo(AuthStatuses.blocked)),
                                DataHelper.BreakCredentialsType.BOTH))
                .clickSubmit()
                .checkMessage("Ошибка! Неверно указан логин или пароль");
    }

    @DisplayName("Неизвестный пользователь")
    @Test
    void unknownUserTest() {
        page
                .fillForm(getAuthorisationInfo(AuthStatuses.active))
                .clickSubmit()
                .checkMessage("Ошибка! Неверно указан логин или пароль");
    }

    @DisplayName("Активен, только пароль не верный")
    @Test
    void activeUserBadPassTest() {
        page
                .fillForm(
                        DataHelper.breakCredentials(
                                DataHelper.setCredentials(
                                        getAuthorisationInfo(AuthStatuses.active)),
                                DataHelper.BreakCredentialsType.PASSWORD))
                .clickSubmit()
                .checkMessage("Ошибка! Неверно указан логин или пароль");
    }

    @DisplayName("Активен, только логин не верный")
    @Test
    void activeUserBadLoginTest() {
        page
                .fillForm(
                        DataHelper.breakCredentials(
                                DataHelper.setCredentials(
                                        getAuthorisationInfo(AuthStatuses.active)),
                                DataHelper.BreakCredentialsType.LOGIN))
                .clickSubmit()
                .checkMessage("Ошибка! Неверно указан логин или пароль");
    }

    @DisplayName("Заблокирован, только пароль не верный")
    @Test
    void blockedUserBadPassTest() {
        page
                .fillForm(
                        DataHelper.breakCredentials(
                                DataHelper.setCredentials(getAuthorisationInfo(AuthStatuses.blocked)),
                                DataHelper.BreakCredentialsType.PASSWORD))
                .clickSubmit()
                .checkMessage("Ошибка! Неверно указан логин или пароль");
    }

    @DisplayName("Заблокирован, только логин не верный")
    @Test
    void blockedUserBadLoginTest() {
        page
                .fillForm(
                        DataHelper.breakCredentials(
                                DataHelper.setCredentials(getAuthorisationInfo(AuthStatuses.blocked)),
                                DataHelper.BreakCredentialsType.LOGIN))
                .clickSubmit()
                .checkMessage("Ошибка! Неверно указан логин или пароль");
    }
}