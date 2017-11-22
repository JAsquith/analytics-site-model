package tests.admin.data;

import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.data.basedata.PublishBaseData;
import pages.data.grades.PublishGrades;
import pages.data.grades.components.PublishAssessmentsYearRow;
import pages.data.grades.components.PublishDatasetsRow;
import pages.data.grades.interfaces.IPublishGradesRow;
import pages.data.students.PublishStudents;
import tests.BaseTest;
import utils.TestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.exparity.hamcrest.date.LocalDateTimeMatchers.after;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.fail;

@Epic("EAP Publishing")
public class RepublishTest extends BaseTest {

    private int publishType = 1;
    private LocalDateTime deployDate = null;

    boolean repubStudents;
    boolean repubBasedata;
    boolean repubGrades;

    String cohorts[];

    List<Object[]> republishEvents;

    @BeforeTest()
    @Step("Login to the school")
    @Parameters({"username", "password"})
    public void setup(ITestContext testContext, String user, String pass)
    {

        // Checks this test is in the run list, creates the browser session and initialises some global test properties
        String initResult = super.initialise(testContext);
        if (!initResult.equals(""))
        {
            fail(initResult);
        }
        String deployDateStr = "00/00/0000 00:00:00";
        try
        {
            // Settings from the test.properties file
            publishType = this.testDomain.equals("www") ? 0 : 1;
            deployDateStr = getStringParam("deploy-date");
            deployDate = TestUtils.parseDeployDateString(deployDateStr);

            // Settings that may be overridden in the XML test parameters
            boolean l_repubStudents = getBooleanParam("student-data", false);
            boolean l_repubBasedata = getBooleanParam("baseline-data", false);
            boolean l_repubGrades = getBooleanParam("grades-data", false);
            repubStudents = l_repubStudents;
            repubBasedata = l_repubBasedata;
            repubGrades = l_repubGrades;

            // Settings from the XML test parameters (other than the username and password)
            cohorts = getArrayParam("cohorts");

            // instantiate the List to hold details of the publishing events we trigger
            republishEvents = new ArrayList<>();
        } catch (Exception e)
        {
            saveScreenshot(context.getName() + "_SetupFail.png");
            fail("Exception during setup::readTestSettings\n\n" + e.getMessage());
        }

        try
        {
            // Login
            login(user, pass, true);
        } catch (Exception e)
        {
            saveScreenshot(context.getName() + "_SetupFail.png");
            fail("Exception during setup::Login\n\n" + e.getMessage());
        }

        try
        {

            // Try to republish any out of date data
            // Todo - different exception handling to ensure all areas in all cohorts are checked
            for (String cohort : cohorts)
            {
                checkCohort(deployDateStr, cohort);
            }

        } catch (Exception e)
        {
            saveScreenshot(context.getName() + "_SetupFail.png");
            fail("Exception during setup::RepublishCohort\n\n" + e.getMessage());
        }

    }

    @Step("Checking for data published before {deployDateStr} in cohort: {cohort}")
    private void checkCohort(String deployDateStr, String cohort)
    {
        // Republish Student Data (if required)
        if (repubStudents)
        {
            try
            {
                checkStudentsPubDate(cohort);
            } catch (Exception e)
            {
                e.printStackTrace();
                String methodName = super.getCurrentMethodName();
                logToAllure("Exception in " + methodName + "::students");
                logToAllure(e.getMessage());
                saveScreenshot(methodName + ".png");
                throw e;
            }
        }

        // Republish Baseline Data (if required)
        if (repubBasedata)
        {
            try
            {
                checkBasedataPubDate(cohort);
            } catch (Exception e)
            {
                e.printStackTrace();
                String methodName = super.getCurrentMethodName();
                logToAllure("Exception in " + methodName + "::basedata");
                logToAllure(e.getMessage());
                saveScreenshot(methodName + ".png");
                throw e;
            }
        }

        // Republish Grades Data (if required)
        if (repubGrades)
        {
            try
            {
                checkGradesPubDates(cohort);
            } catch (Exception e)
            {
                e.printStackTrace();
                String methodName = super.getCurrentMethodName();
                logToAllure("Exception in " + methodName + "::grades");
                logToAllure(e.getMessage());
                saveScreenshot(methodName + ".png");
                throw e;
            }

        }
    }

