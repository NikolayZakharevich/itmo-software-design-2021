package structures;

public class UrlWithTitle {
    private final String url;
    private final String title;

    public UrlWithTitle(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title + ": " + url;
    }
}