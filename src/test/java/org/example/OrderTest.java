package org.example;

import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.pojo.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@Feature("Создание заказа")
public class OrderTest {

    private String endpointCreateOrder = "/api/v1/orders";
    Order order;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        order = new Order();
        order.setFirstName("f_name_" + new Random().nextInt(1000));
        order.setLastName("l_name_" + new Random().nextInt(1000));
        order.setAddress("address_" + new Random().nextInt(1000));
        order.setMetroStation(new Random().nextInt(10));
        order.setPhone("+7800" + new Random().nextInt(1000));
        order.setRentTime(new Random().nextInt(100));
        order.setDeliveryDate("2020-06-06");
        order.setComment("comment_" + new Random().nextInt(1000));
    }

    @Test
    @DisplayName("Создание заказа без указания цвета")
    public void createOrder() {
        order.setColor(null);
        Response response = createNewOrder(order);

        response.then()
                .statusCode(201)
                .and()
                .assertThat().body("track", notNullValue());
    }

    @ParameterizedTest
    @MethodSource("colorCombinations")
    @DisplayName("Создание заказа с указанием цвета")
    public void createOrderWithColor(List<String> color) {
        order.setColor(color);
        Response response = createNewOrder(order);

        response.then()
                .statusCode(201)
                .and()
                .assertThat().body("track", notNullValue());
    }

    static Collection<List<String>> colorCombinations() {
        return Arrays.asList(
                Arrays.asList("BLACK"),
                Arrays.asList("GREY"),
                Arrays.asList("BLACK", "GREY")
        );
    }

    @Step
    public Response createNewOrder(Order order) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(endpointCreateOrder);
        return response;
    }

    }