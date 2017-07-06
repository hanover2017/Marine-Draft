package runners;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.cognizant.framework.selenium.*;
import com.cognizant.framework.ExcelDataAccessforxlsm;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.IterationOptions;
import com.cognizant.framework.Settings;
import com.cognizant.framework.Util;

/**
 * Class to manage the batch execution of test scripts within the framework
 * 
 * @author Cognizant
 */
public class Allocator {
	private FrameworkParameters frameworkParameters = FrameworkParameters
			.getInstance();
	private Properties properties;
	private Properties mobileProperties;
	private ResultSummaryManager resultSummaryManager = ResultSummaryManager
			.getInstance();

	/**
	 * The entry point of the test batch execution <br>
	 * Exits with a value of 0 if the test passes and 1 if the test fails
	 * 
	 * @param args
	 *            Command line arguments to the Allocator (Not applicable)
	 */
	public static void main(String[] args) {
		Allocator allocator = new Allocator();
		allocator.driveBatchExecution();
	}

	private void driveBatchExecution() {
		resultSummaryManager.setRelativePath();
		properties = Settings.getInstance();
		mobileProperties = Settings.getMobilePropertiesInstance();
		String runConfiguration;
		if (System.getProperty("RunConfiguration") != null) {
			runConfiguration = System.getProperty("RunConfiguration");
		} else {
			runConfiguration = properties.getProperty("RunConfiguration");
		}
		resultSummaryManager.initializeTestBatch(runConfiguration);

		int nThreads = Integer.parseInt(properties
				.getProperty("NumberOfThreads"));
		resultSummaryManager.initializeSummaryReport(nThreads);

		resultSummaryManager.setupErrorLog();

		int testBatchStatus = executeTestBatch(nThreads);

		resultSummaryManager.wrapUp(false);
		
		//Changes for Jenkins HTML report publisher
		resultSummaryManager.copyReportsFolder();
		
		
		resultSummaryManager.launchResultSummary();
			
		System.exit(testBatchStatus);
	}

	/***** When working with SeeTest/Perfecto Parellel *****/
	/*
	 * private int executeTestBatch(int nThreads) { List<SeleniumTestParameters>
	 * testInstancesToRun = getRunInfo(frameworkParameters
	 * .getRunConfiguration()); ExecutorService parallelExecutor = Executors
	 * .newFixedThreadPool(nThreads); ParallelRunner testRunner = null; int i=0;
	 * while(i<testInstancesToRun.size()) { System.out.println("I:"+i); int
	 * size=i+nThreads; //System.out.println("First For"); for(int
	 * currentTestInstance
	 * =size-nThreads;currentTestInstance<size;currentTestInstance++) {
	 * testRunner = new ParallelRunner(
	 * testInstancesToRun.get(currentTestInstance));
	 * parallelExecutor.execute(testRunner);
	 * 
	 * if(frameworkParameters.getStopExecution()) { break; } }
	 * 
	 * parallelExecutor.shutdown(); while(!parallelExecutor.isTerminated()) {
	 * try { Thread.sleep(3000); } catch (InterruptedException e) {
	 * e.printStackTrace(); } } System.out.println("Waitng for thread to stop");
	 * i=size; } if (testRunner == null) { return 0; // All tests flagged as
	 * "No" in the Run Manager } else { return testRunner.getTestBatchStatus();
	 * } }
	 */

