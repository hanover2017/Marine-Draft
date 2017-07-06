package com.cognizant.Craft;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cognizant.framework.CraftDataTable;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Settings;
import com.cognizant.framework.Status;
import com.cognizant.framework.selenium.CraftDriver;
import com.cognizant.framework.selenium.SeleniumReport;
import com.cognizant.framework.selenium.WebDriverUtil;

/**
 * Abstract base class for reusable libraries created by the user
 * 
 * @author Cognizant
 */
public abstract class ReusableLibrary {

	Map<String, Object> perfectoCommand = new HashMap<>();

	/**
	 * The {@link CraftDataTable} object (passed from the test script)
	 */
	protected CraftDataTable dataTable;
	/**
	 * The {@link SeleniumReport} object (passed from the test script)
	 */
	protected SeleniumReport report;
	/**
	 * The {@link CraftDriver} object
	 */
	protected CraftDriver driver;

	protected WebDriverUtil driverUtil;

	/**
	 * The {@link ScriptHelper} object (required for calling one reusable
	 * library from another)
	 */
	protected ScriptHelper scriptHelper;

	/**
	 * The {@link Properties} object with settings loaded from the framework
	 * properties file
	 */
	protected Properties properties;
	/**
	 * The {@link FrameworkParameters} object
	 */
	protected FrameworkParameters frameworkParameters;

	/**
	 * Constructor to initialize the {@link ScriptHelper} object and in turn the
	 * objects wrapped by it
	 * 
	 * @param scriptHelper
	 *            The {@link ScriptHelper} object
	 */
	public ReusableLibrary(ScriptHelper scriptHelper) {
		this.scriptHelper = scriptHelper;
		this.dataTable = scriptHelper.getDataTable();
		this.report = scriptHelper.getReport();
		this.driver = scriptHelper.getcraftDriver();
		this.driverUtil = scriptHelper.getDriverUtil();

		properties = Settings.getInstance();
		frameworkParameters = FrameworkParameters.getInstance();
	}

	///////////////////////////////////VERIFY PAGE TITLE////////////////////////////////////////////////////////////////////////////

	public boolean verifyTitle(String title){
		boolean bReturn = false;
		try{
			if (driver.getTitle().equalsIgnoreCase(title)){
				report.updateTestLog("Page Validation", "Page Title is matched.", Status.PASS);
				bReturn = true;
			}else
				report.updateTestLog("Page Validation", "Page Title is not matched.", Status.FAIL);
		}catch (Exception e) {
			report.updateTestLog("Page Validation", "UnKnown Exception occured.", Status.FAIL);
		}
		return bReturn;
	}


	//////////////////////////////////VERIFY PAGE TEXT/////////////////////////////////////////////////////////////////////

	public void verifyText(By StrProperty, String text){
		try {
			String sText = driver.findElement(StrProperty).getText();
			if (sText.equalsIgnoreCase(text)){
				report.updateTestLog("Page Validation", "Text is matched.", Status.PASS);
			}else{
				report.updateTestLog("Page Validation", "Text is not matched.", Status.FAIL);
			}
		}catch (Exception e) {
			report.updateTestLog("Page Validation", "UnKnown Exception occured.", Status.FAIL);
		}
	}

	public void verifyTextContains(By StrProperty, String text){
		try{
			String sText = driver.findElement(StrProperty).getText();
			if (sText.contains(text)){
				report.updateTestLog("Page Validation", "Text is matched.", Status.PASS);
			}else{
				report.updateTestLog("Page Validation", "Text is not matched.", Status.FAIL);
			}
		}catch (Exception e) {
			report.updateTestLog("Page Validation", "UnKnown Exception occured.", Status.FAIL);
		}
	}

	////////////////////////////////DISPLAYED/ SELECTED/ ENABLED ///////////////////////////////////////////////////////////

	public void IsDisplayed(By StrProperty,String StrElementName){

		try
		{

			if(driver.findElement(StrProperty).isDisplayed()){
				report.updateTestLog("Element Validation", StrElementName + " Element is displayed.", Status.PASS);
			}	
			else
			{
				report.updateTestLog("Element Validation", StrElementName + " Element is not displayed.", Status.FAIL);
			}

		}

		catch (Exception e) {
			report.updateTestLog("Element Validation", StrElementName+ " Unknown Exception occured.", Status.FAIL);
		}

	}

	public void IsEnabled(By StrProperty,String StrElementName){

		try
		{

			if(driver.findElement(StrProperty).isEnabled()){
			report.updateTestLog("Element Validation", StrElementName + " Element is enabled.", Status.PASS);
			}
			else {
				report.updateTestLog("Element Validation", StrElementName + " Element is not enabled.", Status.FAIL);	
			}
		}	

		catch (Exception e) {
			report.updateTestLog("Element Validation", StrElementName+ " Element is not enabled.", Status.FAIL);
		}

	}

