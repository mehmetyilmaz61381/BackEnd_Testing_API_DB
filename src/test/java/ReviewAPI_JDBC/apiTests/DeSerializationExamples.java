package ReviewAPI_JDBC.apiTests;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.*;


public class DeSerializationExamples {
       /*
Given Accept application/json
And path zipcode is 22031
When I send a GET request to /us endpoint
Then status code must be 200
And content type must be application/json
And Server header is cloudflare
And Report-To header exists
And body should contains following information
    post code is 22031
    country  is United States
    country abbreviation is US
    place name is Fairfax
    state is Virginia
    latitude is 38.8604
     */

    String zipUrl = "http://api.zippopotam.us";
    String hrUrl = "http://54.91.210.3:1000/ords/hr";

    @Test
    public void CollectionTest(){
        Response response = given().accept(ContentType.JSON)
                .and().pathParam("zip", 22031)
                .when().get(zipUrl+"/us/{zip}");

        response.prettyPrint();

        // JSON to JAVA Collection: De-serialization, as() method
        Map<String,Object> postCode = response.body().as(Map.class);
        System.out.println(postCode);

        assertEquals(postCode.get("country"),"United States");
        assertEquals(postCode.get("post code"),"22031");

        List<Map<String,Object>> placesList = (List<Map<String, Object>>) postCode.get("places");
        System.out.println(placesList);
        assertEquals(placesList.get(0).get("state"),"Virginia");

        double expectedLatitude = 38.8604;

        double actualLatitude = Double.parseDouble((String) placesList.get(0).get("latitude"));

        assertEquals(actualLatitude,expectedLatitude);

    }

    /*
    Test Case:
{{hrurl}}/employees?q={"job_id": "AD_VP"}
Verify:
"employee_id": 102,
"first_name": "Lex",
 last_name": "De Haan",
 "href": "http://54.91.210.3:1000/ords/hr/employees/102"
    "count": 2,
     */

    @Test
    public void hrCollectionTest(){
// example of Query Params request
        Response response = given().accept(ContentType.JSON)
                .and().queryParam("q","{\"job_id\": \"AD_VP\"}")
                .get(hrUrl+"/employees");
        response.prettyPrint();


        // JSON to JAVA Collection: De-serialization, as() method

        Map<String,Object> resultMap = response.body().as(Map.class);

        System.out.println("resultMap = " + resultMap);

        List<Map<String,Object>> employeeList = (List<Map<String, Object>>) resultMap.get("items");

        System.out.println("employeeList = " + employeeList);

        // "employee_id": 102,
        int employee_id = (int) employeeList.get(1).get("employee_id");
        System.out.println("employee_id = " + employee_id);
        assertEquals(employee_id,102);
        assertEquals(employeeList.get(1).get("first_name"),"Lex");
        assertEquals(employeeList.get(1).get("last_name"),"De Haan");
        assertEquals(employeeList.get(1).get("employee_id"),102);

        // HREF verification
        List<Map<String,Object>> links = (List<Map<String, Object>>) employeeList.get(1).get("links");
        String actualHref = (String) links.get(0).get("href");
        System.out.println("actualHref = " + actualHref);

        String expectedHref = "http://54.91.210.3:1000/ords/hr/employees/102";
        assertEquals(actualHref,expectedHref);

        // without variable
        assertEquals(((List<Map<String,Object>>) employeeList.get(1).get("links")).get(0).get("href"),expectedHref);

    }

}
