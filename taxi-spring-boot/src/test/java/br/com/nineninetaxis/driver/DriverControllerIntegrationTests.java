package br.com.nineninetaxis.driver;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;

/**
 * @Author Bruno de Queiroz<creativelikeadog@gmail.com>
 */
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class DriverControllerIntegrationTests extends AbstractTest {

    @Value("${local.server.port}")
    int port;

    private static final String NAME_PARAM = "Test";
    private static final String PLATE_PARAM = "TST-0000";
    private static final Double SW_LATITUDE_PARAM = -23.612474;
    private static final Double SW_LONGITUDE_PARAM = -46.702746;
    private static final Double NE_LATITUDE_PARAM = -23.589548;
    private static final Double NE_LONGITUDE_PARAM = -46.673392;
    private static final Double LATITUDE_PARAM = -22.612474;
    private static final Double LONGITUDE_PARAM = -47.702746;
    private static final Double IN_LATITUDE_PARAM = -23.608107;
    private static final Double IN_LONGITUDE_PARAM = -46.675003;
    private static final Boolean AVAILABILITY_PARAM = true;

    private static final String BASE_PATH = "/drivers";
    private static final String STATUS_PATH = BASE_PATH.concat("/%s/status");
    private static final String IN_AREA_PATH = BASE_PATH.concat("/inArea?sw=%s&ne=%s");

    private static final String DRIVER_JSON_DATA = "{\"name\":\"%s\", \"carPlate\": \"%s\"}";
    private static final String STATUS_JSON_DATA = "{\"driverAvailable\":\"%s\", \"latitude\": \"%s\", \"longitude\": \"%s\"}";

    @Before
    public void setup() {
        RestAssured.port = port;
    }

    private ValidatableResponse createDriver(String data, ContentType type) {
        return given().
                body(data).
                contentType(type).
                when().
                post(BASE_PATH).then();
    }

    private Response listDrivers() {
        return when().
                get(BASE_PATH);
    }

    private Integer lastDriver() {
        Response response = listDrivers();
        Integer total = response.path("$.size()");
        return response.path(String.format("[%d].driverId", total - 1));
    }

    private ValidatableResponse updateDriver(Integer id, String data, ContentType type) {
        Response put = given().
                body(data).
                contentType(type).
                when().
                put(String.format(STATUS_PATH, id));
        return put.then();
    }

    @Test
    public void testCreate() {
        String data = String.format(DRIVER_JSON_DATA, NAME_PARAM, PLATE_PARAM);

        createDriver("{}", ContentType.JSON).
                statusCode(HttpStatus.SC_PRECONDITION_FAILED);

        createDriver(String.format(DRIVER_JSON_DATA, NAME_PARAM, "XXX121"), ContentType.JSON).
                statusCode(HttpStatus.SC_PRECONDITION_FAILED);

        createDriver(String.format(DRIVER_JSON_DATA, NAME_PARAM, PLATE_PARAM), ContentType.HTML).
                statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);

        createDriver(String.format(DRIVER_JSON_DATA, NAME_PARAM, PLATE_PARAM), ContentType.XML).
                statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);

        createDriver(String.format(DRIVER_JSON_DATA, NAME_PARAM, PLATE_PARAM), ContentType.URLENC).
                statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);

        createDriver(data, ContentType.JSON).
                statusCode(HttpStatus.SC_OK);

        createDriver(data, ContentType.JSON).
                statusCode(HttpStatus.SC_CONFLICT);


        Integer id = lastDriver();
        String path = String.format("%s/%s", BASE_PATH, id);

        when().
                get(path).
                then().
                body(not(empty())).
                body("driverId", equalTo(id)).
                body("name", equalTo(NAME_PARAM)).
                body("carPlate", equalTo(PLATE_PARAM)).
                body("driverAvailable", not(empty())).
                body("latitude", isEmptyOrNullString()).
                body("longitude", isEmptyOrNullString());

    }

    @Test
    public void testUpdateDriverStatus() {

        createDriver(String.format(DRIVER_JSON_DATA, "TestUpdate", "TUP-0000"), ContentType.JSON).
                statusCode(HttpStatus.SC_OK);

        Integer id = lastDriver();

        updateDriver(id, "{}", ContentType.JSON).
                statusCode(HttpStatus.SC_PRECONDITION_FAILED);

        updateDriver(id, String.format(DRIVER_JSON_DATA, NAME_PARAM, "XXX121"), ContentType.JSON).
                statusCode(HttpStatus.SC_PRECONDITION_FAILED);

        updateDriver(id, String.format(DRIVER_JSON_DATA, NAME_PARAM, PLATE_PARAM), ContentType.HTML).
                statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);

        updateDriver(id, String.format(DRIVER_JSON_DATA, NAME_PARAM, PLATE_PARAM), ContentType.XML).
                statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);

        updateDriver(id, String.format(DRIVER_JSON_DATA, NAME_PARAM, PLATE_PARAM), ContentType.URLENC).
                statusCode(HttpStatus.SC_UNSUPPORTED_MEDIA_TYPE);

        updateDriver(id, String.format(DRIVER_JSON_DATA, NAME_PARAM, PLATE_PARAM), ContentType.JSON).
                statusCode(HttpStatus.SC_PRECONDITION_FAILED);

        updateDriver(id, String.format(STATUS_JSON_DATA, AVAILABILITY_PARAM, LATITUDE_PARAM, LONGITUDE_PARAM), ContentType.JSON).
                statusCode(HttpStatus.SC_OK);

        String path = String.format(STATUS_PATH, id);

        when().
                get(path).
                then().
                body(not(empty())).
                body("driverId", equalTo(id)).
                body("name", isEmptyOrNullString()).
                body("carPlate", isEmptyOrNullString()).
                body("driverAvailable", equalTo(AVAILABILITY_PARAM)).
                body("latitude", equalTo(LATITUDE_PARAM.floatValue())).
                body("longitude", equalTo(LONGITUDE_PARAM.floatValue()));
    }

    @Test
    public void testSearchInArea() {

        String path = String.format(IN_AREA_PATH, "", "");

        when().
                get(path).
                then().
                statusCode(HttpStatus.SC_BAD_REQUEST);


        path = String.format(IN_AREA_PATH, "ascb", "-23.1231,true");

        when().
                get(path).
                then().
                statusCode(HttpStatus.SC_BAD_REQUEST);

        String sw = String.format("%s,%s", SW_LATITUDE_PARAM, SW_LONGITUDE_PARAM);
        String ne = String.format("%s,%s", NE_LATITUDE_PARAM, NE_LONGITUDE_PARAM);
        path = String.format(IN_AREA_PATH, sw, ne);

        when().
                get(path).
                then().
                statusCode(HttpStatus.SC_OK).
                body("$.size()", equalTo(0));


        createDriver(String.format(DRIVER_JSON_DATA, "TestSearchInArea", "TIA-0000"), ContentType.JSON).
                statusCode(HttpStatus.SC_OK);

        Integer id = lastDriver();

        updateDriver(id, String.format(STATUS_JSON_DATA, AVAILABILITY_PARAM, IN_LATITUDE_PARAM, IN_LONGITUDE_PARAM), ContentType.JSON).
                statusCode(HttpStatus.SC_OK);

        when().
                get(path).
                then().
                statusCode(HttpStatus.SC_OK).
                body(not(empty())).
                body("[0].driverId", equalTo(id)).
                body("[0].driverAvailable", equalTo(AVAILABILITY_PARAM)).
                body("[0].latitude", equalTo(IN_LATITUDE_PARAM.floatValue())).
                body("[0].longitude", equalTo(IN_LONGITUDE_PARAM.floatValue()));
    }
}
