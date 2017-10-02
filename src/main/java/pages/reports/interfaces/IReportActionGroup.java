package pages.reports.interfaces;

import enums.ReportAction;
import pages.reports.EAPView;

import java.util.List;

public interface IReportActionGroup {
    boolean isEnabled();
    List<ReportAction> getValidActionsList();
    List<String> getOptionsForAction(ReportAction action);
    EAPView applyActionOption(ReportAction action, String option);
    String getName();
}