	public void IsSelected(By StrProperty,String StrElementName){

		try
		{

			if(driver.findElement(StrProperty).isSelected()){
			report.updateTestLog("Element Validation", StrElementName + " Element is selected.", Status.PASS);
			}
			else {
				report.updateTestLog("Element Validation", StrElementName+ " Element is not selected.", Status.FAIL);
			}
		}	

		catch (Exception e) {
			report.updateTestLog("Element Validation", StrElementName+ " Element is not selected.", Status.FAIL);
		}

	}

	//////////////////////////////////////////KEYS//////////////////////////////////////////////////////////////////////////////

	public void PressTABKey(By StrProperty,String StrPageName)
	{
		try{
			if(driver.findElement(StrProperty).isDisplayed()) {
			driver.findElement(StrProperty).sendKeys(Keys.TAB);
			report.updateTestLog("Page Validation", StrPageName + " TAB Key is pressed.", Status.PASS);
			}
			else {
				report.updateTestLog("Page validation", StrPageName + "TAB Key is not pressed.", Status.FAIL);
			}
			}
		catch (Exception e) {
			report.updateTestLog("Page validation", StrPageName + "TAB Key is not pressed.", Status.FAIL);
		}

	}

		////////////////////////////////////// SCREEN VALIDATION  ////////////////////////////////////////////////////////////

	public void ScreenValidation(By StrProperty,String StrPageName)
	{

		try
		{

			if(driver.findElement(StrProperty).isDisplayed())
			{
			report.updateTestLog("Page validation", StrPageName + " Page is loaded.", Status.PASS);
			}
			else {
				report.updateTestLog("Page validation", StrPageName + " Page is not loaded.", Status.FAIL);
			}
		}	

		catch (Exception e) {
			report.updateTestLog("Page validation", StrPageName + " Page is not loaded.", Status.FAIL);
		}
	}

		/////////////////////////////////////////////SELECT DROPDOWN/////////////////////////////////////////////////////////////	

	public void selectDropDown_UsingVisibleText(By ObjProp,String StrValue,String StrDescription)
	{
		try{
			Select select=new Select(driver.findElement(ObjProp));
			select.selectByVisibleText(StrValue);
			report.updateTestLog("Drop Down", "Selected value: "+ StrValue + " in the Dropdown: " +
					StrDescription, Status.DONE);
		}
		catch (Exception e)
		{
			report.updateTestLog("Drop Down", "Dropdown " +
					StrDescription + " is not displayed.", Status.FAIL);
		}
	}

	

	public void selectDropDown_UsingValue(By ObjProp,String StrValue,String StrDescription)
	{
		try{
			Select select=new Select(driver.findElement(ObjProp));
			select.selectByValue(StrValue);
			report.updateTestLog("Drop Down", "Selected value: "+ StrValue + " in the Dropdown: " +
					StrDescription, Status.DONE);
		}
		catch (Exception e)
		{
			report.updateTestLog("Drop Down", "Dropdown " +
					StrDescription + " is not displayed.", Status.FAIL);
		}
	}

	
	//////////////////////////////////////////////GET TEXT////////////////////////////////////////////////////////////////////////

	public String getText(By ObjProp){
		String bReturn = "";
		try{
			if(driver.findElement(ObjProp).isDisplayed()){	
			report.updateTestLog("The element", ObjProp+  "is found.", Status.PASS);
			return driver.findElement(ObjProp).getText();
			}
			else{
				report.updateTestLog("The element", ObjProp+  "could not be found.", Status.FAIL);
			}
		} catch (Exception e) {
			report.updateTestLog("The element", ObjProp+  "could not be found.", Status.FAIL);

		}

		return bReturn; 
	}

	//////////////////////////////////Enter Data in Text Box///////////////////////////////////////////////////	

	public void enterTxtInput(By ObjProp,String StrValue,String StrDescription) throws InterruptedException
	{
		try{
			if(driver.findElement(ObjProp).isDisplayed()){
			driver.findElement(ObjProp).sendKeys(Keys.CLEAR);
			driver.findElement(ObjProp).sendKeys(StrValue);
			report.updateTestLog("Data Entry", "Entered value: "+ StrValue + " in the TextBox: " +
					StrDescription, Status.DONE);
			}
			else {
				report.updateTestLog("Data Entry", "TextBox " +
						StrDescription + " is not displayed.", Status.FAIL);
			}
		}
		catch (Exception e)
		{
			report.updateTestLog("Data Entry", "TextBox " +
					StrDescription + " is not displayed.", Status.FAIL);
		}
	}

