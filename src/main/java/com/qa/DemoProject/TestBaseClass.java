package com.qa.DemoProject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.qa.DemoProject.ExtentReportManager;
import com.qa.DemoProject.LoggerHelper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class TestBaseClass {
	public static File file;
	public static Properties OR;
	public static FileInputStream f1;
	public static String parentResultDirectory;
	public static ExtentReports extentReports;
	public static ExtentTest etest;
	public static String str_todaysDateStamp = new SimpleDateFormat("dd-MMM-YYYY").format(new Date());
	public static Logger log = LoggerHelper.getLogger(TestBaseClass.class);
	/**
	 * 
	 * Sets property file and reports before starting of the automation suit
	 * 
	 * @throws Exception
	 * 
	 * 
	 */
	@BeforeSuite
	public void beforeSuite() throws Exception {
		loadPropertiesFile();
		parentResultDirectory = createNewDirectory(System.getProperty("user.dir") + "/Results/Results_");
		setupReport();
	}
	/**
	 * This method sets baseURI for each test
	 * 
	 * @param m
	 */
	@BeforeMethod
	public void beforeMethod(Method m) {
		RestAssured.baseURI = OR.getProperty("BaseUrl");
	}

	/**
	 * This method creates new directory for storing test results
	 * 
	 * @param new directory path
	 * 
	 * @return Returns absolute path for directory
	 */
	public static String createNewDirectory(String path) {
		try {
			file = new File(path + str_todaysDateStamp);
			if (!file.exists()) {
				file.mkdir();
				log.info("New directory created successfully");
			}
			return file.getAbsolutePath() + "/";
		} catch (Exception e) {
			log.info("failed to create New Directoty....");
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * This method loads property files
	 * 
	 * @throws IOException
	 */
	public void loadPropertiesFile() throws IOException {

		OR = new Properties();
		file = new File(System.getProperty("user.dir") + "/src/main/java/com/qa/DemoProject/config.properties");
		f1 = new FileInputStream(file);
		OR.load(f1);
		log.info("Config.Properties file loaded successfully!!");
	}

	/**
	 * Sets html reports for all test cases
	 * 
	 */
	final static void setupReport() {
		try {
			extentReports = ExtentReportManager.getInstance();
			System.out.println("Extent Report Instance created ");
			log.debug("Extent Report Instance created ");
		} catch (Exception e) {
			log.info("Failed to instantiate extent report");
			e.printStackTrace();
			log.error("ERROR :  Failed to instantiate extent report", e);
		}
	}
	/**
	 * This methods starts the Extent reports
	 * 
	 * @param testName
	 * @param authorName
	 */
	public static void startReport(String testName, String authorName) {

		etest = extentReports.createTest(testName).assignAuthor(authorName);

		log.info(".................................................................");
		log.info("Test started is : " + testName);
		log.info(".................................................................");
	}
	/**
	 * Executes after each test and writes the results of the test
	 * 
	 * @param result
	 * @throws IOException
	 */
	@AfterMethod
	public void afterMethod(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.FAILURE) {
			etest.log(Status.FAIL, result.getThrowable());
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			etest.log(Status.PASS, result.getName() + " is pass");
		} else if (result.getStatus() == ITestResult.SKIP) {
			etest.log(Status.SKIP, result.getThrowable());
		}
		log.info("..................................................................");
		log.info("Test Completed is : " + result.getName());
		log.info("..................................................................");
		extentReports.flush();
	}
	/**
	 * 
	 * penpenny Sign In test to pass JWT token to every API through response
	 * 
	 * */
	public String SignInAPI() {
		HashMap<String, Object> credentials = new HashMap<String, Object>();
		credentials.put("usernameOrEmail", OR.getProperty("userName"));
		credentials.put("password", OR.getProperty("passWord"));
		
		Response response = 
				given()
				.contentType("application/json")
				.body(credentials)
				.when()
				.post(OR.getProperty("signInUrl"))
				.then()
				.assertThat()
				.statusCode(200)
				.and()
				.contentType(ContentType.JSON)
				.and()
				.header("Content-Length", equalToIgnoringCase("376"))
				.extract()
				.response();
		String responseToken = response.jsonPath().getString("token");
		return responseToken;
	}
	
}
