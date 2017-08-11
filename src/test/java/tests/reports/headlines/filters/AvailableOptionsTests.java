package tests.reports.headlines.filters;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.reports.components.Report_DatasetOptions;
import pages.reports.components.Report_GradeFilters;
import pages.reports.components.Report_ViewOptions;
import tests.reports.ReportTest;

import static org.hamcrest.Matchers.is;

@Epic( "EAP Reports - Headline Measures" )
@Feature( "Headlines > Filters view" )
public class AvailableOptionsTests extends ReportTest {

    // Tests of the DatasetOptions component
    @Story( "The Tracker tab is disabled" )
    @Severity( SeverityLevel.NORMAL )
    @Test
    public void trackerTabDisabled(){
        Report_DatasetOptions dsOptions = report.dsOptions;
        assertWithScreenshot("Display status of the Tracker tab",
                dsOptions.isDisabled(dsOptions.TRACKER_TAB), is(true));
    }

    // Tests of the Filters tabs
    @Story( "The Student Filters tab should be enabled" )
    @Severity( SeverityLevel.NORMAL )
    @Test
    public void studentFiltersEnabled(){
        assertWithScreenshot("Enabled status of the Student Filters tab",
                report.filterTabs.isEnabled("filter"), is(true));
    }

    @Story( "The Measure Filters tab should be enabled" )
    @Severity( SeverityLevel.NORMAL )
    @Test
    public void measureFiltersEnabled(){
        assertWithScreenshot("Enabled status of the Measure Filters tab",
                report.filterTabs.isEnabled("measure"), is(true));
    }

    @Story( "The Residual Exclusions tab should be disabled" )
    @Severity( SeverityLevel.NORMAL )
    @Test
    public void residualExclusionsDisabled(){
        assertWithScreenshot("Enabled status of the Residual Exclusions tab",
                report.filterTabs.isDisabled("residual"), is(true));
    }

    // Tests of the elements in the Grade Filter options area
    @Story( "The On Track grade filter menu is disabled" )
    @Severity( SeverityLevel.NORMAL )
    @Test
    public void onTrackFilterDisabled(){
        Report_GradeFilters filters = report.gradeFilters;
        assertWithScreenshot("Enabled status of the On/Above/Below Track Menu",
                filters.isDisabled(filters.ON_TRACK_MENU), is(true));
    }

    @Story( "The Faculty grade filter DDL is enabled" )
    @Severity( SeverityLevel.NORMAL )
    @Test
    public void facultyDDLEnabled(){
        Report_GradeFilters filters = report.gradeFilters;
        assertWithScreenshot("Enabled status of the Faculty DDL",
                filters.isEnabled(filters.FACULTY_DDL), is(true));
    }

    @Story( "The Qualification grade filter DDL is enabled" )
    @Severity( SeverityLevel.NORMAL )
    @Test
    public void qualificationsDDLEnabled(){
        Report_GradeFilters filters = report.gradeFilters;
        assertWithScreenshot("Enabled status of the Qualification DDL",
                filters.isEnabled(filters.QUALIFICATION_DDL), is(true));
    }

    @Story( "The Class grade filter DDL is hidden" )
    @Severity( SeverityLevel.NORMAL )
    @Test
    public void classDDLIsHidden(){
        Report_GradeFilters filters = report.gradeFilters;
        assertWithScreenshot("Display status of the Class DDL",
                filters.isDisabled(filters.CLASS_DDL), is(false));
    }

    @Story( "The Student grade filter DDL is hidden" )
    @Severity( SeverityLevel.NORMAL )
    @Test
    public void studentDDLIsHidden(){
        Report_GradeFilters filters = report.gradeFilters;
        assertWithScreenshot("Display status of the Student DDL",
                filters.isDisplayed(filters.STUDENT_FILTER_DDL), is(true));
    }

    // Tests of the elements in the View/Display options area
    @Story( "The Calculation type toggle (Count/%) is disabled" )
    @Severity( SeverityLevel.NORMAL )
    @Test
    public void calcTypeIsDisabled(){
        Report_ViewOptions viewOptions = report.viewOptions;
        assertWithScreenshot("SEnabled status of the Calc Type option (Count/%)",
                viewOptions.isDisabled(viewOptions.CALC_TYPE_TOGGLE), is(true));
    }

    @Story( "The Figure type toggle (Std/Cum) is disabled" )
    @Severity( SeverityLevel.NORMAL )
    @Test
    public void figureTypeIsDisabled(){
        Report_ViewOptions viewOptions = report.viewOptions;
        assertWithScreenshot("SEnabled status of the Figure Type option (Std/Cum)",
                viewOptions.isDisabled(viewOptions.FIG_TYPE_TOGGLE), is(true));
    }

}
