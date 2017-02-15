package analytics.pages.config;

import analytics.AnalyticsDriver;
import analytics.pages.AnalyticsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Represents the CONFIG page which shows a list of KS3/4 EAP Grade Methods
 */
public class GradeMethodsList_EAP extends AnalyticsPage {

    public final String PAGE_PATH = "/Config/EAPGradesMethod/Index/6";
    public final By CREATE_GRADE_METHOD_BUTTON = By.linkText("Create KS3/4 EAP Grade Method");
    public final By RESUME_METHOD_SETUP_BUTTON = By.linkText("Resume KS3/4 EAP Grade Method Set Up");

    /**
     * Constructor
     * @param aDriver       An AnalyticsDriver object logged in to a school in the given domain
     * @param loadByUrl     If <code>true</code>, the constructor loads the page from its URL
     */
    public GradeMethodsList_EAP(AnalyticsDriver aDriver, boolean loadByUrl) {
        super(aDriver);
        if (loadByUrl) {
            driver.get(driver.getDomainRoot() + PAGE_PATH);
        }
        waitForLoadingWrapper(LONG_WAIT);
        waitMedium.until(ExpectedConditions.or(
                ExpectedConditions.elementToBeClickable(RESUME_METHOD_SETUP_BUTTON),
                ExpectedConditions.elementToBeClickable(CREATE_GRADE_METHOD_BUTTON)
        ));
    }

    public boolean userEditingGradeMethod(){
        List<WebElement> resumeButtons = driver.findElements(RESUME_METHOD_SETUP_BUTTON);
        return (resumeButtons.size()>0);
    }

    /**
     * Clicks the Create KS3/4 EAP Grade Method button and waits for the "Loading..." msg to be hidden
     * @return  a page object for the Step 1 page
     */
    public GradeMethodStep1 createNewGradeMethod(){
        driver.findElement(CREATE_GRADE_METHOD_BUTTON).click();
        waitForLoadingWrapper();
        return new GradeMethodStep1(driver);
    }

    /**
     * Clicks the Resume KS3/4 EAP Grade Method Set Up  button and waits for the "Loading..." msg to be hidden
     * @return  a page object for one of the Set Up step pages; use {@link GradeMethodEdit#getPageLegend()} or
     *          {@link AnalyticsDriver#getCurrentUrl()} to determine which step
     */
    public GradeMethodEdit resumeGradeMethod(){
        driver.findElement(RESUME_METHOD_SETUP_BUTTON).click();
        waitForLoadingWrapper();
        return new GradeMethodEdit(driver);
    }

    /**
     * Checks whether the given Grade Method is listed on the page
     * @param methodName    The method name to check for
     * @return  {@code true} if the Method exists; {@code false} if it does not
     */
    public boolean findGradeMethodFor(String methodName){
        // Todo - write a GradeMethodListRow component and change this method to return an object of that class
        List<WebElement> methodLinks = driver.findElements(By.linkText(methodName));
        if (methodLinks.size()==0){
            return false;
        }
        if (methodLinks.get(0).getAttribute("href").
                startsWith(driver.getDomainRoot()+"/Config/EAPGradesMethod/Detail/6")){
            return true;
        }
        return false;
    }
}
