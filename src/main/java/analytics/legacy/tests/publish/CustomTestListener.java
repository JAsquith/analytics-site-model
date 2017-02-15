package analytics.legacy.tests.publish;


import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomTestListener extends TestListenerAdapter{

    /**
     * This should be a relatively generic TestListener
     * It has been verified on Gridlastic, needs testing on Sauce/TestingBot
     */

    @Override
    public void onTestFailure(ITestResult tr) {
        log(tr.getTestContext().getCurrentXmlTest().getParameter("test-id") + ",-,"
            + "FAILURE: " + tr.getTestContext().getCurrentXmlTest().getParameter("test-title"));
        log(tr.getTestContext().getCurrentXmlTest().getParameter("test-id") + ",-,"
            + "TEST METHOD: "+tr.getMethod().getMethodName());
        log(tr.getTestContext().getCurrentXmlTest().getParameter("test-id") + ",-,"
            +" REASON:"+tr.getThrowable().getMessage().substring(0, 30).trim());
        log(tr.getTestContext().getCurrentXmlTest().getParameter("test-id") + ",-,"
            +"... VIDEO: "+tr.getTestContext().getAttribute("video_url"));
      
    }
	 
    @Override
    public void onTestSkipped(ITestResult tr) {
        log(tr.getTestContext().getCurrentXmlTest().getParameter("test-id") + ",-,"
                + tr.getName()+ "--Test method skipped\n");

/*
        log(tr.getTestContext().getSkippedConfigurations().toString());
        Set attributeNames = tr.getAttributeNames();
        log("Attribute Names:" + attributeNames.toString());
        for (int i=0;i<attributeNames.size();i++){
            log("Attribute " + i + ": " + attributeNames.toArray()[i]);
        }
*/
    }
	 
    
    @Override
    public void onTestSuccess(ITestResult tr) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (tr.getTestClass().getName() == "Publish"){
            System.out.println("");
            if(tr.getTestContext().getCurrentXmlTest().getParameter("record-video").equalsIgnoreCase("True")){
                log(tr.getTestContext().getCurrentXmlTest().getParameter("test-id") + ",-,"
                        + "SUCCESS: " + tr.getTestContext().getCurrentXmlTest().getParameter("test-title")
                        +" published "+tr.getTestContext().getAttribute("published")+" cohorts");

                log(tr.getTestContext().getCurrentXmlTest().getParameter("test-id") + ",-,"
                        +"VIDEO: "+tr.getTestContext().getAttribute("video_url"));
            } else {
                log(tr.getTestContext().getCurrentXmlTest().getParameter("test-id") + ",-,"
                        + "SUCCESS: " + tr.getTestContext().getCurrentXmlTest().getParameter("test-title")
                        +" published "+tr.getTestContext().getAttribute("published")+" cohorts");

                log(tr.getTestContext().getCurrentXmlTest().getParameter("test-id") + ",-,"
                        +"SCREENSHOT: "+tr.getTestContext().getAttribute("screenshot_url"));
            }
            System.out.println("");
        }
        if (tr.getMethod().getMethodName().equals("uploadGradesFile")) {
            System.out.println("");
            if(tr.getTestContext().getCurrentXmlTest().getParameter("record-video").equalsIgnoreCase("True")){
                log(tr.getTestContext().getCurrentXmlTest().getParameter("test-id") + ",-,"
                        +"VIDEO: "+tr.getTestContext().getAttribute("video_url"));
            } else {
                log(tr.getTestContext().getCurrentXmlTest().getParameter("test-id") + ",-,"
                        +"SCREENSHOT: "+tr.getTestContext().getAttribute("screenshot_url"));
            }
            System.out.println("");
        } else {
            log("Test Method '" + tr.getMethod().getMethodName() + "' Succeeded");
        }

    }
    
  	 
    private void log(String string) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String d = dateFormat.format(new Date());
        System.out.println("["+d+"],"+string);
	    //System.out.println("");
    }
}