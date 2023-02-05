package post_requests;

import base_urls.JsonplaceholderBaseUrl;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import test_data.JsonPlaceHolderTestData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class Post05ObjectMapper_Map extends JsonplaceholderBaseUrl {
     /*
         Given
           1) https://jsonplaceholder.typicode.com/todos
           2) {
                 "userId": 55,
                 "title": "Tidy your room",
                 "completed": false
               }
     //ObjectMapper ile map'e çevirip post yaptık
     //responstan geleni mape çirerek karşılaştırdık
            I send POST Request to the Url
        Then
            Status code is 201
        And
            response body is like {
                                    "userId": 55,
                                    "title": "Tidy your room",
                                    "completed": false,
                                    "id": 201
                                    }
     */

    @Test
    public void post05ObjectMapper() throws IOException {
        //Set the Url
        spec.pathParam("first","todos");

//Set the Expected Data
//      String jsonInString = "{\n" +
//              "                                    \"userId\": 55,\n" +
//              "                                    \"title\": \"Tidy your room\",\n" +
//              "                                    \"completed\": false,\n" +
//              "                                    \"id\": 201\n" +
//              "                                    }";
        //yukarıdaki yapının yerine yapıyoruz.
        //burada yaptığımız obje maper ile Expected Datayı mape çevirip respansedan gelenide mape çevirerek kaşılaştırdık
      JsonPlaceHolderTestData obj = new JsonPlaceHolderTestData();
      String jsonInString = obj.expectedDataInString(55,"Tidy your room",false);
      //readvalue string ile çalıştığı için stringe çevirdik
      HashMap expectedData =   new ObjectMapper().readValue(jsonInString, HashMap.class);
      //burası bize hash map oluşturuyor bu hash map expected datamız oluyor
      System.out.println("expectedData = " + expectedData);

      //Send the Request and Get the Response
        Response response = given().spec(spec).contentType(ContentType.JSON).body(expectedData).when().post("/{first}");
        //post request ile gönderiyoruz
        response.prettyPrint();

      //Do Assertion
        HashMap actualData = new ObjectMapper().readValue(response.asString(),HashMap.class);
        //response ObjectMapper()kullanarak hashmapa çeviriyoruz
        System.out.println("actualData = " + actualData);
       //burada karşılaştırdık
        assertEquals(201,response.getStatusCode());
        assertEquals(expectedData.get("completed"),actualData.get("completed"));
        assertEquals(expectedData.get("title"),actualData.get("title"));
        assertEquals(expectedData.get("userId"),actualData.get("userId"));

    }
}
