package collegio.com;

public class Notice {

    private String noticeId;
    private String title;
    private String description;
    private String datePosted;

    public Notice(String noticeId, String title, String description, String datePosted) {
        this.noticeId = noticeId;
        this.title = title;
        this.description = description;
        this.datePosted = datePosted;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }
}