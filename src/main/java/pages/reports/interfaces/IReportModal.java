package pages.reports.interfaces;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import pages.reports.EAPView;

import java.util.List;

public interface IReportModal {
    ExpectedCondition<WebElement> modalDisplayed();
    List<String> getGroupsList();
    List<String> getValuesForGroup(String group);
    IReportModal toggleOption(String optionLabel, String option);
    EAPView cancelChanges();
    EAPView applyChanges();
}
