package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestResult;

import java.util.HashMap;
import java.util.Map;

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

    private static final Map<String, ExtentTest> createdTests = new HashMap<>();

    public static ExtentTest createTest(String testName) {
        if (createdTests.containsKey(testName)) {
            return createdTests.get(testName);
        } else {
            ExtentTest test = extent.createTest(testName);
            createdTests.put(testName, test);
            return test;
        }
    }

    public static ExtentTest getTest(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        return createdTests.get(testName);
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
