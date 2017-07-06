package testscripts.LoginScenario;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.cognizant.framework.IterationOptions;
import com.cognizant.framework.selenium.Browser;
import com.cognizant.framework.selenium.ExecutionMode;
import com.cognizant.framework.selenium.SeleniumTestParameters;

//import supportlibraries.DriverScript;

import com.cognizant.Craft.*;

/**
 * Test for login with newly registered user
 * 
 * @author Cognizant
 */
public class TestForLoginWithNewlyRegisteredUser extends CRAFTTestCase {

	@Test(dataProvider = "TestConfigurations")
	public void testRunner(String testInstance, ExecutionMode executionMode,
			Browser browser, int startIteration, int endIteration) {
		SeleniumTestParameters testParameters = new SeleniumTestParameters(
				currentScenario, currentTestcase);
		testParameters
				.setCurrentTestDescription("Test for login with invalid user credentials");
		testParameters.setCurrentTestInstance(testInstance);
		testParameters.setExecutionMode(executionMode);
		testParameters.setBrowser(browser);
		testParameters
				.setIterationMode(IterationOptions.RUN_ONE_ITERATION_ONLY);
		testParameters.setStartIteration(startIteration);
		testParameters.setEndIteration(endIteration);

		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}
	/*public void testRunner(String testInstance, ExecutionMode executionMode,
			MobileToolName mobileToolName,
			MobileExecutionPlatform executionPlatform, String deviceName) {
		SeleniumTestParameters testParameters = new SeleniumTestParameters(
				currentScenario, currentTestcase);
		testParameters
				.setCurrentTestDescription("Test for login with invalid user credentials");
		testParameters.setCurrentTestInstance(testInstance);
		testParameters.setExecutionMode(executionMode);
		testParameters.setMobileToolName(mobileToolName);
		testParameters.setMobileExecutionPlatform(executionPlatform);
		testParameters.setDeviceName(deviceName);
		testParameters
				.setIterationMode(IterationOptions.RUN_ONE_ITERATION_ONLY);

		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();

		tearDownTestRunner(testParameters, driverScript);
	}
*/
	/*@DataProvider(name = "TestConfigurations", parallel = true)
	public Object[][] dataTC2() {
		return new Object[][] { { "Instance1", ExecutionMode.PERFECTO,
				MobileToolName.APPIUM, MobileExecutionPlatform.IOS,
				"4d005cb2c4938197" } };
	}*/
	@DataProvider(name = "TestConfigurations", parallel = true)
	public Object[][] dataTC2() {
		return new Object[][] { { "Instance1", ExecutionMode.LOCAL,
				Browser.INTERNET_EXPLORER, 1, 1 }, };
	}

}