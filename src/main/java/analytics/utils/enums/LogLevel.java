package analytics.utils.enums;

public enum LogLevel {DEBUG(0,"DEBUG"), INFO(10,"INFO"), WARN(20,"WARN"), ERROR(30,"ERROR");
    public int index; public String label;
    LogLevel(int idx, String label){this.index = idx;this.label = label;}
    public static LogLevel get(String name){
        switch (name.toUpperCase()){
            case "DEBUG": return LogLevel.DEBUG;
            case "INFO": return LogLevel.INFO;
            case "WARN": return LogLevel.WARN;
            case "ERROR": return LogLevel.ERROR;
            default: return LogLevel.INFO;
        }
    }
    public static LogLevel get(int level){
        switch (level){
            case 0: return LogLevel.DEBUG;
            case 10: return LogLevel.INFO;
            case 20: return LogLevel.WARN;
            case 30: return LogLevel.ERROR;
            default: return LogLevel.INFO;
        }
    }
}
