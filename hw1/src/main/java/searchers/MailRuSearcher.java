package searchers;

public class MailRuSearcher extends AbstractSearcher {
    public static final String NAME = "Mail.ru";
    private static final String QUERY_PREFIX = "https://www.go.mail.ru/search/?q=";
    private static final String CSS_SELECTOR = "div > div.ec_.si32 > a";

    public MailRuSearcher(String query) {
        super(NAME, QUERY_PREFIX, CSS_SELECTOR, query);
    }
}