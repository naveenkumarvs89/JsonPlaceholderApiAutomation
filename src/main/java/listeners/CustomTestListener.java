package listeners;

import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ExtentReportManager;

public class CustomTestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        // Initialize the report at the start of the test context
        String reportPath = "build/reports/tests/test/report.html"; // Set the path for the report
        ExtentReportManager.initReport(reportPath);
    }

    @Override
    public void onTestStart(ITestResult result) {
        // Create a new test in the report
        ExtentReportManager.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Log the success in the report
        ExtentReportManager.getTest(result).log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Log the failure in the report
        ExtentReportManager.getTest(result).log(Status.FAIL, "Test Failed");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Log the skipped test in the report
        ExtentReportManager.getTest(result).log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        // Flush the reports at the end of the test context
        ExtentReportManager.flush();
    }
}
