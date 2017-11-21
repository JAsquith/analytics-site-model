package tests.admin.data.grades;

import io.qameta.allure.Step;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.data.grades.PublishGrades;
import pages.data.grades.PublishGrades.MainTab;
import pages.data.grades.components.PublishGradesModal;
import tests.admin.data.RepublishTest;

/**
 * Add Javadoc comments here!
 */

public class PublishGradesTest extends RepublishTest {

    PublishGrades page;
    PublishGradesModal modal;

    @BeforeTest
    @Step( "Select the Main Publish Type tab on the Publish Grades page" )
    public void setup(){
        // RepublishTest.setup should have run and got us to the Publish Grades page
        // Now we need to select the "Reports", "Tracker" or "Flight Paths" Main tab
        String gradesPublishType = getStringParam("grades-publish-type", "Report");
        page = selectMainTab(gradesPublishType);

        // For Reports/Flight Paths, open the relevant modal;
        // For Trackers no modal is required
        openPublishModal(gradesPublishType);

    }

    @Test( description = "Publish EAP Grade Data" )
    @Parameters( { "grades-publish-type" })
    public void publishGradesData(String gradesPublishType){
        // Should now be able to trigger the publish by clicking one button

    }


    // SETUP STEPS
    @Step( "Select the {gradePublishType} tab" )
    private PublishGrades selectMainTab(String gradePublishType){

        MainTab mainTab = null;
        String msg = "Grade Publishing Type (" + gradePublishType + ") must be one of: {";
        for (MainTab tab : MainTab.values()){
            msg += "'"+tab.getName()+"', ";
            if (tab.getName().toLowerCase().equals(gradePublishType)){
                mainTab = tab;
                break;
            }
        }
        msg = msg.substring(0, msg.length()-2)+"}";
        if (mainTab==null) {
            throw new IllegalArgumentException(msg);
        }

        page.clickMainTab(mainTab);
        return new PublishGrades(driver);
    }

    // TEST BODY STEPS
    @Step( "Open the publish modal (if required)" )
    private void openPublishModal(String gradesPublishType){
        switch (gradesPublishType.toLowerCase()){
            case "report":
                String dataset = getStringParam("dataset");
                int year = getIntegerParam("year");
                int term = getIntegerParam("term");
                int slot = getIntegerParam("slot");
                if (dataset.equals("")){
                    openPublishModalForAssessment(year, term, slot);
                } else {
                    openPublishModalForDataset(dataset);
                }
                break;
            case "tracker":
                // No modal for Trackers
                break;
            case "flight paths":

                break;
            default:
                String msg = "grades-publish-type ("+gradesPublishType+") must be one of " +
                        "{'report', 'tracker', 'flight paths'}";
                throw new IllegalArgumentException(msg);
        }
    }

    private void publish(String gradesPublishType){

    }

    @Step ( "Open the Publish modal for Dataset '{dataset}'" )
    private PublishGradesModal openPublishModalForDataset(String dataset){
        return page.clickPublishFor(dataset);
    }

    @Step( "Click the Publish button for Year {year}, Term {term}, Slot {slot}" )
    private PublishGradesModal openPublishModalForAssessment(int year, int term, int slot){
        // find the correct publish button and click it
        return new PublishGradesModal(driver);
    }

    @Step( "Click the Generate button" )
    private void clickGenerate(){

    }
}