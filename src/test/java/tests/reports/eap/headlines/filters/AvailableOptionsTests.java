package tests.reports.eap.headlines.filters;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.reports.components.ReportActionsTab_Options;
import pages.reports.components.ReportActions_DisplayOptions;
import tests.reports.eap.ReportTest;

import static org.hamcrest.Matchers.is;

@Epic( "EAP Reports" )
@Feature( "Headlines Report Options Visibility" )
@Story( "Report Options are Hidden/Disabled/Locked/Enabled as appropriate" )
public class AvailableOptionsTests extends ReportTest {

    // Tests of the DatasetOptions component
/*
    @Test( description = "Tracker Tab: disabled" )
    @Severity( SeverityLevel.NORMAL )
    public void trackerTabDisabled(){
        ReportActionsTab_Dataset datasetsTab = report.datasetsTab;
        assertWithScreenshot("Display status of the Tracker tab",
                datasetsTab.isOptionDisabled("Tracker"), is(true));
    }
*/

    // Tests of the Filters tabs
    @Test( description = "Student Filters tab: enabled" )
    @Severity( SeverityLevel.NORMAL )
    public void studentFiltersEnabled(){
        assertWithScreenshot("Enabled status of the Student Filters tab",
                report.filtersTab.isEnabled(), is(true));
    }

    @Test( description = "Measure Filters tab: enabled" )
    @Severity( SeverityLevel.NORMAL )
    public void measureFiltersEnabled(){
        assertWithScreenshot("Enabled status of the Measure Filters tab",
                report.measuresTab.isEnabled(), is(true));
    }

    @Test( description = "Residual Exclusions tab: disabled" )
    @Severity( SeverityLevel.NORMAL )
    public void residualExclusionsDisabled(){
        assertWithScreenshot("Enabled status of the Residual Exclusions tab",
                report.residualsTab.isDisabled(), is(true));
    }

    // Tests of the elements in the Grade Filter options area
    @Test( description = "Tracking filter: disabled" )
    @Severity( SeverityLevel.NORMAL )
    public void onTrackFilterDisabled(){
        ReportActionsTab_Options filters = report.optionsTab;
        assertWithScreenshot("Disabled status of the On/Above/Below Track Menu",
                filters.optionIsDisabled(filters.ON_TRACK_MENU), is(true));
    }

    @Test( description = "Faculty filter: enabled" )
    @Severity( SeverityLevel.NORMAL )
    public void facultyDDLEnabled(){
        ReportActionsTab_Options filters = report.optionsTab;
        assertWithScreenshot("Enabled status of the Faculty DDL",
                filters.optionIsEnabled(filters.FACULTY_DDL), is(true));
    }

    @Test( description = "Qualification filter: enabled" )
    @Severity( SeverityLevel.NORMAL )
    public void qualificationsDDLEnabled(){
        ReportActionsTab_Options filters = report.optionsTab;
        assertWithScreenshot("Enabled status of the Qualification DDL",
                filters.optionIsEnabled(filters.QUALIFICATION_DDL), is(true));
    }

    @Test( description = "Class filter: hidden")
    @Severity( SeverityLevel.NORMAL )
    public void classDDLIsHidden(){
        ReportActionsTab_Options filters = report.optionsTab;
        assertWithScreenshot("Display status of the Class DDL",
                filters.optionIsDisplayed(filters.CLASS_DDL), is(false));
    }

    @Test( description = "Student filter: hidden" )
    @Severity( SeverityLevel.NORMAL )
    public void studentDDLIsHidden(){
        ReportActionsTab_Options filters = report.optionsTab;
        assertWithScreenshot("Display status of the Student DDL",
                filters.optionIsDisplayed(filters.STUDENT_FILTER_DDL), is(true));
    }

    // Tests of the elements in the View/Display options area
    @Test(description = "CalcType (#/%): disabled" )
    @Severity( SeverityLevel.NORMAL )
    public void calcTypeIsDisabled(){
        ReportActions_DisplayOptions viewOptions = report.viewOptions;
        assertWithScreenshot("SEnabled status of the Calc Type option (Count/%)",
                viewOptions.isOptionDisabled(viewOptions.CALC_TYPE_TOGGLE), is(true));
    }

    @Test( description = "FigType (Std/Cum): disabled" )
    @Severity( SeverityLevel.NORMAL )
    public void figureTypeIsDisabled(){
        ReportActions_DisplayOptions viewOptions = report.viewOptions;
        assertWithScreenshot("SEnabled status of the Figure Type option (Std/Cum)",
                viewOptions.isOptionDisabled(viewOptions.FIG_TYPE_TOGGLE), is(true));
    }

}
