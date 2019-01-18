package taskmanager.command;

public enum ExecutionReport {
    FILE_DOWNLOADED("Files download jobs finished"),
    DOWNLOAD_FAILED("Failed download jobs failed"),
    WORDS_COUNTED("Word count jobs finished"),
    WORDS_COUNT_FAILED("Word count jobs failed"),
    //Todo properly work on tracking result
    UNKNOWN("Jobs failed with unknown reason");

    private final String label;

    private ExecutionReport(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
