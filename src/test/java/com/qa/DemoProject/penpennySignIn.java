package com.qa.DemoProject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.cedarsoftware.util.io.JsonWriter;
import com.qa.DemoProject.TestBaseClass;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class penpennySignIn extends TestBaseClass{
	
	/*Test with valid credentials*/
	@Test
	public void test_SignInAPI_ValidCredentials() {

		startReport("test_SignInAPI_ValidCredentials", "Sonali");

		etest.info(".....................Passing Username and password through body.........................");
		
		HashMap<String, Object> credentials = new HashMap<String, Object>();
		credentials.put("usernameOrEmail", OR.getProperty("userName"));
		credentials.put("password", OR.getProperty("passWord"));
		
		log.info("Sign In with credentilas: "+credentials);
		etest.log(Status.INFO, "usernameOrEmail ::"+credentials.get("usernameOrEmail"));
		etest.log(Status.INFO, "password ::"+credentials.get("password"));
		
		Response response = 
				given()
				.contentType("application/json")
				.body(credentials)
				.when()
				.post(OR.getProperty("signInUrl"))
				.then()
				.log()
				.body()
				.assertThat()
				.statusCode(200)
				.and()
				.contentType(ContentType.JSON)
				.and()
				.header("Content-Length", equalToIgnoringCase("376"))
				.extract()
				.response();
		
		etest.info(".....................Response Data.........................");
		etest.info("<pre>"+JsonWriter.formatJson(response.prettyPrint())+"</pre>");
		
		log.info(".....................Response Data.........................");
		log.info(response.jsonPath().prettify());

	}
	
	/*Sign In With Invalid Username*/
	@Test
	public void test_SignInAPI_InvalidUsername() {

		startReport("test_SignInAPI_InvalidUsername", "Sonali");
		
		etest.info(".....................Passing Invalid Username and valid password through body.........................");
		
		HashMap<String, Object> credentials = new HashMap<String, Object>();
		credentials.put("usernameOrEmail", "sonali.ssalunkhe@penpenny.com");
		credentials.put("password", "ABCabc@1234");
		
		log.info("Sign In with credentilas: "+credentials);
		etest.log(Status.INFO, "usernameOrEmail :::"+credentials.get("usernameOrEmail"));
		etest.log(Status.INFO, "password :::"+credentials.get("password"));
		
		Response response = 
				given()
				.contentType("application/json")
				.body(credentials)
				.when()
				.post(OR.getProperty("signInUrl"))
				.then()
				.body("message", containsString("Invalid email or password"))
				.assertThat()
				.statusCode(400)
				.and()
				.contentType(ContentType.JSON)
				.and()
				//.header("Content-Length", equalToIgnoringCase("53"))
				.extract()
				.response();
		
		etest.info(".....................Response Data.........................");
		etest.info("<pre>"+JsonWriter.formatJson(response.prettyPrint())+"</pre>");
		
		log.info(".....................Response Data.........................");
		log.info(response.jsonPath().prettify());
	}
	
	/*Sign with blank username and password*/
	@Test
	public void test_SignInAPI_BlankCredentials() {

		startReport("test_SignInAPI_BlankCredentials", "Sonali");
		
		etest.info(".....................Keeping Username and password fields blank.........................");
		
		HashMap<String, Object> credentials = new HashMap<String, Object>();
		credentials.put("usernameOrEmail", "");
		credentials.put("password", "");
		
		log.info("Sign In with blank credentilas: "+credentials);
		etest.log(Status.INFO, "usernameOrEmail :::"+credentials.get("usernameOrEmail"));
		etest.log(Status.INFO, "password :::"+credentials.get("password"));
		
				Response response=
				given()
				.contentType("application/json")
				.body(credentials)
				.when()
				.post(OR.getProperty("signInUrl"))
				.then()
				.body("errors.msg", contains("username or email is required","password is required"))
				.assertThat()
				.statusCode(422)
				.and()
				.contentType(ContentType.JSON)
				.and()
				.header("Content-Length", equalToIgnoringCase("217"))
				.extract()
				.response();
				
				etest.info(".....................Response Data.........................");
				etest.info("<pre>"+JsonWriter.formatJson(response.prettyPrint())+"</pre>");
				
				log.info(".....................Response Data.........................");
				log.info(response.jsonPath().prettify());
				
	}

}
