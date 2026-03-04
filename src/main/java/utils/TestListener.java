package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/TestReport.html");
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("emir_ata_yalcin_case Report");
        spark.config().setReportName("QA Assessment Test Results");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Author", "Emir Ata Yalçın");
        extent.setSystemInfo("Environment", "Production");
        extent.setSystemInfo("Browser", "Chrome");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
        test.get().log(Status.INFO, result.getMethod().getMethodName() + " testi başlatıldı.");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test başarıyla tamamlandı.");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, "Test başarısız oldu.");
        test.get().log(Status.FAIL, result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().log(Status.SKIP, "Test atlandı.");
        test.get().log(Status.SKIP, result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();

        // Raporun yolunu alıp terminale basıyorum
        String reportPath = new java.io.File("reports/TestReport.html").getAbsolutePath();
        System.out.println("\n=======================================================");
        System.out.println("Test Raporu Hazır! İncelemek için aşağıdaki linki tarayıcınızda açabilirsiniz:");
        System.out.println("file:///" + reportPath.replace('\\', '/'));
        System.out.println("=======================================================\n");
    }
}
