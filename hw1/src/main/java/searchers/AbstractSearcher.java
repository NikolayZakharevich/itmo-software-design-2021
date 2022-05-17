package searchers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import structures.UrlWithTitle;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static utils.Constants.*;

public abstract class AbstractSearcher {

    public static final int URLS_COUNT = 5;
    protected static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";

    protected final String name;
    protected final String queryPrefix;
    protected final String cssSelector;

    protected final String query;

    protected AbstractSearcher(String name, String queryPrefix, String cssSelector, String query) {
        this.name = name;
        this.queryPrefix = queryPrefix;
        this.cssSelector = cssSelector;
        this.query = query;
    }

    public List<UrlWithTitle> search() throws IOException {
        Elements links = Jsoup.connect(getUrlForSearch(query))
                .userAgent(USER_AGENT)
                .get().select(cssSelector);
        return extractUrls(links);
    }

    public String getName() {
        return name;
    }

    private String getUrlForSearch(String query) {
        return queryPrefix + URLEncoder.encode(query, UTF_8) + String.format("&numdoc=%d", URLS_COUNT);
    }

    private List<UrlWithTitle> extractUrls(Elements links) {
        List<UrlWithTitle> urlsWithTitles = new ArrayList<>();
        for (Element link : links) {
            String url = URLDecoder.decode(fixGoogleUrl(link.absUrl("href")), UTF_8);
            if (isUrlValid(url)) {
                urlsWithTitles.add(new UrlWithTitle(url, link.text()));
            }
            if (urlsWithTitles.size() == URLS_COUNT) {
                break;
            }
        }
        return urlsWithTitles;
    }

    private static String fixGoogleUrl(String url) {
        return url.startsWith(GOOGLE_URL) ? url.substring(GOOGLE_URL_START_INDEX) : url;
    }

    private static boolean isUrlValid(String url) {
        return url.startsWith(SCHEME_HTTP) || url.startsWith(SCHEME_HTTPS);
    }
}