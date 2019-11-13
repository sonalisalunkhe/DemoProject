package com.qa.DemoProject;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.qa.DemoProject.TestBaseClass;

public class ExtentReportManager {

	private static ExtentReports extent;
	/**
	 * Returns extent report object
	 * 
	 * @author ajitpawar
	 * @return instance
	 */
	public static ExtentReports getInstance() {
		if (extent == null) {
			//String str_todaysDateTimeStamp = new SimpleDateFormat("dd-MMM-YYYY HH-mm-ss").format(new Date());
			String newReportPath = TestBaseClass.createNewDirectory(TestBaseClass.parentResultDirectory + "Reports");
			String location = newReportPath + "AutomationReport.html";
			return createInstance(location);
		} else {
			return extent;
		}
	}
	/**
	 * Returns extent report object with all configutaion
	 * 
	 * @author ajitpawar
	 * @param fileName
	 * @return
	 */
	public static ExtentReports createInstance(String fileName) {
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTheme(Theme.DARK);
		htmlReporter.config().setDocumentTitle(fileName);
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName("Hospital API Automation Report");
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		return extent;
	}

}