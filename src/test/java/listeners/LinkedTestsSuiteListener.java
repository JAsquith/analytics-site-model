package listeners;

import org.testng.*;

import java.util.Map;
import java.util.Set;

public class LinkedTestsSuiteListener implements ISuiteListener {

    Map<String,String> processed;

    @Override
    public void onStart(ISuite suite) {
    }

    @Override
    public void onFinish(ISuite suite) {
        Map<String, ISuiteResult> tests = suite.getResults();
        Set<Map.Entry<String, ISuiteResult>> set = tests.entrySet();
        for (Map.Entry<String, ISuiteResult> entry : set) {
            ISuiteResult suiteResult = entry.getValue();
            ITestContext context = suiteResult.getTestContext();
            String testId = context.getCurrentXmlTest().getAllParameters().get("test-id");

            IResultMap failedConfigs = context.getFailedConfigurations();
            if (failedConfigs.size()>0){
                Set<ITestResult> failedConfigTests = failedConfigs.getAllResults();
                String msg = "No message";
                for (ITestResult result : failedConfigTests){
                    if(result.getThrowable()!=null){
                        msg = result.getThrowable().getMessage();
                        break;
                    }
                }
                if (processed.containsKey(testId)){
                    processed.replace(testId,context.getName()+","+"setup"+" - "+msg);
                } else {
                    processed.put(testId,context.getName()+","+"setup"+" - "+msg);
                }
            }

            IResultMap failedTests = context.getFailedTests();
            if (failedTests.size()>0){
                if (processed.containsKey(testId)){
                }else {

                }
                writeResult(testId, context.getName()+","+"failed:"+failedTests.size());
            }

            IResultMap skippedTests = context.getSkippedTests();
            if (skippedTests.size()>0){
                writeResult(testId, context.getName()+","+"skipped:"+skippedTests.size());
            }

            IResultMap passedTests = context.getPassedTests();
            if (passedTests.size()>0){
                writeResult(testId, context.getName()+","+"passed:"+passedTests.size());
            }
            writeResult(testId, processed.get(testId));
        }
    }

    private void writeResult(String testID, String result){
        System.out.println(testID + "," + result);
    }
}
