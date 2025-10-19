package ReviewAPI_JDBC.avengersAPIReview;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SimpleGetRequests {

    String hrUrl = "http://54.91.210.3:1000/ords/hr";

    @Test
    public void testOne(){
// get all employees and verify that we have Steven
        Response response = RestAssured.given().accept(ContentType.JSON)
                .when().get(hrUrl+"/employees");

        response.prettyPrint();
        // status code and content type
        Assert.assertEquals(response.statusCode(),200);
        Assert.assertEquals(response.contentType(),"application/json");

        // headers
        Assert.assertTrue(response.headers().hasHeaderWithName("Date"));
        Assert.assertEquals(response.header("Transfer-Encoding"),"chunked");

        Assert.assertTrue(response.asString().contains("Steven"));


    }

    @Test
    public void testTwo(){
        // get employee with ID = 105, and verify name and other info
        // "job_id": "IT_PROG"  verify

        Response response = RestAssured.given().contentType(ContentType.JSON)
                .and().pathParam("id",105)
                .when().get(hrUrl+"/employees/{id}");

       // response.prettyPrint();

        Assert.assertEquals(response.statusCode(),200);
        Assert.assertEquals(response.contentType(),"application/json");

        // header
        Assert.assertEquals(response.header("Content-Type"),"application/json");

        // GPATH syntax

        String actualJobId = response.path("job_id");     // to read response body we used path method
        System.out.println("actualJobId = " + actualJobId);
        Assert.assertEquals(actualJobId,"IT_PROG");

        // create a jsonPath object from response object

        JsonPath jsonPath = response.jsonPath();

        Assert.assertEquals(jsonPath.getString("job_id"),"IT_PROG");

    }

    @Test
    public void testThree(){

        // verify number of people working for AD_VP job_id  == 2
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .and().queryParam("q","{\"job_id\": \"AD_VP\"}")
                .when().get(hrUrl+"/employees");

        // response.prettyPrint();

        int actualCount = response.path("count");       // Data TYpe Consistency
        System.out.println("actualCount = " + actualCount);
          Assert.assertEquals(actualCount,2);

        JsonPath jsonPath = response.jsonPath();

        int actCaountJsonPath = jsonPath.getInt("count");

        System.out.println("actCaountJsonPath = " + actCaountJsonPath);

        Assert.assertEquals(actCaountJsonPath,2);

    }

}
