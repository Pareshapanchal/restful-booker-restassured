package com.restful.booker.crudtest;

import com.restful.booker.model.AuthPojo;
import com.restful.booker.model.BookingPojo;
import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;


public class BookinCRUDTest extends TestBase {

        static String username ="admin";
        static String password ="password123";
        static String token;
        static String firstname= "Jason"+ TestUtils.randomStr(3);
        static String lastname= "Brown";
        static int totalprice= 852;
        static  boolean depositpaid =true;
        static String additionalneeds = "Access to Beach";
        static int bookingid;
    BookingPojo bookingPojo = new BookingPojo();

        @Test()
        public void test001(){
            AuthPojo authPojo = new AuthPojo();
            authPojo.setUsername(username);
            authPojo.setPassword(password);

            Response response = given().log().all()
                    .header("Content-Type","application/json")
                    .body(authPojo)
                    .when()
                    .post("https://restful-booker.herokuapp.com/auth")
                    .then().extract().response();
            String tk= response.jsonPath().get("token").toString();
            System.out.println("Token :  "+tk);
            token = tk;
        }
    @Test
    public void test002(){
             Response response=  given().log().all()
                     .when()
                .get()
                .then().extract().response();
             response.prettyPrint();
             Assert.assertEquals(response.getStatusCode(),200);
    }
    @Test
    public void test003(){
        HashMap<String,Object> bookingdates = new HashMap<String, Object>();
        bookingdates.put("checkin","2018-01-01");
        bookingdates.put("checkout","2019-01-01");


        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setTotalprice(totalprice);
        bookingPojo.setDepositpaid(depositpaid);
        bookingPojo.setBookingdates(bookingdates);
        bookingPojo.setAdditionalneeds(additionalneeds);

       Response response = given().log().all()
                .contentType(ContentType.JSON)
                .header("Accept","application/json")
                .body(bookingPojo)
                .when()
                .post()
                .then().extract().response();
        response.prettyPrint();
        bookingid = response.jsonPath().get("bookingid");

        String actual_firstname = response.jsonPath().get("booking.firstname").toString();

        Assert.assertEquals(200,response.getStatusCode());
        Assert.assertEquals(actual_firstname,firstname);



    }
    @Test
    public void test004(){
        Response response =given().log().all()
                .when()
                .get("/"+bookingid)
                .then().extract().response();
        response.prettyPrint();
        Assert.assertEquals(200,response.statusCode());
    }
    @Test
    public void test005(){
        HashMap<String,Object> bookingdates = new HashMap<String, Object>();
        bookingdates.put("checkin","2018-01-01");
        bookingdates.put("checkout","2019-01-01");

        bookingPojo.setFirstname(firstname);
        bookingPojo.setLastname(lastname);
        bookingPojo.setTotalprice(1500);
        bookingPojo.setDepositpaid(false);
        bookingPojo.setBookingdates(bookingdates);
        bookingPojo.setAdditionalneeds(additionalneeds);

        Response response = given().log().all()
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .header("Cookie","token="+token)
                .body(bookingPojo)
                .when()
                .put("/"+bookingid)
                .then().extract().response();
                 response.prettyPrint();
        String actual_totalprice = response.jsonPath().get("totalprice").toString();
        String actual_depositpaid = response.jsonPath().get("depositpaid").toString();
        System.out.println(actual_totalprice);
        System.out.println(actual_depositpaid);
        Assert.assertEquals("1500",actual_totalprice);
        Assert.assertEquals("false",actual_depositpaid);
        Assert.assertEquals(200,response.getStatusCode());
        }
        @Test
        public void test006(){
            Response response = given()
                    .header("Content-Type","application/json")
                    .header("Accept","application/json")
                    .header("Cookie","token="+token)
                    .body("{ \"firstname\": \"JamesBond\" }")
                    .when()
                    .patch("/"+bookingid)
                    .then().extract().response();
            response.prettyPrint();
            String actual_firstname = response.jsonPath().get("firstname").toString();
            System.out.println(actual_firstname);
            Assert.assertEquals("JamesBond",actual_firstname);

            Assert.assertEquals(200,response.getStatusCode());

        }
    @Test
    public void test007(){

        System.out.println("Token :"+token);
       Response response = given().log().all()
                .header("Content-Type","application/json")
                .header("Accept","application/json")
                .header("Cookie", "token="+token)
                 .when()
                .delete("/"+bookingid)
                .then().extract().response();
       int code = response.statusCode();
        System.out.println(code);
       Assert.assertEquals(201,response.getStatusCode());

        }
}
