package businessComponents;


import pageObjects.*;

import org.openqa.selenium.Keys;

import com.cognizant.Craft.*;
import com.cognizant.framework.Status;


/**
 * Class for storing general purpose business components
 * @author Cognizant
 */
public class GeneralComponents extends ReusableLibrary{
	/**
	 * Constructor to initialize the component library
	 * @param scriptHelper The {@link ScriptHelper} object passed from the {@link DriverScript}
	 */
	public GeneralComponents(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}
	
	
	public void invokeApplication() {
		report.updateTestLog("Invoke Application", "Invoke the application under test @ " +
									properties.getProperty("ApplicationUrl"), Status.DONE);
		driver.get(properties.getProperty("ApplicationUrl"));
		waitForPageLoad();
		
	}
	
	public void login() throws InterruptedException {
		String userName = dataTable.getData("General_Data", "Username");
		String password = dataTable.getData("General_Data", "Password");
		enterTxtInput(MarineLogin.txtUsername, userName, "Enter User Name");
		enterTxtInput(MarineLogin.txtPassword, password, "Enter Password");
		ClickObject(MarineLogin.btnContinue, "Click Continue Button");
        waitForPageLoad();
		driver.findElement(MarineLogin.marine).sendKeys(Keys.ENTER);
		wait(3000);
		switchToLastWindow();
	}
	
	
	public void wait(int args) throws InterruptedException
	{
		Thread.sleep(args);
	}
	/*public void verifyLoginSuccessful() {
		if(driver.findElement(MasterPage.lnkSignOff).isDisplayed()) {
			report.updateTestLog("Verify Login", "Login succeeded for valid user", Status.PASS);
		} else {
			frameworkParameters.setStopExecution(true);
			throw new FrameworkException("Verify Login", "Login failed for valid user");
		}
	}
	
	public void verifyLoginFailed() {
		if(!driverUtil.objectExists(MasterPage.lnkSignOff)) {
			report.updateTestLog("Verify Login", "Login failed for invalid user", Status.PASS);
		} else {
			report.updateTestLog("Verify Login", "Login succeeded for invalid user", Status.FAIL);
		}
	}
	
	public void logout() {
		report.updateTestLog("Logout", "Click the sign-off link", Status.DONE);
		driver.findElement(MasterPage.lnkSignOff).click();
	}*/
}