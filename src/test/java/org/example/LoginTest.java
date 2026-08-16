package org.example;

import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.helpers.CourierActions;
import org.example.pojo.Courier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Feature("Логин курьера")
public class LoginTest {

    private final CourierActions courierActions = new CourierActions();
    Courier courier;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courier = new Courier();
        courier.setFirstName("name_" + new Random().nextInt(1000));
        courier.setLogin("user_" + new Random().nextInt(10000));
        courier.setPassword("pass_" + new Random().nextInt(1000));
    }

    @Test
    @DisplayName("Успешный вход")
    public void loginCourier(){
        courierActions.createNewCourier(courier);

        Response response = courierActions.loginCourier(courier);

        response.then().statusCode(200)
                .and()
                .assertThat().body("id", notNullValue());

        courierActions.deleteCourier(courier);
    }

    @Test
    @DisplayName("Вход без указания логина")
    public void loginCourierWithoutLogin(){
        courierActions.createNewCourier(courier);
        String courierLogin = courier.getLogin();
        courier.setLogin(null);

        Response response = courierActions.loginCourier(courier);

        response.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));

        courier.setLogin(courierLogin);
        courierActions.deleteCourier(courier);
    }

    @Test
    @DisplayName("Вход без указания пароля")
    public void loginCourierWithoutPassword(){
        courierActions.createNewCourier(courier);
        String courierPassword = courier.getPassword();
        courier.setPassword(null);

        Response response = courierActions.loginCourier(courier);

        response.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));

        courier.setPassword(courierPassword);
        courierActions.deleteCourier(courier);
    }

    @Test
    @DisplayName("Вход с неправильным логином")
    public void loginCourierWithWrongLogin(){
        courierActions.createNewCourier(courier);
        String courierLogin = courier.getLogin();
        courier.setLogin(courier.getLogin() + "_test");

        Response response = courierActions.loginCourier(courier);

        response.then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));

        courier.setLogin(courierLogin);
        courierActions.deleteCourier(courier);
    }

    @Test
    @DisplayName("Вход с неправильным паролем")
    public void loginCourierWithWrongPassword(){
        courierActions.createNewCourier(courier);
        String courierPassword = courier.getPassword();
        courier.setPassword(courier.getPassword() + "_test");

        Response response = courierActions.loginCourier(courier);

        response.then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));

        courier.setPassword(courierPassword);
        courierActions.deleteCourier(courier);
    }

    @Test
    @DisplayName("Вход с несуществующим пользователем")
    public void loginCourierWithNotExistedUser(){
        Response response = courierActions.loginCourier(courier);

        response.then()
                .statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

}
