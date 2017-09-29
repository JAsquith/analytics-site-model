package pages.reports.interfaces;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import pages.reports.EAPView;

import java.util.List;

public interface IReportModal {
    ExpectedCondition<WebElement> modalDisplayed();
    List<String> getGroupsList();
    List<String> getOptionsListForGroup(String group);
    EAPView cancelChanges();
    EAPView applyChanges();
}
