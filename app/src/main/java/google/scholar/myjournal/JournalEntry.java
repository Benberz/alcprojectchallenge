package google.scholar.myjournal;

import java.io.Serializable;

public class JournalEntry implements Serializable {

    public String key;
    private String date;
    private String title;
    private String textEntry;

    public JournalEntry() {
    }

    public JournalEntry(String date, String title, String textEntry) {
        this.date = date;
        this.title = title;
        this.textEntry = textEntry;
    }

    public String getDate() {
        return date;
    }

    public String getTextEntry() {
        return textEntry;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTextEntry(String textEntry) {
        this.textEntry = textEntry;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

}
