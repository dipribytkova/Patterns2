package ru.netology;

import com.github.javafaker.Faker;
import com.ibm.icu.text.Transliterator;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import io.restassured.filter.log.LogDetail;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataHelper {
    private DataHelper() {
    }

    public enum AuthStatuses {
        active,
        blocked
    }

    public enum BreakCredentialsType {
        BOTH,
        LOGIN,
        PASSWORD
    }

    @Value
    public static class AuthorisationInfo {
        String login;
        String password;
        AuthStatuses status;
    }

    public static AuthorisationInfo breakCredentials(@NotNull AuthorisationInfo info, @NotNull BreakCredentialsType type) {
        String login = info.getLogin();
        String pass = info.getPassword();
        switch (type) {
            case BOTH:
                login = "rr" + login.substring(2);
                pass = "00" + pass.substring(2);
                break;
            case LOGIN:
                login = "rr" + login.substring(2);
                break;
            case PASSWORD:
                pass = "00" + pass.substring(2);
                break;
        }
        return new AuthorisationInfo(login, pass, info.getStatus());
    }

    public static AuthorisationInfo getAuthorisationInfo(@NotNull AuthStatuses status) {
        Faker faker = new Faker(Locale.forLanguageTag("ru"));
        Transliterator toLatinTrans = Transliterator.getInstance("Russian-Latin/BGN");
        var login = toLatinTrans.transliterate(faker.name().username());
        var pass = faker.internet().password();
        AuthorisationInfo info = new AuthorisationInfo(login, pass, status);
        return info;
    }

    public static AuthorisationInfo setCredentials(@NotNull AuthorisationInfo info) {
        RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(9999)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        given()
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(info) // передаём в теле объект, который будет преобразован в JSON
                .when()
                .post("/api/system/users") // на какой путь относительно BaseUri отправляем запрос
                .then()
                .statusCode(HttpStatus.SC_OK);
        return info;
    }

//    @Test
//    void test() {
//        String date = nowWithDaysShift(-2);
//        String date2 = nowWithYearsShift(-2);
//        String ff = "" + getValidCardOrderInputInfo();
//        System.out.println();
//    }
}
