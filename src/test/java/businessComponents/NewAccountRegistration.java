package businessComponents;

import com.cognizant.Craft.ReusableLibrary;
import com.cognizant.Craft.ScriptHelper;

import pageObjects.RegisterNewAccountPage;

public class NewAccountRegistration extends ReusableLibrary{
    
	private static final String NEW_ACCOUNT_DATA = "New_Account_Data";
	
	public NewAccountRegistration(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}

	public void registerNewAccount() throws InterruptedException {
		enterTxtInput(RegisterNewAccountPage.Named_Insured,dataTable.getData(NEW_ACCOUNT_DATA,"Named Insured"), "Enter Insured Name");
        ClickObject(RegisterNewAccountPage.SearchButton, "Click Search Button");	
        waitForPageLoad();
        ClickObject(RegisterNewAccountPage.RegisterNewAccount, "Click Register New Account");
        waitForPageLoad();
        enterTxtInput(RegisterNewAccountPage.StreetAdress, dataTable.getData(NEW_ACCOUNT_DATA,"Street Address"), "Enter Street Address");
        enterTxtInput(RegisterNewAccountPage.ZipCode, dataTable.getData(NEW_ACCOUNT_DATA,"Zip Code"), "Enter Zip Code");
	    enterTxtInput(RegisterNewAccountPage.City, dataTable.getData(NEW_ACCOUNT_DATA,"City"), "Enter City Name");
	    selectDropDown_UsingVisibleText(RegisterNewAccountPage.BusinessType, dataTable.getData(NEW_ACCOUNT_DATA,"Business Type"), "Select Business Type");
	    enterTxtInput(RegisterNewAccountPage.Year_Established, dataTable.getData(NEW_ACCOUNT_DATA,"Year Established"), "Enter Business Year Established");
	    ClickObject(RegisterNewAccountPage.provideContactInfoNo, "Click No for insured contact information");
	    ClickObject(RegisterNewAccountPage.ContinueButton, "Click on Continue");
	    waitForPageLoad();
	}
}