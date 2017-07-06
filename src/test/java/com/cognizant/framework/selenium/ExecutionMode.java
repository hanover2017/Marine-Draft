package com.cognizant.framework.selenium;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Enumeration to represent the mode of execution
 * 
 * @author Cognizant
 */
public enum ExecutionMode {
	/**
	 * Execute on the local machine
	 */
	LOCAL,

	/**
	 * Execute on a Perfecto MobileCloud device using {@link RemoteWebDriver}
	 */
	PERFECTO,

	/**
	 * Execute on a mobile device using Appium
	 */
	MOBILE;

}