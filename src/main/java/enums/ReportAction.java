package enums;

public enum ReportAction {
    // Dummy Null Action
    NULL("Null"),

    // Actions on the Datasets tab
    CHANGE_FOCUS("Change Focus"),
    FOCUS_VTP("Show Focus As"),
    CHANGE_COMPARE("Change Compare"),
    COMPARE_VTP("Show Compare As"),
    TRACKER_COLUMN("Change Tracker Column"),
    RESET_DATASETS("Reset"),

    // Actions on the Options tab
    EAP_TRACKING("Filter By Track Status"),
    FACULTY("Select Faculty", CHANGE_FOCUS.name),
    QUALIFICATION("Select Qualification"),
    CLASS("Select Class"),
    GRADE_TYPE("Select Grade Type"),
    AWARD_CLASS("Select GCSE/Non-GCSE", CHANGE_FOCUS.name),
    KS2_CORE("Select KS2 Core"),
    FOCUS_GRADE_OPERATOR("Select Focus Grade Operator"),
    FOCUS_GRADE("Select Focus Grade"),
    FOCUS_SUB_GRADE("Select Focus Sub Grade"),
    COMPARE_GRADE_OPERATOR("Select Compare Grade Operator"),
    COMPARE_GRADE("Select Compare Grade"),
    COMPARE_SUB_GRADE("Select Compare Sub Grade"),
    IN_A8_BASKET("Select In A8 Basket"),
    STUDENT("Select Student", CHANGE_FOCUS.name),
    STUDENT_INFO("Select Student Info"),
    RESET_OPTIONS("Reset"),

    // Actions on the Filters tab
    TOGGLE_FILTER("Toggle Filter", CHANGE_FOCUS.name),
    RESET_FILTERS("Reset"),

    // Actions on the Measures tab
    TOGGLE_MEASURE("Toggle Measure", CHANGE_FOCUS.name),
    RESET_MEASURES("Reset"),

    // Actions on the Residual Exclusions tab
    TOGGLE_EXCLUSION("Toggle Qualification Exclusion", CHANGE_FOCUS.name),
    RESET_EXCLUSIONS("Reset"),

    // Actions in the View Navigation menu
    NEW_AREA_AND_REPORT("Select a random Area & Report"),
    NEW_REPORT("Select a new Report"),
    NEW_GROUPING("Select a new Level/Grouping"),

    NEW_AREA_REPORT_AND_GROUPING("Select a new Area & Report", NEW_GROUPING),

    // Actions in the Display options Row
    SORT_COLUMN("Select Sort Column"),
    SORT_DIRECTION("Toggle Sort Direction"),
    FIGURE_TYPE("Toggle Count/Percent"),
    CALCULATION_TYPE("Toggle Standard/Cumulative"),
    SUB_WHOLE("Toggle Sub Whole"),
    BREAKDOWN("Select Breakdown Filter", CHANGE_FOCUS.name),

    // Actions in the Report Tables
    DRILL_DOWN("Drill into a report link")
    ;

    // Members:
    //  Name - A string describing the action
    //  staticUntil - Another ReportAction...
    //      If this == "", options should be refreshed each time the action is taken
    //      If this != "", options can be stored until the named action is taken
    //  subAction - Another ReportAction...
    //      I
    public String name; public String staticUntil; public ReportAction subAction;

    ReportAction(String name){
        this.name = name;
        this.staticUntil = "";
        this.subAction = null;
    }

    ReportAction(String name, String staticUntil){
        this.name = name;
        this.staticUntil = staticUntil;
        this.subAction = null;
    }

    ReportAction(String name, ReportAction subAction){
        this.name = name;
        this.staticUntil = "";
        this.subAction = subAction;
    }

    ReportAction(String name, String staticUntil, ReportAction subAction){
        this.name = name;
        this.staticUntil = staticUntil;
        this.subAction = subAction;
    }


    public boolean isStatic(){
        return !this.staticUntil.equals("");
    }

    @Override
    public String toString(){
        if (this.subAction==null) {
            return name;
        }
        return this.name + "; then "+subAction.name+"";
    }

}
