package org.example;

import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@Feature("Список заказов")
public class OrderListTest {

    private String endpointOrderList = "/api/v1/orders";

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Получение списка заказов")
    public void createOrder() {
       Response response = getOrderList();

        response.then()
                .statusCode(200)
                .and()
                .assertThat().body("orders", notNullValue());
    }

    @Step
    public Response getOrderList() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get(endpointOrderList);
        return response;
    }
}
