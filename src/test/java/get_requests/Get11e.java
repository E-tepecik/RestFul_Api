package get_requests;

import base_urls.GoRestBaseUrl;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import test_data.GoRestTestData;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.assertTrue;

public class Get11e extends GoRestBaseUrl {
      /*
        Given
            https://gorest.co.in/public/v1/users
        When
            User send GET Request
        Then
            The value of "pagination limit" is 10
        And
            The "current link" should be "https://gorest.co.in/public/v1/users?page=1"
        And
            The number of users should  be 10
        And
            We have at least one "active" status
        And
            "Fr. Paramartha Bandopadhyay", "Pres. Amarnath Dhawan" and "Sujata Chaturvedi" are among the users
        And
            The female users are less than or equals to male users
     */


    @Test
    public void get11() {

        //1. Set The URL
        spec.pathParam("first","users");
        Response response= given().spec(spec).when().get("/{first}");
       // response.jsonPath().prettyPrint(); şeklindede yazılabiliyor
        response.prettyPrint();
        //bodyde gelen cevabı datayı dışarı alıp işlem yapamıyoruz
        response.then().assertThat().statusCode(200)
                .body("meta.pagination.limit", equalTo(10),
                      "meta.pagination.links.current",equalTo("https://gorest.co.in/public/v1/users?page=1"),
                        "data",hasSize(10),
                        "data.status",hasItem("active"),
                        "data.name", hasItems("Arjun Mehra", "Siddhran Nambeesan","Aanjaneya Kaur"));

       List<String> genders= response.jsonPath().getList("data.gender");
       int female=0;
       for (String w: genders){
       if(w.equalsIgnoreCase("female")){
           female++;
       }
       }
        Assert.assertTrue(female<= genders.size());
        List<String> femaleNames=response.jsonPath().getList("data.findAll{it. gender=='female'}.name");
        System.out.println(femaleNames);
        List<String> maleNames=response.jsonPath().getList("data.findAll{it. gender=='male'}.name");
        System.out.println(maleNames);

        //listin başladığı yere gelmen lazıım//grubu language tamamını bul it dediğimiz her bi r eleman it. sonra koşul koyuyoruz.
        // } bundan sonra elenmiş dattaların içerindeki istediğimiz bilgiyi dönecek
        assertTrue(femaleNames.size()<=maleNames.size());

    }
}