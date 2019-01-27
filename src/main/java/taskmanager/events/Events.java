package taskmanager.events;

public enum Events {
    FILE_DOWNLOADED("Files download jobs finished"),
    WORDS_COUNTED("Word count jobs finished"),
    FILE_SAVED("File save jobs finished"),
    EXCEPTION("Jobs failed with exception");

    private final String label;

    Events(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