    private void checkStudentsPubDate(String cohort)
    {
        try
        {
            PublishStudents studentsPub = new PublishStudents(driver).load(cohort, true);
            String lastPublishedInfo = studentsPub.getLastPublishedInfo();
            LocalDateTime pubDate = TestUtils.parseLastPubDateString(lastPublishedInfo);
            if (pubDate.isBefore(deployDate))
            {
                publishStudents(studentsPub, lastPublishedInfo);
                republishEvents.add(new Object[]{"Students", cohort, "", 0});
            } else
            {
                logToAllure(String.format("Skipping: %s > Students (Last published: %s)", cohort, lastPublishedInfo));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            String methodName = super.getCurrentMethodName();
            logToAllure("Exception in " + methodName + "::grades");
            logToAllure(e.getMessage());
            saveScreenshot(methodName + ".png");
            throw e;
        }
    }

    @Step("Publishing Student Data (last published: {lastPub})")
    private void publishStudents(PublishStudents studentsPub, String lastPub)
    {
        try
        {
            studentsPub.clickPublishAndWait(publishType);
        } catch (Exception e)
        {
            saveScreenshot("Students.png");
            e.printStackTrace();
            logToAllure("Exception while clicking Students > Publish: " + e.getMessage());
        }
    }

    private void checkBasedataPubDate(String cohort)
    {
        PublishBaseData basedataPub = new PublishBaseData(driver).load(cohort, true);
        String lastPublishedInfo = basedataPub.getLastPublishedInfo();
        LocalDateTime pubDate = TestUtils.parseLastPubDateString(lastPublishedInfo);
        if (pubDate.isBefore(deployDate))
        {
            publishBasedata(basedataPub, lastPublishedInfo);
            republishEvents.add(new Object[]{"BaseData", cohort, "", 0});
        } else
        {
            logToAllure(String.format("Skipping: %s > Basedata (Last published: %s)", cohort, lastPublishedInfo));
        }
    }

    @Step("Publishing Base Data (last published: {lastPub})")
    private void publishBasedata(PublishBaseData basedataPub, String lastPub)
    {
        try
        {
            basedataPub.clickPublishAndWait(publishType);
        } catch (Exception e)
        {
            saveScreenshot("Basedata.png");
            e.printStackTrace();
            logToAllure("Exception while clicking KS2/EAP > Publish: " + e.getMessage());
        }
    }

    private void checkGradesPubDates(String cohort)
    {
        PublishGrades gradesPub = new PublishGrades(driver).load(cohort, true);
        for (String dsName : gradesPub.getDatasetNames())
        {
            PublishDatasetsRow dsRow = new PublishDatasetsRow(driver, dsName);
            String lastPublishedInfo = dsRow.getLastPublishedInfo();
            LocalDateTime pubDate = TestUtils.parseLastPubDateString(lastPublishedInfo);
            if (pubDate.isBefore(deployDate))
            {
                gradesPub = publishGradesData(dsRow, dsName + "(last published: " + lastPublishedInfo + ")");
                republishEvents.add(new Object[]{"Dataset", cohort, dsName, new Integer(0)});
            } else
            {
                logToAllure(String.format("Skipping: %s > Dataset > %s (Last published: %s)", cohort, dsName, lastPublishedInfo));
            }
        }
        for (int year = 7; year < 12; year++)
        {
            int pubActionsCount = gradesPub.selectYearTab(year).publishActionsAvailable();
            for (int slotIndex = 1; slotIndex < pubActionsCount + 1; slotIndex++)
            {
                PublishAssessmentsYearRow yrRow = new PublishAssessmentsYearRow(driver, slotIndex);
                String lastPublishedInfo = yrRow.getLastPublishedInfo();
                if (yrRow.publishAvailable())
                {
                    LocalDateTime pubDate = TestUtils.parseLastPubDateString(lastPublishedInfo);
                    if (pubDate.isBefore(deployDate))
                    {
                        String info = "Year " + year + " slot " + slotIndex + " of " + (pubActionsCount)
                                + " (last published: " + lastPublishedInfo + ")";
                        gradesPub = publishGradesData(yrRow, info);
                        republishEvents.add(new Object[]{"Collection", cohort, String.valueOf(year), slotIndex});
                    } else
                    {
                        logToAllure(String.format("Skipping: %s > Year %d[%d] (Last published: %s)", cohort, year, slotIndex, lastPublishedInfo));
                    }
                } else
                    logToAllure(String.format("Skipping: %s > Year %d[%d] (Last published: %s)", cohort, year, slotIndex, lastPublishedInfo));
            }
        }
    }

    @Step("Publishing Grades Dataset {dsInfo}")
    private PublishGrades publishGradesData(IPublishGradesRow row, String dsInfo)
    {
        try
        {
            return row.clickPublish().clickPublish(publishType);
        } catch (Exception e)
        {
            saveScreenshot("Dataset.png");
            e.printStackTrace();
            logToAllure("Exception while clicking " + dsInfo + " > Publish: " + e.getMessage());
            return null;
        }
    }

    @Test(dataProvider = "publishEvents", description = "Publishing is up to date")
    @Parameters({"pubType", "cohort", "datasetOrYear", "slotIndex"})
    public void checkPublishEventsSuccessful(String pubType, String cohort, String datasetOrYear, Integer slotIndex)
    {
        switch (pubType)
        {
            case "Students":
                checkStudentPublishEvent(cohort);
                return;
            case "BaseData":
                checkBasedataPublishEvent(cohort);
                return;
            case "Dataset":
                checkDatasetPublishEvent(cohort, datasetOrYear);
                return;
            case "Collection":
                checkCollectionPublishEvent(cohort, datasetOrYear, slotIndex);
                return;
            default:
                // Houston, we have a problem!
        }
    }

    @Step("Cohort {cohort} Students publish date check")
    public void checkStudentPublishEvent(String cohort)
    {
        try
        {
            PublishStudents stuPubPage = new PublishStudents(driver).load(cohort, true);
            LocalDateTime published = TestUtils.parseLastPubDateString(stuPubPage.getLastPublishedInfo());
            String rsn = String.format("Published (%1$te/%1$tm/%1$tY at %1$tR) > Deployed (%2$te/%2$tm/%2$tY at %2$tR)", published, deployDate);
            assertWithScreenshot(rsn, published, is(after(deployDate)));
        } catch (Exception e)
        {
            e.printStackTrace();
            String methodName = super.getCurrentMethodName();
            logToAllure("Exception in " + methodName);
            logToAllure(e.getMessage());
            saveScreenshot(methodName + ".png");
            throw e;
        }
    }

    @Step("Cohort {cohort} Basedata publish date check")
    public void checkBasedataPublishEvent(String cohort)
    {
        try
        {
            PublishBaseData pubPage = new PublishBaseData(driver).load(cohort, true);
            LocalDateTime published = TestUtils.parseLastPubDateString(pubPage.getLastPublishedInfo());
            String rsn = String.format("Published (%1$tD at %1$tR) > Deployed (%2$tD at %2$tR)", published, deployDate);
            assertWithScreenshot(rsn, published, after(deployDate));
        } catch (Exception e)
        {
            e.printStackTrace();
            String methodName = super.getCurrentMethodName();
            logToAllure("Exception in " + methodName);
            logToAllure(e.getMessage());
            saveScreenshot(methodName + ".png");
            throw e;
        }
    }

    @Step("Cohort {cohort} > {dsName} publish date check")
    private void checkDatasetPublishEvent(String cohort, String dsName)
    {
        try
        {
            PublishGrades pubPage = new PublishGrades(driver).load(cohort, true);
            pubPage.selectDatasetsTab();
            PublishDatasetsRow dsRow = new PublishDatasetsRow(driver, dsName);
            LocalDateTime published = TestUtils.parseLastPubDateString(dsRow.getLastPublishedInfo());
            String rsn = String.format("Published (%1$tD at %1$tR) > Deployed (%2$tD at %2$tR)", published, deployDate);
            assertWithScreenshot(rsn, published, after(deployDate));
        } catch (Exception e)
        {
            e.printStackTrace();
            String methodName = super.getCurrentMethodName();
            logToAllure("Exception in " + methodName);
            logToAllure(e.getMessage());
            saveScreenshot(methodName + ".png");
            throw e;
        }
    }

    @Step("Cohort {cohort} > Year {eapYear} > Collection[{slotIndex}] publish date check")
    private void checkCollectionPublishEvent(String cohort, String eapYear, int slotIndex)
    {
        try
        {
            PublishGrades pubPage = new PublishGrades(driver).load(cohort, true);
            pubPage.selectYearTab(Integer.valueOf(eapYear));
            PublishAssessmentsYearRow yrRow = new PublishAssessmentsYearRow(driver, slotIndex);
            LocalDateTime published = TestUtils.parseLastPubDateString(yrRow.getLastPublishedInfo());
            String rsn = String.format("Published (%1$tD at %1$tR) > Deployed (%2$tD at %2$tR)", published, deployDate);
            assertWithScreenshot(rsn, published, after(deployDate));
        } catch (Exception e)
        {
            e.printStackTrace();
            String methodName = super.getCurrentMethodName();
            logToAllure("Exception in " + methodName);
            logToAllure(e.getMessage());
            saveScreenshot(methodName + ".png");
            throw e;
        }
    }

    @DataProvider(name = "publishEvents")
    public Iterator<Object[]> getRepublishEventsIterator()
    {
        return republishEvents.iterator();
    }

    @Test(dataProvider = "noPublishEvents", description = "No Republishing Events Triggered")
    @Parameters({"pubEventsCount"})
    public void noEventsDummyTest(int pubEventsCount)
    {
        assertThat("No publishing events were triggered in this school", 0, is(0));
    }

    @DataProvider(name = "noPublishEvents")
    public Iterator<Object[]> getNoEventsDummyTest()
    {
        List<Object[]> noEvents = new ArrayList<Object[]>();
        if (republishEvents.size() == 0)
        {
            noEvents.add(new Object[]{0});
        }
        return noEvents.iterator();
    }

}
