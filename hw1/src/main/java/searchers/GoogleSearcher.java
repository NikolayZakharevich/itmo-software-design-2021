package searchers;

public class GoogleSearcher extends AbstractSearcher {
    public static final String NAME = "Google";
    private static final String QUERY_PREFIX = "http://www.google.com/search?q=";
    private static final String CSS_SELECTOR = "#main > div > div > div:nth-child(1) > a";

    public GoogleSearcher(String query) {
        super(NAME, QUERY_PREFIX, CSS_SELECTOR, query);
    }
}