package enums;

public enum ReportAction {
    // Dummy Null Action
    //  - Description,
    //  - Static options (don't change from action to action unless...),
    //  - Action after which options should be updated
    NULL("Null", false, ""),

    // Actions on the Datasets tab
    CHANGE_FOCUS("Change Focus", false, ""),
    FOCUS_VTP("Show Focus As", false, ""),
    CHANGE_COMPARE("Change Compare", false, ""),
    COMPARE_VTP("Show Compare As", false, ""),
    TRACKER_COLUMN("Change Tracker Column", false, ""),

    // Actions on the Options tab
    EAP_TRACKING("Filter By Track Status", false, ""),
    FACULTY("Select Faculty", true, CHANGE_FOCUS.name),
    QUALIFICATION("Select Qualification", false, ""),
    CLASS("Select Class", false, ""),
    GRADE_TYPE("Select Grade Type", false, ""),
    AWARD_CLASS("Select GCSE/Non-GCSE", true, CHANGE_FOCUS.name),
    KS2_CORE("Select KS2 Core", false, ""),
    FOCUS_GRADE_OPERATOR("Select Focus Grade Operator", false, ""),
    FOCUS_GRADE("Select Focus Grade", false, ""),
    FOCUS_SUB_GRADE("Select Focus Sub Grade", false, ""),
    COMPARE_GRADE_OPERATOR("Select Compare Grade Operator", false, ""),
    COMPARE_GRADE("Select Compare Grade", false, ""),
    COMPARE_SUB_GRADE("Select Compare Sub Grade", false, ""),
    IN_A8_BASKET("Select In A8 Basket", false, ""),
    STUDENT("Select Student", true, CHANGE_FOCUS.name),
    STUDENT_INFO("Select Student Info", false, ""),

    // Actions on the Filters tab
    TOGGLE_FILTER("Toggle Filter", true, CHANGE_FOCUS.name),
    // Actions on the Measures tab
    TOGGLE_MEASURE("Toggle Measure", true, CHANGE_FOCUS.name),
    // Actions on the Residual Exclusions tab
    TOGGLE_EXCLUSION("Toggle Qualification Exclusion", true, CHANGE_FOCUS.name),

    // Actions in the View Navigation menu
    NEW_AREA_AND_REPORT("Navigate to a new Area & Report (same/default Grouping)", false, ""),
    NEW_REPORT("Navigate to a new Report (same/default Area & Grouping)", false, ""),
    NEW_GROUPING("Navigate to a new Grouping (same Area & Report)", false, ""),

    // Actions in the Display options Row
    SORT_COLUMN("Select Sort Column", false, ""),
    SORT_DIRECTION("Toggle Sort Direction", false, ""),
    FIGURE_TYPE("Toggle Count/Percent", false, ""),
    CALCULATION_TYPE("Toggle Standard/Cumulative", false, ""),
    SUB_WHOLE("Toggle Sub Whole", false, ""),
    BREAKDOWN("Select Breakdown Filter", true, CHANGE_FOCUS.name);

    public String name; public boolean optionsStatic; public String staticUntil;

    ReportAction(String name, boolean optionsStatic, String staticUntil){
        this.name = name;
        this.optionsStatic = optionsStatic;
        this.staticUntil = staticUntil;
    }

    @Override
    public String toString(){
        return name;
    }

}
