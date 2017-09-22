package utils;

import java.io.File;

public final class TestConstants {

    // Constants related to: General Test Configuration
    public static final String TEST_PROPERTIES_FILENAME = "test.properties";

    public static final String TESTS_TO_RUN_LOCATION = "";
    public static final String TESTS_TO_RUN_FILE_NAME = "TestsList.csv";

    // Constants related to: Results Logging
    public static final String CUSTOM_RESULTS_FOLDER_NAME = "sisra-results";
    public static final String CUSTOM_RESULTS_SUMMARY_FILE_NAME = "Results_Summary.csv";
    public static final String CUSTOM_RESULTS_PASSES_FILE_NAME = "All_Passes.csv";

    // Constants related to: Extracting/Comparing data in Figure Checking Tests
    public static final String TABLE_DATA_FOLDER_NAME = "test-resources" + File.separator + "table-data";
    public static final String TABLE_DATA_ACTUAL_FOLDER_NAME = TABLE_DATA_FOLDER_NAME + File.separator + "actual";
    public static final String TABLE_DATA_EXPECTED_FOLDER_NAME = TABLE_DATA_FOLDER_NAME + File.separator + "expected";



}
