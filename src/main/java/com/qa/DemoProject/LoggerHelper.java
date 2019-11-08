package com.qa.DemoProject;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerHelper {
	private static boolean root = false;
	private static final String str_todaysDateStamp = new SimpleDateFormat("dd-MMM-YYYY").format(new Date());
	/**
	 * this method configures logger
	 * 
	 * @author ajitpawar
	 * @param cls
	 * @return
	 */
	public static Logger getLogger(Class cls) {
		if (root) {
			return Logger.getLogger(cls);
		}
		System.setProperty("current.date", str_todaysDateStamp);
		String str_todaysDateTimeStamp = new SimpleDateFormat("dd-MMM-YYYY HH-mm-ss").format(new Date());
		System.setProperty("current.date.time", str_todaysDateTimeStamp);
		PropertyConfigurator.configure(System.getProperty("user.dir") + "/src/main/java/com/qa/DemoProject/log4j.properties");
		root = true;
		return Logger.getLogger(cls);
	}
}