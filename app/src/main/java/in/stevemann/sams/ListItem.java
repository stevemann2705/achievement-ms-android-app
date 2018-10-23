package in.stevemann.sams;

public class ListItem {
    private String eventName;
    private String rollNo;

    public ListItem(String eventName, String rollNo) {
        this.eventName = eventName;
        this.rollNo = rollNo;
    }

    public String getEventName() {
        return eventName;
    }

    public String getRollNo() {
        return rollNo;
    }
}