	private int executeTestBatch(int nThreads) {
		List<SeleniumTestParameters> testInstancesToRun = getRunInfo(frameworkParameters
				.getRunConfiguration());
		ExecutorService parallelExecutor = Executors
				.newFixedThreadPool(nThreads);
		ParallelRunner testRunner = null;

		for (int currentTestInstance = 0; currentTestInstance < testInstancesToRun
				.size(); currentTestInstance++) {
			testRunner = new ParallelRunner(
					testInstancesToRun.get(currentTestInstance));
			parallelExecutor.execute(testRunner);

			if (frameworkParameters.getStopExecution()) {
				break;
			}
		}

		parallelExecutor.shutdown();
		while (!parallelExecutor.isTerminated()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (testRunner == null) {
			return 0; // All tests flagged as "No" in the Run Manager
		} else {
			return testRunner.getTestBatchStatus();
		}
	}

	private List<SeleniumTestParameters> getRunInfo(String sheetName) {
		ExcelDataAccessforxlsm runManagerAccess = new ExcelDataAccessforxlsm(
				frameworkParameters.getRelativePath() + Util.getFileSeparator()
						+ "src" + Util.getFileSeparator() + "test"
						+ Util.getFileSeparator() + "resources", "Run Manager");
		runManagerAccess.setDatasheetName(sheetName);
		int nTestInstances = runManagerAccess.getLastRowNum();
		List<SeleniumTestParameters> testInstancesToRun = new ArrayList<SeleniumTestParameters>();

		for (int currentTestInstance = 1; currentTestInstance <= nTestInstances; currentTestInstance++) {
			String executeFlag = runManagerAccess.getValue(currentTestInstance,
					"Execute");

			if ("Yes".equalsIgnoreCase(executeFlag)) {
				String currentScenario = runManagerAccess.getValue(
						currentTestInstance, "TestScenario");
				String currentTestcase = runManagerAccess.getValue(
						currentTestInstance, "TestCase");
				SeleniumTestParameters testParameters = new SeleniumTestParameters(
						currentScenario, currentTestcase);

				testParameters.setCurrentTestInstance("Instance"
						+ runManagerAccess.getValue(currentTestInstance,
								"TestInstance"));
				testParameters.setCurrentTestDescription(runManagerAccess
						.getValue(currentTestInstance, "Description"));

				String iterationMode = runManagerAccess.getValue(
						currentTestInstance, "IterationMode");
				if (!"".equals(iterationMode)) {
					testParameters.setIterationMode(IterationOptions
							.valueOf(iterationMode));
				} else {
					testParameters
							.setIterationMode(IterationOptions.RUN_ALL_ITERATIONS);
				}

				String startIteration = runManagerAccess.getValue(
						currentTestInstance, "StartIteration");
				if (!"".equals(startIteration)) {
					testParameters.setStartIteration(Integer
							.parseInt(startIteration));
				}
				String endIteration = runManagerAccess.getValue(
						currentTestInstance, "EndIteration");
				if (!"".equals(endIteration)) {
					testParameters.setEndIteration(Integer
							.parseInt(endIteration));
				}

				String testConfig = runManagerAccess.getValue(
						currentTestInstance, "TestConfigurationID");
				if (!"".equals(testConfig)) {
					getTestConfigValues(runManagerAccess, "TestConfigurations",
							testConfig, testParameters);
				}
				testInstancesToRun.add(testParameters);
				runManagerAccess.setDatasheetName(sheetName);
			}
		}

		return testInstancesToRun;
	}

	private void getTestConfigValues(ExcelDataAccessforxlsm runManagerAccess,
			String sheetName, String testConfigName,
			SeleniumTestParameters testParameters) {

		runManagerAccess.setDatasheetName(sheetName);
		int rowNum = runManagerAccess.getRowNum(testConfigName, 0, 1);
		String executionMode = runManagerAccess.getValue(rowNum,
				"ExecutionMode");
		if (!"".equals(executionMode)) {
			testParameters.setExecutionMode(ExecutionMode
					.valueOf(executionMode));
		} else {
			testParameters.setExecutionMode(ExecutionMode.valueOf(properties
					.getProperty("DefaultExecutionMode")));
		}

		String toolName = runManagerAccess.getValue(rowNum, "MobileToolName");
		if (!"".equals(toolName)) {
			testParameters.setMobileToolName(MobileToolName.valueOf(toolName));
		} else {
			testParameters.setMobileToolName(MobileToolName
					.valueOf(mobileProperties
							.getProperty("DefaultMobileToolName")));
		}

		String executionPlatform = runManagerAccess.getValue(rowNum,
				"MobileExecutionPlatform");
		if (!"".equals(executionPlatform)) {
			testParameters.setMobileExecutionPlatform(MobileExecutionPlatform
					.valueOf(executionPlatform));
		} else {
			testParameters.setMobileExecutionPlatform(MobileExecutionPlatform
					.valueOf(mobileProperties
							.getProperty("DefaultMobileExecutionPlatform")));
		}

		String mobileOSVersion = runManagerAccess.getValue(rowNum,
				"MobileOSVersion");
		if (!"".equals(mobileOSVersion)) {
			testParameters.setmobileOSVersion(mobileOSVersion);
		}

		String deviceName = runManagerAccess.getValue(rowNum, "DeviceName");
		if (!"".equals(deviceName)) {
			testParameters.setDeviceName(deviceName);
			testParameters.setIsDeviceUdid(true);
		} else {
			String manufacturer = runManagerAccess.getValue(rowNum,
					"Manufacturer");
			if (!"".equals(manufacturer)) {
				testParameters.setManuFacturerName(manufacturer);
				testParameters.setIsDeviceUdid(false);
			}
			String modelName = runManagerAccess.getValue(rowNum, "ModelName");
			if (!"".equals(modelName)) {
				testParameters.setModelName(modelName);
				testParameters.setIsDeviceUdid(false);
			}
		}

		String browser = runManagerAccess.getValue(rowNum, "Browser");
		if (!"".equals(browser)) {
			testParameters.setBrowser(Browser.valueOf(browser));
		} else {
			testParameters.setBrowser(Browser.valueOf(properties
					.getProperty("DefaultBrowser")));
		}
	}

}