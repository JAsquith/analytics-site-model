package tests.reports.headlines.filters;

import io.qameta.allure.*;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.reports.Report;
import pages.reports.ReportsHome_EAP;
import pages.reports.components.Report_GradeFilters;
import tests.reports.ReportTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.net.MalformedURLException;

@Epic( "EAP Reports - Headline Measures" )
@Feature( "Headlines > Filters view" )
public class AvailableOptionsTests extends ReportTest {

    @Story( "The Student Filters tab should be enabled" )
    @Severity( SeverityLevel.CRITICAL )
    @Test
    public void studentFiltersEnabled(){
        assertThat("Enabled status of the Student Filters tab",
                report.filterTabs.isEnabled("filter"), is(true));
    }

    @Story( "The Measure Filters tab should be enabled" )
    @Severity( SeverityLevel.CRITICAL )
    @Test
    public void measureFiltersEnabled(){
        assertThat("Enabled status of the Measure Filters tab",
                report.filterTabs.isEnabled("measure"), is(true));
    }

    @Story( "The Residual Exclusions tab should be disabled" )
    @Severity( SeverityLevel.CRITICAL )
    @Test
    public void residualExclusionsDisabled(){
        assertThat("Enabled status of the Residual Exclusions tab",
                report.filterTabs.isDisabled("residual"), is(true));
    }

    @Story( "The On Track grade filter menu is disabled" )
    @Severity( SeverityLevel.CRITICAL )
    @Test
    public void onTrackFilterDisabled(){
        assertThat("Enabled status of the On/Above/Below Track Menu",
                report.gradeFilters.isDisabled(Report_GradeFilters.ON_TRACK_MENU), is(true));
    }

    @Story( "The Faculty grade filter DDL is enabled" )
    @Severity( SeverityLevel.CRITICAL )
    @Test
    public void facultyDDLEnabled(){
        assertThat("Enabled status of the Faculty DDL",
                report.gradeFilters.isEnabled(Report_GradeFilters.FACULTY_DDL), is(true));
    }

    @Story( "The Qualification grade filter DDL is enabled" )
    @Severity( SeverityLevel.CRITICAL )
    @Test
    public void qualificationsDDLEnabled(){
        assertThat("Enabled status of the Qualification DDL",
                report.gradeFilters.isEnabled(Report_GradeFilters.QUALIFICATION_DDL), is(true));
    }

    @Story( "The Class grade filter DDL is hidden" )
    @Severity( SeverityLevel.CRITICAL )
    @Test
    // Class DDL is not present
    public void classDDLIsHidden(){
        assertThat("Display status of the Class DDL",
                report.gradeFilters.isDisabled(Report_GradeFilters.CLASS_DDL), is(false));
    }

}
