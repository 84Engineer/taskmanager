package taskmanager.events;

public enum Events {
    FILE_DOWNLOADED("Files download jobs finished"),
    DOWNLOAD_FAILED("Failed download jobs failed"),
    WORDS_COUNTED("Word count jobs finished"),
    WORDS_COUNT_FAILED("Word count jobs failed"),
    FILE_DELETED("File delete jobs finished"),
    FILE_DELETION_FAILED("File deletion jobs failed"),
    //Todo properly work on tracking result
    UNKNOWN("Jobs failed with unknown reason");

    private final String label;

    private Events(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
