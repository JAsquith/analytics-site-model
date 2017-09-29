package enums;

public enum ReportAction {
    // Actions on the Datasets tab
    CHANGE_FOCUS("Change Focus"),
    FOCUS_VTP("Show Focus As"),
    CHANGE_COMPARE("Change Compare"),
    COMPARE_VTP("Show Compare As"),
    // Actions on the Options tab
    EAP_TRACKING("Filter By Track Status"),
    FACULTY("Select Faculty"),
    QUALIFICATION("Select Qualification"),
    CLASS("Select Class"),
    GRADE_TYPE("Select Grade Type"),
    AWARD_CLASS("Select GCSE/Non-GCSE"),
    KS2_CORE("Select KS2 Core"),
    FOCUS_GRADE_OPERATOR("Select Focus Grade Operator"),
    FOCUS_GRADE("Select Focus Grade"),
    FOCUS_SUB_GRADE("Select Focus Sub Grade"),
    COMPARE_GRADE_OPERATOR("Select Compare Grade Operator"),
    COMPARE_GRADE("Select Compare Grade"),
    COMPARE_SUB_GRADE("Select Compare Sub Grade"),
    IN_A8_BASKET("Select In A8 Basket"),
    STUDENT("Select Student"),
    // Actions on the Filters tab
    TOGGLE_FILTER("Toggle Filter"),
    // Actions on the Measures tab
    TOGGLE_MEASURE("Toggle Measure"),
    // Actions on the Residual Exclusions tab
    TOGGLE_EXCLUSION("Toggle Qualification Exclusion"),
    // Actions in the Display options Row
    SORT_COLUMN("Select Sort Column"),
    SORT_DIRECTION("Toggle Sort Direction"),
    FIGURE_TYPE("Toggle Count/Percent"),
    CALCULATION_TYPE("Toggle Standard/Cumulative"),
    SUB_WHOLE("Toggle Sub Whole"),
    BREAKDOWN("Select Breakdown Filter");
    public String name;
    ReportAction(String name){
        this.name = name;
    }

}
