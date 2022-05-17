package searchers;

public class YandexSearcher extends AbstractSearcher {
    public static final String NAME = "Yandex";
    private static final String QUERY_PREFIX = "https://www.yandex.ru/search/?text=";
    private static final String CSS_SELECTOR = "div > h2 > a";

    public YandexSearcher(String query) {
        super(NAME, QUERY_PREFIX, CSS_SELECTOR, query);
    }
}