package pageObjects;

import org.openqa.selenium.By;

public class DashboardPage {
    
	//public static final By Start_A_Quote = By.linkText("START A QUOTE ");
	public static final By Start_A_Quote = By.cssSelector(".btn.btn-primary.btn-Start");
	public static final By Eligibility_Guidelines_Text = By.xpath( "//legend[contains(text(), 'Eligibility.Guidelines')]");
	public static final By Continue = By.id("continueButton");
}


