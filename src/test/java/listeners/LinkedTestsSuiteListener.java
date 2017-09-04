package listeners;

import org.testng.*;
import utils.TargetFileManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LinkedTestsSuiteListener implements ISuiteListener {

    boolean firstSuite;
    private SimpleDateFormat stampFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    class ResultSummary {
        String testId; String startTime; String endTime;
        int setupFails; int passCount; int failCount; int skipCount;
        String setupFailReason;

        public String toString(){
            return "\""+testId+"\","+startTime+","+endTime+","+
                    setupFails+","+passCount+","+failCount+","+skipCount+",\""+
                    setupFailReason+"\"";
        }
    }

    @Override
    public void onStart(ISuite suite) {
    }

    @Override
    public void onFinish(ISuite suite) {
        Map<String, ISuiteResult> tests = suite.getResults();
        Set<Map.Entry<String, ISuiteResult>> set = tests.entrySet();

        ResultSummary summary;
        List<String> fullPassesList = new ArrayList<String>();
        int noIDCount = 0;
        TargetFileManager fm = new TargetFileManager();

        firstSuite = true;
        if (fm.fileExistsInTargetDir("sisra-results", "Results_Summary.csv")) {
            firstSuite = false;
        }

        FileOutputStream fos = fm.openTargetFileForOutput(
                "sisra-results", "Results_Summary.csv");
        if (firstSuite) {
            try {
                fos.write(("Test ID,Start,End,Setup Fails,Passes,Fails,Skips,Setup Fail Reason" + System.lineSeparator()).getBytes());
            } catch (IOException e) {
                System.err.println("Error writing column headers to Suite Summary (Results_Summary.csv)");
                e.printStackTrace();
            }
        }

        // Loop through the ISuiteResults (one result for each <test> element in the testng.xml file)
        for (Map.Entry<String, ISuiteResult> entry : set) {
            summary = new ResultSummary();

            ISuiteResult suiteResult = entry.getValue();
            ITestContext context = suiteResult.getTestContext();
            Map<String, String> allParams = context.getCurrentXmlTest().getAllParameters();

            summary.testId = getTestId(allParams, noIDCount);
            summary.startTime = stampFormat.format(context.getStartDate());
            summary.endTime = stampFormat.format(context.getEndDate());

            IResultMap failedConfigs = context.getFailedConfigurations();
            summary.setupFails=failedConfigs.size();
            summary.setupFailReason = "";
            if (summary.setupFails>0){
                summary.setupFailReason = getSetupFailMessage(allParams, failedConfigs);
            }

            // Log any test method that failed
            summary.failCount = context.getFailedTests().size();

            // Log any test methods that were skipped
            summary.skipCount = context.getSkippedTests().size();

            // Log any test methods that passed
            summary.passCount = context.getPassedTests().size();

            // If there were no failures or skips, add this test to the fullPassesList
            if (summary.passCount>0){
                if (summary.setupFails == 0 && summary.failCount == 0 && summary.skipCount == 0){
                    if (!fullPassesList.contains(summary.testId)){
                        fullPassesList.add(summary.testId);
                    }
                }
            }

            writeResultSummary(fos, summary.toString());
        }

        try {
            fos.flush();
            fos.close();
        } catch (IOException e) {
            System.err.println("Error flushing/closing Suite Summary (Results_Summary.csv)");
            e.printStackTrace();
        }

        writePassesList(fullPassesList);

    }

    private String getTestId(Map<String, String> allParams, int noIDCount){
        if (allParams.containsKey("test-id")) {
            return allParams.get("test-id");
        } else {
            // Need to deal with this
            noIDCount++;
            return "Null ID "+noIDCount;
        }

    }

    private String getSetupFailMessage(Map<String, String> allParams, IResultMap failedConfigs){

        // Look for a setup failure reason:
        //  - first check for a "setup-fail-reason" having been added to the test params
        if (allParams.containsKey("setup-fail-reason")){
            return allParams.get("setup-fail-reason");
        } else {
            // - see if any (there should only be one) of the failedConfigs have a throwable we can get
            Set<ITestResult> failedConfigTests = failedConfigs.getAllResults();
            String msg = "No reason found";
            for (ITestResult result : failedConfigTests){
                if(result.getThrowable()!=null){
                    msg = result.getThrowable().getMessage();
                    break;
                }
            }
            return msg;
        }
    }

    private void writeResultSummary(FileOutputStream fos, String result){
        try {
            fos.write(result.getBytes());
            fos.write(System.lineSeparator().getBytes());
        } catch (IOException e) {
            System.err.println("Error writing result to Suite Summary ("+ result +")");
            e.printStackTrace();
        }
    }

    private void writePassesList(List<String> passesList){
        FileOutputStream fos = new TargetFileManager().openTargetFileForOutput("sisra-results", "All_Passes.csv");
        for (String testId : passesList) {
            try {
                fos.write(testId.getBytes());
                fos.write(System.lineSeparator().getBytes());
            } catch (IOException e) {
                System.err.println("Error writing to passes list ("+ testId + ", " + "All_Passes.csv)");
                e.printStackTrace();
            }
        }

        try {
            fos.flush();
            fos.close();
        } catch (IOException e) {
            System.err.println("Error flusing/closing passes list (" + "All_Passes.csv)");
            e.printStackTrace();
        }

    }
}
