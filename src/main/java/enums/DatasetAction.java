package enums;

public enum DatasetAction {
    CHANGE_FOCUS("Change Focus"),
    FOCUS_VTP("Show Focus As"),
    CHANGE_COMPARE("Change Compare"),
    COMPARE_VTP("Show Compare As");
    public String name;
    DatasetAction(String name){
        this.name = name;
    }

}
