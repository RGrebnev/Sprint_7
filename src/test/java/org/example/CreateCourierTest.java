package org.example;

import io.qameta.allure.Feature;
import org.example.helpers.CourierActions;
import org.example.pojo.Courier;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.hamcrest.Matchers.equalTo;

@Feature("Создание курьера")
public class CreateCourierTest {

    private final CourierActions courierActions = new CourierActions();
    Courier courier;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        courier = new Courier();
        courier.setLogin("user_" + new Random().nextInt(10000));
        courier.setPassword("pass_" + new Random().nextInt(1000));
        courier.setFirstName("name_" + new Random().nextInt(1000));
    }

    @Test
    @DisplayName("Создание нового курьера")
    public void createNewCourier(){
        Response response = courierActions.createNewCourier(courier);

        response.then()
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));

        courierActions.deleteCourier(courier);
    }

    @Test
    @DisplayName("Создание нового курьера с уже существующим логином")
    public void createNewCourierWithExistedLogin(){
        Response responseFirst = courierActions.createNewCourier(courier);
        responseFirst.then()
                .statusCode(201);

        Response responseSecond = courierActions.createNewCourier(courier);
        responseSecond.then()
                        .statusCode(409)
                        .and()
                        .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        courierActions.deleteCourier(courier);
    }

    @Test
    @DisplayName("Создание нового курьера без указания логина")
    public void createNewCourierWithoutLogin(){
        courier.setLogin(null);

        Response response = courierActions.createNewCourier(courier);

        response.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание нового курьера без указания пароля")
    public void createNewCourierWithoutPassword(){
        courier.setPassword(null);

        Response response = courierActions.createNewCourier(courier);

        response.then()
                .statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание нового курьера без указания имени")
    public void createNewCourierWithoutName(){
        courier.setFirstName(null);

        Response response = courierActions.createNewCourier(courier);

        response.then()
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));

        courierActions.deleteCourier(courier);
    }



}