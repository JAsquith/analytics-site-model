package utils;

import enums.ReportAction;
import pages.reports.interfaces.IReportActionGroup;

public class ReportActionSet {

    public IReportActionGroup group;
    public ReportAction action;
    public String option;

    public ReportActionSet(IReportActionGroup group){
        this.group = group;
        this.action = ReportAction.NULL;
        this.option = "-null-";
    }

    public String getDescription(){
        return String.format("%s > %s > %s", group.getName(), action.toString(), option);
    }

}
