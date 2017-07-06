package businessComponents;

import com.cognizant.Craft.ReusableLibrary;
import com.cognizant.Craft.ScriptHelper;

import pageObjects.DashboardPage;

public class DashboardComponents extends ReusableLibrary {

	public DashboardComponents(ScriptHelper scriptHelper) {
		super(scriptHelper);
		
	}
	
	public void startAQuote() throws InterruptedException {
    waitForPageLoad();
    ClickObject(DashboardPage.Start_A_Quote, "Start A Quote..");
	waitForPageLoad();
	ClickObject(DashboardPage.Continue,"Click Continue");
	waitForPageLoad();
	}
	
}
