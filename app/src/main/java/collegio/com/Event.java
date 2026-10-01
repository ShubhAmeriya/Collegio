package collegio.com;

public class Event {

    private String eventId;
    private String title;
    private String date;
    private String venue;

    public Event(String eventId, String title, String date, String venue) {
        this.eventId = eventId;
        this.title = title;
        this.date = date;
        this.venue = venue;
    }

    public String getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getVenue() {
        return venue;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}