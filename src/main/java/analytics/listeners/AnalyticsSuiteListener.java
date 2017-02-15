package analytics.listeners;


import org.testng.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * A Listener class which sends an email containing test results.
 * This class can be specified in a TestNG.xml suite property as follows:
 * Todo - add xml example
 */
public class AnalyticsSuiteListener implements ISuiteListener, FilenameFilter {

    private ITestContext testContext;

    public void onFinish(ISuite suite){
        System.out.println("Tests Finished!");

        // Compose an email
        MimeMessage email = null;
        try {
            email = composeEmail(suite.getName() + " Test Results");
        } catch (SendFailedException e) {
            System.out.println("Emailing of test results failed - could not compose email with settings from grid.properties");
            System.out.println(e.toString());
            return;
        }
        Multipart multipart = new MimeMultipart();
        String bodyText = "";

        // Add a Results Table to the body
        bodyText += addResultsTable(suite);

        // If any of the tests being reported have a SuiteSummary attribute, add it to the email body
        bodyText += addSuiteSummary(suite);

        BodyPart bodyPart = new MimeBodyPart();
        try {
            bodyPart.setContent(bodyText,"text/html");
            bodyPart.setDisposition(BodyPart.INLINE);
            multipart.addBodyPart(bodyPart);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // Get an array of all the zip files in the log directory
        String logDir = testContext.getAttribute("logDirectory").toString();
        logDir = (logDir.endsWith(File.separator)) ? logDir : logDir + File.separator;
        File[] zipFilesToEmail = new File(logDir).listFiles(this);

        // Add the zip file attachments
        for (File zipFile: zipFilesToEmail){
            try {
                MimeBodyPart attach = new MimeBodyPart();
                attach.attachFile(zipFile);
                multipart.addBodyPart(attach);
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        }

        // Send the email
        if (zipFilesToEmail.length > 0) {
            try {
                email.setContent(multipart);
                Transport.send(email);

                // Remove the zip files from the log directory so we don't include them next time
                for (File zipFile : zipFilesToEmail)
                    zipFile.delete();

            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }
    }

    protected String addResultsTable(ISuite suite){

        String html = "<table><tbody><tr><th rowspan=\"2\">TEST</th><th colspan=\"5\">METHOD RESULTS</th></tr>";
        html += "<tr><th>Config Failed</th><th>Passed</th><th>Failed</th><th>Skipped</th><th>Total</th></tr>";

        // Stolen & adapted from: http://www.programcreek.com/java-api-examples/index.php?api=org.testng.IResultMap
        Map<String, ISuiteResult> tests = suite.getResults();
        Set<Map.Entry<String, ISuiteResult>> set = tests.entrySet();
        for (Map.Entry<String, ISuiteResult> entry : set) {
            String testName = entry.getKey();
            ISuiteResult suiteResult = entry.getValue();
            ITestContext context = suiteResult.getTestContext();

            testContext = context;

            IResultMap failedConfigs = context.getFailedConfigurations();
            IResultMap successTests = context.getPassedTests();
            IResultMap failedTests = context.getFailedTests();
            IResultMap skippedTests = context.getSkippedTests();

            int[] count = new int[5];

            count[0] = failedConfigs.size();
            count[1] = successTests.size();
            count[2] = failedTests.size();
            count[3] = skippedTests.size();
            count[4] = count[1] + count[2] + count[3];

            html += getRowDetail(testName, count);
        }

        html += "</tbody></table>";

        html += "<br><br>";
        html += "The summary table above only shows the number of Passed/Failed/Skipped Test " +
                "<em><strong>methods</strong></em> for each Test <em><strong>case</strong></em>.";
        html += "<br>" +
                "To investigate the events within a Test Case's Methods, please examine the attached zip file(s).";

        return html;
    }

    protected String addSuiteSummary(ISuite suite){

        String summary = "";

        Map<String, ISuiteResult> tests = suite.getResults();
        Set<Map.Entry<String, ISuiteResult>> set = tests.entrySet();
        for (Map.Entry<String, ISuiteResult> entry : set) {
            ITestContext context = entry.getValue().getTestContext();

            if (context.getAttributeNames().contains("SuiteSummary")){
                summary += "<br><ul>";
                summary += context.getAttribute("SuiteSummary");
                summary += "</ul><br><br>";
            }
        }

        return summary;
    }

    protected String getRowDetail(String testName, int[] count){

        String color = "green";
        if(count[0] + count[2] + count[3] > 0)
            color = count[2] > 0 ? "red" : "yellow";

        String row = "<tr bgcolor=\"" + color + "\"><td>" + testName + "</td>";
        row += "<td>"+count[0]+"</td>";
        row += "<td>"+count[1]+"</td>";
        row += "<td>"+count[2]+"</td>";
        row += "<td>"+count[3]+"</td>";
        row += "<td>"+count[4]+"</td></tr>";

        return row;
    }

    protected MimeMessage composeEmail(String subject) throws SendFailedException {

        String emailFrom; String emailTo; String emailAccName; String emailAccPass;
        String emailAccHost; String emailAccPort; String emailAccTLS;

        File gridConfig = new File("grid.properties");
        try {
            FileReader configReader = new FileReader(gridConfig);
            Properties gridProperties = new Properties();
            gridProperties.load(configReader);
            emailFrom = gridProperties.getProperty("reportEmailFrom");
            emailTo = gridProperties.getProperty("reportEmailTo");
            emailAccName = gridProperties.getProperty("reportEmailAccUsername");
            emailAccPass = gridProperties.getProperty("reportEmailAccPassword");
            emailAccHost = gridProperties.getProperty("reportEmailAccHost");
            emailAccPort = gridProperties.getProperty("reportEmailAccPort");
            emailAccTLS = gridProperties.getProperty("reportEmailAccTLS");
        } catch (IOException e){
            throw new SendFailedException("The 'grid.properties' file was not found or did not contain the required properties");
        }
        Properties props = new Properties();
        props.put("mail.smtp.host", emailAccHost);
        props.put("mail.smtp.socketFactory.port", emailAccPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.starttls.enable",emailAccTLS);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", emailAccPort);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailAccName, emailAccPass);
                    }
                });

