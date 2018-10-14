package fr.socket.florian.dhome.view.about;

public class Github {
    private final String title;
    private final String description;
    private final String link;

    Github(String title, String description, String link) {
        this.title = title;
        this.description = description;
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }
}
