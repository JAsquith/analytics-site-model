package pages.reports.interfaces;

import java.util.List;

public interface IReportActionGroup {
    boolean isEnabled();
    List<String> getRandomActionName();
    String getRandomOptionFor(String actionName);
    boolean applyActionOption(String actionName, String option);
}
