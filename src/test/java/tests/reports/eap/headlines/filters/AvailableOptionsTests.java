package tests.reports.eap.headlines.filters;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.reports.components.Report_DatasetOptions;
import pages.reports.components.Report_GradeFilters;
import pages.reports.components.Report_ViewOptions;
import tests.reports.ReportTest;

import static org.hamcrest.Matchers.is;

@Epic( "EAP Reports" )
@Feature( "Headlines Report Options Visibility" )
@Story( "Report Options are Hidden/Disabled/Locked/Enabled as appropriate" )
public class AvailableOptionsTests extends ReportTest {

    // Tests of the DatasetOptions component
    @Test( description = "Tracker Tab: disabled" )
    @Severity( SeverityLevel.NORMAL )
    public void trackerTabDisabled(){
        Report_DatasetOptions dsOptions = report.dsOptions;
        assertWithScreenshot("Display status of the Tracker tab",
                dsOptions.isDisabled("Tracker"), is(true));
    }

    // Tests of the Filters tabs
    @Test( description = "Student Filters tab: enabled" )
    @Severity( SeverityLevel.NORMAL )
    public void studentFiltersEnabled(){
        assertWithScreenshot("Enabled status of the Student Filters tab",
                report.reportTabs.isEnabled("filter"), is(true));
    }

    @Test( description = "Measure Filters tab: enabled" )
    @Severity( SeverityLevel.NORMAL )
    public void measureFiltersEnabled(){
        assertWithScreenshot("Enabled status of the Measure Filters tab",
                report.reportTabs.isEnabled("measure"), is(true));
    }

    @Test( description = "Residual Exclusions tab: disabled" )
    @Severity( SeverityLevel.NORMAL )
    public void residualExclusionsDisabled(){
        assertWithScreenshot("Enabled status of the Residual Exclusions tab",
                report.reportTabs.isDisabled("residual"), is(true));
    }

    // Tests of the elements in the Grade Filter options area
    @Test( description = "Tracking filter: disabled" )
    @Severity( SeverityLevel.NORMAL )
    public void onTrackFilterDisabled(){
        Report_GradeFilters filters = report.gradeFilters;
        assertWithScreenshot("Enabled status of the On/Above/Below Track Menu",
                filters.isDisabled(filters.ON_TRACK_MENU), is(true));
    }

    @Test( description = "Faculty filter: enabled" )
    @Severity( SeverityLevel.NORMAL )
    public void facultyDDLEnabled(){
        Report_GradeFilters filters = report.gradeFilters;
        assertWithScreenshot("Enabled status of the Faculty DDL",
                filters.isEnabled(filters.FACULTY_DDL), is(true));
    }

    @Test( description = "Qualification filter: enabled" )
    @Severity( SeverityLevel.NORMAL )
    public void qualificationsDDLEnabled(){
        Report_GradeFilters filters = report.gradeFilters;
        assertWithScreenshot("Enabled status of the Qualification DDL",
                filters.isEnabled(filters.QUALIFICATION_DDL), is(true));
    }

    @Test( description = "Class filter: hidden")
    @Severity( SeverityLevel.NORMAL )
    public void classDDLIsHidden(){
        Report_GradeFilters filters = report.gradeFilters;
        assertWithScreenshot("Display status of the Class DDL",
                filters.isDisplayed(filters.CLASS_DDL), is(false));
    }

    @Test( description = "Student filter: hidden" )
    @Severity( SeverityLevel.NORMAL )
    public void studentDDLIsHidden(){
        Report_GradeFilters filters = report.gradeFilters;
        assertWithScreenshot("Display status of the Student DDL",
                filters.isDisplayed(filters.STUDENT_FILTER_DDL), is(true));
    }

    // Tests of the elements in the View/Display options area
    @Test(description = "CalcType (#/%): disabled" )
    @Severity( SeverityLevel.NORMAL )
    public void calcTypeIsDisabled(){
        Report_ViewOptions viewOptions = report.viewOptions;
        assertWithScreenshot("SEnabled status of the Calc Type option (Count/%)",
                viewOptions.isDisabled(viewOptions.CALC_TYPE_TOGGLE), is(true));
    }

    @Test( description = "FigType (Std/Cum): disabled" )
    @Severity( SeverityLevel.NORMAL )
    public void figureTypeIsDisabled(){
        Report_ViewOptions viewOptions = report.viewOptions;
        assertWithScreenshot("SEnabled status of the Figure Type option (Std/Cum)",
                viewOptions.isDisabled(viewOptions.FIG_TYPE_TOGGLE), is(true));
    }

}