	////////////////////////////////Clicking an Object////////////////////////////////////////////////////////// 

	public void ClickObject(By ObjProp,String StrDescription) throws InterruptedException
	{
		try{
			driver.findElement(ObjProp).click();
			report.updateTestLog("Object",StrDescription + " is Clicked.", Status.PASS);
		}
		catch(Exception e)
		{
			report.updateTestLog("Object",StrDescription + " is not Clicked.", Status.FAIL);
		}
	}

		/////////////////////////////////////////////////////SWITCH WINDOWS///////////////////////////////////////////////////////////////////

	public void switchToParentWindow() {
		try {
			Set<String> winHandles = driver.getWindowHandles();
			for (String wHandle : winHandles) {
				driver.switchTo().window(wHandle);
				break;
			}
		} catch (Exception e) {
			report.updateTestLog("Swicth Window:", "Window cannot be switched.", Status.FAIL);
		}
	}
	public void switchToLastWindow() {
		try {
			Set<String> winHandles = driver.getWindowHandles();
			for (String wHandle : winHandles) {
				driver.switchTo().window(wHandle);
			}
			waitForPageLoad();
			driver.manage().window().maximize();
		} catch (Exception e) {
			report.updateTestLog("Swicth Window:", "Window cannot be switched.", Status.FAIL);
		}
	}

	/////////////////////////////////////////////////////CLOSE AND QUIT BROWSER////////////////////////////////////////////////////////////

	public void CloseBrowser() {

		String BroswerName = driver.getCapabilities().getBrowserName();
		try {
			driver.close();
		} catch (Exception e) {
			report.updateTestLog("The Browser:", BroswerName + "could not be closed.", Status.FAIL);

		}

	}


	public void QuitBrowser() {

		String BroswerName = driver.getCapabilities().getBrowserName();
		try {
			driver.quit();
		} catch (Exception e) {
			report.updateTestLog("The Browser:", BroswerName + "could not quit.", Status.FAIL);

		}

	}


	////////////////////////////////////////////Explicit Wait//////////////////////////////////////////////////////////////////////////

	public void ElementToBePresent(By ObjProp){

		WebDriverWait wait=new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.presenceOfElementLocated(ObjProp));
		}
	
	
	public void ElementToBeClickableById(By ObjProp){

		WebDriverWait wait=new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(ObjProp));
		}
	
	///////////////////////////////////Implicit wait/////////////////////////////////
	
	public void waitForPageLoad(){
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	
	
	///////////////////////////////////Get Row Number with the specific text from a Table/////////////////////////////////////////////////
	
	public int getRowWithCellText(String TblXpath, String SearchStr)
	{
		int intRow, intCol,intTotalRows,intTotalCols,intRsltRow;
		String StrCellText;
		intRsltRow=0;
		intTotalRows=driver.findElements(By.xpath(TblXpath + "/tbody/tr")).size();
		intTotalCols=driver.findElements(By.xpath(TblXpath + "/thead/tr/th")).size();

		for(intRow=1;intRow<=intTotalRows;intRow++)
		{
			for(intCol=1;intCol<=intTotalCols;intCol++)
			{
				StrCellText=(driver.findElement(By.xpath(TblXpath + "/tbody/tr[" + intRow + "]/td[" + intCol + "]")).getText()).trim();
				if(StrCellText.equalsIgnoreCase(SearchStr))
				{
					intRsltRow=intRow;
					break;
				}
			}
			if(intRsltRow>0)
			{
				break;
			}
		}	

		return intRsltRow;

	}

	///////////////////////////////////Get Data from a specific cell of a Table/////////////////////////////////////////////////
	public String getCellData(String TblXpath,int DataRow,int DataCol)
	{		
		String StrCellText;

		StrCellText=driver.findElement(By.xpath(TblXpath + "/tbody/tr[" + DataRow + "]/td[" + DataCol + "]")).getText();

		return StrCellText;		
	}	

	///////////////////////////////////Get Row Number with the specific text from a Table/////////////////////////////////////////////////
	public int getColumnIndex(String TblXpath, String StrColName)
	{
		int intCol,intTotalCols,intRsltCol;
		String StrCellText;
		intRsltCol=0;
		intTotalCols=driver.findElements(By.xpath(TblXpath + "/thead/tr/th")).size();

		for(intCol=1;intCol<=intTotalCols;intCol++)
		{
			StrCellText=driver.findElement(By.xpath(TblXpath + "/thead/tr/td[" + intCol + "]")).getText();
			if(StrCellText.equals(StrColName))
			{
				intRsltCol=intCol;
				break;
			}
		}			

		return intRsltCol;		
	}

}