        MimeMessage message = new MimeMessage(session);
        try{
            message.setFrom(new InternetAddress(emailFrom));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            message.setSubject(subject);
        }catch (MessagingException mex) {
            mex.printStackTrace();
            return null;
        }
        return message;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(".zip");
    }

    @Override
    public void onStart(ISuite suite) {

    }

/*
            Set<ITestResult> failedConfigResults = failedConfigs
                    .getAllResults();
            Set<ITestResult> failedResults = failedTests.getAllResults();
            Set<ITestResult> successResults = successTests.getAllResults();
            Set<ITestResult> skippedResults = skippedTests.getAllResults();

            if (!failedConfigResults.isEmpty()) {
                body += "<h3>Configuration Failed Cases</h3>";
                for(ITestResult result: failedConfigResults){
                    body += "<p>Test Name:" + result.getTestName() + "</p>";
                    body += "<p>Name:" + result.getName() + "</p>";
                    for (int i = 0; i < result.getParameters().length; i++){
                        body += "<p>Parameter: " + result.getParameters()[i] + "</p>";
                    }
                }
            }

            if (!failedResults.isEmpty()) {
                body += "<h3>Failed Cases</h3>";
                for(ITestResult result: failedResults){
                    body += "<p>Test Name:" + result.getTestName() + "</p>";
                    body += "<p>Name:" + result.getName() + "</p>";
                    for (int i = 0; i < result.getParameters().length; i++){
                        body += "<p>Parameter: " + result.getParameters()[i] + "</p>";
                    }
                }
            }

            if (!successResults.isEmpty()) {
                body += "<h3>Passed Cases</h3>";
                for(ITestResult result: successResults){
                    body += "<p>Test Name:" + result.getTestName() + "</p>";
                    body += "<p>Name:" + result.getName() + "</p>";
                    for (int i = 0; i < result.getParameters().length; i++){
                        body += "<p>Parameter: " + result.getParameters()[i] + "</p>";
                    }
                }
            }

            if (!skippedResults.isEmpty()) {
                body += "<h3>Passed Cases</h3>";
                for(ITestResult result: skippedResults){
                    body += "<p>Test Name:" + result.getTestName() + "</p>";
                    body += "<p>Name:" + result.getName() + "</p>";
                    for (int i = 0; i < result.getParameters().length; i++){
                        body += "<p>Parameter: " + result.getParameters()[i] + "</p>";
                    }
                }
            }
*/

}
