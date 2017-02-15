package analytics.legacy.tests.demo;

import analytics.utils.HtmlLogger;
import analytics.utils.enums.LogLevel;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by Milton on 03/03/2016.
 */
public class LoggingDemo {

    @Test
    public void logMessages() throws IOException {
        HtmlLogger logger = new HtmlLogger("", LogLevel.DEBUG, Long.toString(System.currentTimeMillis()));
        logger.debug("Test","This is a debug message");
        logger.info("Test","This is an info message");
        logger.warn("Test","This is a warn message");
        logger.error("Test","This is an error message");
        logger.close();

/*
        logger = new HtmlLogger(HtmlLogger.Level.DEBUG, "");
        logger.debug("Test","This is a debug message");
        logger.info("Test","This is an info message");
        logger.warn("Test","This is a warn message");
        logger.error("Test","This is an error message");
        logger.close();
*/


    }

}
