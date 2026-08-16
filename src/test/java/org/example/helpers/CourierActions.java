package org.example.helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.pojo.Courier;

import static io.restassured.RestAssured.given;

public class CourierActions {

    private String endpointCreateCourier = "/api/v1/courier";
    private String endpointLoginCourier = "/api/v1/courier/login";
    private String endpointDeleteCourier = "api/v1/courier/{id}";

    @Step("Создать курьера")
    public Response createNewCourier(Courier courier){
        return given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(endpointCreateCourier);
    }

    @Step("Залогиниться курьером")
    public Response loginCourier(Courier courier){
        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post(endpointLoginCourier);
        return response;
    }

    @Step("Удалить курьера")
    public void deleteCourier(Courier courier){
        String courierId = null;
        Response response = loginCourier(courier);
        if (response.getStatusCode() == 200) {
            courierId = response.jsonPath().getString("id");
        }
        if (courierId != null) {
            given()
                    .header("Content-type", "application/json")
                    .body("{ \"id\": \"" + courierId + "\" }")
                    .when()
                    .delete(endpointDeleteCourier, courierId);
        }
    }
}
