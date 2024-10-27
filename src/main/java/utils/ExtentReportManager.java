package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestResult;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static ExtentTest test;

    // Initialize the extent report
    public static void initReport(String reportPath) {
        if (extent == null) {
            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(reportPath);
            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
        }
    }

    public static ExtentTest createTest(String testName) {
        test = extent.createTest(testName);
        return test;
    }

    public static ExtentTest getTest(ITestResult result) {
        return test;
    }

    public static ExtentReports getExtentReports() {
        return extent;
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
